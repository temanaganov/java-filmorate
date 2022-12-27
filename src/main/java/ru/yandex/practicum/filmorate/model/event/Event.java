package ru.yandex.practicum.filmorate.model.event;

import lombok.Data;

@Data
public class Event {
    private int eventId;
    private long timestamp;
    private int userId;
    private EventType eventType;
    private EventOperation operation;
    private int entityId;
}
