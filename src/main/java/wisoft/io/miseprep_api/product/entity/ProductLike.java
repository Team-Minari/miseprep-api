package wisoft.io.miseprep_api.product.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wisoft.io.miseprep_api.member.entity.Member;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(
        name = "product_likes",
        uniqueConstraints = @UniqueConstraint(columnNames = {"member_id", "product_id"})
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private LocalDateTime likedAt;

    public static ProductLike create(Member member, Product product) {
        ProductLike like = new ProductLike();
        like.member = member;
        like.product = product;
        like.likedAt = LocalDateTime.now();
        return like;
    }
}
