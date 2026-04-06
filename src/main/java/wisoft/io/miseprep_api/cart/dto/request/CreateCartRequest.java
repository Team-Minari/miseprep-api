package wisoft.io.miseprep_api.cart.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateCartRequest(
        String name,
        String purpose,
        @JsonProperty("is_public") boolean isPublic,
        Integer budget
) {
}
