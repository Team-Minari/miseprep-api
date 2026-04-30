package wisoft.io.miseprep_api.auth.client;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "kakao")
public record KakaoProperties(String clientId, List<String> allowedRedirectUris) {
}
