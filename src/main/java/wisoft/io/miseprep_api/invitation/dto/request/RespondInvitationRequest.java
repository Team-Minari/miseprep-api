package wisoft.io.miseprep_api.invitation.dto.request;

import wisoft.io.miseprep_api.invitation.entity.enums.InvitationStatus;

public record RespondInvitationRequest(InvitationStatus status) {
}
