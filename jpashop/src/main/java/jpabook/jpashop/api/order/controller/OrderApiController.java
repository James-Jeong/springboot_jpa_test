package jpabook.jpashop.api.order.controller;

import jpabook.jpashop.api.common.Result;
import jpabook.jpashop.api.order.dto.OrderDto;
import jpabook.jpashop.domain.model.order.Order;
import jpabook.jpashop.domain.model.order.OrderItem;
import jpabook.jpashop.domain.model.order.OrderSearch;
import jpabook.jpashop.domain.repository.OrderRepository;
import jpabook.jpashop.domain.repository.order.query.OrderFlatDto;
import jpabook.jpashop.domain.repository.order.query.OrderItemQueryDto;
import jpabook.jpashop.domain.repository.order.query.OrderQueryDto;
import jpabook.jpashop.domain.repository.order.query.OrderQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;

    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1() {
        List<Order> orders = orderRepository.findAll(new OrderSearch());
        for (Order order : orders) {
            order.getMember().getName(); // lazy init
            order.getDelivery().getAddress(); // lazy init

            List<OrderItem> orderItems = order.getOrderItems();
            orderItems.stream().forEach(
                    orderItem -> orderItem.getItem().getName() // lazy init
            );
        }
        return orders;
    }

    @GetMapping("/api/v2/orders")
    public Result<OrderDto> ordersV2() { // Query N 번
        List<Order> orders = orderRepository.findAll(new OrderSearch());
        List<OrderDto> orderDtoList = orders.stream()
                .map(OrderDto::new)
                .collect(Collectors.toList());
        return new Result(orderDtoList);
    }

    @GetMapping("/api/v3/orders")
    public Result<OrderDto> ordersV3() { // Query 1 번
        List<Order> orders = orderRepository.findAllWithItem();
        List<OrderDto> orderDtoList = orders.stream()
                .map(OrderDto::new)
                .collect(Collectors.toList());
        return new Result(orderDtoList);
    }

    @GetMapping("/api/v3.1/orders")
    public Result<OrderDto> ordersV3_page(@RequestParam(value = "offest", defaultValue = "0") int offset,
                                          @RequestParam(value = "limit", defaultValue = "100") int limit) {
        //List<Order> orders = orderRepository.findAllWithMemberDelivery();
        List<Order> orders = orderRepository.findAllWithMemberDeliveryWithLimit(offset, limit);
        List<OrderDto> orderDtoList = orders.stream()
                .map(OrderDto::new)
                .collect(Collectors.toList());
        return new Result(orderDtoList);
    }

    @GetMapping("/api/v4/orders")
    public Result<OrderQueryDto> ordersV4() {
        return new Result(orderQueryRepository.findOrderQueryDtos());
    }

    @GetMapping("/api/v5/orders")
    public Result<OrderQueryDto> orderV5() {
        return new Result(orderQueryRepository.findOrderQueryDtos_optimization());
    }

    /**
     * 장점 : Query 1 번
     * 단점
     * - Order 를 기준으로 페이징 불가
     * - 쿼리 결과가 많을 경우 애플리케이션에 과부하 발생 가능 또는 V5 보다 느릴 수 있음
     * > 무조건 쿼리 줄인다고 좋은 건 아님 > TradeOff 를 생각해야함
     */
    @GetMapping("/api/v6/orders")
    public Result<OrderQueryDto> orderV6() {
        List<OrderFlatDto> orderFlatDtos = orderQueryRepository.findOrderQueryDtos_optimization_flat();

        List<OrderQueryDto> orderQueryDtos = orderFlatDtos.stream()
                .collect(Collectors.groupingBy(
                        orderFlatDto -> new OrderQueryDto(
                                orderFlatDto.getOrderId(), orderFlatDto.getMemberName(), orderFlatDto.getOrderDateTime(),
                                orderFlatDto.getOrderStatus(), orderFlatDto.getDeliveryAddress()
                        ),
                        Collectors.mapping(orderFlatDto -> new OrderItemQueryDto(
                                        orderFlatDto.getOrderId(), orderFlatDto.getItemName(),
                                        orderFlatDto.getOrderPrice(), orderFlatDto.getCount()
                                ), Collectors.toList()
                        )
                ))
                .entrySet().stream()
                .map(e -> new OrderQueryDto(
                        e.getKey().getOrderId(), e.getKey().getMemberName(),
                        e.getKey().getOrderDateTime(), e.getKey().getOrderStatus(),
                        e.getKey().getDeliveryAddress(), e.getValue()
                ))
                .collect(Collectors.toList());

        return new Result(orderQueryDtos);
    }

}
