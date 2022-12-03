package ru.yandex.practicum.filmorate.film.model;

import lombok.Value;
import lombok.With;

import java.time.LocalDate;
import java.util.Set;

@With
@Value
public class Film {
    int id;
    String name;
    String description;
    LocalDate releaseDate;
    int duration;
    Set<Integer> likes;
}
