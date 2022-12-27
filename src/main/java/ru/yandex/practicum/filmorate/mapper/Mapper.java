package ru.yandex.practicum.filmorate.mapper;

public interface Mapper<F, T> {
    T mapFrom(F object);
}
