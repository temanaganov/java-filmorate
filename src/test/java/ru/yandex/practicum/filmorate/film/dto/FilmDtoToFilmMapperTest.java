package ru.yandex.practicum.filmorate.film.dto;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.film.model.Film;
import ru.yandex.practicum.filmorate.mpa.model.Mpa;

import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

public class FilmDtoToFilmMapperTest {
    private final FilmDtoToFilmMapper filmDtoToFilmMapper = new FilmDtoToFilmMapper();

    @Test
    void mapFrom_shouldCreateFilmWithSameFields() {
        FilmDto filmDto = new FilmDto(
                1,
                "Test film",
                "Test description",
                LocalDate.EPOCH,
                120,
                new Mpa(1, "G"),
                Collections.emptyList(),
                Collections.emptyList()
        );

        Film film = filmDtoToFilmMapper.mapFrom(filmDto);

        assertAll(() -> {
            assertThat(film.getId()).isEqualTo(filmDto.getId());
            assertThat(film.getName()).isEqualTo(filmDto.getName());
            assertThat(film.getDescription()).isEqualTo(filmDto.getDescription());
            assertThat(film.getReleaseDate()).isEqualTo(filmDto.getReleaseDate());
            assertThat(film.getDuration()).isEqualTo(filmDto.getDuration());
            assertThat(film.getMpa()).isEqualTo(filmDto.getMpa());
            assertThat(film.getGenres()).isEqualTo(Collections.emptyList());
        });
    }
}
