package jpabook.jpashop.api.order.dto;

import jpabook.jpashop.domain.model.member.Address;
import jpabook.jpashop.domain.model.order.Order;
import jpabook.jpashop.domain.model.order.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class OrderDto {

    private Long orderId;
    private String memberName;
    private LocalDateTime orderDateTime;
    private OrderStatus orderStatus;
    private Address deliveryAddress;

    private List<OrderItemDto> orderItems; // OrderItem 조차도 DTO 로 바꿔야 한다.

    public OrderDto(Order order) {
        this.orderId = order.getId();
        this.memberName = order.getMember().getName(); // LAZY 엔티티 초기화
        this.orderDateTime = order.getOrderDateTime();
        this.orderStatus = order.getOrderStatus();
        this.deliveryAddress = order.getDelivery().getAddress(); // LAZY 엔티티 초기화

        /*order.getOrderItems().stream()
                .forEach(orderItem -> orderItem.getItem().getName()); // LAZY 엔티티 초기화 */
        this.orderItems = order.getOrderItems().stream()
                .map(OrderItemDto::new)
                .collect(Collectors.toList()); // 쿼리가 계속 나간다..
    }

}
