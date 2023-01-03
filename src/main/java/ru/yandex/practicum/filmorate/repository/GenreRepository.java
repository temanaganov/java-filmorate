package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreRepository {
    List<Genre> getAll();
    List<Genre> getByFilmId(int filmId);
    Genre getById(int id);
}
