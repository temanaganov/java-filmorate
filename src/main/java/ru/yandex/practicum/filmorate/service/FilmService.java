package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.dto.FilmDto;

import java.util.List;

public interface FilmService {
    List<Film> search(String query, String by);
    List<Film> getAll();
    List<Film> getFilmsByDirectorId(int directorId, String sortBy);
    Film getById(int id);
    Film create(FilmDto dto);
    Film update(FilmDto dto);
    Film delete(int id);
    void likeFilm(int filmId, int userId);
    void deleteLikeFromFilm(int filmId, int userId);
    List<Film> getPopularFilms(int count, Integer genreId, Integer year);
    List<Film> getCommonFilms(int userId, int friendId);
}
