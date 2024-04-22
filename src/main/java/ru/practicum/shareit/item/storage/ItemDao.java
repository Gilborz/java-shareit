package ru.practicum.shareit.item.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.exception.ItemNotFoundToUser;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.exception.UserFoundException;
import ru.practicum.shareit.user.storage.UserDao;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class ItemDao {
    private int id = 0;

    private Map<Integer, Item> items = new HashMap<>();

    private final UserDao userDao;

    public ItemDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public Item addItem(int userId, Item item) throws UserFoundException {
        validSaveUser(userId);

        id++;
        item.setId(id);
        item.setOwner(userDao.getUsers().get(userId));

        items.put(id, item);

        log.info("Предмет {} добавлен", id);
        return item;
    }

    public Item updateItem(int userId, int itemId, Item item) throws UserFoundException, ItemNotFoundToUser {
        validSaveUser(userId);
        validSaveItemToUser(userId, items.get(itemId).getOwner().getId());

        if (item.getName() == null && item.getDescription() == null) {
            items.get(itemId).setAvailable(item.isAvailable());

            log.info("Инф о предмете {} обновлена", itemId);
            return items.get(itemId);
        } else if (item.getName() == null && !item.isAvailable()) {
            items.get(itemId).setDescription(item.getDescription());

            log.info("Инф о предмете {} обновлена", itemId);
            return items.get(itemId);
        } else if (item.getDescription() == null && !item.isAvailable()) {
            items.get(itemId).setName(item.getName());

            log.info("Инф о предмете {} обновлена", itemId);
            return items.get(itemId);
        } else {
            items.get(itemId).setName(item.getName());
            items.get(itemId).setDescription(item.getDescription());
            items.get(itemId).setAvailable(item.isAvailable());

            log.info("Инф о предмете {} обновлена", itemId);
            return items.get(itemId);
        }
    }

    public Item getItemById(int userId, int itemId) throws UserFoundException {
        validSaveUser(userId);

        log.info("Данные о предмете {} пользователя {} отправлены", itemId, userId);
        return items.get(itemId);
    }

    public List<Item> getAllItemsByUser(int userId) {
        List<Item> itemUser = new ArrayList<>();

        for (Item item : items.values()) {
            if (item.getOwner().getId() == userId) {
                itemUser.add(item);
            }
        }

        log.info("Предметы пользователя {} отправлены", userId);
        return itemUser;
    }

    public List<Item> getItemByName(int userId, String text) throws UserFoundException {
        validSaveUser(userId);
        List<Item> itemDescription = new ArrayList<>();
        String txt = text.toLowerCase();

        if (txt.equals("")) {
            log.info("Отправлен пустой список");
            return itemDescription;
        }

        for (Item item : items.values()) {
            if (item.getDescription().toLowerCase().contains(txt) && item.isAvailable()) {
                itemDescription.add(item);
            }
        }

        log.info("Список инструментов отправлен");
        return itemDescription;
    }

    private void validSaveUser(int id) throws UserFoundException {
        if (!userDao.getUsers().containsKey(id)) {
            log.info("Пользователя {} не существует", id);
            throw new UserFoundException("Неверный id пользователя");
        }
    }

    private void validSaveItemToUser(int userId, int id) throws ItemNotFoundToUser {
        if (id != userId) {
            log.info("Неверное id {} пользователя", userId);
            throw new ItemNotFoundToUser("Передан неверный id пользователя");
        }
    }
}
