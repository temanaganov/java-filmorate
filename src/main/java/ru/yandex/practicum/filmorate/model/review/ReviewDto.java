package ru.yandex.practicum.filmorate.model.review;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ReviewDto {
    int reviewId;

    @NotBlank(message = "Content is required")
    String content;

    @NotNull(message = "IsPositive is required")
    Boolean isPositive;

    @NotNull(message = "UserId is required")
    Integer userId;

    @NotNull(message = "FilmId is required")
    Integer filmId;
}
