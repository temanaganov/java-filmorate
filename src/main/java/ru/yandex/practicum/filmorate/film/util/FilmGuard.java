package ru.yandex.practicum.filmorate.film.util;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.core.util.Guard;
import ru.yandex.practicum.filmorate.film.model.Film;
import ru.yandex.practicum.filmorate.film.storage.FilmStorage;

@Component
@RequiredArgsConstructor
public class FilmGuard extends Guard<Film> {
    @Qualifier("dbFilmStorage")
    private final FilmStorage filmStorage;

    @Override
    protected String getGuardClass() {
        return "Film";
    }

    @Override
    protected Film checkMethod(int id) {
        return filmStorage.getById(id);
    }
}
