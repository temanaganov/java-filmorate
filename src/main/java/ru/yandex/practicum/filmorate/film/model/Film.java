package ru.yandex.practicum.filmorate.film.model;

import lombok.Value;
import lombok.With;
import ru.yandex.practicum.filmorate.director.model.Director;
import ru.yandex.practicum.filmorate.genre.model.Genre;
import ru.yandex.practicum.filmorate.mpa.model.Mpa;

import java.time.LocalDate;
import java.util.List;

@With
@Value
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
