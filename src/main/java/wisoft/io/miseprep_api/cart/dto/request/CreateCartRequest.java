package wisoft.io.miseprep_api.cart.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import wisoft.io.miseprep_api.cart.entity.enums.CartType;

public record CreateCartRequest(
        String name,
        String purpose,
        @JsonProperty("is_public") boolean isPublic,
        Integer budget,
        @NotNull @JsonProperty("cart_type") CartType cartType
) {
}
