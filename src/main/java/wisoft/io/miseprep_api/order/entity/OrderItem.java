package wisoft.io.miseprep_api.order.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "order_items")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private int productPrice;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private int subtotal;

    public static OrderItem create(Order order, String productName, int productPrice, int quantity) {
        OrderItem item = new OrderItem();
        item.order = order;
        item.productName = productName;
        item.productPrice = productPrice;
        item.quantity = quantity;
        item.subtotal = productPrice * quantity;
        return item;
    }
}
