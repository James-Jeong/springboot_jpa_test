package jpabook.jpashop.model;

import jpabook.jpashop.common.ItemCreator;
import jpabook.jpashop.domain.model.item.impl.Book;
import org.junit.Assert;
import org.junit.Test;

public class ItemTest {

    @Test
    public void testIncStockWithBook() throws Exception {
        // 1) Given
        Book book = ItemCreator. createTestBook("시골 JPA", 10000, 10);

        // 2) When
        book.incStock(10);

        // 3) Then
        Assert.assertEquals("재고 수량이 맞지 않습니다.", 20, book.getStock());
    }

    @Test
    public void testDecStockWithBook() throws Exception {
        // 1) Given
        Book book = ItemCreator. createTestBook("시골 JPA", 10000, 10);

        // 2) When
        book.decStock(10);

        // 3) Then
        Assert.assertEquals("재고 수량이 맞지 않습니다.", 0, book.getStock());
    }

    @Test
    public void testDecStockFailWithBook() throws Exception {
        // 1) Given
        Book book = ItemCreator. createTestBook("시골 JPA", 10000, 10);

        // 2) When
        boolean decStockResult = book.decStock(20);

        // 3) Then
        Assert.assertEquals("재고 수량이 맞지 않습니다.", 10, book.getStock());
        Assert.assertFalse(decStockResult);
    }

}
