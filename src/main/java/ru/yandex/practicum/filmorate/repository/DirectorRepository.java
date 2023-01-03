package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;

public interface DirectorRepository {
    List<Director> getAll();
    List<Director> getByFilmId(int filmId);
    Director getById(int id);
    Director create(Director director);
    Director update(Director director);
    void delete(int id);
}
