package wisoft.io.miseprep_api.invitation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import wisoft.io.miseprep_api.global.dto.ApiResponse;
import wisoft.io.miseprep_api.invitation.dto.request.RespondInvitationRequest;
import wisoft.io.miseprep_api.invitation.dto.request.SendInvitationRequest;
import wisoft.io.miseprep_api.invitation.dto.response.InvitationResponse;
import wisoft.io.miseprep_api.invitation.entity.enums.InvitationStatus;
import wisoft.io.miseprep_api.invitation.service.InvitationService;

import java.util.List;

@Tag(name = "Invitation", description = "초대 API")
@RestController
@RequiredArgsConstructor
public class InvitationController {

    private final InvitationService invitationService;

    @Operation(summary = "초대 보내기")
    @PostMapping("/api/carts/{cartId}/invitations")
    public ResponseEntity<ApiResponse<InvitationResponse>> sendInvitation(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long cartId,
            @RequestBody SendInvitationRequest request) {
        InvitationResponse data = invitationService.sendInvitation(memberId, cartId, request);
        ApiResponse<InvitationResponse> response = ApiResponse.of(data, "초대를 보냈습니다.");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "내 초대 목록 조회")
    @GetMapping("/api/invitations/me")
    public ResponseEntity<ApiResponse<List<InvitationResponse>>> getMyInvitations(
            @AuthenticationPrincipal Long memberId) {
        List<InvitationResponse> data = invitationService.getMyInvitations(memberId);
        ApiResponse<List<InvitationResponse>> response = ApiResponse.of(data, "초대 목록을 조회했습니다.");
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "초대 수락/거절")
    @PatchMapping("/api/invitations/{invitationId}")
    public ResponseEntity<ApiResponse<InvitationResponse>> respond(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long invitationId,
            @RequestBody RespondInvitationRequest request) {
        InvitationResponse data = invitationService.respond(memberId, invitationId, request);
        String message = request.status() == InvitationStatus.ACCEPTED ? "초대를 수락했습니다." : "초대를 거절했습니다.";
        ApiResponse<InvitationResponse> response = ApiResponse.of(data, message);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "초대 취소")
    @DeleteMapping("/api/invitations/{invitationId}")
    public ResponseEntity<ApiResponse<Void>> cancel(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long invitationId) {
        invitationService.cancel(memberId, invitationId);
        ApiResponse<Void> response = ApiResponse.of(null, "초대를 취소했습니다.");
        return ResponseEntity.ok(response);
    }
}
