package com.reflection.util;

import com.reflection.annotations.Column;
import com.reflection.annotations.PrimaryKey;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Metamodel {

    private Class clss;

    public static <T> Metamodel of(Class<T> clss) {
        return  new Metamodel(clss);
    }

    private Metamodel(Class clss) {
        this.clss = clss;

    }

    public PrimaryKeyField getPrimaryKey() {
        Field[] declaredFields = clss.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            PrimaryKey primaryKey = declaredField.getAnnotation(PrimaryKey.class);
            if(primaryKey != null){
                PrimaryKeyField primaryKeyField = new PrimaryKeyField(declaredField);
                return primaryKeyField;
            }
        }
        return null;
    }

    public List<ColumnField> getColumns() {
        List<ColumnField> listColumnField = new ArrayList<>();
        Field[] declaredFields = clss.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            Column column = declaredField.getAnnotation(Column.class);
            if(column != null){
                ColumnField columnField = new ColumnField(declaredField);
                listColumnField.add(columnField);
            }
        }
        return listColumnField  ;
    }

    public String buildInsertRequest() {
        //insert into Person (id, name, age) values (?, ?, ?)
        String columns = getColumnString();

        String parameters =
                IntStream.range(0, getColumns().size() + 1)
                        .mapToObj(index -> "?")
                        .collect(Collectors.joining(", "));

        StringBuilder query = new StringBuilder();
        return query.append("insert into ")
                .append(clss.getSimpleName() + " ")
                .append("(")
                .append(columns)
                .append(")")
                .append(" values (")
                .append(parameters)
                .append(")")
                .toString();
    }

    private String getColumnString() {
        String primaryKey = getPrimaryKey().getName();
        List<String> listColumns =
                getColumns().stream().map(columnField -> columnField.getName()).collect(Collectors.toList());
        listColumns.add(0, primaryKey);
        String columns = listColumns.stream().collect(Collectors.joining(", "));
        return columns;
    }

    public String buildSelectQuery(Object primaryKey) {
        //select * from Person where id = ?
        String columns = getColumnString();
        return "select " + columns + " from " + clss.getSimpleName() + " where " + getPrimaryKey().getName() + " = ?";
    }
}
