package ru.yandex.practicum.filmorate.film.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.core.storage.InMemoryStorage;
import ru.yandex.practicum.filmorate.film.model.Film;

import java.util.List;

@Component("inMemoryFilmStorage")
public class InMemoryFilmStorage extends InMemoryStorage<Film> implements FilmStorage {
    @Override
    protected Film withId(Film film, int id) {
        return film.withId(id);
    }

    @Override
    public List<Film> getAllFilmsByDirectorId(int directorId, String sortBy) {
        return null;
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        return null;
    }

    @Override
    public void likeFilm(int filmId, int userId) {

    }

    @Override
    public void deleteLikeFromFilm(int filmId, int userId) {

    }
}
