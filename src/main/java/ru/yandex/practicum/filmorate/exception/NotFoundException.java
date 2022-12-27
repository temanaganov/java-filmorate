package ru.yandex.practicum.filmorate.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String entity, int id) {
        super(entity + " with id=" + id + " not found");
    }
}
