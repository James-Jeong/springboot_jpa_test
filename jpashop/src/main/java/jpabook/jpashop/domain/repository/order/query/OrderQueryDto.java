package jpabook.jpashop.domain.repository.order.query;

import jpabook.jpashop.domain.model.member.Address;
import jpabook.jpashop.domain.model.order.OrderStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(of = "orderId")
public class OrderQueryDto {

    private Long orderId;
    private String memberName;
    private LocalDateTime orderDateTime;
    private OrderStatus orderStatus;
    private Address deliveryAddress;

    private List<OrderItemQueryDto> orderItemQueryDtos;

    public OrderQueryDto(Long orderId, String memberName, LocalDateTime orderDateTime, OrderStatus orderStatus, Address deliveryAddress) {
        this.orderId = orderId;
        this.memberName = memberName;
        this.orderDateTime = orderDateTime;
        this.orderStatus = orderStatus;
        this.deliveryAddress = deliveryAddress;
    }

    public OrderQueryDto(Long orderId, String memberName, LocalDateTime orderDateTime, OrderStatus orderStatus, Address deliveryAddress, List<OrderItemQueryDto> orderItemQueryDtos) {
        this.orderId = orderId;
        this.memberName = memberName;
        this.orderDateTime = orderDateTime;
        this.orderStatus = orderStatus;
        this.deliveryAddress = deliveryAddress;
        this.orderItemQueryDtos = orderItemQueryDtos;
    }
}
