package jpabook.jpashop.domain.repository.order.simplequery;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderSimpleQueryRepository {

    private EntityManager entityManager;

    /**
     * 조회 전용으로 사용한다.
     * 화면에 출력하기 위한 API
     */
    public List<OrderSimpleQueryDto> findOrderDtos() {
        return entityManager.createQuery(
                "select new jpabook.jpashop.domain.repository.order.simplequery.OrderSimpleQueryDto(" +
                        "o.id, m.name, o.orderDateTime, o.orderStatus, o.delivery.address" +
                        ") from Order o" +
                        " join o.member m" +
                        " join o.delivery d",
                OrderSimpleQueryDto.class
        ).getResultList();
    }

}
