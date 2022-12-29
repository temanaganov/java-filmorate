package ru.yandex.practicum.filmorate.model.event;

import lombok.Builder;
import lombok.Value;
import lombok.With;

@With
@Value
@Builder
public class Event {
    int eventId;
    long timestamp;
    int userId;
    EventType eventType;
    EventOperation operation;
    int entityId;
}
