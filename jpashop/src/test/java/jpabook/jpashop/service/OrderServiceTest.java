package jpabook.jpashop.service;

import jpabook.jpashop.common.ItemCreator;
import jpabook.jpashop.common.MemberCreator;
import jpabook.jpashop.domain.model.item.NotEnoughStockException;
import jpabook.jpashop.domain.model.item.impl.Book;
import jpabook.jpashop.domain.model.member.Member;
import jpabook.jpashop.domain.model.order.Order;
import jpabook.jpashop.domain.model.order.OrderStatus;
import jpabook.jpashop.domain.repository.OrderRepository;
import jpabook.jpashop.domain.service.OrderService;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @Test
    public void testMakeOrder() throws Exception {
        // 1) Given
        Member member = MemberCreator.createTestMember("test_member");
        entityManager.persist(member);

        Book book = ItemCreator.createTestBook("시골 JPA", 10000, 10);
        entityManager.persist(book);
        int stock = book.getStock();

        // 2) When
        int orderCount = 2;
        Long orderId = orderService.makeOrder(member.getId(), book.getId(), orderCount);

        // 3) Then
        Order order = orderRepository.find(orderId);
        Assertions.assertThat(order).isNotNull();
        assertEquals("상품 주문 시 상태는 ORDER", OrderStatus.ORDER, order.getOrderStatus());
        assertEquals("주문한 상품 종류 수가 정확해야 한다", 1, order.getOrderItems().size());
        assertEquals("주문 가격은 [가격 * 수량] 이다", book.getPrice() * orderCount, order.getTotalPrice());
        assertEquals("주문하면 주문 수량만큼 재고가 줄어야 한다", stock - orderCount, book.getStock());
    }

    @Test(expected = NotEnoughStockException.class)
    public void testOrderOverStock() throws Exception {
        // 1) Given
        Member member = MemberCreator.createTestMember("test_member");
        entityManager.persist(member);

        Book book = ItemCreator. createTestBook("시골 JPA", 10000, 10);
        entityManager.persist(book);

        // 2) When
        int orderCount = 11;
        Long orderId = orderService.makeOrder(member.getId(), book.getId(), orderCount);

        // 3) Then
        fail("재고 수량 부족 예외가 발생해야 한다!");
    }

    @Test
    public void testCancelOrder() throws Exception {
        // 1) Given
        Member member = MemberCreator.createTestMember("test_member");
        entityManager.persist(member);

        Book book = ItemCreator. createTestBook("시골 JPA", 10000, 10);
        entityManager.persist(book);
        int stock = book.getStock();

        int orderCount = 2;
        Long orderId = orderService.makeOrder(member.getId(), book.getId(), orderCount);

        // 2) When
        orderService.cancelOrder(orderId);

        // 3) Then
        Order order = orderRepository.find(orderId);
        assertNotNull(order);
        assertEquals("주문 취소시 상태는 CANCEL", OrderStatus.CANCEL, order.getOrderStatus());
        assertEquals("주문이 취소된 상품은 그만큼 재고가 증가해야 한다.", 10, book.getStock());
    }
    
}