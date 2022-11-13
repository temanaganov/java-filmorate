package ru.yandex.practicum.filmorate.user;

import lombok.Value;
import lombok.With;

import java.time.LocalDate;
import java.util.Set;

@With
@Value
public class User {
    int id;
    String email;
    String login;
    String name;
    LocalDate birthday;
    Set<Integer> friends;
}
