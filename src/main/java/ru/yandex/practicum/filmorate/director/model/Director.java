package ru.yandex.practicum.filmorate.director.model;

import lombok.Value;
import lombok.With;

@With
@Value
public class Director {
    int id;
    String name;
}
