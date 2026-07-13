package wisoft.io.miseprep_api.order.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wisoft.io.miseprep_api.cart.entity.Cart;
import wisoft.io.miseprep_api.global.entity.BaseEntity;
import wisoft.io.miseprep_api.member.entity.Member;
import wisoft.io.miseprep_api.order.entity.enums.OrderStatus;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {

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
    private String shippingAddress;

    @Column(nullable = false)
    private int totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Column
    private String paymentKey;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items = new ArrayList<>();

    public static Order create(Member member, Cart cart, String shippingAddress, int totalAmount) {
        Order order = new Order();
        order.member = member;
        order.cart = cart;
        order.shippingAddress = shippingAddress;
        order.totalAmount = totalAmount;
        order.status = OrderStatus.PENDING;
        return order;
    }

    public void paid(String paymentKey) {
        this.status = OrderStatus.PAID;
        this.paymentKey = paymentKey;
    }

    public void failed() {
        this.status = OrderStatus.FAILED;
    }
}
