package ru.yandex.practicum.filmorate.film.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.core.exception.NotFoundException;
import ru.yandex.practicum.filmorate.core.util.Mapper;
import ru.yandex.practicum.filmorate.film.model.Film;
import ru.yandex.practicum.filmorate.film.storage.FilmStorage;
import ru.yandex.practicum.filmorate.film.dto.FilmDto;
import ru.yandex.practicum.filmorate.genre.storage.GenreStorage;
import ru.yandex.practicum.filmorate.mpa.model.Mpa;
import ru.yandex.practicum.filmorate.mpa.storage.MpaStorage;
import ru.yandex.practicum.filmorate.user.model.User;
import ru.yandex.practicum.filmorate.user.storage.UserStorage;

import java.util.List;

@Service
public class FilmServiceImpl implements FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final MpaStorage mpaStorage;
    private final GenreStorage genreStorage;
    private final Mapper<FilmDto, Film> filmDtoToFilmMapper;

    public FilmServiceImpl(
            @Qualifier("dbFilmStorage") FilmStorage filmStorage,
            @Qualifier("dbUserStorage") UserStorage userStorage,
            MpaStorage mpaStorage,
            GenreStorage genreStorage,
            Mapper<FilmDto, Film> filmDtoToFilmMapper
    ) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.mpaStorage = mpaStorage;
        this.genreStorage = genreStorage;
        this.filmDtoToFilmMapper = filmDtoToFilmMapper;
    }

    @Override
    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    @Override
    public Film getById(int id) {
        Film film = filmStorage.getById(id);

        if (film == null) {
            throw new NotFoundException("film", id);
        }

        return film;
    }

    @Override
    public Film create(FilmDto dto) {
        Film newFilm = filmDtoToFilmMapper.mapFrom(dto);
        Mpa mpa = mpaStorage.getById(newFilm.getMpa().getId());

        if (mpa == null) {
            throw new NotFoundException("mpa", newFilm.getMpa().getId());
        }

        newFilm.getGenres().forEach(genre -> {
            if (genreStorage.getById(genre.getId()) == null) {
                throw new NotFoundException("genre", genre.getId());
            }
        });

        return filmStorage.create(newFilm);
    }

    @Override
    public Film update(FilmDto dto) {
        Film currentFilm = filmStorage.getById(dto.getId());

        if (currentFilm == null) {
            throw new NotFoundException("film", dto.getId());
        }

        Film film = filmDtoToFilmMapper.mapFrom(dto);

        if (mpaStorage.getById(film.getMpa().getId()) == null) {
            throw new NotFoundException("mpa", film.getMpa().getId());
        }

        film.getGenres().forEach(genre -> {
            if (genreStorage.getById(genre.getId()) == null) {
                throw new NotFoundException("genre", genre.getId());
            }
        });


        return filmStorage.update(film.getId(), film);
    }

    @Override
    public Film delete(int id) {
        Film film = filmStorage.delete(id);

        if (film == null) {
            throw new NotFoundException("film", id);
        }

        return film;
    }

    @Override
    public void likeFilm(int filmId, int userId) {
        Film film = filmStorage.getById(filmId);
        User user = userStorage.getById(userId);

        if (film == null) {
            throw new NotFoundException("film", filmId);
        }

        if (user == null) {
            throw new NotFoundException("user", userId);
        }

        filmStorage.likeFilm(filmId, userId);
    }

    @Override
    public void deleteLikeFromFilm(int filmId, int userId) {
        Film film = filmStorage.getById(filmId);
        User user = userStorage.getById(userId);

        if (film == null) {
            throw new NotFoundException("film", filmId);
        }

        if (user == null) {
            throw new NotFoundException("user", userId);
        }

        filmStorage.deleteLikeFromFilm(filmId, userId);
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        return filmStorage.getPopularFilms(count);
    }

    @Override
    public List<Film> getCommonFilms(int userId, int friendId) {
        if (userStorage.getById(userId) == null) {
            throw new NotFoundException("user", userId);
        }

        if (userStorage.getById(friendId) == null) {
            throw new NotFoundException("user", friendId);
        }

        return filmStorage.getCommonFilms(userId, friendId);
    }
}
