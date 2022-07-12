package jpabook.jpashop.domain.model.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderSearch {

    private String memberName;
    private OrderStatus orderStatus;

    @Override
    public String toString() {
        return "OrderSearch{" +
                "memberName='" + memberName + '\'' +
                ", orderStatus=" + orderStatus +
                '}';
    }
}
