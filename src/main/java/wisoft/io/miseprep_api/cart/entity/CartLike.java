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
        name = "cart_likes",
        uniqueConstraints = @UniqueConstraint(columnNames = {"member_id", "cart_id"})
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @Column(nullable = false)
    private LocalDateTime likedAt;

    public static CartLike create(Member member, Cart cart) {
        CartLike like = new CartLike();
        like.member = member;
        like.cart = cart;
        like.likedAt = LocalDateTime.now();
        return like;
    }
}
