package ru.yandex.practicum.filmorate.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.core.storage.InMemoryStorage;

@Component
public class InMemoryFilmStorage extends InMemoryStorage<Film> implements FilmStorage {
    @Override
    protected Film withId(Film film, int id) {
        return film.withId(id);
    }
}
