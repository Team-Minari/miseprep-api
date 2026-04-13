package wisoft.io.miseprep_api.cart.event.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ItemDeletedEventData(
        @JsonProperty("cart_id") Long cartId,
        @JsonProperty("cart_item_id") Long cartItemId,
        @JsonProperty("deleter_name") String deleterName
) {
}
