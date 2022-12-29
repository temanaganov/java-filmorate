package ru.yandex.practicum.filmorate.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@RequiredArgsConstructor
@ResponseStatus(HttpStatus.BAD_REQUEST)
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
