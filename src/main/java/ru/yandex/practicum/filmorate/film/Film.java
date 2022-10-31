package ru.yandex.practicum.filmorate.film;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.With;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@With
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Film {
    int id;

    @NotBlank
    String name;

    @NotBlank
    @Size(max = 200)
    String description;

    @NotNull
    LocalDate releaseDate;

    @Positive
    int duration;
}
