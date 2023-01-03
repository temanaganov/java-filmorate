package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Value;
import lombok.With;

import java.time.LocalDate;
import java.util.List;

@With
@Value
@Builder
public class Film {
    int id;
    String name;
    String description;
    LocalDate releaseDate;
    int duration;
    Mpa mpa;
    List<Genre> genres;
    List<Director> directors;
}
