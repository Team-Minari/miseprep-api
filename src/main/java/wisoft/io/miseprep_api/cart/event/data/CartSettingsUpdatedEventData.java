package wisoft.io.miseprep_api.cart.event.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import wisoft.io.miseprep_api.global.enums.Category;

public record CartSettingsUpdatedEventData(
        @JsonProperty("cart_id") Long cartId,
        @JsonProperty("cart_name") String cartName,
        @JsonProperty("is_public") Boolean isPublic,
        Category category,
        String purpose,
        Integer budget,
        @JsonProperty("editor_name") String editorName
) {
}
