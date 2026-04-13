package wisoft.io.miseprep_api.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wisoft.io.miseprep_api.cart.entity.CartParticipant;

import java.util.List;
import java.util.Optional;

public interface CartParticipantRepository extends JpaRepository<CartParticipant, Long> {

    boolean existsByCartIdAndMemberId(Long cartId, Long memberId);

    Optional<CartParticipant> findByCartIdAndMemberId(Long cartId, Long memberId);

    List<CartParticipant> findAllByCartId(Long cartId);

    void deleteAllByCartId(Long cartId);

    void deleteAllByMemberId(Long memberId);
}
