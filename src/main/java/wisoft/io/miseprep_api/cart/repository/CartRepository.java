package wisoft.io.miseprep_api.cart.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import wisoft.io.miseprep_api.cart.entity.Cart;
import wisoft.io.miseprep_api.global.enums.Category;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("SELECT c FROM Cart c JOIN CartParticipant cp ON cp.cart = c WHERE cp.member.id = :memberId")
    List<Cart> findAllByMemberId(Long memberId);

    List<Cart> findAllByOwnerId(Long ownerId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM Cart c WHERE c.id = :cartId")
    Optional<Cart> findByIdWithLock(Long cartId);

    List<Cart> findAllByIsPublicTrue();

    List<Cart> findAllByIsPublicTrueAndCategory(Category category);
}
