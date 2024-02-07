package ru.practicum.shareit.item.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.exception.ItemAvailableException;
import ru.practicum.shareit.item.exception.ItemNotFoundToUser;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.exception.UserFoundException;
import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */

@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public Item addItem(@RequestHeader(value = "X-Sharer-User-Id") int userId,
                        @Valid @RequestBody Item item) throws UserFoundException, ItemAvailableException {
        return itemService.addItem(userId, item);
    }

    @PatchMapping("/{itemId}")
    public Item updateItem(@RequestHeader(value = "X-Sharer-User-Id") int userId,
                           @PathVariable(required = false) int itemId,
                           @RequestBody Item item) throws UserFoundException, ItemNotFoundToUser {
        item.setId(itemId);

        return itemService.updateItem(userId, itemId, item);
    }

    @GetMapping("/{itemId}")
    public Item getItemById(@RequestHeader(value = "X-Sharer-User-Id") int userId,
                            @PathVariable(required = false) int itemId) throws UserFoundException {
        return itemService.getItemById(userId, itemId);
    }

    @GetMapping
    public List<Item> getAllItemsByUser(@RequestHeader(value = "X-Sharer-User-Id") int userId) {
        return itemService.getAllItemsByUser(userId);
    }

    @GetMapping("/search")
    public List<Item> getItemByName(@RequestHeader(value = "X-Sharer-User-Id") int userId,
                                    @RequestParam(required = false) String text) throws UserFoundException {
        return itemService.getItemByName(userId, text);
    }
}
