package ru.practicum.shareit.item.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class CommentDto {
    private Integer id;

    private String text;

    private String authorName;

    private LocalDateTime created;

    public CommentDto(Integer id, String text, String authorName, LocalDateTime created) {
        this.id = id;
        this.text = text;
        this.authorName = authorName;
        this.created = created;
    }
}
