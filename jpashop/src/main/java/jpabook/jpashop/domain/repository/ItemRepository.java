package jpabook.jpashop.domain.repository;

import jpabook.jpashop.domain.model.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager entityManager;

    public void save(Item item) {
        if (item == null) { return; }

        if (item.getId() == null) { // 새로 생성한 객체 (insert)
            entityManager.persist(item);
        } else { // DB 에서 가져온 객체 (update 랑 비슷함, 강제 업데이트)
            entityManager.merge(item);
        }
    }

    public Item find(Long id) {
        return entityManager.find(Item.class, id);
    }

    public List<Item> findAll() {
        return entityManager.createQuery(
                "select i from Item i",
                Item.class
        ).getResultList();
    }

}
