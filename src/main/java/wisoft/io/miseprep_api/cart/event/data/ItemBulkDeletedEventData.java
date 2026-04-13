package wisoft.io.miseprep_api.cart.event.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ItemBulkDeletedEventData(
        @JsonProperty("cart_id") Long cartId,
        @JsonProperty("cart_item_ids") List<Long> cartItemIds,
        @JsonProperty("deleter_name") String deleterName
) {
}
