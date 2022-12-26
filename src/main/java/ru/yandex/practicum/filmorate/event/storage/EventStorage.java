package ru.yandex.practicum.filmorate.event.storage;

import ru.yandex.practicum.filmorate.event.model.Event;

import java.util.List;

public interface EventStorage {
    List<Event> getFeed(int userId);
    Event create(Event event);
}
