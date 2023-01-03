package ru.yandex.practicum.filmorate.model;

import lombok.Value;
import lombok.With;

@With
@Value
public class Director {
    int id;
    String name;
}
