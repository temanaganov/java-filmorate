package ru.yandex.practicum.filmorate.film;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.core.exception.NotFoundException;
import ru.yandex.practicum.filmorate.core.util.Mapper;
import ru.yandex.practicum.filmorate.film.dto.CreateFilmDto;
import ru.yandex.practicum.filmorate.film.dto.UpdateFilmDto;
import ru.yandex.practicum.filmorate.user.User;
import ru.yandex.practicum.filmorate.user.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class InMemoryFilmService implements FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final Mapper<CreateFilmDto, Film> createFilmDtoToFilmMapper;
    private final Mapper<UpdateFilmDto, Film> updateFilmDtoToFilmMapper;

    public InMemoryFilmService(
            FilmStorage filmStorage,
            UserStorage userStorage,
            Mapper<CreateFilmDto, Film> createFilmDtoToFilmMapper,
            Mapper<UpdateFilmDto, Film> updateFilmDtoToFilmMapper
    ) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.createFilmDtoToFilmMapper = createFilmDtoToFilmMapper;
        this.updateFilmDtoToFilmMapper = updateFilmDtoToFilmMapper;
    }

    @Override
    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    @Override
    public Film getById(int id) {
        Film film = filmStorage.getById(id);
        if (film == null) throw new NotFoundException("film", id);
        return film;
    }

    @Override
    public Film create(CreateFilmDto dto) {
        Film newFilm = createFilmDtoToFilmMapper.mapFrom(dto);
        return filmStorage.create(newFilm);
    }

    @Override
    public Film update(UpdateFilmDto dto) {
        Film currentFilm = filmStorage.getById(dto.getId());

        if (currentFilm == null) throw new NotFoundException("film", dto.getId());

        Film film = updateFilmDtoToFilmMapper.mapFrom(dto).withLikes(currentFilm.getLikes());

        return filmStorage.update(film);
    }

    @Override
    public Film delete(int id) {
        Film film = filmStorage.delete(id);
        if (film == null) throw new NotFoundException("film", id);
        return film;
    }

    @Override
    public Film likeFilm(int filmId, int userId) {
        Film film = filmStorage.getById(filmId);
        User user = userStorage.getById(userId);

        if (film == null) throw new NotFoundException("film", filmId);
        if (user == null) throw new NotFoundException("user", userId);

        Set<Integer> currentLikes = new HashSet<>(film.getLikes());
        currentLikes.add(userId);

        Film updatedFilm = film.withLikes(currentLikes);

        return filmStorage.update(updatedFilm);
    }

    @Override
    public Film deleteLikeFromFilm(int filmId, int userId) {
        Film film = filmStorage.getById(filmId);
        User user = userStorage.getById(userId);

        if (film == null) throw new NotFoundException("film", filmId);
        if (user == null) throw new NotFoundException("user", userId);

        Set<Integer> currentLikes = new HashSet<>(film.getLikes());
        currentLikes.remove(userId);

        Film updatedFilm = film.withLikes(currentLikes);

        return filmStorage.update(updatedFilm);
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        List<Film> films = filmStorage.getAll();
        return films
                .stream()
                .sorted((f1, f2) -> f2.getLikes().size() - f1.getLikes().size())
                .limit(Math.max(count, 0))
                .collect(Collectors.toList());
    }
}
