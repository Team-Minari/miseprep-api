package wisoft.io.miseprep_api.cart.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wisoft.io.miseprep_api.global.entity.BaseEntity;
import wisoft.io.miseprep_api.member.entity.Member;
import wisoft.io.miseprep_api.product.entity.Product;

@Getter
@Entity
@Table(
        name = "cart_items",
        uniqueConstraints = @UniqueConstraint(columnNames = {"cart_id", "product_id"})
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "checker_id")
    private Member checker;

    @Column(nullable = false)
    private int quantity;

    public static CartItem create(Cart cart, Product product, int quantity) {
        CartItem item = new CartItem();
        item.cart = cart;
        item.product = product;
        item.quantity = quantity;
        return item;
    }

    public void updateQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void check(Member checker) {
        this.checker = checker;
    }

    public void uncheck() {
        this.checker = null;
    }

    public boolean isChecked() {
        return this.checker != null;
    }
}
