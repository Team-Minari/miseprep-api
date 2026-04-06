package wisoft.io.miseprep_api.cart.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AddCartItemRequest(
        @JsonProperty("product_id") Long productId,
        int quantity
) {
}
