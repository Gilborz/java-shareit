package ru.practicum.shareit.user.model;

import lombok.Data;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class User {
    private int id;

    private String name;

    @NotNull(message = "Поле не должно быть пустым")
    @Email(message = "Email неверно передан")
    private String email;
}
