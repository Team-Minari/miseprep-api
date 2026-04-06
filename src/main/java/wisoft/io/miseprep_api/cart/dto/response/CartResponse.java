package wisoft.io.miseprep_api.cart.dto.response;

import wisoft.io.miseprep_api.cart.entity.Cart;

public record CartResponse(
        Long id,
        String name,
        String purpose,
        boolean isPublic,
        Integer budget,
        Long ownerId
) {
    public static CartResponse from(Cart cart) {
        return new CartResponse(
                cart.getId(),
                cart.getName(),
                cart.getPurpose(),
                cart.isPublic(),
                cart.getBudget(),
                cart.getOwner().getId()
        );
    }
}
