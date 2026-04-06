package wisoft.io.miseprep_api.cart.dto.response;

import wisoft.io.miseprep_api.cart.entity.CartParticipant;

public record ParticipantResponse(
        Long memberId,
        String username,
        String profileImageUrl
) {
    public static ParticipantResponse from(CartParticipant participant) {
        return new ParticipantResponse(
                participant.getMember().getId(),
                participant.getMember().getUsername(),
                participant.getMember().getProfileImageUrl()
        );
    }
}
