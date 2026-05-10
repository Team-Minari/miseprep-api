package wisoft.io.miseprep_api.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wisoft.io.miseprep_api.global.enums.Category;
import wisoft.io.miseprep_api.global.exception.BusinessException;
import wisoft.io.miseprep_api.global.exception.ErrorCode;
import wisoft.io.miseprep_api.product.dto.request.CreateProductRequest;
import wisoft.io.miseprep_api.product.dto.response.ProductResponse;
import wisoft.io.miseprep_api.product.entity.Product;
import wisoft.io.miseprep_api.product.repository.ProductRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    public List<ProductResponse> getProducts(Category category, String keyword) {
        if (category != null) {
            return productRepository.findByCategory(category).stream()
                    .map(ProductResponse::from)
                    .toList();
        }
        if (keyword != null) {
            return productRepository.findByNameContainingIgnoreCase(keyword).stream()
                    .map(ProductResponse::from)
                    .toList();
        }
        return productRepository.findAll().stream()
                .map(ProductResponse::from)
                .toList();
    }

    @Transactional
    public ProductResponse createProduct(CreateProductRequest request) {
        Product product = Product.create(
                request.name(),
                request.description(),
                request.price(),
                request.imageUrl(),
                request.category()
        );
        return ProductResponse.from(productRepository.save(product));
    }

    public ProductResponse getProduct(Long productId) {
        return productRepository.findById(productId)
                .map(ProductResponse::from)
                .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND));
    }
}
