package ru.practicum.shareit.item.dto;

/**
 * TODO Sprint add-controllers.
 */

public class ItemDto {
    private String name;

    private String description;

    private boolean isAvailable;

    public ItemDto(String name, String description, boolean isAvailable) {
        this.name = name;
        this.description = description;
        this.isAvailable = isAvailable;
    }
}
