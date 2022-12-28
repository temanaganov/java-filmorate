package ru.yandex.practicum.filmorate.mapper;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.FilmDto;
import ru.yandex.practicum.filmorate.model.mpa.Mpa;

import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

public class FilmDtoToFilmMapperTest {
    private final FilmDtoToFilmMapper filmDtoToFilmMapper = new FilmDtoToFilmMapper();

    @Test
    void mapFrom_shouldCreateFilmWithSameFields() {
        FilmDto filmDto = FilmDto.builder()
                .id(1)
                .name("Test film")
                .description("Test description")
                .releaseDate(LocalDate.EPOCH)
                .duration(120)
                .mpa(new Mpa(1, "G"))
                .genres(Collections.emptyList())
                .directors(Collections.emptyList())
                .build();

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
