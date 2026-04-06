package wisoft.io.miseprep_api.cart.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import wisoft.io.miseprep_api.cart.entity.CartItem;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findAllByCartId(Long cartId);

    Optional<CartItem> findByIdAndCartId(Long id, Long cartId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT ci FROM CartItem ci WHERE ci.id = :id AND ci.cart.id = :cartId")
    Optional<CartItem> findByIdAndCartIdWithLock(Long id, Long cartId);

    Optional<CartItem> findByCartIdAndProductId(Long cartId, Long productId);

    void deleteAllByCartId(Long cartId);

    List<CartItem> findAllByCheckerId(Long checkerId);
}
