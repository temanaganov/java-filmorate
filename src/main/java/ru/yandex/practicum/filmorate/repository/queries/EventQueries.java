package ru.yandex.practicum.filmorate.repository.queries;

public class EventQueries {
    public static final String GET_FEED = "SELECT * FROM event WHERE user_id = ?";
}
