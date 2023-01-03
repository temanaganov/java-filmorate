package ru.yandex.practicum.filmorate.guard;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.MpaRepository;

@Component
@RequiredArgsConstructor
public class MpaGuard extends Guard<Mpa> {
    private final MpaRepository mpaRepository;

    @Override
    protected String getGuardClass() {
        return "Mpa";
    }

    @Override
    protected Mpa checkMethod(int id) {
        return mpaRepository.getById(id);
    }
}
