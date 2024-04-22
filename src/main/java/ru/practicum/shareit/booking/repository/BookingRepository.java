package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import java.util.List;
import java.util.Optional;

@Component
public interface BookingRepository extends JpaRepository<Booking, Integer> {

    Booking save(Booking booking);

    Optional<Booking> findById(Integer id);

    List<Booking> findByBookerEquals(User booker);

    List<Booking> findByItemEqualsOrderByStartDesc(Item item);

    List<Booking> findByStatusAndBookerEquals(BookingStatus status, User booker);

    List<Booking> findByStatusAndItemEquals(BookingStatus status, Item item);

    List<Booking> findByItemEqualsOrderByStartAsc(Item item);

    List<Booking> findByItemEqualsAndBookerEquals(Item item, User booker);
}
