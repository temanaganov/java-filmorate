package ru.yandex.practicum.filmorate.event.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.core.util.Guard;
import ru.yandex.practicum.filmorate.event.model.Event;
import ru.yandex.practicum.filmorate.event.model.EventOperation;
import ru.yandex.practicum.filmorate.event.model.EventType;
import ru.yandex.practicum.filmorate.event.storage.EventStorage;
import ru.yandex.practicum.filmorate.user.model.User;
import ru.yandex.practicum.filmorate.user.storage.UserStorage;
import java.time.Instant;
import java.util.List;

@Service
public class EventService {
    private final EventStorage eventStorage;
    private final Guard<User> userGuard;
    private final UserStorage userStorage;


    public EventService(EventStorage eventStorage, @Qualifier("dbUserStorage") UserStorage userStorage) {
        this.eventStorage = eventStorage;
        this.userStorage = userStorage;
        this.userGuard = new Guard<>(userStorage::getById, User.class);
    }

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

    public void deleteFriendEvent(int id, int friendId){
        Event event = getBaseEvent(id, friendId);
        event.setEventType(EventType.FRIEND);
        event.setOperation(EventOperation.REMOVE);

        eventStorage.create(event);
    }

    public void addReviewEvent(int userId, int reviewId){
        Event event = getBaseEvent(userId, reviewId);
        event.setEventType(EventType.REVIEW);
        event.setOperation(EventOperation.ADD);

        eventStorage.create(event);
    }

    public void deleteReviewEvent(int userId, int reviewId){
        Event event = getBaseEvent(userId, reviewId);
        event.setEventType(EventType.REVIEW);
        event.setOperation(EventOperation.REMOVE);

        eventStorage.create(event);
    }

    public void updateReviewEvent(int userId, int reviewId){
        Event event = getBaseEvent(userId, reviewId);
        event.setEventType(EventType.REVIEW);
        event.setOperation(EventOperation.UPDATE);

        eventStorage.create(event);
    }

    private Event getBaseEvent(int userId, int entityId){
        Event event = new Event();
        event.setTimestamp(Instant.now().toEpochMilli());
        event.setUserId(userId);
        event.setEntityId(entityId);

        return event;
    }

}

