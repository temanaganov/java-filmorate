package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.model.film.FilmSort;

import java.util.List;

public interface FilmService {
    List<Film> search(String query, String criteria);
    List<Film> getAll();
    List<Film> getByDirectorId(int directorId, FilmSort sortBy);
    Film getById(int id);
    Film create(FilmDto dto);
    Film update(FilmDto dto);
    Film delete(int id);
    void likeFilm(int filmId, int userId);
    void deleteLikeFromFilm(int filmId, int userId);
    List<Film> getPopularFilms(int count, Integer genreId, Integer year);
    List<Film> getCommonFilms(int userId, int friendId);
}
