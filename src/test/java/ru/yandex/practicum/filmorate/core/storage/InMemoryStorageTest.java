package ru.yandex.practicum.filmorate.core.storage;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.storage.InMemoryStorage;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

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

        assertThat(storage.getAll()).isEqualTo(List.of(entity1, entity2));
    }

    @Test
    void getById_shouldReturnEntity() {
        int id = 1;
        T entity1 = getEntity(id);
        storage.create(entity1);

        assertThat(storage.getById(id)).isEqualTo(entity1);
    }

    @Test
    void getById_shouldReturnNull_ifStorageHasNoEntityWithGivenId() {
        assertNull(storage.getById(1));
    }

    @Test
    void create_shouldCreateNewEntityAndReturnItWithNewId() {
        T entity = getEntity(0);

        assertAll(() -> {
            assertThat(storage.create(entity)).isEqualTo(getEntity(1));
            assertThat(storage.getAll().size()).isEqualTo(1);
        });
    }

    @Test
    void create_shouldCreateTwoNewEntitiesAndReturnItWithNewId() {
        T entity = getEntity(0);

        assertAll(() -> {
            assertThat(storage.create(entity)).isEqualTo(getEntity(1));
            assertThat(storage.create(entity)).isEqualTo(getEntity(2));
            assertThat(storage.getAll().size()).isEqualTo(2);
        });
    }

    @Test
    void update_shouldUpdateEntityAndReturnUpdated() {
        int id = 1;
        T entity = getEntity(id);
        T updatedEntity = getEntityForUpdate(id);
        storage.create(entity);

        assertAll(() -> {
            assertThat(storage.getById(id)).isEqualTo(entity);
            assertThat(storage.update(id, updatedEntity)).isEqualTo(updatedEntity);
        });
    }

    @Test
    void update_shouldReturnNull_ifStorageHasNoEntityWithGivenId() {
        assertThat(storage.update(1, getEntity(1))).isNull();
    }

    @Test
    void delete_shouldDeleteEntityAndReturnIt() {
        int id = 1;
        T entity = getEntity(id);

        storage.create(entity);

        assertAll(() -> {
            assertThat(storage.getAll().size()).isEqualTo(1);
            storage.delete(id);
            assertThat(storage.getAll().size()).isEqualTo(0);
        });
    }

    protected abstract T getEntity(int id);

    protected abstract T getEntityForUpdate(int id);
}
