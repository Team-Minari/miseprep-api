package wisoft.io.miseprep_api.cart.event.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ParticipantJoinedEventData(
        @JsonProperty("cart_id") Long cartId,
        @JsonProperty("member_id") Long memberId,
        String username,
        String email,
        String role,
        @JsonProperty("profile_image_url") String profileImageUrl
) {
}
