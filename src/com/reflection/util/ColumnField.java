package com.reflection.util;

import com.reflection.annotations.Column;

import java.lang.reflect.Field;

public class ColumnField {

    private final Field field;
    private final String name;

    public ColumnField(Field field) {
        this.field = field;
        this.name = field.getAnnotation(Column.class).name();
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
