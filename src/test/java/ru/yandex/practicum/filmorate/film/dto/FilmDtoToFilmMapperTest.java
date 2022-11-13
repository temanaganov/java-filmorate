package ru.yandex.practicum.filmorate.film.dto;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.film.Film;

import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class FilmDtoToFilmMapperTest {
    private final FilmDtoToFilmMapper filmDtoToFilmMapper = new FilmDtoToFilmMapper();

    @Test
    void mapFrom_shouldCreateFilmWithSameFields() {
        FilmDto filmDto = new FilmDto(
                1,
                "Test film",
                "Test description",
                LocalDate.EPOCH,
                120
        );

        Film film = filmDtoToFilmMapper.mapFrom(filmDto);

        assertAll(() -> {
            assertEquals(filmDto.getId(), film.getId());
            assertEquals(filmDto.getName(), film.getName());
            assertEquals(filmDto.getDescription(), film.getDescription());
            assertEquals(filmDto.getReleaseDate(), film.getReleaseDate());
            assertEquals(filmDto.getDuration(), film.getDuration());
            assertEquals(Collections.emptySet(), film.getLikes());
        });
    }
}
