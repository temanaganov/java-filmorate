package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.event.Event;

import java.util.List;

public interface EventRepository {
    List<Event> getFeed(int userId);
    void create(Event event);
}
