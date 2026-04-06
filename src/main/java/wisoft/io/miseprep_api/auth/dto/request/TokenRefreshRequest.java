package wisoft.io.miseprep_api.auth.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TokenRefreshRequest(@JsonProperty("refresh_token") String refreshToken) {
}
