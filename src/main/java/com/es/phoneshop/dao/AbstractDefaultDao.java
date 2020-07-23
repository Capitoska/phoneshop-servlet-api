package com.es.phoneshop.dao;

import com.es.phoneshop.model.DaoItem;

import java.util.List;
import java.util.NoSuchElementException;

public class AbstractDefaultDao<T extends DaoItem> implements DefaultDao<T> {

    private List<T> items;

    public void init(List<T> items) {
        this.items = items;
    }

    @Override
    public synchronized List<T> getAll() {
        return items;
    }

    @Override
    public synchronized T getById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException();
        }

        return getAll().stream()
                .filter(t -> t.getId().equals(id))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
    }

    @Override
    public synchronized void save(T object) {
        if (object == null) {
            throw new IllegalArgumentException();
        }

        if (getAll().stream().noneMatch(t -> t.getId().equals(object.getId()))) {
            getAll().add(object);
        } else {
            throw new IllegalArgumentException();
        }
    }


    @Override
    public synchronized void saveAll(List<T> objects) {
        objects.forEach(this::save);
    }

    @Override
    public synchronized void deleteById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException();
        }

        if (!getAll().removeIf(t -> t.getId().equals(id))) {
            throw new IllegalArgumentException();
        }
    }
}
