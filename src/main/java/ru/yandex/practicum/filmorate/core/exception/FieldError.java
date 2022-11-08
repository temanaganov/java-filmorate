package ru.yandex.practicum.filmorate.core.exception;

public class FieldError {
    private final String field;
    private final String description;

    public FieldError(String field, String description) {
        this.field = field;
        this.description = description;
    }

    public String getField() {
        return field;
    }

    public String getDescription() {
        return description;
    }
}
