package wisoft.io.miseprep_api.order.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import wisoft.io.miseprep_api.order.entity.Order;
import wisoft.io.miseprep_api.order.entity.enums.OrderStatus;

import java.util.List;

public record OrderResponse(
        Long id,
        @JsonProperty("toss_order_id") String tossOrderId,
        @JsonProperty("cart_id") Long cartId,
        @JsonProperty("shipping_address") String shippingAddress,
        @JsonProperty("total_amount") int totalAmount,
        OrderStatus status,
        List<OrderItemResponse> items
) {
    public record OrderItemResponse(
            Long id,
            @JsonProperty("product_name") String productName,
            @JsonProperty("product_price") int productPrice,
            int quantity,
            int subtotal
    ) {}

    public static OrderResponse from(Order order) {
        List<OrderItemResponse> items = order.getItems().stream()
                .map(item -> new OrderItemResponse(
                        item.getId(),
                        item.getProductName(),
                        item.getProductPrice(),
                        item.getQuantity(),
                        item.getSubtotal()
                ))
                .toList();

        return new OrderResponse(
                order.getId(),
                "ORDER-" + order.getId(),
                order.getCart().getId(),
                order.getShippingAddress(),
                order.getTotalAmount(),
                order.getStatus(),
                items
        );
    }
}
