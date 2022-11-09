package ru.yandex.practicum.filmorate.core.exception;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FieldValidationException extends RuntimeException {
    private final String field;
    private final String description;

    public String getField() {
        return field;
    }

    public String getDescription() {
        return description;
    }
}
