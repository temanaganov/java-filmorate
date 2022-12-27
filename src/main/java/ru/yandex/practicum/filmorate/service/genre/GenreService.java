package ru.yandex.practicum.filmorate.service.genre;

import ru.yandex.practicum.filmorate.model.genre.Genre;

import java.util.List;

public interface GenreService {
    List<Genre> getAll();
    Genre getById(int id);
}
