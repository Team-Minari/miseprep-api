package wisoft.io.miseprep_api.invitation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wisoft.io.miseprep_api.cart.entity.Cart;
import wisoft.io.miseprep_api.cart.entity.CartParticipant;
import wisoft.io.miseprep_api.cart.repository.CartParticipantRepository;
import wisoft.io.miseprep_api.cart.repository.CartRepository;
import wisoft.io.miseprep_api.global.exception.BusinessException;
import wisoft.io.miseprep_api.global.exception.ErrorCode;
import wisoft.io.miseprep_api.invitation.dto.request.RespondInvitationRequest;
import wisoft.io.miseprep_api.invitation.dto.request.SendInvitationRequest;
import wisoft.io.miseprep_api.invitation.dto.response.InvitationResponse;
import wisoft.io.miseprep_api.invitation.entity.EmailInvitation;
import wisoft.io.miseprep_api.invitation.entity.enums.InvitationStatus;
import wisoft.io.miseprep_api.invitation.repository.EmailInvitationRepository;
import wisoft.io.miseprep_api.member.entity.Member;
import wisoft.io.miseprep_api.member.repository.MemberRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class InvitationService {

    private final EmailInvitationRepository emailInvitationRepository;
    private final CartRepository cartRepository;
    private final CartParticipantRepository cartParticipantRepository;
    private final MemberRepository memberRepository;

    public InvitationResponse sendInvitation(Long inviterId, Long cartId, SendInvitationRequest request) {
        Cart cart = findCartAsParticipant(inviterId, cartId);
        Member invitee = memberRepository.findByEmail(request.email())
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        if (invitee.getId().equals(inviterId)) throw new BusinessException(ErrorCode.SELF_INVITATION_NOT_ALLOWED);
        if (cartParticipantRepository.existsByCartIdAndMemberId(cartId, invitee.getId())) {
            throw new BusinessException(ErrorCode.ALREADY_CART_PARTICIPANT);
        }
        if (emailInvitationRepository.existsByCartIdAndInviteeIdAndStatus(cartId, invitee.getId(), InvitationStatus.PENDING)) {
            throw new BusinessException(ErrorCode.INVITATION_ALREADY_PENDING);
        }

        Member inviter = memberRepository.findById(inviterId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
        EmailInvitation invitation = emailInvitationRepository.save(EmailInvitation.create(cart, inviter, invitee));
        return InvitationResponse.from(invitation);
    }

    @Transactional(readOnly = true)
    public List<InvitationResponse> getMyInvitations(Long memberId) {
        return emailInvitationRepository.findAllByInviteeIdAndStatus(memberId, InvitationStatus.PENDING).stream()
                .map(InvitationResponse::from)
                .toList();
    }

    public InvitationResponse respond(Long memberId, Long invitationId, RespondInvitationRequest request) {
        EmailInvitation invitation = emailInvitationRepository.findByIdAndInviteeId(invitationId, memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.INVITATION_NOT_FOUND));
        if (!invitation.isPending()) throw new BusinessException(ErrorCode.INVITATION_NOT_FOUND);

        if (request.status() == InvitationStatus.ACCEPTED) {
            if (cartParticipantRepository.existsByCartIdAndMemberId(invitation.getCart().getId(), memberId)) {
                throw new BusinessException(ErrorCode.ALREADY_CART_PARTICIPANT);
            }
            invitation.accept();
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
            cartParticipantRepository.save(CartParticipant.create(invitation.getCart(), member));
        } else if (request.status() == InvitationStatus.REJECTED) {
            invitation.reject();
        }

        return InvitationResponse.from(invitation);
    }

    public void cancel(Long inviterId, Long invitationId) {
        EmailInvitation invitation = emailInvitationRepository.findByIdAndInviterId(invitationId, inviterId)
                .orElseThrow(() -> new BusinessException(ErrorCode.INVITATION_NOT_FOUND));
        if (!invitation.isPending()) throw new BusinessException(ErrorCode.INVITATION_NOT_FOUND);
        emailInvitationRepository.delete(invitation);
    }

    private Cart findCartAsParticipant(Long memberId, Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CART_NOT_FOUND));
        if (!cartParticipantRepository.existsByCartIdAndMemberId(cartId, memberId)) {
            throw new BusinessException(ErrorCode.CART_ACCESS_DENIED);
        }
        return cart;
    }
}
