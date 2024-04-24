package ru.practicum.shareit.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User addUser(User user) {
        User saveUser = userRepository.save(user);
        log.info("Пользователь добавлен");
        return saveUser;
    }

    public User updateUser(int id, User user) {
        Optional<User> userFromBd = Optional.ofNullable(userRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Пользователя с таким " + id + " не найдено")));

        if (user.getName() == null) {
            user.setName(userFromBd.get().getName());
        }

        if (user.getEmail() == null) {
            user.setEmail(userFromBd.get().getEmail());
        }

        log.info("Данные о пользователе {} обновлены", id);
        return userRepository.save(user);
    }

    public void deleteUser(int id) {
        userRepository.deleteById(id);
        log.info("Пользователь {} удалён", id);
    }

    public User getUserById(int id) {
        Optional<User> user = userRepository.findById(id);
        log.info("Данные о пользователе {} отправлены", id);
        return user.get();
    }

    public List<User> getAllUser() {
        List<User> userList = userRepository.findAll();
        log.info("Данные о всех пользователях отправлены");
        return userList;
    }
}
