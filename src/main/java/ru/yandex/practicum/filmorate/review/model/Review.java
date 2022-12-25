package ru.yandex.practicum.filmorate.review.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;

@With
@Data
@AllArgsConstructor
public class Review {
    int reviewId;
    String content;
    @JsonProperty("isPositive")
    boolean isPositive;
    int userId;
    int filmId;
    int useful;
}
