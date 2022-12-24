package ru.yandex.practicum.filmorate.film.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.core.util.Guard;
import ru.yandex.practicum.filmorate.core.util.Mapper;
import ru.yandex.practicum.filmorate.director.model.Director;
import ru.yandex.practicum.filmorate.director.storage.DirectorStorage;
import ru.yandex.practicum.filmorate.film.model.Film;
import ru.yandex.practicum.filmorate.film.storage.FilmStorage;
import ru.yandex.practicum.filmorate.film.dto.FilmDto;
import ru.yandex.practicum.filmorate.genre.model.Genre;
import ru.yandex.practicum.filmorate.genre.storage.GenreStorage;
import ru.yandex.practicum.filmorate.mpa.model.Mpa;
import ru.yandex.practicum.filmorate.mpa.storage.MpaStorage;
import ru.yandex.practicum.filmorate.user.model.User;
import ru.yandex.practicum.filmorate.user.storage.UserStorage;

import java.util.List;

@Service
public class FilmServiceImpl implements FilmService {
    private final FilmStorage filmStorage;
    private final Mapper<FilmDto, Film> filmDtoToFilmMapper;
    private final Guard<Film> filmGuard;
    private final Guard<User> userGuard;
    private final Guard<Mpa> mpaGuard;
    private final Guard<Genre> genreGuard;
    private final Guard<Director> directorGuard;

    public FilmServiceImpl(
            @Qualifier("dbFilmStorage") FilmStorage filmStorage,
            @Qualifier("dbUserStorage") UserStorage userStorage,
            MpaStorage mpaStorage,
            GenreStorage genreStorage,
            DirectorStorage directorStorage,
            Mapper<FilmDto, Film> filmDtoToFilmMapper
    ) {
        this.filmStorage = filmStorage;
        this.filmDtoToFilmMapper = filmDtoToFilmMapper;
        this.filmGuard = new Guard<>(filmStorage::getById, Film.class);
        this.userGuard = new Guard<>(userStorage::getById, User.class);
        this.mpaGuard = new Guard<>(mpaStorage::getById, Mpa.class);
        this.genreGuard = new Guard<>(genreStorage::getById, Genre.class);
        this.directorGuard = new Guard<>(directorStorage::getById, Director.class);
    }

    @Override
    public List<Film> search(String query, String by) {
        return filmStorage.search(query, by);
    }

    @Override
    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    @Override
    public List<Film> getAllFilmsByDirectorId(int directorId, String sortBy) {
        directorGuard.checkIfExists(directorId);

        return filmStorage.getAllFilmsByDirectorId(directorId, sortBy);
    }

    @Override
    public Film getById(int id) {
        return filmGuard.checkIfExists(id);
    }

    @Override
    public Film create(FilmDto dto) {
        Film newFilm = filmDtoToFilmMapper.mapFrom(dto);
        checkExistenceOfFilmFields(newFilm);

        return filmStorage.create(newFilm);
    }

    @Override
    public Film update(FilmDto dto) {
        filmGuard.checkIfExists(dto.getId());
        Film film = filmDtoToFilmMapper.mapFrom(dto);
        checkExistenceOfFilmFields(film);

        return filmStorage.update(film.getId(), film);
    }

    @Override
    public Film delete(int id) {
        Film film = filmGuard.checkIfExists(id);
        filmStorage.delete(id);

        return film;
    }

    @Override
    public void likeFilm(int filmId, int userId) {
        filmGuard.checkIfExists(filmId);
        userGuard.checkIfExists(userId);

        filmStorage.likeFilm(filmId, userId);
    }

    @Override
    public void deleteLikeFromFilm(int filmId, int userId) {
        filmGuard.checkIfExists(filmId);
        userGuard.checkIfExists(userId);

        filmStorage.deleteLikeFromFilm(filmId, userId);
    }

    @Override
    public List<Film> getPopularFilms(int count, Integer genreId, Integer year) {
        return filmStorage.getPopularFilms(count, genreId, year);
    }

    @Override
    public List<Film> getCommonFilms(int userId, int friendId) {
        userGuard.checkIfExists(userId);
        userGuard.checkIfExists(friendId);

        return filmStorage.getCommonFilms(userId, friendId);
    }

    private void checkExistenceOfFilmFields(Film film) {
        mpaGuard.checkIfExists(film.getMpa().getId());
        film.getGenres().forEach(genre -> genreGuard.checkIfExists(genre.getId()));
        film.getDirectors().forEach(director -> directorGuard.checkIfExists(director.getId()));
    }
}
