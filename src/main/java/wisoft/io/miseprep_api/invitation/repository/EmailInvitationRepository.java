package wisoft.io.miseprep_api.invitation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wisoft.io.miseprep_api.invitation.entity.EmailInvitation;
import wisoft.io.miseprep_api.invitation.entity.enums.InvitationStatus;

import java.util.List;
import java.util.Optional;

public interface EmailInvitationRepository extends JpaRepository<EmailInvitation, Long> {

    List<EmailInvitation> findAllByInviteeIdAndStatus(Long inviteeId, InvitationStatus status);

    boolean existsByCartIdAndInviteeIdAndStatus(Long cartId, Long inviteeId, InvitationStatus status);

    Optional<EmailInvitation> findByIdAndInviteeId(Long id, Long inviteeId);

    Optional<EmailInvitation> findByIdAndInviterId(Long id, Long inviterId);

    void deleteAllByCartId(Long cartId);
}
