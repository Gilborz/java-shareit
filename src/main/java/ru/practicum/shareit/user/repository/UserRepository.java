package ru.practicum.shareit.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;
import java.util.List;
import java.util.Optional;

@Component
public interface UserRepository extends JpaRepository<User, Integer> {
    User save(User user);

    Optional<User> findById(Integer id);

    void deleteById(Integer id);

    List<User> findAll();

    @Query(value = "select distinct u from User as u " +
            "join Item as i on i.owner_id = u.id where i.owner_id = ?1", nativeQuery = true)
    User findByItemId(Integer id);
}
