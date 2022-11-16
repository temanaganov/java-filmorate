package ru.yandex.practicum.filmorate.film;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.filmorate.core.exception.NotFoundException;
import ru.yandex.practicum.filmorate.film.dto.FilmDto;
import ru.yandex.practicum.filmorate.film.dto.FilmDtoToFilmMapper;
import ru.yandex.practicum.filmorate.user.User;
import ru.yandex.practicum.filmorate.user.UserStorage;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
public class FilmServiceTest {
    @Mock
    FilmStorage filmStorage;

    @Mock
    UserStorage userStorage;

    @Mock
    FilmDtoToFilmMapper filmDtoToFilmMapper;

    @InjectMocks
    private FilmServiceImpl filmService;

    @Test
    void getAll_shouldReturnEmptyList_ifStorageIsEmpty() {
        when(filmStorage.getAll()).thenReturn(Collections.emptyList());
        assertEquals(Collections.EMPTY_LIST, filmService.getAll());
    }

    @Test()
    void getAll_shouldReturnThreeFilms_ifStorageHasThreeFilms() {
        List<Film> films = List.of(getFilm(1), getFilm(2), getFilm(3));

        when(filmStorage.getAll()).thenReturn(films);

        assertEquals(films, filmService.getAll());
    }

    @Test()
    void getById_shouldReturnFilm_ifStorageHasIt() {
        int id = 1;
        Film film = getFilm(id);

        when(filmStorage.getById(id)).thenReturn(film);

        Film resultFilm = filmService.getById(id);

        assertEquals(film, resultFilm);
        verify(filmStorage).getById(id);
    }

