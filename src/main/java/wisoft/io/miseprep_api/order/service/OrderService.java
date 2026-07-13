package wisoft.io.miseprep_api.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import wisoft.io.miseprep_api.cart.entity.CartItem;
import wisoft.io.miseprep_api.cart.repository.CartItemRepository;
import wisoft.io.miseprep_api.cart.repository.CartParticipantRepository;
import wisoft.io.miseprep_api.cart.repository.CartRepository;
import wisoft.io.miseprep_api.global.exception.BusinessException;
import wisoft.io.miseprep_api.global.exception.ErrorCode;
import wisoft.io.miseprep_api.member.entity.Member;
import wisoft.io.miseprep_api.member.repository.MemberRepository;
import wisoft.io.miseprep_api.order.dto.request.CreateOrderRequest;
import wisoft.io.miseprep_api.order.dto.request.PaymentConfirmRequest;
import wisoft.io.miseprep_api.order.dto.response.OrderResponse;
import wisoft.io.miseprep_api.order.entity.Order;
import wisoft.io.miseprep_api.order.entity.OrderItem;
import wisoft.io.miseprep_api.order.repository.OrderRepository;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final CartParticipantRepository cartParticipantRepository;

    @Value("${toss.secret-key}")
    private String tossSecretKey;

    public OrderResponse createOrder(Long memberId, CreateOrderRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        var cart = cartRepository.findById(request.cartId())
                .orElseThrow(() -> new BusinessException(ErrorCode.CART_NOT_FOUND));

        if (!cartParticipantRepository.existsByCartIdAndMemberId(cart.getId(), memberId)) {
            throw new BusinessException(ErrorCode.CART_ACCESS_DENIED);
        }

        List<CartItem> cartItems = cartItemRepository.findAllById(request.cartItemIds());
        if (cartItems.size() != request.cartItemIds().size()) {
            throw new BusinessException(ErrorCode.ORDER_CART_ITEM_NOT_FOUND);
        }

        int totalAmount = cartItems.stream()
                .mapToInt(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();

        Order order = orderRepository.save(Order.create(member, cart, request.shippingAddress(), totalAmount));

        cartItems.forEach(cartItem ->
                order.getItems().add(OrderItem.create(order,
                        cartItem.getProduct().getName(),
                        cartItem.getProduct().getPrice(),
                        cartItem.getQuantity()))
        );

        orderRepository.saveAndFlush(order);

        return OrderResponse.from(order);
    }

    public OrderResponse confirmPayment(Long memberId, PaymentConfirmRequest request) {
        Order order = orderRepository.findByIdAndMemberId(request.orderId(), memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));

        if (order.getTotalAmount() != request.amount()) {
            throw new BusinessException(ErrorCode.PAYMENT_AMOUNT_MISMATCH);
        }

        try {
            String encodedKey = Base64.getEncoder()
                    .encodeToString((tossSecretKey + ":").getBytes(StandardCharsets.UTF_8));

            RestClient.create().post()
                    .uri("https://api.tosspayments.com/v1/payments/confirm")
                    .header("Authorization", "Basic " + encodedKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of(
                            "paymentKey", request.paymentKey(),
                            "orderId", String.valueOf(request.orderId()),
                            "amount", request.amount()
                    ))
                    .retrieve()
                    .toBodilessEntity();

            order.paid(request.paymentKey());
        } catch (Exception e) {
            order.failed();
            throw new BusinessException(ErrorCode.PAYMENT_CONFIRM_FAILED);
        }

        return OrderResponse.from(order);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getMyOrders(Long memberId) {
        return orderRepository.findAllByMemberId(memberId).stream()
                .map(OrderResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrder(Long memberId, Long orderId) {
        Order order = orderRepository.findByIdAndMemberId(orderId, memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));
        return OrderResponse.from(order);
    }
}
