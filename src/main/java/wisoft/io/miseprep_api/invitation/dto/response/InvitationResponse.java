package wisoft.io.miseprep_api.invitation.dto.response;

import wisoft.io.miseprep_api.invitation.entity.EmailInvitation;
import wisoft.io.miseprep_api.invitation.entity.enums.InvitationStatus;

public record InvitationResponse(
        Long id,
        Long cartId,
        String cartName,
        Long inviterId,
        String inviterName,
        InvitationStatus status
) {
    public static InvitationResponse from(EmailInvitation invitation) {
        return new InvitationResponse(
                invitation.getId(),
                invitation.getCart().getId(),
                invitation.getCart().getName(),
                invitation.getInviter().getId(),
                invitation.getInviter().getUsername(),
                invitation.getStatus()
        );
    }
}
