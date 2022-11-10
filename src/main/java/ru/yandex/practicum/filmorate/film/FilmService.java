package ru.yandex.practicum.filmorate.film;

import ru.yandex.practicum.filmorate.film.dto.FilmDto;

import java.util.List;

public interface FilmService {
    List<Film> getAll();
    Film getById(int id);
    Film create(FilmDto dto);
    Film update(FilmDto dto);
    Film delete(int id);
    Film likeFilm(int filmId, int userId);
    Film deleteLikeFromFilm(int filmId, int userId);
    List<Film> getPopularFilms(int count);
}
