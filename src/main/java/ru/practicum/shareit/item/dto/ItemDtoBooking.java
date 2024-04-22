package ru.practicum.shareit.item.dto;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.dto.BookingDtoOwner;
import java.util.List;

@Getter
@Setter
public class ItemDtoBooking {
    private Integer id;

    private String name;

    private String description;

    private Boolean available;

    private BookingDtoOwner lastBooking;

    private BookingDtoOwner nextBooking;

    private List<CommentDto> comments;

    public ItemDtoBooking(Integer id, String name, String description, Boolean available) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
    }
}
