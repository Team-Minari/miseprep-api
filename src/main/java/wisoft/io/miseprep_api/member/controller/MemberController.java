package wisoft.io.miseprep_api.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import wisoft.io.miseprep_api.global.dto.ApiResponse;
import wisoft.io.miseprep_api.member.dto.request.UpdateMemberRequest;
import wisoft.io.miseprep_api.member.dto.response.MemberResponse;
import wisoft.io.miseprep_api.member.service.MemberService;

@Tag(name = "Member", description = "회원 API")
@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "내 정보 조회")
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<MemberResponse>> getMe(@AuthenticationPrincipal Long memberId) {
        MemberResponse data = memberService.getMe(memberId);
        ApiResponse<MemberResponse> response = ApiResponse.of(data, "내 정보를 조회했습니다.");
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "내 정보 수정")
    @PatchMapping("/me")
    public ResponseEntity<ApiResponse<MemberResponse>> updateMe(
            @AuthenticationPrincipal Long memberId,
            @RequestBody UpdateMemberRequest request) {
        MemberResponse data = memberService.updateMe(memberId, request);
        ApiResponse<MemberResponse> response = ApiResponse.of(data, "내 정보를 수정했습니다.");
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "회원 탈퇴")
    @DeleteMapping("/me")
    public ResponseEntity<ApiResponse<Void>> deleteMe(@AuthenticationPrincipal Long memberId) {
        memberService.deleteMe(memberId);
        ApiResponse<Void> response = ApiResponse.of(null, "회원 탈퇴가 완료되었습니다.");
        return ResponseEntity.ok(response);
    }
}
