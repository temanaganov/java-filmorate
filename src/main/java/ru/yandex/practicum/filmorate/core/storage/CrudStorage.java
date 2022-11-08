package ru.yandex.practicum.filmorate.core.storage;

import java.util.List;

public interface CrudStorage<K> {
    List<K> getAll();
    K getById(int id);
    K create(K film);
    K update(K film);
    K delete(int id);
}
