package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDtoBook;
import ru.practicum.shareit.user.dto.UserDto;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class BookingDtoPut {
    private Integer id;

    private LocalDateTime start;

    private LocalDateTime end;

    private BookingStatus status;

    private UserDto booker;

    private ItemDtoBook item;
}
