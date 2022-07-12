package jpabook.jpashop.domain.model.delivery;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jpabook.jpashop.domain.model.member.Address;
import jpabook.jpashop.domain.model.order.Order;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Delivery {

    @Id
    @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "delivery") // 그냥 읽기 전용, 거울
    private Order order;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    public Delivery(Address address) {
        this.address = address;
        this.deliveryStatus = DeliveryStatus.READY;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void setDeliveryStatus(DeliveryStatus deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }
}
