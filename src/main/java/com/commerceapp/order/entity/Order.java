package com.commerceapp.order.entity;

import com.commerceapp.admin.entity.Admin;
import com.commerceapp.common.entity.BaseEntity;
import com.commerceapp.customer.entity.Customer;
import com.commerceapp.order.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Column(nullable = false, unique = true, length = 20)
    private String orderNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Admin admin;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItemList = new ArrayList<>();

    @Column(name = "total_price", nullable = false)
    private int totalPrice = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrderStatus status = OrderStatus.READY;

    @Column(name = "is_canceled", nullable = false)
    private boolean isCanceled = false;

    @Column(name = "cancel_reason")
    private String cancelReason;

    // 주문 생성 메서드
    public static Order create(Customer customer, Admin admin, String orderNumber) {
        Order order = new Order();
        order.customer = customer;
        order.admin = admin;
        order.orderNumber = orderNumber;

        return order;
    }

    // OrderItem 추가
    public void addOrderItem(OrderItem item) {
        orderItemList.add(item);
        item.assignOrder(this);
        this.totalPrice += item.getTotalSum();
    }

    // 상태 변경
    public void updateStatus() {
        if (this.status == OrderStatus.READY) {
            this.status = OrderStatus.SHIPPING;
        } else if (this.status == OrderStatus.SHIPPING) {
            this.status = OrderStatus.COMPLETED;
        } else {
            throw new IllegalArgumentException("변경 불가 상태");
        }
    }

    // 주문 취소
    public void cancel(String reason) {
        if (this.status != OrderStatus.READY) {
            throw new IllegalArgumentException("상품이 준비중이므로 취소할 수 없습니다.");
        }

        this.status = OrderStatus.CANCELED;
        this.isCanceled = true;
        this.cancelReason = reason;
    }
}
