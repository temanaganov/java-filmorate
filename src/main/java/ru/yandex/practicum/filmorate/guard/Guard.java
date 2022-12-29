package ru.yandex.practicum.filmorate.guard;

import lombok.RequiredArgsConstructor;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

@RequiredArgsConstructor
public abstract class Guard<T> {
    protected abstract String getGuardClass();
    protected abstract T checkMethod(int id);

    public T checkIfExists(int id) {
        T entity = checkMethod(id);

        if (entity == null){
            throw new NotFoundException(getGuardClass(), id);
        }

        return entity;
    }
}
