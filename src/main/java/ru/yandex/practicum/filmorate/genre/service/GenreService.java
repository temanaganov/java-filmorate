package ru.yandex.practicum.filmorate.genre.service;

import ru.yandex.practicum.filmorate.genre.model.Genre;

import java.util.List;

public interface GenreService {
    List<Genre> getAll();
    Genre getById(int id);
}
