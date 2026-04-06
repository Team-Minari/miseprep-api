package wisoft.io.miseprep_api.cart.dto.response;

import wisoft.io.miseprep_api.cart.entity.CartItem;

public record CartItemResponse(
        Long id,
        Long productId,
        String productName,
        int price,
        String imageUrl,
        int quantity,
        boolean checked,
        Long checkerId
) {
    public static CartItemResponse from(CartItem item) {
        return new CartItemResponse(
                item.getId(),
                item.getProduct().getId(),
                item.getProduct().getName(),
                item.getProduct().getPrice(),
                item.getProduct().getImageUrl(),
                item.getQuantity(),
                item.isChecked(),
                item.isChecked() ? item.getChecker().getId() : null
        );
    }
}
