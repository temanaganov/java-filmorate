package ru.yandex.practicum.filmorate.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.core.storage.InMemoryStorage;

@Component
public class InMemoryFilmStorage extends InMemoryStorage<Film> implements FilmStorage {
    @Override
    protected int getId(Film film) {
        return film.getId();
    }

    @Override
    protected Film withId(Film film) {
        return film.withId(getNextId());
    }
}
