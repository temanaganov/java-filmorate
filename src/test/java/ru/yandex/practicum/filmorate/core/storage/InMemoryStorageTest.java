package ru.yandex.practicum.filmorate.core.storage;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class InMemoryStorageTest<T> {
    protected InMemoryStorage<T> storage;

    @Test
    void getAll_shouldReturnEmptyList_ifStorageIsEmpty() {
        assertEquals(Collections.emptyList(), storage.getAll());
    }

    @Test
    void getAll_shouldReturnTwoEntities() {
        T entity1 = getEntity(1);
        T entity2 = getEntity(2);
        storage.create(entity1);
        storage.create(entity2);

        assertEquals(List.of(entity1, entity2), storage.getAll());
    }

    @Test
    void getById_shouldReturnEntity() {
        int id = 1;
        T entity1 = getEntity(id);
        storage.create(entity1);

        assertEquals(entity1, storage.getById(id));
    }

    @Test
    void getById_shouldReturnNull_ifStorageHasNoEntityWithGivenId() {
        assertNull(storage.getById(1));
    }

    @Test
    void create_shouldCreateNewEntityAndReturnItWithNewId() {
        T entity = getEntity(0);

        assertEquals(getEntity(1), storage.create(entity));
        assertEquals(1, storage.getAll().size());
    }

    @Test
    void create_shouldCreateTwoNewEntitiesAndReturnItWithNewId() {
        T entity = getEntity(0);

        assertEquals(getEntity(1), storage.create(entity));
        assertEquals(getEntity(2), storage.create(entity));
        assertEquals(2, storage.getAll().size());
    }

    @Test
    void update_shouldUpdateEntityAndReturnUpdated() {
        int id = 1;
        T entity = getEntity(id);
        T updatedEntity = getEntityForUpdate(id);
        storage.create(entity);

        assertEquals(entity, storage.getById(id));
        assertEquals(updatedEntity, storage.update(id, updatedEntity));
    }

    @Test
    void update_shouldReturnNull_ifStorageHasNoEntityWithGivenId() {
        assertNull(storage.update(1, getEntity(1)));
    }

    @Test
    void delete_shouldDeleteEntityAndReturnIt() {
        int id = 1;
        T entity = getEntity(id);

        storage.create(entity);

        assertEquals(1, storage.getAll().size());
        assertEquals(entity, storage.delete(id));
        assertEquals(0, storage.getAll().size());
    }

    @Test
    void delete_shouldReturnNull_ifStorageHasNoEntityWithGivenId() {
        assertNull(storage.delete(1));
    }

    protected abstract T getEntity(int id);
    protected abstract T getEntityForUpdate(int id);
}
