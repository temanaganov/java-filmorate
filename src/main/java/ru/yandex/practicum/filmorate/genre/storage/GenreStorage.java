package ru.yandex.practicum.filmorate.genre.storage;

import ru.yandex.practicum.filmorate.genre.model.Genre;

import java.util.List;

public interface GenreStorage {
    List<Genre> getAll();
    Genre getById(int id);
}
