package com.reflection.domain;

import com.reflection.annotations.Column;
import com.reflection.annotations.PrimaryKey;

public class Person {
    @PrimaryKey(name = "id")
    private long id1;
    @Column(name="name")
    private String name1;
    @Column(name = "age")
    private int age1;

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id1 +
                ", name='" + name1 + '\'' +
                ", age=" + age1 +
                '}';
    }

    public Person() {
    }

    public Person(String name1, int age1) {
        this.name1 = name1;
        this.age1 = age1;
    }
}
