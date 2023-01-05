package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.With;

@With
@Data
@Builder
@AllArgsConstructor
public class Review {
    int reviewId;
    String content;
    Boolean isPositive;
    int userId;
    int filmId;
    int useful;
}
