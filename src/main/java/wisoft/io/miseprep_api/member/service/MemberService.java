package wisoft.io.miseprep_api.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wisoft.io.miseprep_api.auth.repository.RefreshTokenRepository;
import wisoft.io.miseprep_api.cart.repository.CartItemRepository;
import wisoft.io.miseprep_api.global.exception.BusinessException;
import wisoft.io.miseprep_api.global.exception.ErrorCode;
import wisoft.io.miseprep_api.member.dto.request.UpdateMemberRequest;
import wisoft.io.miseprep_api.member.dto.response.MemberResponse;
import wisoft.io.miseprep_api.member.entity.Member;
import wisoft.io.miseprep_api.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final CartItemRepository cartItemRepository;

    @Transactional(readOnly = true)
    public MemberResponse getMe(Long memberId) {
        Member member = findActiveMember(memberId);
        return MemberResponse.from(member);
    }

    public MemberResponse updateMe(Long memberId, UpdateMemberRequest request) {
        Member member = findActiveMember(memberId);
        member.updateProfile(request.username(), request.profileImageUrl());
        return MemberResponse.from(member);
    }

    public void deleteMe(Long memberId) {
        Member member = findActiveMember(memberId);
        cartItemRepository.findAllByCheckerId(memberId).forEach(item -> item.uncheck());
        refreshTokenRepository.deleteByMemberId(memberId);
        memberRepository.delete(member);
    }

    private Member findActiveMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
    }
}
