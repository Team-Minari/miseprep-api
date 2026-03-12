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

    // 도메인별 추가 예정
    ;

    private final HttpStatus status;
    private final String message;
}
