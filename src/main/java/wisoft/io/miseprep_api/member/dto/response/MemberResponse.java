package wisoft.io.miseprep_api.member.dto.response;

import wisoft.io.miseprep_api.member.entity.Member;

public record MemberResponse(
        Long id,
        String email,
        String username,
        String profileImageUrl
) {
    public static MemberResponse from(Member member) {
        return new MemberResponse(
                member.getId(),
                member.getEmail(),
                member.getUsername(),
                member.getProfileImageUrl()
        );
    }
}
