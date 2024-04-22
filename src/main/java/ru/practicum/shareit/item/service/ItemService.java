package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.exception.ItemAvailableException;
import ru.practicum.shareit.item.exception.ItemNotFoundToUser;
import ru.practicum.shareit.item.exception.UserCommentException;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ItemService {
    private final ItemRepository itemRepository;

    private final UserRepository userRepository;

    private final BookingRepository bookingRepository;

    private final CommentRepository commentRepository;

    @Autowired
    public ItemService(ItemRepository itemRepository,
                       UserRepository userRepository,
                       BookingRepository bookingRepository,
                       CommentRepository commentRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
        this.commentRepository = commentRepository;
    }

    public Item addItem(Integer userId, Item item) throws ItemAvailableException {
        validIsAvailable(item);

        User user = validSaveUser(userId);
        item.setOwner(user);

        itemRepository.save(item);
        log.info("Предмет добавлен");

        return item;
    }

    public ItemDto updateItem(int userId, int itemId, Item item) throws ItemNotFoundToUser {
        Item it = validItem(itemId);

        validSaveItemToUser(userId, it.getOwner().getId());

        if (item.getName() == null && item.getDescription() == null) {
            it.setAvailable(item.getAvailable());

            Item itemSave = itemRepository.save(it);
            log.info("Инф о предмете {} обновлена", itemId);
            return ItemMapper.toItemDto(itemSave);
        } else if (item.getName() == null && item.getAvailable() == null) {
            it.setDescription(item.getDescription());

            Item itemSave = itemRepository.save(it);
            log.info("Инф о предмете {} обновлена", itemId);
            return ItemMapper.toItemDto(itemSave);
        } else if (item.getDescription() == null && item.getAvailable() == null) {
            it.setName(item.getName());

            Item itemSave = itemRepository.save(it);
            log.info("Инф о предмете {} обновлена", itemId);
            return ItemMapper.toItemDto(itemSave);
        } else {
            it.setName(item.getName());
            it.setDescription(item.getDescription());
            it.setAvailable(item.getAvailable());

            Item itemSave = itemRepository.save(it);
            log.info("Инф о предмете {} обновлена", itemId);

            return ItemMapper.toItemDto(itemSave);
        }
    }

    public ItemDtoBooking getItemById(int userId, int itemId) {
        validSaveUser(userId);

        Item itemBd = itemRepository.findById(itemId).get();
        List<Booking> bookings = bookingRepository.findByItemEqualsOrderByStartAsc(itemBd);
        ItemDtoBooking itemDtoBooking = ItemMapper.toItemDtoBooking(itemBd);

        if (itemBd.getOwner().getId() != userId || bookings.size() == 0) {
            itemDtoBooking.setLastBooking(null);
            itemDtoBooking.setNextBooking(null);
            itemDtoBooking.setComments(commentRepository.findByItemEquals(itemBd).stream()
                    .map(CommentMapper::toCommentDto)
                    .collect(Collectors.toList()));

            log.info("Данные о предмете {} пользователя {} отправлены", itemId, userId);
            return itemDtoBooking;
        }

        if (bookings.size() == 1) {
            if(bookings.get(0).getStatus().equals(BookingStatus.REJECTED)) {
                itemDtoBooking.setLastBooking(null);
                itemDtoBooking.setNextBooking(null);
                itemDtoBooking.setComments(commentRepository.findByItemEquals(itemBd).stream()
                        .map(CommentMapper::toCommentDto)
                        .collect(Collectors.toList()));

                log.info("Данные о предмете {} пользователя {} отправлены", itemId, userId);
                return itemDtoBooking;
            }
            itemDtoBooking.setLastBooking(BookingMapper.bookingDtoGet(bookings.get(0)));
            itemDtoBooking.setNextBooking(null);
            itemDtoBooking.setComments(commentRepository.findByItemEquals(itemBd).stream()
                    .map(CommentMapper::toCommentDto)
                    .collect(Collectors.toList()));

            log.info("Данные о предмете {} пользователя {} отправлены", itemId, userId);
            return itemDtoBooking;
        }

        int id = 0;
        for (Booking b : bookings) {
            if (b.getEnd().isBefore(LocalDateTime.now())) {
                bookings.set(id, b);
            } else {
                id++;
            }
        }

        Set<Booking> bookingSet = new HashSet<>(bookings);
        bookings = List.copyOf(bookingSet).stream().sorted((o1, o2) -> {
            if (o1.getEnd().isBefore(o2.getEnd())) {
                return -1;
            } else {
                return 1;
            }
        }).collect(Collectors.toList());

        itemDtoBooking.setLastBooking(BookingMapper.bookingDtoGet(bookings.get(0)));
        itemDtoBooking.setNextBooking(BookingMapper.bookingDtoGet(bookings.get(1)));
        itemDtoBooking.setComments(commentRepository.findByItemEquals(itemBd).stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList()));

        log.info("Данные о предмете {} пользователя {} отправлены", itemId, userId);
        return itemDtoBooking;
    }

    public List<ItemDtoBooking> getAllItemsByUser(int userId) {
        User user = validSaveUser(userId);
        List<Item> items = itemRepository.findByOwnerEquals(user);

        List<ItemDtoBooking> itemsList = new ArrayList<>();

        for (Item it : items) {
            List<Booking> bookings = bookingRepository.findByItemEqualsOrderByStartAsc(it);
            ItemDtoBooking itemDtoBooking = ItemMapper.toItemDtoBooking(it);

            if (bookings.size() == 0 || bookings.size() == 1) {
                itemDtoBooking.setLastBooking(null);
                itemDtoBooking.setNextBooking(null);
                itemDtoBooking.setComments(commentRepository.findByItemEquals(it).stream()
                        .map(CommentMapper::toCommentDto)
                        .collect(Collectors.toList()));
                itemsList.add(itemDtoBooking);
            } else {
                itemDtoBooking.setLastBooking(BookingMapper.bookingDtoGet(bookings.get(0)));
                itemDtoBooking.setNextBooking(BookingMapper.bookingDtoGet(bookings.get(1)));
                itemDtoBooking.setComments(commentRepository.findByItemEquals(it).stream()
                        .map(CommentMapper::toCommentDto)
                        .collect(Collectors.toList()));
                itemsList.add(itemDtoBooking);
            }
        }

        log.info("Предметы пользователя {} отправлены", userId);
        return itemsList.stream().sorted(new Comparator<ItemDtoBooking>() {
            @Override
            public int compare(ItemDtoBooking o1, ItemDtoBooking o2) {
                return o1.getId() - o2.getId();
            }
        }).collect(Collectors.toList());
    }

    public List<ItemDto> getItemByName(String text) {
        List<ItemDto> itemDescription = new ArrayList<>();
        String txt = text.toLowerCase();

        if (txt.equals("")) {
            log.info("Отправлен пустой список");
            return itemDescription;
        }

        List<ItemDto> itemDto = new ArrayList<>();
        for (Item it : itemRepository.findByNameAndDescriptionEndsWith(text)) {
            itemDto.add(ItemMapper.toItemDto(it));
        }

        log.info("Список инструментов отправлен");
        return itemDto;
    }

    public CommentDto addCommentToItem(Integer userId, Integer itemId, Comment textComment) {
        User booker = validSaveUser(userId);
        Item item = validItem(itemId);

        if (textComment.getText() == null || textComment.getText().equals("")) {
            throw new UserCommentException("Комментарий не может быть пустым");
        }

        if (item.getOwner().getId() == booker.getId()) {
            throw new UserCommentException("Владелец не может оставлять комментарии");
        }

        List<Booking> bookings = bookingRepository.findByItemEqualsAndBookerEquals(item, booker).stream()
                .filter((b1) -> b1.getEnd().isBefore(LocalDateTime.now()))
                .collect(Collectors.toList());
        if (bookings.size() == 0) {
            throw new UserCommentException("Пользователь не брал вещь в аренду");
        }

        Comment comment = new Comment();
        comment.setText(textComment.getText());
        comment.setAuthor(booker);
        comment.setItem(item);
        comment.setCreated(LocalDateTime.now());

        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }

    private void validIsAvailable(Item item) throws ItemAvailableException {
        if (item.getAvailable() == null || !item.getAvailable()) {
            log.info("Ошибка доступа к предмету {}", item.getId());
            throw new ItemAvailableException("Предмет должен быть доступен");
        }
    }

    private Item validItem(Integer itemId) {
        Optional<Item> itemFromBd = Optional.ofNullable(itemRepository
                .findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Предмета с таким " + itemId + " не найдено")));

        return itemFromBd.get();
    }

    private void validSaveItemToUser(int userId, int id) throws ItemNotFoundToUser {
        if (id != userId) {
            log.info("Неверное id {} пользователя", userId);
            throw new ItemNotFoundToUser("Передан неверный id пользователя");
        }
    }

    private User validSaveUser(int userId) {
        Optional<User> user = Optional.ofNullable(userRepository
                .findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с таким " + userId + " не найден")));

        return user.get();
    }
}
