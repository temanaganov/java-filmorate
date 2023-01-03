package ru.yandex.practicum.filmorate.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.event.Event;
import ru.yandex.practicum.filmorate.model.event.EventOperation;
import ru.yandex.practicum.filmorate.model.event.EventType;
import ru.yandex.practicum.filmorate.repository.EventRepository;
import ru.yandex.practicum.filmorate.repository.queries.EventQueries;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class EventRepositoryImpl implements EventRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Event> getFeed(int userId) {
        return jdbcTemplate.query(EventQueries.GET_FEED, this::mapRowToEvent, userId);
    }

    @Override
    public void create(Event event) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("event")
                .usingGeneratedKeyColumns("event_id");

        Map<String, Object> values = new HashMap<>();
        values.put("timestamp", event.getTimestamp());
        values.put("user_id", event.getUserId());
        values.put("event_type", event.getEventType().toString());
        values.put("operation", event.getOperation().toString());
        values.put("entity_id", event.getEntityId());

        simpleJdbcInsert.executeAndReturnKey(values).intValue();
    }

    private Event mapRowToEvent(ResultSet resultSet, int num) throws SQLException {
        return Event.builder()
                .eventId(resultSet.getInt("event_id"))
                .timestamp(resultSet.getLong("timestamp"))
                .userId(resultSet.getInt("user_id"))
                .eventType(EventType.valueOf(resultSet.getString("event_type")))
                .operation(EventOperation.valueOf(resultSet.getString("operation")))
                .entityId(resultSet.getInt("entity_id"))
                .build();
    }
}
