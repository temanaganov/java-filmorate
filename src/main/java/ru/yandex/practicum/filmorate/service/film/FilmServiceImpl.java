package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.mapper.Mapper;
import ru.yandex.practicum.filmorate.service.event.EventService;
import ru.yandex.practicum.filmorate.guard.DirectorGuard;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.model.film.FilmDto;
import ru.yandex.practicum.filmorate.guard.FilmGuard;
import ru.yandex.practicum.filmorate.guard.GenreGuard;
import ru.yandex.practicum.filmorate.guard.MpaGuard;
import ru.yandex.practicum.filmorate.guard.UserGuard;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {
    private final FilmStorage filmStorage;
    private final Mapper<FilmDto, Film> filmDtoToFilmMapper;
    private final EventService eventService;
    private final FilmGuard filmGuard;
    private final UserGuard userGuard;
    private final MpaGuard mpaGuard;
    private final GenreGuard genreGuard;
    private final DirectorGuard directorGuard;

    @Override
    public List<Film> search(String query, String by) {
        return filmStorage.search(query, by);
    }

    @Override
    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    @Override
    public List<Film> getFilmsByDirectorId(int directorId, String sortBy) {
        directorGuard.checkIfExists(directorId);

        return filmStorage.getFilmsByDirectorId(directorId, sortBy);
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
        eventService.addLikeEvent(filmId, userId);
    }

    @Override
    public void deleteLikeFromFilm(int filmId, int userId) {
        filmGuard.checkIfExists(filmId);
        userGuard.checkIfExists(userId);

        eventService.deleteLikeEvent(filmId, userId);
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
