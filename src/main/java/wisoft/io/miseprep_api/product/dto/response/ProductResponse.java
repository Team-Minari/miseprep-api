package wisoft.io.miseprep_api.product.dto.response;

import wisoft.io.miseprep_api.global.enums.Category;
import wisoft.io.miseprep_api.product.entity.Product;

public record ProductResponse(
        Long id,
        String name,
        String description,
        int price,
        String imageUrl,
        Category category,
        long likeCount,
        boolean isLiked
) {
    public static ProductResponse from(Product product, long likeCount, boolean isLiked) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getImageUrl(),
                product.getCategory(),
                likeCount,
                isLiked
        );
    }
}
