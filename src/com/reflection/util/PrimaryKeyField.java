package com.reflection.util;

import com.reflection.annotations.PrimaryKey;

import java.lang.reflect.Field;

public class PrimaryKeyField {

    private final Field field;
    private final String name;

    public PrimaryKeyField(Field field) {
        this.field = field;
        this.name = field.getAnnotation(PrimaryKey.class).name();
    }

    public String getName() {
        return name;
    }

    public Class<?> getType() {
        return field.getType();
    }

    public Field getField() {
        return field;
    }
}
