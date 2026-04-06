package wisoft.io.miseprep_api.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wisoft.io.miseprep_api.auth.dto.request.TestLoginRequest;
import wisoft.io.miseprep_api.auth.dto.response.AuthResponse;
import wisoft.io.miseprep_api.auth.service.AuthService;
import wisoft.io.miseprep_api.global.dto.ApiResponse;

@Tag(name = "Test", description = "테스트용 API")
@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestAuthController {

    private final AuthService authService;

    @Operation(summary = "[테스트용] 회원 생성 및 로그인", description = "이메일로 회원을 생성하거나 기존 회원으로 로그인합니다. 인증 불필요.")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> testLogin(@RequestBody TestLoginRequest request) {
        AuthResponse data = authService.testLogin(request);
        ApiResponse<AuthResponse> response = ApiResponse.of(data, "테스트 로그인이 완료되었습니다.");
        return ResponseEntity.ok(response);
    }
}
