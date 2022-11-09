package ru.yandex.practicum.filmorate.core.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class InMemoryStorage<T> {
    private int id = 0;
    private final Map<Integer, T> storage = new HashMap<>();

    public List<T> getAll() {
        return new ArrayList<>(storage.values());
    }

    public T getById(int id) {
        return storage.get(id);
    }

    public T create(T entity) {
        T newEntity = withId(entity);
        storage.put(getId(newEntity), newEntity);
        return newEntity;
    }

    public T update(T entity) {
        storage.put(getId(entity), entity);
        return entity;
    }

    public T delete(int id) {
        return storage.remove(id);
    }

    protected int getNextId() {
        return ++id;
    }

    protected abstract int getId(T entity);
    protected abstract T withId(T entity);
}
