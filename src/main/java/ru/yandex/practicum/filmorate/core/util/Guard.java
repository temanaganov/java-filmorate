package ru.yandex.practicum.filmorate.core.util;

import lombok.RequiredArgsConstructor;
import ru.yandex.practicum.filmorate.core.exception.NotFoundException;

import java.util.function.Function;

@RequiredArgsConstructor
public class Guard<T> {
    private final Function<Integer, T> checkMethod;
    private final Class<T> tClass;

    public T checkIfExists(int id) {
        T entity = checkMethod.apply(id);

        if (entity == null){
            throw new NotFoundException(tClass.getName(), id);
        }

        return entity;
    }
}
