package ru.yandex.practicum.filmorate.model.user;

import lombok.Value;
import lombok.With;

import java.time.LocalDate;

@With
@Value
public class User {
    int id;
    String email;
    String login;
    String name;
    LocalDate birthday;
}
