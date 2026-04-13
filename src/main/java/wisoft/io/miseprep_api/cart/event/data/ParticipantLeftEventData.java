package wisoft.io.miseprep_api.cart.event.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ParticipantLeftEventData(
        @JsonProperty("cart_id") Long cartId,
        @JsonProperty("member_id") Long memberId,
        String reason
) {
}
