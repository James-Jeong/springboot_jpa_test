package jpabook.jpashop.domain.repository.order.simplequery;

import jpabook.jpashop.domain.model.member.Address;
import jpabook.jpashop.domain.model.order.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderSimpleQueryDto {

    private Long orderId;
    private String memberName;
    private LocalDateTime orderDateTime;
    private OrderStatus orderStatus;
    private Address deliveryAddress;

    public OrderSimpleQueryDto(Long orderId, String memberName, LocalDateTime orderDateTime, OrderStatus orderStatus, Address deliveryAddress) {
        this.orderId = orderId;
        this.memberName = memberName;
        this.orderDateTime = orderDateTime;
        this.orderStatus = orderStatus;
        this.deliveryAddress = deliveryAddress;
    }

}
