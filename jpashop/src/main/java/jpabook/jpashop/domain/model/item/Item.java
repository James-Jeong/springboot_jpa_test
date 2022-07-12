package jpabook.jpashop.domain.model.item;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Item {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;

    private int price;

    private final AtomicInteger stockQuantity = new AtomicInteger(0);

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    public Item(Long id, String name, int price, int stockQuantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stockQuantity.set(stockQuantity);
    }

    public Item(String name, int price, int stockQuantity) {
        this.name = name;
        this.price = price;
        this.stockQuantity.set(stockQuantity);
    }

    public Category addCategory(Category category) {
        if (category == null) { return null; }

        if (!this.categories.contains(category)) {
            this.categories.add(category);
        }

        return category;
    }

    public void incStock(int quantity) {
        stockQuantity.addAndGet(quantity);
    }

    public boolean decStock(int quantity) {
        if (quantity <= 0) {
            //throw new IllegalArgumentException("quantity is not positive");
            log.warn("Quantity is not positive. (quantity={})", quantity);
            return false;
        }

        int currentStockQuantity = stockQuantity.get();
        int diff = currentStockQuantity - quantity;
        if (diff <= 0) {
            //throw new NotEnoughStockException("need more stock");
            log.warn("Need more stock. (current={}, given={}, diff={})", currentStockQuantity, quantity, diff);
            return false;
        }

        stockQuantity.addAndGet(-quantity);
        return true;
    }

    public int getStock() {
        return stockQuantity.get();
    }

    private void setStock(int stock) {
        stockQuantity.set(stock);
    }

    public void changeItemOptions(String name, int price, int stockQuantity) {
        this.name = name;
        this.price = price;
        setStock(stockQuantity);
    }

}
