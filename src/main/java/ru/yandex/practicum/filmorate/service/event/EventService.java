package ru.yandex.practicum.filmorate.service.event;

import ru.yandex.practicum.filmorate.model.event.Event;
import ru.yandex.practicum.filmorate.model.event.EventOperation;
import ru.yandex.practicum.filmorate.model.event.EventType;

import java.util.List;

public interface EventService {


    List<Event> getFeed(int userId);

    Event createEvent(int userId, EventType eventTYpe, EventOperation eventOperation, int entityId);

    void addLikeEvent(int filmId, int userId);

    void deleteLikeEvent(int filmId, int userId);

    void addFriendEvent(int id, int friendId);

    void deleteFriendEvent(int id, int friendId);

    void addReviewEvent(int userId, int reviewId);

    void deleteReviewEvent(int userId, int reviewId);

    void updateReviewEvent(int userId, int reviewId);
}
