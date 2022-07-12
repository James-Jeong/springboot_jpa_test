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
@DiscriminatorValue("ALBUM")
public class Album extends Item {

    private String artist;
    private String etc;

    public Album(Long id, String name, int price, int stockQuantity,
                 String artist, String etc) {
        super(id, name, price, stockQuantity);
        this.artist = artist;
        this.etc = etc;
    }

    public Album(String name, int price, int stockQuantity,
                 String artist, String etc) {
        super(name, price, stockQuantity);
        this.artist = artist;
        this.etc = etc;
    }

}
