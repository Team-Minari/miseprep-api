package wisoft.io.miseprep_api.cart.event.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ItemAddedEventData(
        @JsonProperty("cart_id") Long cartId,
        @JsonProperty("cart_item_id") Long cartItemId,
        @JsonProperty("product_id") Long productId,
        @JsonProperty("product_name") String productName,
        int price,
        @JsonProperty("image_url") String imageUrl,
        String description,
        String category,
        int quantity,
        @JsonProperty("adder_name") String adderName,
        @JsonProperty("adder_profile_image_url") String adderProfileImageUrl
) {
}
