package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Value;
import lombok.With;

import java.time.LocalDate;

@With
@Value
@Builder
public class User {
    int id;
    String email;
    String login;
    String name;
    LocalDate birthday;
}
