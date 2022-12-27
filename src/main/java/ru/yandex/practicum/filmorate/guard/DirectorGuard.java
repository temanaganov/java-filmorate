package ru.yandex.practicum.filmorate.guard;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.director.Director;
import ru.yandex.practicum.filmorate.storage.director.DirectorStorage;

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
