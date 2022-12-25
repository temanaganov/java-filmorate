package ru.yandex.practicum.filmorate.events.storage;

import ru.yandex.practicum.filmorate.events.model.Event;
import ru.yandex.practicum.filmorate.events.model.EventType;

import java.util.List;

public interface EventStorage {

    List<Event> getFeed(int userId);

    Event create(Event event);

}
