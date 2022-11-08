package ru.yandex.practicum.filmorate.core.storage;

public abstract class InMemoryStorage {
    protected int id = 0;

    protected int getNextId() {
        return ++id;
    }
}
