package jpabook.jpashop.domain.service;

import jpabook.jpashop.domain.model.delivery.Delivery;
import jpabook.jpashop.domain.model.item.Item;
import jpabook.jpashop.domain.model.item.NotEnoughStockException;
import jpabook.jpashop.domain.model.member.Member;
import jpabook.jpashop.domain.model.order.Order;
import jpabook.jpashop.domain.model.order.OrderItem;
import jpabook.jpashop.domain.model.order.OrderSearch;
import jpabook.jpashop.domain.repository.ItemRepository;
import jpabook.jpashop.domain.repository.MemberRepository;
import jpabook.jpashop.domain.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Long makeOrder(Long memberId, Long itemId, int count) {
        // 1) 회원 조회
        Member member = memberRepository.find(memberId);
        if (member == null) {
            throw new IllegalStateException("회원을 찾을 수 없습니다. (id=" + memberId + ")");
        }

        // 2) 항목 조회
        Item item = itemRepository.find(itemId);
        if (item == null) {
            throw new IllegalStateException("항목을 찾을 수 없습니다. (id=" + itemId + ")");
        } else {
            int stock = item.getStock();
            if (count > stock) {
                throw new NotEnoughStockException("주문 수량이 재고 개수보다 많습니다. (orderCount=" + count + ", currentStock="  + stock + ")");
            }
        }

        // 3) 배달 정보 생성
        Delivery delivery = new Delivery(member.getAddress());

        // 4) 주문 정보 생성
        // 서비스 계츠은 단순히 엔티티에 필요한 요청을 해당 엔티티 클래스에 위임하는 역할만 해야 한다.
        // 이처럼 엔티티가 비즈니스 로직을 가지고 객체 지향의 특성을 적극 활용하는 것을 [도메인 모델 패턴] 이라고 한다.
        // 반대로 엔티티에는 비즈니스 로직이 거의 없고 서비스 계층에서 처리하는 것을 [트랜잭션 스크립트 패턴] 이라고 한다.
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        // 지금은 하나의 주문 정보만 추가 가능 > 추후에 여러 개 등록 가능
        Order order = Order.createOrder(member, delivery, orderItem);

        // 5) 주문 정보 저장
        orderRepository.save(order); // 자동 cascade 이기 때문에 delivery 에 설정 안해도됨

        return order.getId();
    }

    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.find(orderId);
        if (order != null) {
            // 도메인 모델 패턴
            order.cancel();
        }
    }

    @Transactional
    public void saveOrder(Order order) {
        orderRepository.save(order);
    }

    public Order findOrder(Long id) {
        return orderRepository.find(id);
    }

    public List<Order> findAllOrders(OrderSearch orderSearch) {
        return orderRepository.findAll(orderSearch);
    }

}
