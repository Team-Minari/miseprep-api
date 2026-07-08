package wisoft.io.miseprep_api.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import wisoft.io.miseprep_api.cart.entity.Cart;
import wisoft.io.miseprep_api.cart.entity.CartLike;

import java.util.List;
import java.util.Optional;

public interface CartLikeRepository extends JpaRepository<CartLike, Long> {

    boolean existsByMemberIdAndCartId(Long memberId, Long cartId);

    Optional<CartLike> findByMemberIdAndCartId(Long memberId, Long cartId);

    long countByCartId(Long cartId);

    @Query("SELECT c FROM Cart c WHERE c.isPublic = true ORDER BY (SELECT COUNT(cl) FROM CartLike cl WHERE cl.cart = c) DESC")
    List<Cart> findAllPublicOrderByLikeCountDesc();
}
