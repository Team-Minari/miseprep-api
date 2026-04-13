package wisoft.io.miseprep_api.cart.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wisoft.io.miseprep_api.cart.dto.request.*;
import wisoft.io.miseprep_api.cart.dto.response.CartDetailResponse;
import wisoft.io.miseprep_api.cart.dto.response.CartItemResponse;
import wisoft.io.miseprep_api.cart.dto.response.CartResponse;
import wisoft.io.miseprep_api.cart.dto.response.OwnerTransferResponse;
import wisoft.io.miseprep_api.cart.dto.response.ParticipantResponse;
import wisoft.io.miseprep_api.cart.entity.Cart;
import wisoft.io.miseprep_api.cart.entity.CartItem;
import wisoft.io.miseprep_api.cart.entity.CartParticipant;
import wisoft.io.miseprep_api.cart.event.*;
import wisoft.io.miseprep_api.cart.event.data.*;
import wisoft.io.miseprep_api.cart.repository.CartItemRepository;
import wisoft.io.miseprep_api.cart.repository.CartParticipantRepository;
import wisoft.io.miseprep_api.cart.repository.CartRepository;
import wisoft.io.miseprep_api.global.exception.BusinessException;
import wisoft.io.miseprep_api.global.exception.ErrorCode;
import wisoft.io.miseprep_api.invitation.entity.LinkInvitation;
import wisoft.io.miseprep_api.invitation.repository.EmailInvitationRepository;
import wisoft.io.miseprep_api.invitation.repository.LinkInvitationRepository;
import wisoft.io.miseprep_api.member.entity.Member;
import wisoft.io.miseprep_api.member.repository.MemberRepository;
import wisoft.io.miseprep_api.product.entity.Product;
import wisoft.io.miseprep_api.product.repository.ProductRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final CartRepository cartRepository;
    private final CartParticipantRepository cartParticipantRepository;
    private final CartItemRepository cartItemRepository;
    private final LinkInvitationRepository linkInvitationRepository;
    private final EmailInvitationRepository emailInvitationRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final ApplicationEventPublisher eventPublisher;

    public CartResponse createCart(Long memberId, CreateCartRequest request) {
        if (request.budget() != null && request.budget() <= 0) {
            throw new BusinessException(ErrorCode.INVALID_BUDGET);
        }
        Member member = findMember(memberId);
        Cart cart = cartRepository.save(Cart.create(member, request.name(), request.purpose(), request.isPublic(), request.budget()));
        cartParticipantRepository.save(CartParticipant.create(cart, member));
        linkInvitationRepository.save(LinkInvitation.create(cart, UUID.randomUUID().toString()));
        return CartResponse.from(cart);
    }

    @Transactional(readOnly = true)
    public List<CartResponse> getMyCarts(Long memberId) {
        return cartRepository.findAllByMemberId(memberId).stream()
                .map(CartResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public CartDetailResponse getCart(Long memberId, Long cartId) {
        Cart cart = findCartAsParticipant(memberId, cartId);
        String token = linkInvitationRepository.findByCartId(cartId)
                .map(LinkInvitation::getToken)
                .orElse(null);
        return CartDetailResponse.of(cart, token);
    }

    @Transactional(readOnly = true)
    public List<CartItemResponse> getCartItems(Long memberId, Long cartId) {
        findCartAsParticipant(memberId, cartId);
        return cartItemRepository.findAllByCartId(cartId).stream()
                .map(CartItemResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ParticipantResponse> getParticipants(Long memberId, Long cartId) {
        findCartAsParticipant(memberId, cartId);
        return cartParticipantRepository.findAllByCartId(cartId).stream()
                .map(ParticipantResponse::from)
                .toList();
    }

    public CartResponse updateCartSetting(Long memberId, Long cartId, UpdateCartSettingRequest request) {
        Cart cart = findCartAsOwner(memberId, cartId);
        Member member = findMember(memberId);

        if (request.budget() != null) {
            if (request.budget() < 0) {
                throw new BusinessException(ErrorCode.INVALID_BUDGET);
            }

            if (request.budget() == 0) {
                cart.updateBudget(null);
            }

            if (request.budget() > 0) {
                int currentTotal = cartItemRepository.findAllByCartId(cartId).stream()
                        .mapToInt(item -> item.getProduct().getPrice() * item.getQuantity())
                        .sum();

                if (currentTotal > request.budget()) {
                    throw new BusinessException(ErrorCode.CART_BUDGET_EXCEEDED);
                }

                cart.updateBudget(request.budget());
            }
        }

        if (request.purpose() != null) cart.updatePurpose(request.purpose());
        if (request.cartName() != null) cart.updateName(request.cartName());
        if (request.isPublic() != null) cart.updateIsPublic(request.isPublic());

        eventPublisher.publishEvent(new CartEvent(cartId, CartEventType.CART_SETTINGS_UPDATED,
                new CartSettingsUpdatedEventData(cartId, cart.getName(), cart.isPublic(), cart.getPurpose(), cart.getBudget(), member.getUsername())));

        return CartResponse.from(cart);
    }

    public void deleteCart(Long memberId, Long cartId) {
        Cart cart = findCartAsOwner(memberId, cartId);
        String ownerName = cart.getOwner().getUsername();

        eventPublisher.publishEvent(new CartEvent(cartId, CartEventType.CART_DELETED,
                new CartDeletedEventData(cartId, ownerName)));

        emailInvitationRepository.deleteAllByCartId(cartId);
        linkInvitationRepository.deleteByCartId(cartId);
        cartItemRepository.deleteAllByCartId(cartId);
        cartParticipantRepository.deleteAllByCartId(cartId);
        cartRepository.delete(cart);
    }

    public void leaveCart(Long memberId, Long cartId) {
        Cart cart = findCartAsParticipant(memberId, cartId);
        if (cart.isOwner(memberId)) {
            throw new BusinessException(ErrorCode.OWNER_CANNOT_LEAVE_CART);
        }
        Member member = findMember(memberId);
        cartItemRepository.findAllByCheckerId(memberId).stream()
                .filter(item -> item.getCart().getId().equals(cartId))
                .forEach(CartItem::uncheck);
        CartParticipant participant = cartParticipantRepository.findByCartIdAndMemberId(cartId, memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CART_ACCESS_DENIED));
        cartParticipantRepository.delete(participant);

        eventPublisher.publishEvent(new CartEvent(cartId, CartEventType.CART_PARTICIPANT_LEFT,
                new ParticipantLeftEventData(cartId, member.getId(), "LEAVE")));
    }

    public Long joinByLink(Long memberId, String token) {
        LinkInvitation linkInvitation = linkInvitationRepository.findByToken(token)
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_LINK_TOKEN));
        Cart cart = linkInvitation.getCart();
        if (cartParticipantRepository.existsByCartIdAndMemberId(cart.getId(), memberId)) {
            throw new BusinessException(ErrorCode.ALREADY_CART_PARTICIPANT);
        }
        Member member = findMember(memberId);
        cartParticipantRepository.save(CartParticipant.create(cart, member));

        eventPublisher.publishEvent(new CartEvent(cart.getId(), CartEventType.CART_PARTICIPANT_JOINED,
                new ParticipantJoinedEventData(cart.getId(), member.getId(), member.getUsername(), member.getEmail(), "MEMBER", member.getProfileImageUrl())));

        return cart.getId();
    }

    public CartItemResponse addItem(Long memberId, Long cartId, AddCartItemRequest request) {
        if (request.quantity() < 1) throw new BusinessException(ErrorCode.INVALID_QUANTITY);
        Cart cart = findCartAsParticipant(memberId, cartId);
        Member member = findMember(memberId);
        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND));
        CartItemResponse result = cartItemRepository.findByCartIdAndProductId(cartId, request.productId())
                .map(existingItem -> {
                    int newQuantity = existingItem.getQuantity() + request.quantity();
                    validateBudget(cart, cartId, product.getPrice(), newQuantity - existingItem.getQuantity());
                    existingItem.updateQuantity(newQuantity);
                    return CartItemResponse.from(existingItem);
                })
                .orElseGet(() -> {
                    validateBudget(cart, cartId, product.getPrice(), request.quantity());
                    return CartItemResponse.from(cartItemRepository.save(CartItem.create(cart, product, request.quantity())));
                });

        eventPublisher.publishEvent(new CartEvent(cartId, CartEventType.CART_ITEM_ADDED,
                new ItemAddedEventData(cartId, result.id(), product.getId(), product.getName(), product.getPrice(),
                        product.getImageUrl(), product.getDescription(), product.getCategory(), result.quantity(),
                        member.getUsername(), member.getProfileImageUrl())));

        return result;
    }

    public CartItemResponse updateItem(Long memberId, Long cartId, Long itemId, UpdateCartItemRequest request) {
        if (request.quantity() < 1) {
            throw new BusinessException(ErrorCode.INVALID_QUANTITY);
        }

        Cart cart = findCartAsParticipant(memberId, cartId);
        CartItem item = findCartItem(cartId, itemId);
        int quantityDiff = request.quantity() - item.getQuantity();

        if (quantityDiff > 0) {
            validateBudget(cart, cartId, item.getProduct().getPrice(), quantityDiff);
        }

        item.updateQuantity(request.quantity());
        return CartItemResponse.from(item);
    }

    public void deleteItem(Long memberId, Long cartId, Long itemId) {
        findCartAsParticipant(memberId, cartId);
        Member member = findMember(memberId);
        CartItem item = findCartItem(cartId, itemId);
        cartItemRepository.delete(item);

        eventPublisher.publishEvent(new CartEvent(cartId, CartEventType.CART_ITEM_DELETED,
                new ItemDeletedEventData(cartId, itemId, member.getUsername())));
    }

    public void deleteAllItems(Long memberId, Long cartId) {
        findCartAsParticipant(memberId, cartId);
        Member member = findMember(memberId);
        List<Long> itemIds = cartItemRepository.findAllByCartId(cartId).stream()
                .map(CartItem::getId)
                .toList();
        cartItemRepository.deleteAllByCartId(cartId);

        eventPublisher.publishEvent(new CartEvent(cartId, CartEventType.CART_ITEM_BULK_DELETED,
                new ItemBulkDeletedEventData(cartId, itemIds, member.getUsername())));
    }

    public void kickParticipant(Long memberId, Long cartId, Long targetMemberId) {
        Cart cart = findCartAsOwner(memberId, cartId);
        if (cart.isOwner(targetMemberId)) {
            throw new BusinessException(ErrorCode.CART_OWNER_CANNOT_BE_KICKED);
        }
        CartParticipant participant = cartParticipantRepository.findByCartIdAndMemberId(cartId, targetMemberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CART_ACCESS_DENIED));
        cartItemRepository.findAllByCheckerId(targetMemberId).stream()
                .filter(item -> item.getCart().getId().equals(cartId))
                .forEach(CartItem::uncheck);
        cartParticipantRepository.delete(participant);

        eventPublisher.publishEvent(new CartEvent(cartId, CartEventType.CART_PARTICIPANT_LEFT,
                new ParticipantLeftEventData(cartId, targetMemberId, "KICK")));
    }

    public CartItemResponse checkItem(Long memberId, Long cartId, Long itemId) {
        findCartAsParticipant(memberId, cartId);

        CartItem item = cartItemRepository.findByIdAndCartIdWithLock(itemId, cartId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CART_ITEM_NOT_FOUND));

        if (item.isChecked()) {
            throw new BusinessException(ErrorCode.CART_ITEM_ALREADY_CHECKED);
        }

        Member member = findMember(memberId);
        item.check(member);

        return CartItemResponse.from(item);
    }

    public CartItemResponse uncheckItem(Long memberId, Long cartId, Long itemId) {
        findCartAsParticipant(memberId, cartId);
        CartItem item = findCartItem(cartId, itemId);
        if (!item.isChecked()) throw new BusinessException(ErrorCode.CART_ITEM_NOT_CHECKED);
        if (!item.getChecker().getId().equals(memberId)) throw new BusinessException(ErrorCode.CART_ACCESS_DENIED);
        item.uncheck();
        return CartItemResponse.from(item);
    }

    public OwnerTransferResponse transferOwner(Long memberId, Long cartId, OwnerTransferRequest request) {
        Cart cart = findCartAsOwner(memberId, cartId);

        if (!cartParticipantRepository.existsByCartIdAndMemberId(cartId, request.newOwnerId())) {
            throw new BusinessException(ErrorCode.TRANSFER_TO_NON_PARTICIPANT);
        }

        Member newOwner = findMember(request.newOwnerId());
        cart.transferOwner(newOwner);

        eventPublisher.publishEvent(new CartEvent(cartId, CartEventType.CART_OWNER_TRANSFERRED,
                new OwnerTransferredEventData(cartId, memberId, newOwner.getId())));

        return new OwnerTransferResponse(memberId, newOwner.getId(), newOwner.getUsername());
    }

    private Cart findCart(Long cartId) {
        return cartRepository.findById(cartId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CART_NOT_FOUND));
    }

    private Cart findCartAsParticipant(Long memberId, Long cartId) {
        Cart cart = findCart(cartId);
        if (!cartParticipantRepository.existsByCartIdAndMemberId(cartId, memberId)) {
            throw new BusinessException(ErrorCode.CART_ACCESS_DENIED);
        }
        return cart;
    }

    private Cart findCartAsOwner(Long memberId, Long cartId) {
        Cart cart = findCart(cartId);
        if (!cart.isOwner(memberId)) throw new BusinessException(ErrorCode.CART_OWNER_REQUIRED);
        return cart;
    }

    private CartItem findCartItem(Long cartId, Long itemId) {
        return cartItemRepository.findByIdAndCartId(itemId, cartId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CART_ITEM_NOT_FOUND));
    }

    private Member findMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
    }

    private void validateBudget(Cart cart, Long cartId, int unitPrice, int additionalQuantity) {
        if (cart.getBudget() == null) return;
        Cart lockedCart = cartRepository.findByIdWithLock(cartId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CART_NOT_FOUND));
        int currentTotal = cartItemRepository.findAllByCartId(cartId).stream()
                .mapToInt(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();
        if (currentTotal + (unitPrice * additionalQuantity) > lockedCart.getBudget()) {
            throw new BusinessException(ErrorCode.CART_BUDGET_EXCEEDED);
        }
    }

}
