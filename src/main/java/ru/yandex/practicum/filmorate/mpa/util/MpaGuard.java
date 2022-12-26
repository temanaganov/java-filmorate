package ru.yandex.practicum.filmorate.mpa.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.core.util.Guard;
import ru.yandex.practicum.filmorate.mpa.model.Mpa;
import ru.yandex.practicum.filmorate.mpa.storage.MpaStorage;

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
