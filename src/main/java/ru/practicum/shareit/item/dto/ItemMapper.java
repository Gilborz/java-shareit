package ru.practicum.shareit.item.dto;

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

    public static ItemDtoBook toItemDtoForBooking(Item item) {
        return new ItemDtoBook(
                item.getId(),
                item.getName()
        );
    }

    public static ItemDtoBooking toItemDtoBooking(Item item) {
        return new ItemDtoBooking(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable()
        );
    }
}
