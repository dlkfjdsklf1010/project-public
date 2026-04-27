package com.commerceapp.order.entity;

import com.commerceapp.product.entity.Product;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "order_item")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    // 주문 당시 상품 이름
    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(nullable = false)
    private int price = 0;

    @Column(nullable = false)
    private int quantity = 0;

    @Column(name = "total_price", nullable = false)
    private int totalPrice = price * quantity;

    public static OrderItem create(Product product, int quantity) {
        OrderItem item = new OrderItem();
        item.product = product;
        item.productName = product.getName();
        item.price = product.getPrice();
        item.quantity = quantity;
        item.totalPrice = item.price * item.quantity;

        return item;
    }

    public void assignOrder(Order order) {
        this.order = order;
    }
}
