package ru.yandex.practicum.filmorate.core.util;

public interface Mapper<F, T> {
    T mapFrom(F object);
}
