package ru.yandex.practicum.filmorate.film;

import java.util.List;

public interface FilmService {
    List<Film> getAllFilms();
    Film getFilm(int id);
    Film createFilm(Film film);
    Film updateFilm(int id, Film film);
    Film deleteFilm(int id);
}
