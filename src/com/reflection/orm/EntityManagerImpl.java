package com.reflection.orm;

import com.reflection.util.ColumnField;
import com.reflection.util.Metamodel;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class EntityManagerImpl<T> implements EntityManager<T> {

    private AtomicLong idGenerator = new AtomicLong(0L);

    @Override
    public void persist(T t) throws SQLException, IllegalAccessException {

        Metamodel metamodel = Metamodel.of(t.getClass());
        String sql = metamodel.buildInsertRequest();
        try (PreparedStatement statement = prepareStatementWith(sql).andParameter(t)) {
            statement.executeUpdate();
        }
    }

    @Override
    public T find(Class<T> clss, Object primaryKey) throws SQLException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException {
        Metamodel metamodel = Metamodel.of(clss);
        String sql = metamodel.buildSelectQuery(primaryKey);
        ResultSet resultSet;
        try (PreparedStatement statement = prepareStatementWith(sql).andPrimaryKey(primaryKey)) {
            resultSet = statement.executeQuery();
            return buildClassInstance(clss, resultSet);
        }
    }

    private T buildClassInstance(Class<T> clss, ResultSet resultSet) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, SQLException {
        T t = clss.getConstructor().newInstance();
        Metamodel metamodel = Metamodel.of(clss);
        Field primaryKeyField = metamodel.getPrimaryKey().getField();
        primaryKeyField.setAccessible(true);
        String primaryKetName = metamodel.getPrimaryKey().getName();
        resultSet.next();
        if(primaryKeyField.getType() == long.class){
            long primaryKeyValue = resultSet.getInt(primaryKetName);
            primaryKeyField.setLong(t, primaryKeyValue);
        }
        List<ColumnField> columns = metamodel.getColumns();
        for (int i = 0; i < columns.size(); i++) {
            String name = columns.get(i).getName();
            Field field = columns.get(i).getField();
            field.setAccessible(true);
            if(field.getType() == String.class){
                String string = resultSet.getString(name);
                field.set(t, string);
            } else if(field.getType() == int.class){
                int anInt = resultSet.getInt(name);
                field.set(t, anInt);
            }
        }
        return t;
    }

    private PreparedStatementWrapper prepareStatementWith(String sql) throws SQLException {

        Connection connection =
                DriverManager.getConnection("jdbc:h2:/Users/chiragpatel/Code/Training/db/db_person", "sa", "");
        PreparedStatement statement = connection.prepareStatement(sql);
        return new PreparedStatementWrapper(statement);
    }

    private class PreparedStatementWrapper {
        private PreparedStatement statement;

        public PreparedStatementWrapper(PreparedStatement statement) {

            this.statement = statement;
        }

        public PreparedStatement andParameter(T t) throws SQLException, IllegalAccessException {

            Metamodel clss = Metamodel.of(t.getClass());
            Class<?> primaryKeyType = clss.getPrimaryKey().getType();
            if(primaryKeyType == long.class){
                long id = idGenerator.incrementAndGet();
                statement.setLong(1, id);
                Field field = clss.getPrimaryKey().getField();
                field.setAccessible(true);
                field.set(t, (long)id);
            }
            List<ColumnField> columns = clss.getColumns();
            for (int i = 0; i < columns.size(); i++) {
                Class<?> columnType = columns.get(i).getType();
                Field field = columns.get(i).getField();
                field.setAccessible(true);
                Object o = field.get(t);
                if(columnType == String.class){
                    statement.setString(i+2, (String) o);
                } else if(columnType == int.class){
                    statement.setInt(i+2, (int)o);
                }
            }
            return statement;
        }

        public PreparedStatement andParameter(long l) throws SQLException {
            statement.setLong(1, l);
            return statement;
        }

        public PreparedStatement andPrimaryKey(Object primaryKey) throws SQLException {
            if(primaryKey.getClass() == Long.class){
                statement.setLong(1, (Long) primaryKey);
            }
            System.out.println(statement);
            return statement;
        }
    }
}
