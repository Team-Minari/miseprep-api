package wisoft.io.miseprep_api.auth.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TestLoginRequest(
        String email,
        String username,
        @JsonProperty("profile_image_url") String profileImageUrl
) {
}
