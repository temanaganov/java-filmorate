package ru.yandex.practicum.filmorate.service.event;

import ru.yandex.practicum.filmorate.model.event.Event;
import ru.yandex.practicum.filmorate.model.event.EventOperation;
import ru.yandex.practicum.filmorate.model.event.EventType;

import java.util.List;

public interface EventService {
    List<Event> getFeed(int userId);
    void createEvent(int userId, EventType eventTYpe, EventOperation eventOperation, int entityId);
}
