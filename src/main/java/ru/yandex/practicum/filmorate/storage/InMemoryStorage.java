package ru.yandex.practicum.filmorate.storage;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class InMemoryStorage<T> {
    private int id = 0;
    private final Map<Integer, T> storage = new LinkedHashMap<>();

    public List<T> getAll() {
        return new ArrayList<>(storage.values());
    }

    public T getById(int id) {
        return storage.get(id);
    }

    public T create(T entity) {
        int id = getNextId();
        T newEntity = withId(entity, id);
        storage.put(id, newEntity);
        return newEntity;
    }

    public T update(int id, T entity) {
        if (!storage.containsKey(id)) {
            return null;
        }

        storage.put(id, entity);
        return entity;
    }

    public void delete(int id) {
        storage.remove(id);
    }

    protected int getNextId() {
        return ++id;
    }

    protected abstract T withId(T entity, int id);
}
