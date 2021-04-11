package com.reflection.domain;

import com.reflection.annotations.Column;
import com.reflection.annotations.PrimaryKey;

public class Person {
    @PrimaryKey
private long id;
    @Column
    private String name;
    @Column
    private int age;

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    public Person() {
    }

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
}
