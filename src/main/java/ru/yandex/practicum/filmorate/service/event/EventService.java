package ru.yandex.practicum.filmorate.service.event;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.event.Event;
import ru.yandex.practicum.filmorate.model.event.EventOperation;
import ru.yandex.practicum.filmorate.model.event.EventType;
import ru.yandex.practicum.filmorate.storage.event.EventStorage;
import ru.yandex.practicum.filmorate.guard.UserGuard;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventStorage eventStorage;
    private final UserGuard userGuard;

    public List<Event> getFeed(int userId) {
        userGuard.checkIfExists(userId);
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

    public void deleteFriendEvent(int id, int friendId) {
        Event event = getBaseEvent(id, friendId);
        event.setEventType(EventType.FRIEND);
        event.setOperation(EventOperation.REMOVE);

        eventStorage.create(event);
    }

    public void addReviewEvent(int userId, int reviewId) {
        Event event = getBaseEvent(userId, reviewId);
        event.setEventType(EventType.REVIEW);
        event.setOperation(EventOperation.ADD);

        eventStorage.create(event);
    }

    public void deleteReviewEvent(int userId, int reviewId) {
        Event event = getBaseEvent(userId, reviewId);
        event.setEventType(EventType.REVIEW);
        event.setOperation(EventOperation.REMOVE);

        eventStorage.create(event);
    }

    public void updateReviewEvent(int userId, int reviewId) {
        Event event = getBaseEvent(userId, reviewId);
        event.setEventType(EventType.REVIEW);
        event.setOperation(EventOperation.UPDATE);

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

