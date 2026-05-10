package wisoft.io.miseprep_api.cart.dto.request;

import wisoft.io.miseprep_api.global.enums.Category;

public record UpdateCartSettingRequest(
        String cartName,
        Boolean isPublic,
        Category category,
        Integer budget
) {
}
