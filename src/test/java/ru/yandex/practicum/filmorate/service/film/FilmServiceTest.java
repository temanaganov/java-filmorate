package ru.yandex.practicum.filmorate.service.film;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.model.film.FilmDto;
import ru.yandex.practicum.filmorate.mapper.FilmDtoToFilmMapper;
import ru.yandex.practicum.filmorate.guard.FilmGuard;
import ru.yandex.practicum.filmorate.model.mpa.Mpa;
import ru.yandex.practicum.filmorate.guard.MpaGuard;

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
    FilmGuard filmGuard;

    @Mock
    MpaGuard mpaGuard;

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

        when(filmGuard.checkIfExists(id)).thenReturn(film);

        Film resultFilm = filmService.getById(id);

        assertThat(resultFilm).isEqualTo(film);
        verify(filmGuard).checkIfExists(id);
    }

    @Test()
    void getById_shouldThrowNotFoundException_ifStorageHasNoFilm() {
        int id = 1;
        when(filmGuard.checkIfExists(id)).thenThrow(new NotFoundException("Film", id));

        NotFoundException exception = assertThrows(NotFoundException.class, () -> filmService.getById(id));
        assertThat(exception.getMessage()).isEqualTo("Film with id=" + id + " not found");
    }

    @Test()
    void create_shouldReturnNewFilm() {
        FilmDto dto = getFilmDto(0);
        Film film = getFilm(0);
        Film resultFilm = getFilm(1);

        when(filmDtoToFilmMapper.mapFrom(dto)).thenReturn(film);
        when(mpaGuard.checkIfExists(dto.getMpa().getId())).thenReturn(dto.getMpa());
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

        when(filmGuard.checkIfExists(id)).thenReturn(oldFilm);
        when(mpaGuard.checkIfExists(dto.getMpa().getId())).thenReturn(dto.getMpa());
        when(filmDtoToFilmMapper.mapFrom(dto)).thenReturn(newFilm);
        when(filmStorage.update(id, newFilm)).thenReturn(newFilm);

        assertThat(filmService.update(dto)).isEqualTo(newFilm);
    }

    @Test()
    void update_shouldThrowNotFoundException_ifStorageHasNoFilm() {
        int id = 1;
        FilmDto dto = getFilmDto(id);

        when(filmGuard.checkIfExists(id)).thenThrow(new NotFoundException("Film", id));

        NotFoundException exception = assertThrows(NotFoundException.class, () -> filmService.update(dto));
        assertThat(exception.getMessage()).isEqualTo("Film with id=" + id + " not found");
    }

    @Test()
    void delete_shouldDeleteFilmByIdAndReturnIt() {
        int id = 1;
        Film film = getFilm(id);

        when(filmGuard.checkIfExists(id)).thenReturn(film);

        assertThat(filmService.delete(id)).isEqualTo(film);
    }

    @Test()
    void delete_shouldThrowNotFoundException_ifStorageHasNoFilm() {
        int id = 1;

        when(filmGuard.checkIfExists(id)).thenThrow(new NotFoundException("Film", id));

        NotFoundException exception = assertThrows(NotFoundException.class, () -> filmService.delete(id));
        assertThat(exception.getMessage()).isEqualTo("Film with id=" + id + " not found");
    }

    private Film getFilm(int id) {
        return Film.builder()
                .id(id)
                .name("Test film")
                .description("Test description")
                .releaseDate(LocalDate.EPOCH)
                .duration(120)
                .mpa(new Mpa(1, "G"))
                .genres(Collections.emptyList())
                .directors(Collections.emptyList())
                .build();
    }

    private FilmDto getFilmDto(int id) {
        return FilmDto.builder()
                .id(id)
                .name("Test film")
                .description("Test description")
                .releaseDate(LocalDate.EPOCH)
                .duration(120)
                .mpa(new Mpa(1, "G"))
                .genres(Collections.emptyList())
                .directors(Collections.emptyList())
                .build();
    }
}
