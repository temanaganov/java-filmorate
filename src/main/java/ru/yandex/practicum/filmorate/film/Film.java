package ru.yandex.practicum.filmorate.film;

import lombok.Value;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Value
public class Film {
    int id;

    @NotBlank
    String name;

    @NotBlank
    @Size(max = 200)
    String description;

    @DateTimeFormat(pattern = "YYYY-mm-dd")
    LocalDate releaseDate;

    @Positive
    int duration;
}
