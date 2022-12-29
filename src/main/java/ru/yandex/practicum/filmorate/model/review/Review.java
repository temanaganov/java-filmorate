package ru.yandex.practicum.filmorate.model.review;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("isPositive")
    boolean isPositive;
    int userId;
    int filmId;
    int useful;
}
