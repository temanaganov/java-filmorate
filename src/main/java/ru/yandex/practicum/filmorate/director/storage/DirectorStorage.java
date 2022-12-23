package ru.yandex.practicum.filmorate.director.storage;

import ru.yandex.practicum.filmorate.director.model.Director;

import java.util.List;

public interface DirectorStorage {
    List<Director> getAll();
    List<Director> getAllByFilmId(int filmId);
    Director getById(int id);
    Director create(Director director);
    Director update(Director director);
    Director delete(int id);
}
