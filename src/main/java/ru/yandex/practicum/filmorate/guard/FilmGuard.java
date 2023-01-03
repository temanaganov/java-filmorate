package ru.yandex.practicum.filmorate.guard;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.FilmRepository;

@Component
@RequiredArgsConstructor
public class FilmGuard extends Guard<Film> {
    private final FilmRepository filmRepository;

    @Override
    protected String getGuardClass() {
        return "Film";
    }

    @Override
    protected Film checkMethod(int id) {
        return filmRepository.getById(id);
    }
}
