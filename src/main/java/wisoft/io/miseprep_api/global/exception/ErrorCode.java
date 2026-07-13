package wisoft.io.miseprep_api.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // Common
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "입력값이 올바르지 않습니다"),
    INVALID_JSON_FORMAT(HttpStatus.BAD_REQUEST, "JSON 형식이 올바르지 않습니다"),
    INVALID_PATH_VARIABLE(HttpStatus.BAD_REQUEST, "경로 변수 타입이 올바르지 않습니다"),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "지원하지 않는 HTTP 메서드입니다"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다"),

    // Auth
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 리프레시 토큰입니다"),
    UNSUPPORTED_OAUTH_PROVIDER(HttpStatus.BAD_REQUEST, "지원하지 않는 OAuth 제공자입니다"),
    INVALID_KAKAO_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 카카오 토큰입니다"),
    PERSONAL_CART_INVITE_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "개인 장바구니는 초대할 수 없습니다"),
    INVALID_REDIRECT_URI(HttpStatus.BAD_REQUEST, "허용되지 않은 redirect URI입니다"),

    // Member
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다"),
    MEMBER_DELETED(HttpStatus.FORBIDDEN, "탈퇴한 회원입니다"),

    // Product
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 상품입니다"),
    ALREADY_LIKED_PRODUCT(HttpStatus.CONFLICT, "이미 좋아요한 상품입니다"),
    PRODUCT_LIKE_NOT_FOUND(HttpStatus.NOT_FOUND, "좋아요하지 않은 상품입니다"),

    // Cart
    CART_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 장바구니입니다"),
    ALREADY_LIKED_CART(HttpStatus.CONFLICT, "이미 좋아요한 장바구니입니다"),
    CART_LIKE_NOT_FOUND(HttpStatus.NOT_FOUND, "좋아요하지 않은 장바구니입니다"),
    CART_NOT_PUBLIC(HttpStatus.FORBIDDEN, "공개 장바구니에만 좋아요할 수 있습니다"),
    CART_ACCESS_DENIED(HttpStatus.FORBIDDEN, "장바구니에 접근할 권한이 없습니다"),
    CART_OWNER_REQUIRED(HttpStatus.FORBIDDEN, "장바구니 소유자만 수행할 수 있습니다"),
    ALREADY_CART_PARTICIPANT(HttpStatus.CONFLICT, "이미 참여 중인 장바구니입니다"),
    CART_ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 장바구니 상품입니다"),
    CART_ITEM_ALREADY_CHECKED(HttpStatus.CONFLICT, "이미 체크된 상품입니다"),
    CART_ITEM_NOT_CHECKED(HttpStatus.CONFLICT, "체크되지 않은 상품입니다"),
    CART_BUDGET_EXCEEDED(HttpStatus.BAD_REQUEST, "예산을 초과합니다"),
    INVALID_QUANTITY(HttpStatus.BAD_REQUEST, "수량은 1 이상이어야 합니다"),
    INVALID_BUDGET(HttpStatus.BAD_REQUEST, "예산은 0보다 커야 합니다"),
    TRANSFER_TO_NON_PARTICIPANT(HttpStatus.BAD_REQUEST, "장바구니 참여자에게만 소유권을 이전할 수 있습니다"),
    OWNER_CANNOT_LEAVE_CART(HttpStatus.FORBIDDEN, "소유자는 장바구니를 나갈 수 없습니다. 소유권을 이전한 후 나가주세요."),
    CART_OWNER_CANNOT_BE_KICKED(HttpStatus.FORBIDDEN, "소유자는 강퇴할 수 없습니다."),

    // Order
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 주문입니다"),
    ORDER_CART_ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "장바구니에 존재하지 않는 상품이 포함되어 있습니다"),
    PAYMENT_AMOUNT_MISMATCH(HttpStatus.BAD_REQUEST, "결제 금액이 일치하지 않습니다"),
    PAYMENT_CONFIRM_FAILED(HttpStatus.BAD_REQUEST, "결제 승인에 실패했습니다"),

    // Invitation
    INVITATION_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 초대입니다"),
    INVITATION_ALREADY_PENDING(HttpStatus.CONFLICT, "이미 대기 중인 초대가 있습니다"),
    INVALID_LINK_TOKEN(HttpStatus.BAD_REQUEST, "유효하지 않은 초대 링크입니다"),
    SELF_INVITATION_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "본인에게 초대를 보낼 수 없습니다"),
    ;

    private final HttpStatus status;
    private final String message;
}
