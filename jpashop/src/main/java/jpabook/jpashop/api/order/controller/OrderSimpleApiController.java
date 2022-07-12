package jpabook.jpashop.api.order.controller;

import jpabook.jpashop.api.common.Result;
import jpabook.jpashop.api.order.dto.OrderSimpleDto;
import jpabook.jpashop.domain.model.order.Order;
import jpabook.jpashop.domain.model.order.OrderSearch;
import jpabook.jpashop.domain.repository.OrderRepository;
import jpabook.jpashop.domain.repository.order.simplequery.OrderSimpleQueryDto;
import jpabook.jpashop.domain.repository.order.simplequery.OrderSimpleQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * xToOne (ManyToOne & OneToOne)
 * <p>
 * - Order
 * 1 Order -> N Member
 * 1 Order -> 1 Delivery
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;
    private final OrderSimpleQueryRepository orderSimpleQueryRepository;

    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
        List<Order> orders = orderRepository.findAll(new OrderSearch());
        for (Order order : orders) {
            order.getMember().getName(); // LAZY 강제 초기화
            order.getDelivery().getAddress(); // LAZY 강제 초기화
        }
        return orders;

        // Order 는 멤버와 양방향 연관관계이기 때문에 개박살난다.
        // Order > Member > Order > ...

        // 1. 그럼 양방향 걸리는 곳을 @JsonIgnore 를 선언해줘야 한다.
        // 2. 그런데 Type definition 에러가 발생한다. 왜?
        // - 사용자 정의 클래스를 json 으로 변경 시 ByteBuddy 라이브러리에서 모르기 때문에 변경 불가능하다.
        // > Hibernate5Module 로 해결 가능하다.
        // 3. 하지만 엔티티를 그대로 모두 노출시키기 때문에 API 스펙이 바뀌게 되면 치명적인 유지 보수가 발생한다.
        // 4. 그리고 불필요한 정보를 포함하여 모두 노출되기 때문에 불필요한 쿼리가 발생한다.
    }

    @GetMapping("/api/v2/simple-orders")
    public Result<OrderSimpleDto> ordersV2() {
        List<Order> orders = orderRepository.findAll(new OrderSearch());
        List<OrderSimpleDto> orderDtoList = orders.stream()
                .map(OrderSimpleDto::new)
                .collect(Collectors.toList());
        return new Result(orderDtoList);
    }

    @GetMapping("/api/v3/simple-orders")
    public Result<OrderSimpleDto> ordersV3() {
        List<Order> orders = orderRepository.findAllWithMemberDelivery();
        List<OrderSimpleDto> orderDtoList = orders.stream()
                .map(OrderSimpleDto::new)
                .collect(Collectors.toList());
        return new Result(orderDtoList);
    }

    @GetMapping("/api/v4/simple-orders")
    public List<OrderSimpleQueryDto> ordersV4() {
        return orderSimpleQueryRepository.findOrderDtos();
    }

}
