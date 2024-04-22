package ru.practicum.shareit.booking.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.exception.*;
import ru.practicum.shareit.booking.dto.BookingDtoGet;
import ru.practicum.shareit.booking.dto.BookingDtoPut;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.exception.ItemAvailableException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BookingService {
    private final BookingRepository bookingRepository;

    private final ItemRepository itemRepository;

    private final UserRepository userRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository, ItemRepository itemRepository, UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    public BookingDtoPut addBooking(Integer userId, BookingDtoGet bookingDtoGet) throws ItemAvailableException, DateTimeException {
        User user = validSaveUser(userId);
        Item item = validSaveItemAndAvailable(bookingDtoGet.getItemId(), userId);
        validTime(bookingDtoGet.getStart(), bookingDtoGet.getEnd());

        Booking booking = new Booking();
        booking.setBooker(user);
        booking.setItem(item);
        booking.setStatus(BookingStatus.WAITING);
        booking.setStart(bookingDtoGet.getStart());
        booking.setEnd(bookingDtoGet.getEnd());
        bookingRepository.save(booking);

        log.info("Статус с {} добавлен", booking.getId());
        return BookingMapper.toBookingDtoPut(booking);
    }

    public BookingDtoPut updateBooking(Integer userId, Integer bookingId, Boolean approved) {
        Booking booking = validSaveBooking(bookingId);
        User user = validSaveUser(userId);

        if (userId == booking.getItem().getOwner().getId()) {
            if (approved) {
                if(booking.getStatus().equals(BookingStatus.APPROVED)) {
                    throw new ApprovedException("Статус уже подтверждён");
                }

                booking.setStatus(BookingStatus.APPROVED);
                bookingRepository.save(booking);
                return BookingMapper.toBookingDtoPut(booking);
            } else {
                if(booking.getStatus().equals(BookingStatus.REJECTED)) {
                    throw new ApprovedException("Статус уже отклонён");
                }

                booking.setStatus(BookingStatus.REJECTED);
                bookingRepository.save(booking);
                return BookingMapper.toBookingDtoPut(booking);
            }
        } else if (booking.getBooker().getId() == userId) {
            throw new EntityNotFoundException("Изменять статус может только владелец");
        } else {
            throw new BookerHaveBookingException("Изменять статус может только владелец");
        }
    }

    public BookingDtoPut getBooking(Integer userId, Integer bookingId) {
        Booking booking = validSaveBooking(bookingId);
        User user = validSaveUser(userId);

        if (userId == booking.getBooker().getId() || userId == booking.getItem().getOwner().getId()) {
            return BookingMapper.toBookingDtoPut(booking);
        } else {
            throw new UserHaveNotBookingException("Данный запрос не относится к пользователю");
        }
    }

    public List<BookingDtoPut> getAllBookingByBooker(Integer userId, String state) {
        User booker = validSaveUser(userId);
        List<BookingDtoPut> bookings = new ArrayList<>();

        switch (state) {
            case ("CURRENT") :
                for (Booking bkg : bookingRepository.findByBookerEquals(booker)) {
                    bookings.add(BookingMapper.toBookingDtoPut(bkg));
                }

                return sorted(bookings).stream()
                        .filter((b1) -> b1.getStart().isBefore(LocalDateTime.now()))
                        .filter((b1) -> b1.getEnd().isAfter(LocalDateTime.now()))
                        .collect(Collectors.toList());
            case ("FUTURE") :
                for (Booking bkg : bookingRepository.findByBookerEquals(booker)) {
                    bookings.add(BookingMapper.toBookingDtoPut(bkg));
                }

                return sorted(bookings).stream()
                        .filter((b1) -> b1.getEnd().isAfter(LocalDateTime.now()))
                        .collect(Collectors.toList());
            case ("WAITING") :
                for (Booking bkg : bookingRepository.findByStatusAndBookerEquals(BookingStatus.WAITING, booker)) {
                    bookings.add(BookingMapper.toBookingDtoPut(bkg));
                }

                return sorted(bookings);
            case ("REJECTED") :
                for (Booking bkg : bookingRepository.findByStatusAndBookerEquals(BookingStatus.REJECTED, booker)) {
                    bookings.add(BookingMapper.toBookingDtoPut(bkg));
                }

                return sorted(bookings);
            case ("PAST") :
                for (Booking bkg : bookingRepository.findByBookerEquals(booker)) {
                    bookings.add(BookingMapper.toBookingDtoPut(bkg));
                }

                return sorted(bookings).stream()
                        .filter(b1 -> b1.getEnd().isBefore(LocalDateTime.now()))
                        .collect(Collectors.toList());
            case ("ALL") :
                for (Booking bkg : bookingRepository.findByBookerEquals(booker)) {
                    bookings.add(BookingMapper.toBookingDtoPut(bkg));
                }

                return sorted(bookings);
            default:
                throw new StateException("Unknown state: UNSUPPORTED_STATUS");
        }
    }

    public List<BookingDtoPut> getAllBookingByOwner(Integer userId, String state) {
        User owner = validSaveUser(userId);
        List<Item> items = itemRepository.findByOwnerEquals(owner);
        List<BookingDtoPut> bookings = new ArrayList<>();

        switch (state) {
            case ("CURRENT") :
                for(Item item : items) {
                    bookings.addAll(bookingRepository.findByItemEqualsOrderByStartDesc(item).stream()
                            .map(BookingMapper::toBookingDtoPut).collect(Collectors.toList()));
                }

                return sorted(bookings).stream()
                        .filter((b1) -> b1.getStart().isBefore(LocalDateTime.now()))
                        .filter((b1) -> b1.getEnd().isAfter(LocalDateTime.now()))
                        .collect(Collectors.toList());
            case ("FUTURE") :
                for(Item item : items) {
                    bookings.addAll(bookingRepository.findByItemEqualsOrderByStartDesc(item).stream()
                            .map(BookingMapper::toBookingDtoPut).collect(Collectors.toList()));
                }

                return sorted(bookings).stream()
                        .filter((b1) -> b1.getEnd().isAfter(LocalDateTime.now()))
                        .collect(Collectors.toList());
            case ("WAITING") :
                for(Item item : items) {
                    bookings.addAll(bookingRepository.findByStatusAndItemEquals(BookingStatus.WAITING, item).stream()
                            .map(BookingMapper::toBookingDtoPut).collect(Collectors.toList()));
                }

                return sorted(bookings);
            case ("REJECTED") :
                for(Item item : items) {
                    bookings.addAll(bookingRepository.findByStatusAndItemEquals(BookingStatus.REJECTED, item).stream()
                            .map(BookingMapper::toBookingDtoPut).collect(Collectors.toList()));
                }

                return sorted(bookings);
            case ("PAST") :
                for(Item item : items) {
                    bookings.addAll(bookingRepository.findByItemEqualsOrderByStartDesc(item).stream()
                            .map(BookingMapper::toBookingDtoPut).collect(Collectors.toList()));
                }

                return sorted(bookings).stream()
                        .filter(b1 -> b1.getEnd().isBefore(LocalDateTime.now()))
                        .collect(Collectors.toList());
            case ("ALL") :
                for(Item item : items) {
                    bookings.addAll(bookingRepository.findByItemEqualsOrderByStartDesc(item).stream()
                            .map(BookingMapper::toBookingDtoPut).collect(Collectors.toList()));
                }

                return bookings;
            default:
                throw new StateException("Unknown state: UNSUPPORTED_STATUS");
        }
    }

    private Booking validSaveBooking(Integer bookingId) {
        Optional<Booking> booking = Optional.ofNullable(bookingRepository
                .findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Запроса с таким " + bookingId + " не найдено")));

        return booking.get();
    }

    private User validSaveUser(Integer userId) {
        Optional<User> user = Optional.ofNullable(userRepository
                .findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с таким " + userId + " не найден")));

        return user.get();
    }

    private Item validSaveItemAndAvailable(Integer itemId, Integer userId) throws ItemAvailableException {
        Optional<Item> item = Optional.ofNullable(itemRepository
                .findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Предмета с таким " + itemId + " не найдено")));

        if (!item.get().getAvailable()) {
            throw new ItemAvailableException("Предмет не доступен");
        }

        if (item.get().getOwner().getId() == userId) {
            throw new ItemBeOwnerException("Нельзя создать предмет на принадлежащий предмет");
        }

        return item.get();
    }

    private void validTime(LocalDateTime start, LocalDateTime end) throws DateTimeException {
        if (start == null || end == null) {
            throw new DateTimeException("Поля start и end не должны быть пусты");
        }

        if (end.equals(start) || end.isBefore(start) || end.isBefore(LocalDateTime.now()) || start.isBefore(LocalDateTime.now())) {
            throw new DateTimeException("Неверный формат даты");
        }
    }

    private List<BookingDtoPut> sorted(List<BookingDtoPut> list) {
        return list.stream().sorted((o1, o2) -> {
            if (o1.getEnd().isBefore(o2.getEnd())) {
                return 1;
            } else {
                return -1;
            }
        }).collect(Collectors.toList());
    }
}
