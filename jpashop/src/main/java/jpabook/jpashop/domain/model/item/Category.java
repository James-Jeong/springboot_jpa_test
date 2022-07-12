package jpabook.jpashop.domain.model.item;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Category {

    @Id
    @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    private String name;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable( // 중간 테이블 > 유연하지 못함
            name = "category_item",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id")
    )
    private List<Item> items = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> children = new ArrayList<>();

    public Category(String name, Category parent) {
        this.name = name;
        this.parent = parent;
    }

    public Item addItem(Item item) {
        if (item == null) { return null; }

        if (!this.items.contains(item)) {
            this.items.add(item);
        }

        return item;
    }

    public Category addChildCategory(Category category) {
        if (category == null) { return null; }

        if (!this.children.contains(category)) {
            this.children.add(category);
        }

        return category;
    }

}
