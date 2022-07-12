package jpabook.jpashop.domain.model.item.impl;

import jpabook.jpashop.domain.model.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@DiscriminatorValue("BOOK")
public class Book extends Item {

    private String author;
    private String isbn;

    public Book(Long id, String name, int price, int stockQuantity,
                String author, String isbn) {
        super(id, name, price, stockQuantity);
        this.author = author;
        this.isbn = isbn;
    }

    public Book(String name, int price, int stockQuantity,
                String author, String isbn) {
        super(name, price, stockQuantity);
        this.author = author;
        this.isbn = isbn;
    }

}
