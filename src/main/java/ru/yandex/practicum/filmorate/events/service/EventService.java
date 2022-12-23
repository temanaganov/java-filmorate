package ru.yandex.practicum.filmorate.events.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.events.model.Event;
import ru.yandex.practicum.filmorate.events.model.EventOperation;
import ru.yandex.practicum.filmorate.events.model.EventType;
import ru.yandex.practicum.filmorate.events.storage.EventStorage;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventStorage eventStorage;

    public List<Event> getFeed(int userId) {
        return eventStorage.getFeed(userId);
    }



    public void addLikeEvent(int filmId, int userId) {
        Event event = getBaseEvent(userId, filmId);
        event.setEventType(EventType.LIKE);
        event.setOperation(EventOperation.ADD);

        eventStorage.create(event);
    }


    public void deleteLikeEvent(int filmId, int userId) {
        Event event = getBaseEvent(userId, filmId);
        event.setEventType(EventType.LIKE);
        event.setOperation(EventOperation.REMOVE);

        eventStorage.create(event);
    }

    public void addFriendEvent(int id, int friendId) {
        Event event = getBaseEvent(id, friendId);
        event.setEventType(EventType.FRIEND);
        event.setOperation(EventOperation.ADD);

        eventStorage.create(event);
    }


    public void deleteFriendEvent(int id, int friendId){
        Event event = getBaseEvent(id, friendId);
        event.setEventType(EventType.FRIEND);
        event.setOperation(EventOperation.REMOVE);

        eventStorage.create(event);
    }

    private Event getBaseEvent(int userId, int entityId) {
        Event event = new Event();
        event.setTimestamp(Instant.now().toEpochMilli());
        event.setUserId(userId);
        event.setEntityId(entityId);

        return event;
    }
}

