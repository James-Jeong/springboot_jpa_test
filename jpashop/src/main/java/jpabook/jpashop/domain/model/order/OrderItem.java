package jpabook.jpashop.domain.model.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jpabook.jpashop.domain.model.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class OrderItem {

    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id") // FK 를 가지므로 연관관계의 주인이 된다.
    private Order order;

    private int orderPrice;

    private int count;

    public OrderItem(Item item, int orderPrice, int count) {
        this.item = item;
        this.orderPrice = orderPrice;
        this.count = count;
    }

    public boolean run() {
        return getItem().decStock(count);
    }

    public void cancel() {
        getItem().incStock(count);
    }

    public int getTotalPrice() {
        return orderPrice * count;
    }

    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        return new OrderItem(item, orderPrice, count);
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + id +
                ", item=" + item.getId() +
                ", order=" + (order != null? order.getId() : -1) +
                ", orderPrice=" + orderPrice +
                ", count=" + count +
                '}';
    }
}
