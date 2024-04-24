package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;

public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable()
        );
    }

    public static BookingDtoOwner toBookingDtoOwner(Booking booking) {
        return new BookingDtoOwner(
                booking.getId(),
                booking.getBooker().getId()
        );
    }
}
