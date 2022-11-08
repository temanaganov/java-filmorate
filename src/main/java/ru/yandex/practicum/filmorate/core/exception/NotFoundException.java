package ru.yandex.practicum.filmorate.core.exception;

public class NotFoundException extends RuntimeException {
    private final String entity;
    private final int id;

    public NotFoundException(String entity, int id) {
        this.entity = entity;
        this.id = id;
    }

    @Override
    public String getMessage() {
        return entity + " with " + id + " not found";
    }
}
