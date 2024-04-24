package ru.practicum.shareit.item.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class ItemDto {
    private Integer id;

    private String name;

    private String description;

    private Boolean available;

    private UpdateBookingForItemDto lastBooking;

    private UpdateBookingForItemDto nextBooking;

    private List<CommentDto> comments;

    public ItemDto(Integer id, String name, String description, Boolean available) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
    }
}