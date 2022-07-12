package jpabook.jpashop.common;

import jpabook.jpashop.domain.model.item.impl.Album;
import jpabook.jpashop.domain.model.item.impl.Book;
import jpabook.jpashop.domain.model.item.impl.Movie;

public class ItemCreator {

    public static Book createTestBook(String name, int price, int stockQuantity) {
        return new Book(name, price, stockQuantity, "김영한", "123456");
    }

    public static Movie createTestMovie(String name, int price, int stockQuantity) {
        return new Movie(name, price, stockQuantity, "김영한",  "Kim");
    }

    public static Album createTestAlbum(String name, int price, int stockQuantity) {
        return new Album(name, price, stockQuantity, "김영한",  "good");
    }

}
