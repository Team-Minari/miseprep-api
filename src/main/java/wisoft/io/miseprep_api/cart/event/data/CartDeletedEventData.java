package wisoft.io.miseprep_api.cart.event.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CartDeletedEventData(
        @JsonProperty("cart_id") Long cartId,
        @JsonProperty("deleter_name") String deleterName
) {
}
