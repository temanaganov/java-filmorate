package ru.yandex.practicum.filmorate.film;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InMemoryFilmService implements FilmService{
    private int id = 0;
    private final Map<Integer, Film> films = new HashMap<>();

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilm(int id) {
        if (!films.containsKey(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return films.get(id);
    }

    @Override
    public Film createFilm(Film film) {
        Film newFilm = film.withId(getNextId());
        films.put(newFilm.getId(), newFilm);
        return newFilm;
    }

    @Override
    public Film updateFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film deleteFilm(int id) {
        if (!films.containsKey(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        Film film = films.get(id);
        films.remove(id);
        return film;
    }

    private int getNextId() {
        return ++id;
    }
}
