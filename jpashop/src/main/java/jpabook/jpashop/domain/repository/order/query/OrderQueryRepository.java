package jpabook.jpashop.domain.repository.order.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

    private final EntityManager entityManager;

    public List<OrderQueryDto> findOrderQueryDtos() {
        List<OrderQueryDto> orderQueryDtos = findOrders();
        // Query N + 1 번 > 최적화 필요
        orderQueryDtos.forEach(
                orderQueryDto -> {
                    List<OrderItemQueryDto> orderItemQueryDtos = findOrderItems(orderQueryDto.getOrderId());
                    orderQueryDto.setOrderItemQueryDtos(orderItemQueryDtos);
                }
        );
        return orderQueryDtos;
    }

    /**
     * in 키워드를 사용해서 OrderItem 레코드들을 한 번에 가져오도록 한다.
     * Query 2 번
     */
    public List<OrderQueryDto> findOrderQueryDtos_optimization() {
        List<OrderQueryDto> orders = findOrders(); // Query 1
        List<Long> orderIds = toOrderIds(orders);
        Map<Long, List<OrderItemQueryDto>> orderItemQueryDtoMap = findOrderItemMap(orderIds); // Query 2
        applyOrderItemToOrders(orders, orderItemQueryDtoMap);
        return orders;
    }

    /**
     * [Order 와 OrderItem join] + [OrderItem 과 Item join]
     *
     * Query 1번
     */
    public List<OrderFlatDto> findOrderQueryDtos_optimization_flat() {
        return entityManager.createQuery(
                "select new jpabook.jpashop.domain.repository.order.query.OrderFlatDto(" +
                        "o.id, m.name, o.orderDateTime, " +
                        "o.orderStatus, d.address," +
                        " i.name, oi.orderPrice, oi.count)" +
                        " from Order o" +
                        " join o.member m" +
                        " join o.delivery d" +
                        " join o.orderItems oi " +
                        " join oi.item i",
                OrderFlatDto.class
        ).getResultList();
    }

    private void applyOrderItemToOrders(List<OrderQueryDto> orders, Map<Long, List<OrderItemQueryDto>> orderItemQueryDtoMap) {
        orders.forEach(
                orderQueryDto -> orderQueryDto.setOrderItemQueryDtos(
                        orderItemQueryDtoMap.get(orderQueryDto.getOrderId())
                )
        );
    }

    private Map<Long, List<OrderItemQueryDto>> findOrderItemMap(List<Long> orderIds) {
        List<OrderItemQueryDto> orderItemQueryDtos = entityManager.createQuery(
                        "select new jpabook.jpashop.domain.repository.order.query.OrderItemQueryDto(" +
                                "oi.order.id, i.name, i.price, oi.count)" +
                                " from OrderItem oi" +
                                " join oi.item i" +
                                " where oi.order.id in :orderIds",
                        OrderItemQueryDto.class
                )
                .setParameter("orderIds", orderIds)
                .getResultList();

        return orderItemQueryDtos.stream()
                .collect(Collectors.groupingBy(OrderItemQueryDto::getOrderId));
    }

    private List<Long> toOrderIds(List<OrderQueryDto> orders) {
        return orders.stream()
                .map(OrderQueryDto::getOrderId)
                .collect(Collectors.toList());
    }

    /**
     * OneToMany, 컬렉션 조인에서는 최적화하기 어려우므로 별도로 이 함수와 같이 DTO 를 통해서 넣어주면 된다.
     * 즉, XToOne 인 연관관계들에게 개별적으로 최적화하도록 한다.
     * 호출하는 곳에서는 컬렉션 조회이므로 N + 1 이슈 발생
     */
    private List<OrderItemQueryDto> findOrderItems(Long orderId) {
        return entityManager.createQuery(
                        "select new jpabook.jpashop.domain.repository.order.query.OrderItemQueryDto(" +
                                "oi.order.id, i.name, i.price, oi.count)" +
                                " from OrderItem oi" +
                                " join oi.item i" +
                                " where oi.order.id = :orderId",
                        OrderItemQueryDto.class
                )
                .setParameter("orderId", orderId)
                .getResultList();
    }

    public List<OrderQueryDto> findOrders() {
        return entityManager.createQuery(
                "select new jpabook.jpashop.domain.repository.order.query.OrderQueryDto(" +
                        "o.id, m.name, o.orderDateTime, o.orderStatus, d.address)" +
                        " from Order o" +
                        " join o.member m" +
                        " join o.delivery d",
                OrderQueryDto.class
        ).getResultList();
    }

}
