package ru.practicum.shareit.booking.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingDtoOwner {
    private Integer id;

    private Integer bookerId;

    public BookingDtoOwner(Integer id, Integer bookerId) {
        this.id = id;
        this.bookerId = bookerId;
    }
}
