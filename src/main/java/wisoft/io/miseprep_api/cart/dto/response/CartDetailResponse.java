package wisoft.io.miseprep_api.cart.dto.response;

import wisoft.io.miseprep_api.cart.entity.Cart;

public record CartDetailResponse(
        Long id,
        String name,
        String purpose,
        boolean isPublic,
        Integer budget,
        Long ownerId,
        String linkToken
) {
    public static CartDetailResponse of(Cart cart, String linkToken) {
        return new CartDetailResponse(
                cart.getId(),
                cart.getName(),
                cart.getPurpose(),
                cart.isPublic(),
                cart.getBudget(),
                cart.getOwner().getId(),
                linkToken
        );
    }
}
