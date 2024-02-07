package ru.practicum.shareit.item.model;

import lombok.Data;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */

@Data
public class Item {
    private int id;

    @NotBlank(message = "В поле должно быть имя")
    private String name;

    @NotNull(message = "Поле не должно быть пустым")
    private String description;

    private boolean isAvailable;

    private User owner;

    private ItemRequest request;
}
