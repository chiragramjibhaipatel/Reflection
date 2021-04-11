package com.reflection;

import com.reflection.domain.Person;
import com.reflection.orm.EntityManager;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

public class ReadingObject {
    public static void main(String[] args) throws SQLException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException {
        EntityManager<Person> entityManager = EntityManager.of(Person.class);

        Person chirag = entityManager.find(Person.class, 27L);
        System.out.println(chirag);
    }
}
