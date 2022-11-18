package ru.yandex.practicum.filmorate.core.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String entity, int id) {
        super(entity + " with id=" + id + " not found");
    }
}
