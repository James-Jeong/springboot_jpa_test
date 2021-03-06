package jpabook.jpashop.domain.controller;

import jpabook.jpashop.domain.controller.form.BookForm;
import jpabook.jpashop.domain.model.item.Item;
import jpabook.jpashop.domain.model.item.UpdateItemDto;
import jpabook.jpashop.domain.model.item.impl.Book;
import jpabook.jpashop.domain.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/items/new")
    public String createForm(Model model) {
        model.addAttribute("form", new BookForm());
        return "items/createItemForm";
    }

    @PostMapping("/items/new")
    public String create(BookForm bookForm) {
        Book book = new Book(
                bookForm.getName(), bookForm.getPrice(), bookForm.getStockQuantity(),
                bookForm.getAuthor(), bookForm.getIsbn()
        );
        itemService.saveItem(book);
        return "redirect:/";
    }

    @GetMapping("/items")
    public String list(Model model) {
        List<Item> allItems = itemService.findAllItems();
        model.addAttribute("items", allItems);
        return "items/itemList";
    }

    @GetMapping("/items/{itemId}/edit")
    public String updateItemForm(@PathVariable("itemId") Long itemId, Model model) {
        Book book = (Book) itemService.findItem(itemId);
        if (book == null) {
            return "redirect:/items/itemList";
        }

        BookForm bookForm = new BookForm();
        bookForm.setId(book.getId());
        bookForm.setName(book.getName());
        bookForm.setPrice(book.getPrice());
        bookForm.setStockQuantity(book.getStock());
        bookForm.setAuthor(book.getAuthor());
        bookForm.setIsbn(book.getIsbn());

        model.addAttribute("form", bookForm);
        return "items/updateItemForm";
    }

    @PostMapping("items/{itemId}/edit")
    public String updateItem(@PathVariable("itemId") Long itemId, @ModelAttribute("form") BookForm bookForm) {
        // TODO : ?????? ???????????? ?????? ???????????? ?????? ?????? ??????

        // ????????? ????????? (????????? ?????????????????? ??? ?????? ???????????? ?????? ?????????)
        // ????????? ???????????? ???????????? ?????? ???????????? ????????? ????????? ??? ?????? ???????????? ??? ??? ??????.
        // JPA ??? ???????????? ?????????! > DB update ??? ???????????? ?????????!
        UpdateItemDto updateItemDto = new UpdateItemDto();
        updateItemDto.setName(bookForm.getName());
        updateItemDto.setPrice(bookForm.getPrice());
        updateItemDto.setStockQuantity(bookForm.getStockQuantity());
        itemService.updateItem(itemId, updateItemDto);
        return "redirect:/items";
    }

}
