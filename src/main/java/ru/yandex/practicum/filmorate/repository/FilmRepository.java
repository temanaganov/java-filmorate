package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmRepository {
    List<Film> search(String query, String criteria);
    List<Film> getAll();
    List<Film> getByDirectorId(int directorId, String sortBy);
    Film getById(int id);
    Film create(Film film);
    Film update(Film film);
    void delete(int id);
    List<Film> getPopularFilms(int count, Integer genreId, Integer year);
    void likeFilm(int filmId, int userId);
    void deleteLikeFromFilm(int filmId, int userId);
    List<Film> getCommonFilms(int userId, int friendId);
}
