package ru.practicum.shareit.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.EmailDuplicateException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserDao;
import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User addUser(User user) {
        validDuplicateEmail(user);
        return userDao.addUser(user);
    }

    @Override
    public User updateUser(int id, User user) {
        return userDao.updateUser(id, user);
    }

    @Override
    public void deleteUser(int id) {
        userDao.deleteUser(id);
    }

    @Override
    public UserDto getUserById(int id) {
        return userDao.getUserById(id);
    }

    @Override
    public List<UserDto> getAllUser() {
        return userDao.getAllUsers();
    }

    private void validDuplicateEmail(User user) {
        for (User usr : userDao.getUsers().values()) {
            if (usr.getEmail().equals(user.getEmail())) {
                log.info("Повторяющийся email");
                throw new EmailDuplicateException("Такой email уже существует");
            }
        }
    }
}
