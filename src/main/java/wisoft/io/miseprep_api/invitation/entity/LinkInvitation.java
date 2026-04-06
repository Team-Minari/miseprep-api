package wisoft.io.miseprep_api.invitation.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wisoft.io.miseprep_api.cart.entity.Cart;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "link_invitations")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LinkInvitation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false, unique = true)
    private Cart cart;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public static LinkInvitation create(Cart cart, String token) {
        LinkInvitation invitation = new LinkInvitation();
        invitation.cart = cart;
        invitation.token = token;
        invitation.createdAt = LocalDateTime.now();
        return invitation;
    }
}
