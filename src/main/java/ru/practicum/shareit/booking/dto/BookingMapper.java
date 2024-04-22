package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.user.dto.UserMapper;

public class BookingMapper {
    public static BookingDtoPut toBookingDtoPut(Booking booking) {
        return new BookingDtoPut(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getStatus(),
                UserMapper.toUserDto(booking.getBooker()),
                ItemMapper.toItemDtoForBooking(booking.getItem())
        );
    }

    public static BookingDtoOwner bookingDtoGet(Booking booking) {
        return new BookingDtoOwner(
                booking.getId(),
                booking.getBooker().getId()
        );
    }
}
