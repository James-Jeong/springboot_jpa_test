package jpabook.jpashop.domain.model.member;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jpabook.jpashop.domain.model.order.Order;
import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @NotEmpty
    private String name;

    @Embedded
    private Address address;

    @JsonIgnore
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Order> orders = new ArrayList<>();

    protected Member() {}

    public Member(String name) {
        this.name = name;
    }

    public Member(String name, Address address) {
        this.name = name;
        this.address = address;
    }

    public void change(String name, Address address) {
        if (name != null && !name.isEmpty()) {
            this.name = name;
        }

        if (address != null) {
           this.address = address;
        }
    }

    public Order addOrder(Order order) {
        if (order == null) { return null; }

        if (!this.orders.contains(order)) {
            this.orders.add(order);
        }

        return order;
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address=" + address +
                '}';
    }
}