package jpabook.jpashop;

import jpabook.jpashop.domain.model.delivery.Delivery;
import jpabook.jpashop.domain.model.item.impl.Book;
import jpabook.jpashop.domain.model.member.Address;
import jpabook.jpashop.domain.model.member.Member;
import jpabook.jpashop.domain.model.order.Order;
import jpabook.jpashop.domain.model.order.OrderItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

/**
 * 총 주문 2개
 *
 * 1. userA
 *      JPA1 BOOK
 *      JPA2 BOOK
 *
 * 2. userB
 *      SPRING1 BOOK
 *      SPRING2 BOOK
 */

//@Component
@RequiredArgsConstructor
public class initDB {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbInit1();
        initService.dbInit2();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final EntityManager entityManager;

        public void dbInit1() {
            Member member = getMember("userA", "서울", "1", "1111");

            Book book1 = getBook("JPA1 BOOK", 10000, 100, "JHO", "1234");
            Book book2 = getBook("JPA2 BOOK", 20000, 200, "TAN", "5678");

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 10000, 1);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20000, 2);

            Delivery delivery = getDelivery("대전", "5", "5555");
            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            entityManager.persist(order);
        }

        public void dbInit2() {
            Member member = getMember("userB", "경주", "2", "2222");

            Book book1 = getBook("SPRING1 BOOK", 15000, 150, "JHO", "1234");

            Book book2 = getBook("SPRING2 BOOK", 25000, 250, "JHO", "1234");
            new Book(
                    "SPRING2 BOOK",
                    25000,
                    250,
                    "TAN",
                    "5678"
            );
            entityManager.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 15000, 15);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 25000, 25);

            Delivery delivery = getDelivery("거창", "8", "8888");
            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            entityManager.persist(order);
        }

        private Member getMember(String userName, String city, String street, String zipcode) {
            Member member = new Member(
                    userName,
                    getAddress(city, street, zipcode)
            );
            entityManager.persist(member);
            return member;
        }

        private Book getBook(String bookName, int price, int stockQuantity, String author, String isbn) {
            Book book1 = new Book(
                    bookName,
                    price,
                    stockQuantity,
                    author,
                    isbn
            );
            entityManager.persist(book1);
            return book1;
        }

        private Delivery getDelivery(String city, String street, String zipcode) {
            return new Delivery(
                    getAddress(city, street, zipcode)
            );
        }

        private Address getAddress(String city, String street, String zipcode) {
            return new Address(
                    city,
                    street,
                    zipcode
            );
        }

    }

}
