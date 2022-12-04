package ru.yandex.practicum.filmorate.user.model;

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
