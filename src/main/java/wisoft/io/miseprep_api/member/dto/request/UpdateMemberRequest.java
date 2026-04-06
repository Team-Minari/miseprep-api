package wisoft.io.miseprep_api.member.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UpdateMemberRequest(
        String username,
        @JsonProperty("profile_image_url") String profileImageUrl
) {
}
