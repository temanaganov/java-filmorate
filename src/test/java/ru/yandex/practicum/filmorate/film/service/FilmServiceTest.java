package ru.yandex.practicum.filmorate.film.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.filmorate.core.exception.NotFoundException;
import ru.yandex.practicum.filmorate.film.model.Film;
import ru.yandex.practicum.filmorate.film.storage.FilmStorage;
import ru.yandex.practicum.filmorate.film.dto.FilmDto;
import ru.yandex.practicum.filmorate.film.dto.FilmDtoToFilmMapper;
import ru.yandex.practicum.filmorate.mpa.model.Mpa;
import ru.yandex.practicum.filmorate.mpa.storage.MpaStorage;
import ru.yandex.practicum.filmorate.user.model.User;
import ru.yandex.practicum.filmorate.user.storage.UserStorage;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class FilmServiceTest {
    @Mock
    private FilmStorage filmStorage;

    @Mock
    private UserStorage userStorage;

    @Mock
    private MpaStorage mpaStorage;

    @Mock
    private FilmDtoToFilmMapper filmDtoToFilmMapper;

    @InjectMocks
    private FilmServiceImpl filmService;

    @Test
    void getAll_shouldReturnEmptyList_ifStorageIsEmpty() {
        when(filmStorage.getAll()).thenReturn(Collections.emptyList());
        assertThat(filmService.getAll()).isEqualTo(Collections.emptyList());
    }

    @Test()
    void getAll_shouldReturnThreeFilms_ifStorageHasThreeFilms() {
        List<Film> films = List.of(getFilm(1), getFilm(2), getFilm(3));

        when(filmStorage.getAll()).thenReturn(films);

        assertThat(filmService.getAll()).isEqualTo(films);
    }

    @Test()
    void getById_shouldReturnFilm_ifStorageHasIt() {
        int id = 1;
        Film film = getFilm(id);

        when(filmStorage.getById(id)).thenReturn(film);

        Film resultFilm = filmService.getById(id);

        assertThat(resultFilm).isEqualTo(film);
        verify(filmStorage).getById(id);
    }

    @Test()
    void getById_shouldThrowNotFoundException_ifStorageHasNoFilm() {
        int id = 1;
        when(filmStorage.getById(id)).thenReturn(null);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> filmService.getById(id));
        assertThat(exception.getMessage()).isEqualTo("film with id=" + id + " not found");
    }

    @Test()
    void create_shouldReturnNewFilm() {
        FilmDto dto = getFilmDto(0);
        Film film = getFilm(0);
        Film resultFilm = getFilm(1);

        when(filmDtoToFilmMapper.mapFrom(dto)).thenReturn(film);
        when(mpaStorage.getById(dto.getMpa().getId())).thenReturn(dto.getMpa());
        when(filmStorage.create(film)).thenReturn(resultFilm);

        assertThat(filmService.create(dto)).isEqualTo(resultFilm);
        verify(filmStorage).create(film);
    }

    @Test()
    void update_shouldReturnUpdatedFilm() {
        int id = 1;
        String newName = "New name";
        FilmDto dto = getFilmDto(id).withName(newName);
        Film oldFilm = getFilm(id);
        Film newFilm = oldFilm.withName(newName);

        when(filmStorage.getById(id)).thenReturn(oldFilm);
        when(mpaStorage.getById(dto.getMpa().getId())).thenReturn(dto.getMpa());
        when(filmDtoToFilmMapper.mapFrom(dto)).thenReturn(newFilm);
        when(filmStorage.update(id, newFilm)).thenReturn(newFilm);

        assertThat(filmService.update(dto)).isEqualTo(newFilm);
    }

    @Test()
    void update_shouldThrowNotFoundException_ifStorageHasNoFilm() {
        int id = 1;
        FilmDto dto = getFilmDto(id);

        when(filmStorage.getById(id)).thenReturn(null);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> filmService.update(dto));
        assertThat(exception.getMessage()).isEqualTo("film with id=" + id + " not found");
    }

    @Test()
    void delete_shouldDeleteFilmByIdAndReturnIt() {
        int id = 1;
        Film film = getFilm(id);

        when(filmStorage.delete(id)).thenReturn(film);

        assertThat(filmService.delete(id)).isEqualTo(film);
    }

    @Test()
    void delete_shouldThrowNotFoundException_ifStorageHasNoFilm() {
        int id = 1;

        when(filmStorage.delete(id)).thenReturn(null);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> filmService.delete(id));
        assertThat(exception.getMessage()).isEqualTo("film with id=" + id + " not found");
    }

    private Film getFilm(int id) {
        return new Film(
                id,
                "Test film",
                "Test description",
                LocalDate.EPOCH,
                120,
                new Mpa(1, "G"),
                Collections.emptyList()
        );
    }

    private FilmDto getFilmDto(int id) {
        return new FilmDto(
                id,
                "Test film",
                "Test description",
                LocalDate.EPOCH,
                120,
                new Mpa(1, "G"),
                Collections.emptyList()
        );
    }

    private User getUser(int id) {
        return new User(
                id,
                "test@test.test",
                "Test login",
                "Test name",
                LocalDate.EPOCH
        );
    }
}
