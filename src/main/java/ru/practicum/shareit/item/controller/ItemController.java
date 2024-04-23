package ru.practicum.shareit.item.controller;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.ItemAvailableException;
import ru.practicum.shareit.item.exception.ItemNotFoundToUser;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public Item addItem(@RequestHeader(value = "X-Sharer-User-Id") int userId,
                        @Valid @RequestBody Item item) throws ItemAvailableException {
        return itemService.addItem(userId, item);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader(value = "X-Sharer-User-Id") int userId,
                              @PathVariable(required = false) int itemId,
                              @RequestBody Item item) throws ItemNotFoundToUser {
        item.setId(itemId);

        return itemService.updateItem(userId, itemId, item);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@RequestHeader(value = "X-Sharer-User-Id") int userId,
                               @PathVariable(required = false) int itemId) {
        return itemService.getItemById(userId, itemId);
    }

    @GetMapping
    public List<ItemDto> getAllItemsByUser(@RequestHeader(value = "X-Sharer-User-Id") int userId) {
        return itemService.getAllItemsByUser(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> getItemByName(@RequestParam(required = false) String text) {
        return itemService.getItemByName(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addCommentItem(@RequestHeader(value = "X-Sharer-User-Id") int userId,
                                     @PathVariable(required = false) int itemId,
                                     @RequestBody(required = false) Comment comment) {
        return itemService.addCommentToItem(userId, itemId, comment);
    }
}
