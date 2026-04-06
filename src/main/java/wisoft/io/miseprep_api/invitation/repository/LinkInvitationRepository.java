package wisoft.io.miseprep_api.invitation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wisoft.io.miseprep_api.invitation.entity.LinkInvitation;

import java.util.Optional;

public interface LinkInvitationRepository extends JpaRepository<LinkInvitation, Long> {

    Optional<LinkInvitation> findByToken(String token);

    Optional<LinkInvitation> findByCartId(Long cartId);

    void deleteByCartId(Long cartId);
}
