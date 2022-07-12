package jpabook.jpashop.domain.model.order;

import jpabook.jpashop.domain.model.delivery.Delivery;
import jpabook.jpashop.domain.model.delivery.DeliveryStatus;
import jpabook.jpashop.domain.model.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "orders")
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id") // FK 를 가지고 있으므로 연관관계의 주인이 된다.
    private Member member;

    //@BatchSize(size = 10)
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id") // Order 가 연관관계 주인이 된다.
    private Delivery delivery;

    private LocalDateTime orderDateTime;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    public Order(Member member, Delivery delivery) {
        this.member = member;

        this.delivery = delivery;

        this.orderDateTime = LocalDateTime.now();
        this.orderStatus = OrderStatus.IDLE;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
        Order order = new Order(member, delivery);

        for (OrderItem orderItem : orderItems) {
            if (orderItem.run()) {
                order.addOrderItem(orderItem);
            }
        }

        order.setOrderStatus(OrderStatus.ORDER);
        return order;
    }

    private void addOrderItem(OrderItem orderItem) {
        if (orderItem == null) { return; }
        if (!this.orderItems.contains(orderItem)) {
            this.orderItems.add(orderItem);
            orderItem.setOrder(this); // 상호 연결 필요함
        }
    }

    public void cancel() {
        if (delivery.getDeliveryStatus() == DeliveryStatus.COMPLETED) {
            throw new IllegalStateException("이미 배송 완료되어서 취소가 불가능합니다.");
        }

        setOrderStatus(OrderStatus.CANCEL);
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
            orderItem.setOrder(null);
        }
        orderItems.clear();
    }

    public int getTotalPrice() {
        return orderItems.stream()
                .mapToInt(OrderItem::getTotalPrice)
                .sum();
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", member=" + member.getId() +
                ", orderItems=" + orderItems.size() +
                ", delivery=" + delivery.getId() +
                ", orderDateTime=" + orderDateTime +
                ", orderStatus=" + orderStatus +
                '}';
    }
}
