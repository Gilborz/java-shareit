package ru.practicum.shareit.user.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.exception.EmailDuplicateException;
import ru.practicum.shareit.user.model.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class UserDao {
    private int id = 0;
    private Map<Integer, User> users = new HashMap<>();

    public Map<Integer, User> getUsers() {
        return users;
    }

    public User addUser(User user) {
        id++;
        user.setId(id);
        users.put(id, user);

        log.info("Пользователь {} добавлен", user.getId());
        return user;
    }

    public void deleteUser(int id) {
        log.info("Пользователь {} удалён", id);
        users.remove(id);
    }

    public User updateUser(int id, User user) {
        validDuplicateEmail(user);

        if (user.getName() == null) {
            user.setName(users.get(id).getName());
        }

        if (user.getEmail() == null) {
            user.setEmail(users.get(id).getEmail());
        }

        users.replace(id, user);
        log.info("Данные о пользователе {} обновлены", id);
        return users.get(id);
    }

    public UserDto getUserById(int id) {
        log.info("Данные о пользователе {} отправлены", id);
        return UserMapper.toUserDto(users.get(id));
    }

    public List<UserDto> getAllUsers() {
        List<UserDto> userDtoList = new ArrayList<>();
        for (User user : users.values()) {
            userDtoList.add(UserMapper.toUserDto(user));
        }

        log.info("Данные о всех пользователях отправлены");
        return userDtoList;
    }

    private void validDuplicateEmail(User user) {
        for (User usr : users.values()) {
            if (user.getId() == usr.getId()) {
                continue;
            } else {
                if (usr.getEmail().equals(user.getEmail())) {
                    log.info("Повторяющийся email");
                    throw new EmailDuplicateException("Такой email уже занят");
                }
            }
        }
    }
}
