package ru.yandex.practicum.filmorate.guard;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.mpa.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

@Component
@RequiredArgsConstructor
public class MpaGuard extends Guard<Mpa> {
    private final MpaStorage mpaStorage;

    @Override
    protected String getGuardClass() {
        return "Mpa";
    }

    @Override
    protected Mpa checkMethod(int id) {
        return mpaStorage.getById(id);
    }
}