    @Test()
    void getById_shouldThrowNotFoundException_ifStorageHasNoFilm() {
        int id = 1;
        when(filmStorage.getById(id)).thenReturn(null);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> filmService.getById(id));
        assertEquals("film with id=" + id + " not found", exception.getMessage());
    }

    @Test()
    void create_shouldReturnNewFilm() {
        FilmDto dto = getFilmDto(0);
        Film film = getFilm(0);
        Film resultFilm = getFilm(1);

        when(filmDtoToFilmMapper.mapFrom(dto)).thenReturn(film);
        when(filmStorage.create(film)).thenReturn(resultFilm);

        assertEquals(resultFilm, filmService.create(dto));
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
        when(filmDtoToFilmMapper.mapFrom(dto)).thenReturn(newFilm);
        when(filmStorage.update(id, newFilm)).thenReturn(newFilm);

        assertEquals(newFilm, filmService.update(dto));
    }

    @Test()
    void update_shouldThrowNotFoundException_ifStorageHasNoFilm() {
        int id = 1;
        FilmDto dto = getFilmDto(id);

        when(filmStorage.getById(id)).thenReturn(null);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> filmService.update(dto));
        assertEquals("film with id=" + id + " not found", exception.getMessage());
    }

    @Test()
    void delete_shouldDeleteFilmByIdAndReturnIt() {
        int id = 1;
        Film film = getFilm(id);

        when(filmStorage.delete(id)).thenReturn(film);

        assertEquals(film, filmService.delete(id));
    }

    @Test()
    void delete_shouldThrowNotFoundException_ifStorageHasNoFilm() {
        int id = 1;

        when(filmStorage.delete(id)).thenReturn(null);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> filmService.delete(id));
        assertEquals("film with id=" + id + " not found", exception.getMessage());
    }

    @Test
    void likeFilm_shouldLikeFilmAndReturnUpdated() {
        int filmId = 1;
        int userId = 2;
        Film oldFilm = getFilm(filmId);
        Film newFilm = oldFilm.withLikes(Set.of(userId));
        User user = getUser(userId);

        when(filmStorage.getById(filmId)).thenReturn(oldFilm);
        when(userStorage.getById(userId)).thenReturn(user);
        when(filmStorage.update(filmId, newFilm)).thenReturn(newFilm);

        Film resultFilm = filmService.likeFilm(filmId, userId);

        assertEquals(newFilm, resultFilm);
        verify(filmStorage).update(filmId, newFilm);
    }

    @Test
    void likeFilm_shouldReturnNotChangedFilm_ifUserHasAlreadyLikedIt() {
        int filmId = 1;
        int userId = 2;
        Film film = getFilm(filmId).withLikes(Set.of(userId));
        User user = getUser(userId);

        when(filmStorage.getById(filmId)).thenReturn(film);
        when(userStorage.getById(userId)).thenReturn(user);
        when(filmStorage.update(filmId, film)).thenReturn(film);

        Film resultFilm = filmService.likeFilm(filmId, userId);

        assertEquals(film, resultFilm);
        verify(filmStorage).update(filmId, film);
    }

    @Test
    void likeFilm_shouldThrowNotFoundException_ifStorageHasNoFilm() {
        int filmId = 1;
        int userId = 2;
        User user = getUser(userId);

        when(filmStorage.getById(filmId)).thenReturn(null);
        when(userStorage.getById(userId)).thenReturn(user);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> filmService.likeFilm(filmId, userId));
        assertEquals("film with id=" + filmId + " not found", exception.getMessage());
    }

    @Test
    void likeFilm_shouldThrowNotFoundException_ifStorageHasNoUser() {
        int filmId = 1;
        int userId = 2;
        Film film = getFilm(filmId);

        when(filmStorage.getById(filmId)).thenReturn(film);
        when(userStorage.getById(userId)).thenReturn(null);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> filmService.likeFilm(filmId, userId));
        assertEquals("user with id=" + userId + " not found", exception.getMessage());
    }

    @Test
    void deleteLikeFromFilm_shouldLikeFilmAndReturnUpdated() {
        int filmId = 1;
        int userId = 2;
        Film oldFilm = getFilm(filmId).withLikes(Set.of(userId));
        Film newFilm = oldFilm.withLikes(Collections.emptySet());
        User user = getUser(userId);

        when(filmStorage.getById(filmId)).thenReturn(oldFilm);
        when(userStorage.getById(userId)).thenReturn(user);
        when(filmStorage.update(filmId, newFilm)).thenReturn(newFilm);

        Film resultFilm = filmService.deleteLikeFromFilm(filmId, userId);

        assertEquals(newFilm, resultFilm);
        verify(filmStorage).update(filmId, newFilm);
    }

    @Test
    void deleteLikeFromFilm_shouldReturnNotChangedFilm_ifUserHasNotLikedIt() {
        int filmId = 1;
        int userId = 2;
        Film film = getFilm(filmId);
        User user = getUser(userId);

        when(filmStorage.getById(filmId)).thenReturn(film);
        when(userStorage.getById(userId)).thenReturn(user);
        when(filmStorage.update(filmId, film)).thenReturn(film);

        Film resultFilm = filmService.deleteLikeFromFilm(filmId, userId);

        assertEquals(film, resultFilm);
        verify(filmStorage).update(filmId, film);
    }

    @Test
    void deleteLikeFromFilm_shouldThrowNotFoundException_ifStorageHasNoFilm() {
        int filmId = 1;
        int userId = 2;
        User user = getUser(userId);

        when(filmStorage.getById(filmId)).thenReturn(null);
        when(userStorage.getById(userId)).thenReturn(user);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> filmService.deleteLikeFromFilm(filmId, userId));
        assertEquals("film with id=" + filmId + " not found", exception.getMessage());
    }

    @Test
    void deleteLikeFromFilm_shouldThrowNotFoundException_ifStorageHasNoUser() {
        int filmId = 1;
        int userId = 2;
        Film film = getFilm(filmId);

        when(filmStorage.getById(filmId)).thenReturn(film);
        when(userStorage.getById(userId)).thenReturn(null);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> filmService.deleteLikeFromFilm(filmId, userId));
        assertEquals("user with id=" + userId + " not found", exception.getMessage());
    }

    @Test
    void getPopularFilms_shouldReturnEmptyList_ifStorageIsEmpty() {
        int count = 10;

        when(filmStorage.getAll()).thenReturn(Collections.emptyList());

        assertEquals(Collections.emptyList(), filmService.getPopularFilms(count));
    }

    @Test
    void getPopularFilms_shouldReturnEmptyList_ifCountIsNegative() {
        int count = -1;
        List<Film> films = List.of(
                getFilm(1).withLikes(Set.of(1)),
                getFilm(2).withLikes(Set.of(1, 2)),
                getFilm(3).withLikes(Set.of(1, 2, 3))
        );

        when(filmStorage.getAll()).thenReturn(films);

        assertEquals(Collections.emptyList(), filmService.getPopularFilms(count));
    }

    @Test
    void getPopularFilms_shouldReturnSortedListOfFilms() {
        int count = 10;
        List<Film> films = List.of(
                getFilm(1).withLikes(Set.of(1)),
                getFilm(2).withLikes(Set.of(1, 2)),
                getFilm(3).withLikes(Set.of(1, 2, 3))
        );

        when(filmStorage.getAll()).thenReturn(films);

        assertEquals(
                List.of(
                        getFilm(3).withLikes(Set.of(1, 2, 3)),
                        getFilm(2).withLikes(Set.of(1, 2)),
                        getFilm(1).withLikes(Set.of(1))
                ),
                filmService.getPopularFilms(count)
        );
    }

    @Test
    void getPopularFilms_shouldReturnSortedListOfFilmsWithLimit() {
        int count = 2;
        List<Film> films = List.of(
                getFilm(1).withLikes(Set.of(1)),
                getFilm(2).withLikes(Set.of(1, 2)),
                getFilm(3).withLikes(Set.of(1, 2, 3))
        );

        when(filmStorage.getAll()).thenReturn(films);

        assertEquals(
                List.of(
                        getFilm(3).withLikes(Set.of(1, 2, 3)),
                        getFilm(2).withLikes(Set.of(1, 2))
                ),
                filmService.getPopularFilms(count)
        );
    }

    private Film getFilm(int id) {
        return new Film(
                id,
                "Test film",
                "Test description",
                LocalDate.EPOCH,
                120,
                Collections.emptySet()
        );
    }

    private FilmDto getFilmDto(int id) {
        return new FilmDto(
                id,
                "Test film",
                "Test description",
                LocalDate.EPOCH,
                120
        );
    }

    private User getUser(int id) {
        return new User(
                id,
                "test@test.test",
                "Test login",
                "Test name",
                LocalDate.EPOCH,
                Collections.emptySet()
        );
    }
}
