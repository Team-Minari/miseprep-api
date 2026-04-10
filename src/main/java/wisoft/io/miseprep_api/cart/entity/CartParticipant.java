package wisoft.io.miseprep_api.cart.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wisoft.io.miseprep_api.member.entity.Member;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(
        name = "cart_participants",
        uniqueConstraints = @UniqueConstraint(columnNames = {"cart_id", "member_id"})
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private LocalDateTime joinedAt;

    public static CartParticipant create(Cart cart, Member member) {
        CartParticipant participant = new CartParticipant();
        participant.cart = cart;
        participant.member = member;
        participant.joinedAt = LocalDateTime.now();
        return participant;
    }
}
