package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.Collections;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@Sql(statements = "DELETE FROM film")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmRepositoryTest {
    private final FilmRepository filmRepository;

    @Test
    void getAll_shouldReturnEmptyList() {
        assertThat(filmRepository.getAll()).isEmpty();
    }

    @Test
    void getAll_shouldReturnListOfTwoFilms() {
        filmRepository.create(getFilm(1));
        filmRepository.create(getFilm(2));

        assertThat(filmRepository.getAll()).hasSize(2);
    }

    @Test
    void getById_shouldReturnNull_IfFilmIsNotExists() {
        assertThat(filmRepository.getById(1)).isNull();
    }

    @Test
    void getById_shouldReturnFilm() {
        Film film = filmRepository.create(getFilm(1));

        assertThat(filmRepository.getById(film.getId())).isEqualTo(getFilm(film.getId()));
    }

    @Test
    void update_shouldReturnNull_IfFilmIsNotExists() {
        assertThat(filmRepository.update(getFilm(1))).isNull();
    }

    @Test
    void update_shouldUpdateFilm() {
        Film film = filmRepository.create(getFilm(1));

        assertThat(filmRepository.update(film.withName("new name")))
                .hasFieldOrPropertyWithValue("name", "new name");
    }

    @Test
    void delete_shouldReturnNull_IfFilmIsNotExists() {
        assertThat(filmRepository.update(getFilm(1))).isNull();
    }

    @Test
    void delete_shouldDeleteFilm() {
        Film film = filmRepository.create(getFilm(1));

        assertThat(filmRepository.getAll()).hasSize(1);
        filmRepository.delete(film.getId());
        assertThat(filmRepository.getAll()).isEmpty();
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
}
