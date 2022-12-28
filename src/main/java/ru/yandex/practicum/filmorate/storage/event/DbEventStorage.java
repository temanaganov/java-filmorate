package ru.yandex.practicum.filmorate.storage.event;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.event.Event;
import ru.yandex.practicum.filmorate.model.event.EventOperation;
import ru.yandex.practicum.filmorate.model.event.EventType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class DbEventStorage implements EventStorage {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public DbEventStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("event")
                .usingGeneratedKeyColumns("event_id");
    }

    @Override
    public List<Event> getFeed(int userId) {
        String sqlQuery = "SELECT * FROM event WHERE user_id = ?";

        return jdbcTemplate.query(sqlQuery, this::mapRowToEvent, userId);
    }

    @Override
    public Event create(Event event) {
        Map<String, Object> values = new HashMap<>();
        values.put("timestamp", event.getTimestamp());
        values.put("user_id", event.getUserId());
        values.put("event_type", event.getEventType().toString());
        values.put("operation", event.getOperation().toString());
        values.put("entity_id", event.getEntityId());

        event.setEventId(simpleJdbcInsert.executeAndReturnKey(values).intValue());
        return event;
    }

    private Event mapRowToEvent(ResultSet resultSet, int num) throws SQLException {
        Event event = new Event();
        event.setEventId(resultSet.getInt("event_id"));
        event.setTimestamp(resultSet.getLong("timestamp"));
        event.setUserId(resultSet.getInt("user_id"));
        event.setEventType(EventType.valueOf(resultSet.getString("event_type")));
        event.setOperation(EventOperation.valueOf(resultSet.getString("operation")));
        event.setEntityId(resultSet.getInt("entity_id"));

        return event;
    }
}
