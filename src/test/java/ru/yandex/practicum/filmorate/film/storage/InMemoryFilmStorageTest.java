package ru.yandex.practicum.filmorate.film.storage;

import org.junit.jupiter.api.BeforeEach;
import ru.yandex.practicum.filmorate.core.storage.InMemoryStorageTest;
import ru.yandex.practicum.filmorate.film.model.Film;
import ru.yandex.practicum.filmorate.mpa.model.Mpa;

import java.time.LocalDate;
import java.util.Collections;

public class InMemoryFilmStorageTest extends InMemoryStorageTest<Film> {
    @BeforeEach
    void beforeEach() {
        storage = new InMemoryFilmStorage();
    }

    @Override
    protected Film getEntity(int id) {
        return new Film(
                id,
                "Test film",
                "Test description",
                LocalDate.EPOCH,
                120,
                new Mpa(1, "test mpa"),
                Collections.emptyList()
        );
    }

    @Override
    protected Film getEntityForUpdate(int id) {
        return new Film(
                id,
                "Test film updated",
                "Test description updated",
                LocalDate.EPOCH,
                120,
                new Mpa(1, "test mpa"),
                Collections.emptyList()
        );
    }
}
