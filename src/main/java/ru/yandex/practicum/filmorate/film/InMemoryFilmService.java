package ru.yandex.practicum.filmorate.film;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryFilmService implements FilmService{
    private int id = 0;
    private final Map<Integer, Film> films = new HashMap<>();

    @Override
    public List<Film> getAllFilms() {
        return films.values().stream().toList();
    }

    @Override
    public Film getFilm(int id) {
        return films.get(id);
    }

    @Override
    public Film createFilm(Film film) {
        Film newFilm = new Film(getNextId(), film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration());
        films.put(newFilm.getId(), newFilm);
        return newFilm;
    }

    @Override
    public Film updateFilm(int id, Film film) {
        if (!films.containsKey(id)) {
            return null;
        }
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film deleteFilm(int id) {
        Film film = films.get(id);
        films.remove(id);
        return film;
    }

    private int getNextId() {
        return ++id;
    }
}
