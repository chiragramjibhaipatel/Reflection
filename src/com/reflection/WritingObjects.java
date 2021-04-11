package com.reflection;

import com.reflection.domain.Person;
import com.reflection.orm.EntityManager;

import java.sql.SQLException;

public class WritingObjects {

    public static void main(String[] args) throws SQLException, IllegalAccessException {

        EntityManager entityManager = EntityManager.of(Person.class);

        Person chirag = new Person("Chirag", 31);
        Person ankita = new Person("Ankita", 31);
        Person nitya = new Person("Nitya", 31);
        Person ayansh = new Person("Ayansh", 31);

        System.out.println(chirag);
        System.out.println(ankita);
        System.out.println(nitya);
        System.out.println(ayansh);

        entityManager.persist(chirag);
        entityManager.persist(ankita);
        entityManager.persist(nitya);
        entityManager.persist(ayansh);

        System.out.println(chirag);
        System.out.println(ankita);
        System.out.println(nitya);
        System.out.println(ayansh);
    }
}
