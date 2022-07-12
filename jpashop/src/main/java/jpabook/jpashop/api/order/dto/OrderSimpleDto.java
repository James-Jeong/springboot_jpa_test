package jpabook.jpashop.api.order.dto;

import jpabook.jpashop.domain.model.member.Address;
import jpabook.jpashop.domain.model.order.Order;
import jpabook.jpashop.domain.model.order.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderSimpleDto {

    private Long orderId;
    private String memberName;
    private LocalDateTime orderDateTime;
    private OrderStatus orderStatus;
    private Address deliveryAddress;

    public OrderSimpleDto(Order order) {
        orderId = order.getId();
        memberName = order.getMember().getName(); // LAZY 엔티티 초기화
        orderDateTime = order.getOrderDateTime();
        orderStatus = order.getOrderStatus();
        deliveryAddress = order.getDelivery().getAddress(); // LAZY 엔티티 초기화
    }

}
