package wisoft.io.miseprep_api.auth.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import wisoft.io.miseprep_api.global.exception.BusinessException;
import wisoft.io.miseprep_api.global.exception.ErrorCode;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoClient {

    private static final String TOKEN_URL = "https://kauth.kakao.com/oauth/token";
    private static final String USER_INFO_URL = "https://kapi.kakao.com/v2/user/me";

    private final KakaoProperties kakaoProperties;
    private final RestClient restClient = RestClient.create();

    public String getAccessToken(String code) {
        try {
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type", "authorization_code");
            params.add("client_id", kakaoProperties.clientId());
            params.add("redirect_uri", kakaoProperties.redirectUri());
            params.add("code", code);

            KakaoTokenResponse response = restClient.post()
                    .uri(TOKEN_URL)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(params)
                    .retrieve()
                    .body(KakaoTokenResponse.class);

            return response.accessToken();
        } catch (RestClientException e) {
            log.error("[KakaoClient] getAccessToken 실패: {}", e.getMessage());
            throw new BusinessException(ErrorCode.INVALID_KAKAO_TOKEN);
        }
    }

    public KakaoUserResponse getUserInfo(String accessToken) {
        try {
            return restClient.get()
                    .uri(USER_INFO_URL)
                    .header("Authorization", "Bearer " + accessToken)
                    .retrieve()
                    .body(KakaoUserResponse.class);
        } catch (RestClientException e) {
            log.error("[KakaoClient] getUserInfo 실패: {}", e.getMessage());
            throw new BusinessException(ErrorCode.INVALID_KAKAO_TOKEN);
        }
    }

    public record KakaoTokenResponse(@JsonProperty("access_token") String accessToken) {}

    public record KakaoUserResponse(
            Long id,
            @JsonProperty("kakao_account") KakaoAccount kakaoAccount
    ) {
        public record KakaoAccount(
                String email,
                Profile profile
        ) {
            public record Profile(
                    String nickname,
                    @JsonProperty("profile_image_url") String profileImageUrl
            ) {}
        }
    }
}
