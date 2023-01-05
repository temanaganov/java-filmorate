package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.mapper.Mapper;
import ru.yandex.practicum.filmorate.model.film.FilmSort;
import ru.yandex.practicum.filmorate.model.event.EventOperation;
import ru.yandex.practicum.filmorate.model.event.EventType;
import ru.yandex.practicum.filmorate.service.EventService;
import ru.yandex.practicum.filmorate.guard.DirectorGuard;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.guard.FilmGuard;
import ru.yandex.practicum.filmorate.guard.GenreGuard;
import ru.yandex.practicum.filmorate.guard.MpaGuard;
import ru.yandex.practicum.filmorate.guard.UserGuard;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {
    private final FilmRepository filmRepository;
    private final Mapper<FilmDto, Film> filmDtoToFilmMapper;
    private final EventService eventService;
    private final FilmGuard filmGuard;
    private final UserGuard userGuard;
    private final MpaGuard mpaGuard;
    private final GenreGuard genreGuard;
    private final DirectorGuard directorGuard;

    @Override
    public List<Film> search(String query, String criteria) {
        return filmRepository.search(query, criteria);
    }

    @Override
    public List<Film> getAll() {
        return filmRepository.getAll();
    }

    @Override
    public List<Film> getByDirectorId(int directorId, FilmSort sortBy) {
        directorGuard.checkIfExists(directorId);

        return filmRepository.getByDirectorId(directorId, sortBy);
    }

    @Override
    public Film getById(int id) {
        return filmGuard.checkIfExists(id);
    }

    @Override
    public Film create(FilmDto dto) {
        Film newFilm = filmDtoToFilmMapper.mapFrom(dto);
        checkExistenceOfFilmFields(newFilm);

        return filmRepository.create(newFilm);
    }

    @Override
    public Film update(FilmDto dto) {
        filmGuard.checkIfExists(dto.getId());
        Film film = filmDtoToFilmMapper.mapFrom(dto);
        checkExistenceOfFilmFields(film);

        return filmRepository.update(film);
    }

    @Override
    public Film delete(int id) {
        Film film = filmGuard.checkIfExists(id);
        filmRepository.delete(id);

        return film;
    }

    @Override
    public void likeFilm(int filmId, int userId) {
        filmGuard.checkIfExists(filmId);
        userGuard.checkIfExists(userId);

        filmRepository.likeFilm(filmId, userId);
        eventService.createEvent(userId, EventType.LIKE, EventOperation.ADD, filmId);
    }

    @Override
    public void deleteLikeFromFilm(int filmId, int userId) {
        filmGuard.checkIfExists(filmId);
        userGuard.checkIfExists(userId);

        filmRepository.deleteLikeFromFilm(filmId, userId);
        eventService.createEvent(userId, EventType.LIKE, EventOperation.REMOVE, filmId);
    }

    @Override
    public List<Film> getPopularFilms(int count, Integer genreId, Integer year) {
        return filmRepository.getPopularFilms(count, genreId, year);
    }

    @Override
    public List<Film> getCommonFilms(int userId, int friendId) {
        userGuard.checkIfExists(userId);
        userGuard.checkIfExists(friendId);

        return filmRepository.getCommonFilms(userId, friendId);
    }

    private void checkExistenceOfFilmFields(Film film) {
        mpaGuard.checkIfExists(film.getMpa().getId());
        film.getGenres().forEach(genre -> genreGuard.checkIfExists(genre.getId()));
        film.getDirectors().forEach(director -> directorGuard.checkIfExists(director.getId()));
    }
}
