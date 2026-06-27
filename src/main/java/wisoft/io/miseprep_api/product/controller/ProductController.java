package wisoft.io.miseprep_api.product.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import wisoft.io.miseprep_api.global.dto.ApiResponse;
import wisoft.io.miseprep_api.global.enums.Category;
import wisoft.io.miseprep_api.product.dto.request.CreateProductRequest;
import wisoft.io.miseprep_api.product.dto.response.ProductResponse;
import wisoft.io.miseprep_api.product.service.ProductService;

import java.util.List;

@Tag(name = "Product", description = "상품 API")
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "[테스트용] 상품 생성")
    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(@RequestBody CreateProductRequest request) {
        ProductResponse data = productService.createProduct(request);
        ApiResponse<ProductResponse> response = ApiResponse.of(data, "상품을 생성했습니다.");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "상품 목록 조회", description = "category, keyword로 필터링 가능")
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getProducts(
            @AuthenticationPrincipal Long memberId,
            @RequestParam(required = false) Category category,
            @RequestParam(required = false) String keyword) {
        List<ProductResponse> data = productService.getProducts(memberId, category, keyword);
        ApiResponse<List<ProductResponse>> response = ApiResponse.of(data, "상품 목록을 조회했습니다.");
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "좋아요 순 상품 목록 조회 (베스트)")
    @GetMapping("/best")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getBestProducts(
            @AuthenticationPrincipal Long memberId) {
        List<ProductResponse> data = productService.getBestProducts(memberId);
        ApiResponse<List<ProductResponse>> response = ApiResponse.of(data, "베스트 상품 목록을 조회했습니다.");
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "내가 좋아요한 상품 목록 조회")
    @GetMapping("/liked")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getLikedProducts(
            @AuthenticationPrincipal Long memberId) {
        List<ProductResponse> data = productService.getLikedProducts(memberId);
        ApiResponse<List<ProductResponse>> response = ApiResponse.of(data, "좋아요한 상품 목록을 조회했습니다.");
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "상품 단건 조회")
    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProduct(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long productId) {
        ProductResponse data = productService.getProduct(productId, memberId);
        ApiResponse<ProductResponse> response = ApiResponse.of(data, "상품을 조회했습니다.");
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "상품 좋아요")
    @PostMapping("/{productId}/like")
    public ResponseEntity<ApiResponse<Void>> likeProduct(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long productId) {
        productService.likeProduct(memberId, productId);
        ApiResponse<Void> response = ApiResponse.of(null, "상품에 좋아요를 눌렀습니다.");
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "상품 좋아요 취소")
    @DeleteMapping("/{productId}/like")
    public ResponseEntity<ApiResponse<Void>> unlikeProduct(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long productId) {
        productService.unlikeProduct(memberId, productId);
        ApiResponse<Void> response = ApiResponse.of(null, "상품 좋아요를 취소했습니다.");
        return ResponseEntity.ok(response);
    }
}
