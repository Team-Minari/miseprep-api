package wisoft.io.miseprep_api.cart.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import wisoft.io.miseprep_api.cart.dto.request.*;
import wisoft.io.miseprep_api.cart.dto.response.*;
import wisoft.io.miseprep_api.cart.service.CartService;
import wisoft.io.miseprep_api.global.dto.ApiResponse;

import java.util.List;

@Tag(name = "Cart", description = "장바구니 API")
@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @Operation(summary = "장바구니 생성")
    @PostMapping
    public ResponseEntity<ApiResponse<CartResponse>> createCart(
            @AuthenticationPrincipal Long memberId,
            @RequestBody CreateCartRequest request) {
        CartResponse data = cartService.createCart(memberId, request);
        ApiResponse<CartResponse> response = ApiResponse.of(data, "장바구니를 생성했습니다.");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "내 장바구니 목록 조회")
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<List<CartResponse>>> getMyCarts(@AuthenticationPrincipal Long memberId) {
        List<CartResponse> data = cartService.getMyCarts(memberId);
        ApiResponse<List<CartResponse>> response = ApiResponse.of(data, "참여 중인 장바구니 목록을 조회했습니다.");
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "장바구니 단건 조회")
    @GetMapping("/{cartId}")
    public ResponseEntity<ApiResponse<CartDetailResponse>> getCart(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long cartId) {
        CartDetailResponse data = cartService.getCart(memberId, cartId);
        ApiResponse<CartDetailResponse> response = ApiResponse.of(data, "장바구니를 조회했습니다.");
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "장바구니 참여자 목록 조회")
    @GetMapping("/{cartId}/participants")
    public ResponseEntity<ApiResponse<List<ParticipantResponse>>> getParticipants(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long cartId) {
        List<ParticipantResponse> data = cartService.getParticipants(memberId, cartId);
        ApiResponse<List<ParticipantResponse>> response = ApiResponse.of(data, "참여자 목록을 조회했습니다.");
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "링크로 장바구니 참여", description = "초대 링크의 token 값을 쿼리 파라미터로 전달합니다.")
    @PostMapping("/join")
    public ResponseEntity<ApiResponse<Long>> joinByLink(
            @AuthenticationPrincipal Long memberId,
            @Parameter(description = "초대 링크 토큰") @RequestParam String token) {
        Long cartId = cartService.joinByLink(memberId, token);
        ApiResponse<Long> response = ApiResponse.of(cartId, "장바구니에 참여했습니다.");
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "장바구니 설정 수정 (소유자)", description = "이름, 공개 여부, 목적, 예산을 수정합니다. null 필드는 변경되지 않습니다. budget=0 이면 예산 제한을 해제합니다.")
    @PatchMapping("/{cartId}/settings")
    public ResponseEntity<ApiResponse<CartResponse>> updateCartSetting(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long cartId,
            @RequestBody UpdateCartSettingRequest request) {
        CartResponse data = cartService.updateCartSetting(memberId, cartId, request);
        ApiResponse<CartResponse> response = ApiResponse.of(data, "장바구니 설정을 변경했습니다.");
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "장바구니 삭제 (소유자)", description = "장바구니와 모든 데이터를 삭제합니다. 참여자 전원이 퇴장됩니다.")
    @DeleteMapping("/{cartId}")
    public ResponseEntity<ApiResponse<Void>> deleteCart(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long cartId) {
        cartService.deleteCart(memberId, cartId);
        ApiResponse<Void> response = ApiResponse.of(null, "장바구니를 삭제했습니다.");
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "장바구니 나가기", description = "소유자는 나갈 수 없습니다. 소유권을 먼저 이전한 후 나가주세요.")
    @DeleteMapping("/{cartId}/participants/me")
    public ResponseEntity<ApiResponse<Void>> leaveCart(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long cartId) {
        cartService.leaveCart(memberId, cartId);
        ApiResponse<Void> response = ApiResponse.of(null, "장바구니에서 탈퇴했습니다.");
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "장바구니 상품 목록 조회")
    @GetMapping("/{cartId}/items")
    public ResponseEntity<ApiResponse<List<CartItemResponse>>> getCartItems(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long cartId) {
        List<CartItemResponse> data = cartService.getCartItems(memberId, cartId);
        ApiResponse<List<CartItemResponse>> response = ApiResponse.of(data, "장바구니 상품 목록을 조회했습니다.");
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "장바구니 상품 추가")
    @PostMapping("/{cartId}/items")
    public ResponseEntity<ApiResponse<CartItemResponse>> addItem(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long cartId,
            @RequestBody AddCartItemRequest request) {
        CartItemResponse data = cartService.addItem(memberId, cartId, request);
        ApiResponse<CartItemResponse> response = ApiResponse.of(data, "상품을 추가했습니다.");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "장바구니 상품 수량 수정")
    @PatchMapping("/{cartId}/items/{itemId}")
    public ResponseEntity<ApiResponse<CartItemResponse>> updateItem(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long cartId,
            @PathVariable Long itemId,
            @RequestBody UpdateCartItemRequest request) {
        CartItemResponse data = cartService.updateItem(memberId, cartId, itemId, request);
        ApiResponse<CartItemResponse> response = ApiResponse.of(data, "상품 수량을 변경했습니다.");
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "장바구니 상품 단건 삭제", description = "체크 여부와 관계없이 장바구니의 상품을 삭제합니다.")
    @DeleteMapping("/{cartId}/items/{itemId}")
    public ResponseEntity<ApiResponse<Void>> deleteItem(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long cartId,
            @PathVariable Long itemId) {
        cartService.deleteItem(memberId, cartId, itemId);
        ApiResponse<Void> response = ApiResponse.of(null, "상품을 삭제했습니다.");
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "장바구니 상품 체크", description = "상품을 본인 것으로 찜합니다. 이미 다른 사람이 체크한 상품은 체크할 수 없습니다.")
    @PostMapping("/{cartId}/items/{itemId}/check")
    public ResponseEntity<ApiResponse<CartItemResponse>> checkItem(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long cartId,
            @PathVariable Long itemId) {
        CartItemResponse data = cartService.checkItem(memberId, cartId, itemId);
        ApiResponse<CartItemResponse> response = ApiResponse.of(data, "상품을 체크했습니다.");
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "장바구니 상품 체크 해제")
    @DeleteMapping("/{cartId}/items/{itemId}/check")
    public ResponseEntity<ApiResponse<CartItemResponse>> uncheckItem(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long cartId,
            @PathVariable Long itemId) {
        CartItemResponse data = cartService.uncheckItem(memberId, cartId, itemId);
        ApiResponse<CartItemResponse> response = ApiResponse.of(data, "상품 체크를 해제했습니다.");
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "장바구니 상품 일괄 삭제", description = "체크 여부와 관계없이 장바구니의 모든 상품을 삭제합니다.")
    @DeleteMapping("/{cartId}/items")
    public ResponseEntity<ApiResponse<Void>> deleteAllItems(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long cartId
    ) {
        cartService.deleteAllItems(memberId, cartId);
        ApiResponse<Void> response = ApiResponse.of(null, "상품을 일괄적으로 삭제했습니다.");
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "참여자 강퇴 (소유자)", description = "소유자는 강퇴할 수 없습니다. 강퇴된 참여자의 체크 상태는 해제됩니다.")
    @DeleteMapping("/{cartId}/participants/{targetMemberId}")
    public ResponseEntity<ApiResponse<Void>> kickParticipant(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long cartId,
            @PathVariable Long targetMemberId) {
        cartService.kickParticipant(memberId, cartId, targetMemberId);
        ApiResponse<Void> response = ApiResponse.of(null, "참여자를 강퇴했습니다.");
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "소유권 이전 (소유자)", description = "현재 참여 중인 멤버에게만 소유권을 이전할 수 있습니다.")
    @PatchMapping("/{cartId}/owner")
    public ResponseEntity<ApiResponse<OwnerTransferResponse>> transferOwner(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long cartId,
            @RequestBody OwnerTransferRequest request
    ) {
        OwnerTransferResponse data = cartService.transferOwner(memberId, cartId, request);
        ApiResponse<OwnerTransferResponse> response = ApiResponse.of(data, "장바구니 소유자가 이전됐습니다.");
        return ResponseEntity.ok(response);
    }
}
