package wisoft.io.miseprep_api.cart.event.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CartSettingsUpdatedEventData(
        @JsonProperty("cart_id") Long cartId,
        @JsonProperty("cart_name") String cartName,
        @JsonProperty("is_public") Boolean isPublic,
        String purpose,
        Integer budget,
        @JsonProperty("editor_name") String editorName
) {
}
