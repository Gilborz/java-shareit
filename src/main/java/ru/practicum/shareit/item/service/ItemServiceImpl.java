package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.exception.ItemAvailableException;
import ru.practicum.shareit.item.exception.ItemNotFoundToUser;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemDao;
import ru.practicum.shareit.user.exception.UserFoundException;
import java.util.List;

@Service
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemDao itemDao;

    public ItemServiceImpl(ItemDao itemDao) {
        this.itemDao = itemDao;
    }

    @Override
    public Item addItem(int userId, Item item) throws UserFoundException, ItemAvailableException {
        validIsAvailable(item);

        return itemDao.addItem(userId, item);
    }

    @Override
    public Item updateItem(int userId, int itemId, Item item) throws UserFoundException, ItemNotFoundToUser {
        return itemDao.updateItem(userId, itemId, item);
    }

    @Override
    public Item getItemById(int userId, int itemId) throws UserFoundException {
        return itemDao.getItemById(userId, itemId);
    }

    @Override
    public List<Item> getAllItemsByUser(int userId) {
        return itemDao.getAllItemsByUser(userId);
    }

    @Override
    public List<Item> getItemByName(int userId, String text) throws UserFoundException {
        return itemDao.getItemByName(userId, text);
    }

    private void validIsAvailable(Item item) throws ItemAvailableException {
        if (!item.isAvailable()) {
            log.info("Ошибка доступа к предмету {}", item.getId());
            throw new ItemAvailableException("Предмет должен быть доступен");
        }
    }
}
