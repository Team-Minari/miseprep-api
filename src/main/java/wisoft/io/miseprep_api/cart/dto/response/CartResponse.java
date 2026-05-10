package wisoft.io.miseprep_api.cart.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import wisoft.io.miseprep_api.cart.entity.Cart;
import wisoft.io.miseprep_api.cart.entity.enums.CartType;
import wisoft.io.miseprep_api.global.enums.Category;

public record CartResponse(
        Long id,
        String name,
        Category category,
        @JsonProperty("is_public") boolean isPublic,
        Integer budget,
        @JsonProperty("owner_id") Long ownerId,
        @JsonProperty("cart_type") CartType cartType
) {
    public static CartResponse from(Cart cart) {
        return new CartResponse(
                cart.getId(),
                cart.getName(),
                cart.getCategory(),
                cart.isPublic(),
                cart.getBudget(),
                cart.getOwner().getId(),
                cart.getCartType()
        );
    }
}
