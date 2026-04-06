package wisoft.io.miseprep_api.invitation.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wisoft.io.miseprep_api.cart.entity.Cart;
import wisoft.io.miseprep_api.invitation.entity.enums.InvitationStatus;
import wisoft.io.miseprep_api.member.entity.Member;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "email_invitations")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailInvitation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inviter_id", nullable = false)
    private Member inviter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invitee_id", nullable = false)
    private Member invitee;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InvitationStatus status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public static EmailInvitation create(Cart cart, Member inviter, Member invitee) {
        EmailInvitation invitation = new EmailInvitation();
        invitation.cart = cart;
        invitation.inviter = inviter;
        invitation.invitee = invitee;
        invitation.status = InvitationStatus.PENDING;
        invitation.createdAt = LocalDateTime.now();
        return invitation;
    }

    public void accept() {
        this.status = InvitationStatus.ACCEPTED;
    }

    public void reject() {
        this.status = InvitationStatus.REJECTED;
    }

    public boolean isPending() {
        return this.status == InvitationStatus.PENDING;
    }
}
