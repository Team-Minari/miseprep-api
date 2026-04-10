package wisoft.io.miseprep_api.cart.dto.request;

public record UpdateCartSettingRequest(
        String cartName,
        Boolean isPublic,
        String purpose,
        Integer budget
) {
}
