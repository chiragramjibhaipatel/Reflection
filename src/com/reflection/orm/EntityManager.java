package com.reflection.orm;

import com.reflection.domain.Person;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

public interface EntityManager<T> {
    static <T> EntityManager<T> of(Class<T> clss) {
        return new EntityManagerImpl<>();
    }

    void persist(T data) throws SQLException, IllegalAccessException;

    T find(Class<T> t, Object primaryKey) throws SQLException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException;
}
