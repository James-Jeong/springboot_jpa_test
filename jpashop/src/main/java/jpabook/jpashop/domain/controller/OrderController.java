package jpabook.jpashop.domain.controller;

import jpabook.jpashop.domain.model.item.Item;
import jpabook.jpashop.domain.model.member.Member;
import jpabook.jpashop.domain.model.order.Order;
import jpabook.jpashop.domain.model.order.OrderSearch;
import jpabook.jpashop.domain.service.ItemService;
import jpabook.jpashop.domain.service.MemberService;
import jpabook.jpashop.domain.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final MemberService memberService;
    private final ItemService itemService;

    @GetMapping("/order")
    public String createForm(Model model) {
        List<Member> allMembers = memberService.findAllMembers();
        List<Item> allItems = itemService.findAllItems();

        model.addAttribute("members", allMembers);
        model.addAttribute("items", allItems);
        return "order/orderForm";
    }

    @PostMapping("/order")
    public String order(@RequestParam("memberId") Long memberId,
                      @RequestParam("itemId") Long itemId,
                      @RequestParam("count") int count) {
        Long orderId = orderService.makeOrder(memberId, itemId, count);
        return "redirect:/orders";
    }

    @GetMapping("/orders")
    public String orderList(@ModelAttribute("orderSearch") OrderSearch orderSearch, Model model) {
        List<Order> allOrders = orderService.findAllOrders(orderSearch);
        for (Order allOrder : allOrders) {
            log.trace("order: {}", allOrder);
        }

        model.addAttribute("orders", allOrders);
        return "order/orderList";
    }

    @PostMapping("/orders/{orderId}/cancel")
    public String cancelOrder(@PathVariable("orderId") Long orderId) {
        orderService.cancelOrder(orderId);
        return "redirect:/orders";
    }

}
