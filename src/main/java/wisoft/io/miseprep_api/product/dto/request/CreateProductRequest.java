package wisoft.io.miseprep_api.product.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateProductRequest(
        String name,
        String description,
        int price,
        @JsonProperty("image_url") String imageUrl,
        String category
) {
}
