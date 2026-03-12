package wisoft.io.miseprep_api.global.exception;

import org.springframework.validation.FieldError;

public record ErrorDetail(
        String field,
        String message
) {
    public static ErrorDetail of(FieldError fieldError) {
        return new ErrorDetail(fieldError.getField(), fieldError.getDefaultMessage());
    }
}
