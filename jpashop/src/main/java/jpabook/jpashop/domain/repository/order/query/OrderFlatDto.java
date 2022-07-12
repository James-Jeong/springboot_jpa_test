package jpabook.jpashop.domain.repository.order.query;

import jpabook.jpashop.domain.model.member.Address;
import jpabook.jpashop.domain.model.order.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderFlatDto {

    private Long orderId;
    private String memberName;
    private LocalDateTime orderDateTime;
    private OrderStatus orderStatus;
    private Address deliveryAddress;

    private List<OrderItemQueryDto> orderItemQueryDtos;

    private String itemName;
    private int orderPrice;
    private int count;

    public OrderFlatDto(Long orderId, String memberName, LocalDateTime orderDateTime,
                        OrderStatus orderStatus, Address deliveryAddress,
                        String itemName, int orderPrice, int count) {
        this.orderId = orderId;
        this.memberName = memberName;
        this.orderDateTime = orderDateTime;
        this.orderStatus = orderStatus;
        this.deliveryAddress = deliveryAddress;
        this.itemName = itemName;
        this.orderPrice = orderPrice;
        this.count = count;
    }

}
