package jpabook.jpashop.domain.service;

import jpabook.jpashop.domain.model.item.Item;
import jpabook.jpashop.domain.model.item.UpdateItemDto;
import jpabook.jpashop.domain.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    @Transactional
    public Item updateItem(Long itemId, UpdateItemDto updateItemDto) {
        // 변경 감지(Dirty check) 를 사용하면 필요한 필드만 수정 가능
        Item currentItem = itemRepository.find(itemId);
        currentItem.changeItemOptions( // 반드시 의미 있는 함수로 내부 값을 변경해야함
                updateItemDto.getName(),
                updateItemDto.getPrice(),
                updateItemDto.getStockQuantity()
        );
        return currentItem;
    }

    public Item findItem(Long id) {
        return itemRepository.find(id);
    }

    public List<Item> findAllItems() {
        return itemRepository.findAll();
    }

}
