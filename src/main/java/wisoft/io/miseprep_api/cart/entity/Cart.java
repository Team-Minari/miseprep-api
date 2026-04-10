package wisoft.io.miseprep_api.cart.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wisoft.io.miseprep_api.global.entity.BaseEntity;
import wisoft.io.miseprep_api.member.entity.Member;

@Getter
@Entity
@Table(name = "carts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cart extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private Member owner;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String purpose;

    @Column(nullable = false)
    private boolean isPublic;

    @Column
    private Integer budget;

    public static Cart create(Member owner, String name, String purpose, boolean isPublic, Integer budget) {
        Cart cart = new Cart();
        cart.owner = owner;
        cart.name = name;
        cart.purpose = purpose;
        cart.isPublic = isPublic;
        cart.budget = budget;
        return cart;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateBudget(Integer budget) {
        this.budget = budget;
    }

    public void updatePurpose(String purpose) {
        this.purpose = purpose;
    }

    public void updateIsPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public void transferOwner(Member owner) {
        this.owner = owner;
    }

    public boolean isOwner(Long memberId) {
        return this.owner.getId().equals(memberId);
    }
}
