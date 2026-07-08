package wisoft.io.miseprep_api.cart.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import wisoft.io.miseprep_api.cart.entity.Cart;
import wisoft.io.miseprep_api.cart.entity.enums.CartType;
import wisoft.io.miseprep_api.global.enums.Category;

public record CartResponse(
        Long id,
        String name,
        Category category,
        String purpose,
        @JsonProperty("is_public") boolean isPublic,
        Integer budget,
        @JsonProperty("owner_id") Long ownerId,
        @JsonProperty("cart_type") CartType cartType,
        @JsonProperty("like_count") long likeCount,
        @JsonProperty("is_liked") boolean isLiked
) {
    public static CartResponse from(Cart cart, long likeCount, boolean isLiked) {
        return new CartResponse(
                cart.getId(),
                cart.getName(),
                cart.getCategory(),
                cart.getPurpose(),
                cart.isPublic(),
                cart.getBudget(),
                cart.getOwner().getId(),
                cart.getCartType(),
                likeCount,
                isLiked
        );
    }
}
