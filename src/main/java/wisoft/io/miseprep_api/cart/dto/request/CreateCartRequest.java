package wisoft.io.miseprep_api.cart.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import wisoft.io.miseprep_api.cart.entity.enums.CartType;
import wisoft.io.miseprep_api.global.enums.Category;

public record CreateCartRequest(
        String name,
        @NotNull Category category,
        @JsonProperty("is_public") boolean isPublic,
        Integer budget,
        @NotNull @JsonProperty("cart_type") CartType cartType
) {
}
