package wisoft.io.miseprep_api.cart.event.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OwnerTransferredEventData(
        @JsonProperty("cart_id") Long cartId,
        @JsonProperty("prev_owner_id") Long prevOwnerId,
        @JsonProperty("new_owner_id") Long newOwnerId
) {
}
