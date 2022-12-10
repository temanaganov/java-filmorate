package ru.yandex.practicum.filmorate.genre.storage;

import ru.yandex.practicum.filmorate.genre.model.Genre;

import java.util.List;

public interface GenreStorage {
    List<Genre> getAll();
    List<Genre> getAllByFilmId(int filmId);
    Genre getById(int id);
}
