package ru.practicum.shareit.item.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemDtoBook {
    private Integer id;

    private String name;

    public ItemDtoBook(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
