package wisoft.io.miseprep_api.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wisoft.io.miseprep_api.global.enums.Category;
import wisoft.io.miseprep_api.global.exception.BusinessException;
import wisoft.io.miseprep_api.global.exception.ErrorCode;
import wisoft.io.miseprep_api.member.entity.Member;
import wisoft.io.miseprep_api.member.repository.MemberRepository;
import wisoft.io.miseprep_api.product.dto.request.CreateProductRequest;
import wisoft.io.miseprep_api.product.dto.response.ProductResponse;
import wisoft.io.miseprep_api.product.entity.Product;
import wisoft.io.miseprep_api.product.entity.ProductLike;
import wisoft.io.miseprep_api.product.repository.ProductLikeRepository;
import wisoft.io.miseprep_api.product.repository.ProductRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductLikeRepository productLikeRepository;
    private final MemberRepository memberRepository;

    public List<ProductResponse> getProducts(Long memberId, Category category, String keyword) {
        List<Product> products;
        if (category != null) {
            products = productRepository.findByCategory(category);
        } else if (keyword != null) {
            products = productRepository.findByNameContainingIgnoreCase(keyword);
        } else {
            products = productRepository.findAll();
        }
        return products.stream()
                .map(p -> toResponse(p, memberId))
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
        Product saved = productRepository.save(product);
        return toResponse(saved, null);
    }

    public ProductResponse getProduct(Long productId, Long memberId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND));
        return toResponse(product, memberId);
    }

    @Transactional
    public void likeProduct(Long memberId, Long productId) {
        if (productLikeRepository.existsByMemberIdAndProductId(memberId, productId)) {
            throw new BusinessException(ErrorCode.ALREADY_LIKED_PRODUCT);
        }
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND));
        productLikeRepository.save(ProductLike.create(member, product));
    }

    @Transactional
    public void unlikeProduct(Long memberId, Long productId) {
        ProductLike like = productLikeRepository.findByMemberIdAndProductId(memberId, productId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_LIKE_NOT_FOUND));
        productLikeRepository.delete(like);
    }

    public List<ProductResponse> getLikedProducts(Long memberId) {
        return productLikeRepository.findLikedProductsByMemberId(memberId).stream()
                .map(p -> toResponse(p, memberId))
                .toList();
    }

    public List<ProductResponse> getBestProducts(Long memberId) {
        return productLikeRepository.findAllOrderByLikeCountDesc().stream()
                .map(p -> toResponse(p, memberId))
                .toList();
    }

    private ProductResponse toResponse(Product product, Long memberId) {
        long likeCount = productLikeRepository.countByProductId(product.getId());
        boolean isLiked = memberId != null && productLikeRepository.existsByMemberIdAndProductId(memberId, product.getId());
        return ProductResponse.from(product, likeCount, isLiked);
    }
}
