package jpabook.jpashop.domain.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jpabook.jpashop.domain.model.member.QMember;
import jpabook.jpashop.domain.model.order.Order;
import jpabook.jpashop.domain.model.order.OrderSearch;
import jpabook.jpashop.domain.model.order.OrderStatus;
import jpabook.jpashop.domain.model.order.QOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager entityManager;

    public void save(Order order) {
        entityManager.persist(order);
    }

    public Order find(Long id) {
        return entityManager.find(Order.class, id);
    }

    /*public List<Order> findAll(OrderSearch orderSearch) {
        if (orderSearch == null) { return Collections.EMPTY_LIST; }

        // Criteria
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
        Root<Order> o = cq.from(Order.class);
        Join<Order, Member> m = o.join("member", JoinType.INNER); //회원과 조인
        List<Predicate> criteria = new ArrayList<>();

        //주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            Predicate status = cb.equal(o.get("orderStatus"), orderSearch.getOrderStatus());
            criteria.add(status);
        }

        //회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            Predicate name = cb.like(m.get("name"), "%" + orderSearch.getMemberName() + "%");
            criteria.add(name);
        }

        cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
        TypedQuery<Order> query = entityManager.createQuery(cq).setMaxResults(1000); //최대 1000건

        List<Order> resultList = query.getResultList();
        log.info("query.getResultList(): {}", resultList);
        return resultList;

        // TODO : QueryDSL

        // 만약 orderStatus 와 멤버 이름이 다 있다면 아래와 같이 작성
        *//*return entityManager.createQuery(
                        "select o from Order o join o.member m" +
                                " where o.orderStatus = :status" +
                                " and m.name like :name",
                        Order.class
                )
                .setParameter("status", orderSearch.getOrderStatus())
                .setParameter("name", orderSearch.getMemberName())
                .setMaxResults(1000) // 최대 1000 건만 조회
                .getResultList();*//*
    }*/

    // QueryDSL
    public List<Order> findAll(OrderSearch orderSearch) {
        QOrder order = QOrder.order;
        QMember member = QMember.member;

        JPAQueryFactory query = new JPAQueryFactory(entityManager);
        return query
                .select(order)
                .from(order)
                .join(order.member, member)
                .where(statusEq(orderSearch.getOrderStatus()),
                        nameLike(orderSearch.getMemberName()))
                .limit(1000)
                .fetch();
    }

    private BooleanExpression statusEq(OrderStatus statusCond) {
        if (statusCond == null) {
            return null;
        }
        return QOrder.order.orderStatus.eq(statusCond);
    }

    private BooleanExpression nameLike(String nameCond) {
        if (!StringUtils.hasText(nameCond)) {
            return null;
        }
        return QMember.member.name.like(nameCond);
    }
    // QueryDSL

    /**
     * Fetch join
     *
     * LAZY 다 무시하고 다 땡겨와서 join 한다.
     */
    public List<Order> findAllWithMemberDelivery() {
        return entityManager.createQuery(
                "select o from Order o" +
                        " join fetch o.member m" +
                        " join fetch o.delivery d",
                Order.class
        ).getResultList();
    }

    public List<Order> findAllWithMemberDeliveryWithLimit(int offset, int limit) {
        return entityManager.createQuery(
                        "select o from Order o" +
                                " join fetch o.member m" +
                                " join fetch o.delivery d",
                        Order.class
                ).setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    public List<Order> findAllWithItem() {
        return entityManager.createQuery(
                "select distinct o from Order o" +
                        " join fetch o.member m" +
                        " join fetch o.delivery d" +
                        " join fetch o.orderItems oi" +
                        " join fetch oi.item i",
                Order.class
        ).getResultList();
    }
}
