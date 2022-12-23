package ru.yandex.practicum.filmorate.events.storage;

import ru.yandex.practicum.filmorate.events.model.Event;
import ru.yandex.practicum.filmorate.events.model.EventType;

import java.util.List;

public interface EventStorage {

    List<Event> getFeed(int userId);

    Event create(Event event);

//    void addFriendEvent(int id, int friendId);
//
//    void deleteFriendEvent(int id, int friendId);
//
//    void addLikeEvent(int id, int userId);
//
//    void deleteLikeEvent(int id, int userId);
}

//    void addReviewEvent(Review reviewAnswer);
//
//    void updateReviewEvent(Review reviewAnswer);
//
//    void deleteReviewEvent(Review review);
//}
