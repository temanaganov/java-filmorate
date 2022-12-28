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
    public void createEvent(int userId, EventType eventType, EventOperation eventOperation, int entityId){
        Event event = Event.builder()
                .timestamp(Instant.now().toEpochMilli())
                .userId(userId)
                .eventType(eventType)
                .operation(eventOperation)
                .entityId(entityId)
                .build();

         eventStorage.create(event);
    }
}

