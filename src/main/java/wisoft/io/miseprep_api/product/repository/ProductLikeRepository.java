package wisoft.io.miseprep_api.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wisoft.io.miseprep_api.product.entity.Product;
import wisoft.io.miseprep_api.product.entity.ProductLike;

import java.util.List;
import java.util.Optional;

public interface ProductLikeRepository extends JpaRepository<ProductLike, Long> {

    boolean existsByMemberIdAndProductId(Long memberId, Long productId);

    Optional<ProductLike> findByMemberIdAndProductId(Long memberId, Long productId);

    long countByProductId(Long productId);

    @Query("SELECT pl.product FROM ProductLike pl WHERE pl.member.id = :memberId")
    List<Product> findLikedProductsByMemberId(@Param("memberId") Long memberId);

    @Query("SELECT p FROM Product p ORDER BY (SELECT COUNT(pl) FROM ProductLike pl WHERE pl.product = p) DESC")
    List<Product> findAllOrderByLikeCountDesc();
}
