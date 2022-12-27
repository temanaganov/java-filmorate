package ru.yandex.practicum.filmorate.exception;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FieldError {
    private final String field;
    private final String description;

    public String getField() {
        return field;
    }

    public String getDescription() {
        return description;
    }
}
