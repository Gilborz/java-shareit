package ru.practicum.shareit.item.service;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.exception.ItemAvailableException;
import ru.practicum.shareit.item.exception.ItemNotFoundToUser;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.exception.UserFoundException;
import java.util.List;

@Component
public interface ItemService {
    Item addItem(int userId, Item item) throws UserFoundException, ItemAvailableException;

    Item updateItem(int userId, int itemId, Item item) throws UserFoundException, ItemNotFoundToUser;

    Item getItemById(int userId, int itemId) throws UserFoundException;

    List<Item> getAllItemsByUser(int userId);

    List<Item> getItemByName(int userId, String text) throws UserFoundException;
}
