package ru.yandex.practicum.filmorate.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@With
@Getter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class FilmDto {
    int id;

    @NotBlank(message = "Name is required")
    String name;

    @NotBlank(message = "Description is required")
    @Size(max = 200, message = "Description must be maximum 200 characters")
    String description;

    @NotNull(message = "Release date is required")
    LocalDate releaseDate;

    @Positive(message = "Duration is required and must be greater than 0")
    int duration;

    @NotNull(message = "Mpa is required")
    Mpa mpa;

    List<Genre> genres;

    List<Director> directors;
}
