package ru.practicum.shareit.user.service;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import java.util.List;

@Component
public interface UserService {
    User addUser(User user);

    User updateUser(int id, User user);

    void deleteUser(int id);

    UserDto getUserById(int id);

    List<UserDto> getAllUser();
}
