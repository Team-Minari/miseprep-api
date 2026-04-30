package wisoft.io.miseprep_api.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wisoft.io.miseprep_api.auth.client.KakaoProperties;
import wisoft.io.miseprep_api.auth.dto.request.TokenRefreshRequest;
import wisoft.io.miseprep_api.auth.dto.response.AuthResponse;
import wisoft.io.miseprep_api.auth.service.AuthService;
import wisoft.io.miseprep_api.global.dto.ApiResponse;
import wisoft.io.miseprep_api.global.exception.BusinessException;
import wisoft.io.miseprep_api.global.exception.ErrorCode;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Tag(name = "Auth", description = "인증 API")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private static final String KAKAO_AUTH_URL = "https://kauth.kakao.com/oauth/authorize";

    private final AuthService authService;
    private final KakaoProperties kakaoProperties;

    @Operation(summary = "카카오 로그인", description = "브라우저에서 직접 접속 — 카카오 로그인 페이지로 리다이렉트됩니다.")
    @GetMapping("/oauth/kakao/authorize")
    public ResponseEntity<Void> authorize(@RequestParam("redirect_uri") String redirectUri) {
        if (!kakaoProperties.allowedRedirectUris().contains(redirectUri)) {
            throw new BusinessException(ErrorCode.INVALID_REDIRECT_URI);
        }
        String kakaoLoginUrl = KAKAO_AUTH_URL
                + "?client_id=" + kakaoProperties.clientId()
                + "&redirect_uri=" + URLEncoder.encode(redirectUri, StandardCharsets.UTF_8)
                + "&response_type=code";
        return ResponseEntity.status(302).location(URI.create(kakaoLoginUrl)).build();
    }

    @Operation(summary = "카카오 로그인 콜백", description = "카카오 서버가 자동 호출 — 직접 호출 불필요")
    @GetMapping("/oauth/kakao/callback")
    public ResponseEntity<ApiResponse<AuthResponse>> callback(@RequestParam String code, @RequestParam("redirect_uri") String redirectUri) {
        AuthResponse data = authService.login(code, redirectUri);
        ApiResponse<AuthResponse> response = ApiResponse.of(data, "로그인이 완료되었습니다.");
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "액세스 토큰 재발급")
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refresh(@RequestBody TokenRefreshRequest request) {
        AuthResponse data = authService.refresh(request);
        ApiResponse<AuthResponse> response = ApiResponse.of(data, "액세스 토큰이 재발급되었습니다.");
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "로그아웃")
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@RequestHeader("Refresh-Token") String refreshToken) {
        authService.logout(refreshToken);
        ApiResponse<Void> response = ApiResponse.of(null, "로그아웃이 완료되었습니다.");
        return ResponseEntity.ok(response);
    }
}
