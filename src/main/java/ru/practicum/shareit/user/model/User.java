package ru.practicum.shareit.user.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull(message = "Поле не должно быть пустым")
    private String name;

    @NotNull(message = "Пустой email")
    @Email(message = "Неверный email")
    @Column(unique = true)
    private String email;
}
