package ru.yandex.practicum.filmorate.film.storage;

import ru.yandex.practicum.filmorate.film.model.Film;

import java.util.List;

public interface FilmStorage {
    List<Film>search(String query,String by);
    List<Film> getAll();
    List<Film> getAllFilmsByDirectorId(int directorId, String sortBy);
    Film getById(int id);
    Film create(Film film);
    Film update(int id, Film film);
    Film delete(int id);
    List<Film> getPopularFilms(int count);
    void likeFilm(int filmId, int userId);
    void deleteLikeFromFilm(int filmId, int userId);
}
