package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import java.util.List;
import java.util.Optional;

@Component
public interface ItemRepository extends JpaRepository<Item, Integer> {
    Item save(Item item);

    Optional<Item> findById(Integer id);

    List<Item> findByOwnerEquals(User owner);

    @Query(" select i from Item i " +
            "where upper(i.name) like upper(concat('%', ?1, '%')) " +
            " or upper(i.description) like upper(concat('%', ?1, '%'))" +
            " and i.available = true")
    List<Item> findByNameAndDescriptionEndsWith(String text);


}
