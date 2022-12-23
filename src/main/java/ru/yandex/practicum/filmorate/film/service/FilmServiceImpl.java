package ru.yandex.practicum.filmorate.film.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.core.exception.NotFoundException;
import ru.yandex.practicum.filmorate.core.util.Mapper;
import ru.yandex.practicum.filmorate.events.service.EventService;
import ru.yandex.practicum.filmorate.events.storage.EventStorage;
import ru.yandex.practicum.filmorate.director.model.Director;
import ru.yandex.practicum.filmorate.director.storage.DirectorStorage;
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
    private final DirectorStorage directorStorage;
    private final EventService eventService;
    private final Mapper<FilmDto, Film> filmDtoToFilmMapper;

    public FilmServiceImpl(
            @Qualifier("dbFilmStorage") FilmStorage filmStorage,
            @Qualifier("dbUserStorage") UserStorage userStorage,
            MpaStorage mpaStorage,
            GenreStorage genreStorage,
            DirectorStorage directorStorage,
            EventService eventService,
            Mapper<FilmDto, Film> filmDtoToFilmMapper
    ) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.mpaStorage = mpaStorage;
        this.genreStorage = genreStorage;
        this.directorStorage = directorStorage;
        this.filmDtoToFilmMapper = filmDtoToFilmMapper;
        this.eventService = eventService;
    }



    @Override
    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    @Override
    public List<Film> getAllFilmsByDirectorId(int directorId, String sortBy) {
        Director director = directorStorage.getById(directorId);

        if (director == null) {
            throw new NotFoundException("director", directorId);
        }

        return filmStorage.getAllFilmsByDirectorId(directorId, sortBy);
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
        checkExistenceOfFilmFields(newFilm);
        return filmStorage.create(newFilm);
    }

    @Override
    public Film update(FilmDto dto) {
        Film currentFilm = filmStorage.getById(dto.getId());

        if (currentFilm == null) {
            throw new NotFoundException("film", dto.getId());
        }

        Film film = filmDtoToFilmMapper.mapFrom(dto);

        checkExistenceOfFilmFields(film);

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
        eventService.addLikeEvent(filmId, userId);
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
        eventService.deleteLikeEvent(filmId, userId);
        filmStorage.deleteLikeFromFilm(filmId, userId);
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        return filmStorage.getPopularFilms(count);
    }

    private void checkExistenceOfFilmFields(Film film) {
        Mpa mpa = mpaStorage.getById(film.getMpa().getId());

        if (mpa == null) {
            throw new NotFoundException("mpa", film.getMpa().getId());
        }

        film.getGenres().forEach(genre -> {
            if (genreStorage.getById(genre.getId()) == null) {
                throw new NotFoundException("genre", genre.getId());
            }
        });

        film.getDirectors().forEach(director -> {
            if (directorStorage.getById(director.getId()) == null) {
                throw new NotFoundException("genre", director.getId());
            }
        });
    }
}
