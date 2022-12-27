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
public class EventServiceImpl implements EventService {
    private final EventStorage eventStorage;
    private final UserGuard userGuard;
    @Override
    public List<Event> getFeed(int userId) {
        userGuard.checkIfExists(userId);
        return eventStorage.getFeed(userId);
    }
    @Override
    public Event createEvent(int userId, EventType eventTYpe, EventOperation eventOperation, int entityId){
        Event event = new Event();
        event.setTimestamp(Instant.now().toEpochMilli());
        event.setUserId(userId);
        event.setEntityId(entityId);
        event.setEventType(eventTYpe);
        event.setOperation(eventOperation);
        return eventStorage.create(event);
    }
    @Override
    public void addLikeEvent(int filmId, int userId) {
        createEvent(userId, EventType.LIKE,EventOperation.ADD, filmId);
//        Event event = getBaseEvent(userId, filmId);
//        event.setEventType(EventType.LIKE);
//        event.setOperation(EventOperation.ADD);
//
//        eventStorage.create(event);
    }
    @Override
    public void deleteLikeEvent(int filmId, int userId) {
        createEvent(userId, EventType.LIKE, EventOperation.REMOVE, filmId);
//        Event event = getBaseEvent(userId, filmId);
//        event.setEventType(EventType.LIKE);
//        event.setOperation(EventOperation.REMOVE);
//
//        eventStorage.create(event);
    }
    @Override
    public void addFriendEvent(int id, int friendId) {
        createEvent(id, EventType.FRIEND, EventOperation.ADD, friendId);
//        Event event = getBaseEvent(id, friendId);
//        event.setEventType(EventType.FRIEND);
//        event.setOperation(EventOperation.ADD);
//
//        eventStorage.create(event);
    }
    @Override
    public void deleteFriendEvent(int id, int friendId) {
        createEvent(id, EventType.FRIEND, EventOperation.REMOVE, friendId);
//        Event event = getBaseEvent(id, friendId);
//        event.setEventType(EventType.FRIEND);
//        event.setOperation(EventOperation.REMOVE);
//
//        eventStorage.create(event);
    }
    @Override
    public void addReviewEvent(int userId, int reviewId) {
        createEvent(userId, EventType.REVIEW, EventOperation.ADD, reviewId);
//        Event event = getBaseEvent(userId, reviewId);
//        event.setEventType(EventType.REVIEW);
//        event.setOperation(EventOperation.ADD);
//
//        eventStorage.create(event);
    }
    @Override
    public void deleteReviewEvent(int userId, int reviewId) {
        createEvent(userId, EventType.REVIEW, EventOperation.REMOVE, reviewId);
//        Event event = getBaseEvent(userId, reviewId);
//        event.setEventType(EventType.REVIEW);
//        event.setOperation(EventOperation.REMOVE);
//
//        eventStorage.create(event);
    }
    @Override
    public void updateReviewEvent(int userId, int reviewId) {
        createEvent(userId, EventType.REVIEW, EventOperation.UPDATE, reviewId);
//        Event event = getBaseEvent(userId, reviewId);
//        event.setEventType(EventType.REVIEW);
//        event.setOperation(EventOperation.UPDATE);
//
//        eventStorage.create(event);
    }

//    private Event getBaseEvent(int userId, int entityId) {
//        Event event = new Event();
//        event.setTimestamp(Instant.now().toEpochMilli());
//        event.setUserId(userId);
//        event.setEntityId(entityId);
//
//        return event;
//    }
}

