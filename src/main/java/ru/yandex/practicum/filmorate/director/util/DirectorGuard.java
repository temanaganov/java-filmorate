package ru.yandex.practicum.filmorate.director.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.core.util.Guard;
import ru.yandex.practicum.filmorate.director.model.Director;
import ru.yandex.practicum.filmorate.director.storage.DirectorStorage;

@Component
@RequiredArgsConstructor
public class DirectorGuard extends Guard<Director> {
    private final DirectorStorage directorStorage;

    @Override
    protected String getGuardClass() {
        return "Director";
    }

    @Override
    protected Director checkMethod(int id) {
        return directorStorage.getById(id);
    }
}
