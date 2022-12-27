package ru.yandex.practicum.filmorate.storage.film;

import org.junit.jupiter.api.BeforeEach;
import ru.yandex.practicum.filmorate.storage.InMemoryStorageTest;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.mpa.Mpa;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

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
                new Mpa(1, "G"),
                Collections.emptyList(),
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
                new Mpa(1, "G"),
                Collections.emptyList(),
                Collections.emptyList()
        );
    }
}
