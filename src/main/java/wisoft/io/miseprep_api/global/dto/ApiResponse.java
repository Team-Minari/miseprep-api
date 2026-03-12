package wisoft.io.miseprep_api.global.dto;

public record ApiResponse<T>(
        T data,
        String message
) {

    public static <T> ApiResponse<T> of(T data, String message) {
        return new ApiResponse<T>(data, message);
    }
}
