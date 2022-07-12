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
@DiscriminatorValue("MOVIE")
public class Movie extends Item {

    private String director;
    private String actor;

    public Movie(Long id, String name, int price, int stockQuantity,
                 String director, String actor) {
        super(id, name, price, stockQuantity);
        this.director = director;
        this.actor = actor;
    }

    public Movie(String name, int price, int stockQuantity,
                 String director, String actor) {
        super(name, price, stockQuantity);
        this.director = director;
        this.actor = actor;
    }

}
