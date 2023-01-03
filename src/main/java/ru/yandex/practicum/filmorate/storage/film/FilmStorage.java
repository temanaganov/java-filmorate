package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.film.Film;

import java.util.List;

public interface FilmStorage {
    List<Film> search(String query, String by);
    List<Film> getAll();
    List<Film> getFilmsByDirectorId(int directorId, String sortBy);
    Film getById(int id);
    Film create(Film film);
    Film update(Film film);
    void delete(int id);
    List<Film> getPopularFilms(int count, Integer genreId, Integer year);
    void likeFilm(int filmId, int userId);
    void deleteLikeFromFilm(int filmId, int userId);
    List<Film> getCommonFilms(int userId, int friendId);
}
