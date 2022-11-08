package ru.yandex.practicum.filmorate.core.exception;

public class FieldValidationException extends RuntimeException {
    private final String field;
    private final String description;

    public FieldValidationException(String field, String description) {
        super(description);
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
