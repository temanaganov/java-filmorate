package ru.yandex.practicum.filmorate.film;

import ru.yandex.practicum.filmorate.film.dto.CreateFilmDto;
import ru.yandex.practicum.filmorate.film.dto.UpdateFilmDto;

import java.util.List;

public interface FilmService {
    List<Film> getAll();
    Film getById(int id);
    Film create(CreateFilmDto dto);
    Film update(UpdateFilmDto dto);
    Film delete(int id);
    Film likeFilm(int filmId, int userId);
    Film deleteLikeFromFilm(int filmId, int userId);
    List<Film> getPopularFilms(int count);
}
