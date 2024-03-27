package org.example.jdbc;

import java.sql.*;

public class App {
    public static void main(String[] args) {
        try(Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/student", "root", "12345678")) {
            acceptConnection(connection);
            insertData(connection);
            insertData(connection);
            insertData(connection);
            distinctData(connection);

            try (Statement statement = connection.createStatement()) {
                ResultSet resultSet = statement.executeQuery("SELECT id, firstname FROM `student`.`students`");
                while (resultSet.next()){
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("firstname");
                    System.out.println(id+ " " + name);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } ;

    }

    private static void acceptConnection(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("""
                    create table if not exists Students(
                    id bigint,
                    firstname varchar(256),
                    secondname varchar(256))
                    """);
        }
    }
    private static void insertData(Connection connection) throws SQLException {
        try(Statement statement = connection.createStatement()) {
            statement.execute("""
                    INSERT INTO Students (id, firstname, secondname) VALUES
                    (1, 'Иван', 'Иванов'),
                    (2, 'Петр', 'Петров'),
                    (3, 'Сергей', 'Сергеев'),
                    (4, 'Алексей', 'Алексеев'),
                    (5, 'Дмитрий', 'Дмитриев'),
                    (6, 'Андрей', 'Андреев'),
                    (7, 'Александр', 'Александров'),
                    (8, 'Николай', 'Николаев'),
                    (9, 'Виктор', 'Викторов'),
                    (10, 'Михаил', 'Михайлов');                
                    """);
        }
    }

//    Почему -то  MySQL не работает с многострочной командой с несколькоми командами
//    пришлось CREATE DROP RENAME по отдельности писать.
    private static void distinctData(Connection connection) throws SQLException {
        // Включаем управление транзакцией
        connection.setAutoCommit(false);

        try {
            // Создание временной таблицы с уникальными данными из Students
            try (PreparedStatement createTemp = connection.prepareStatement("CREATE TABLE `temp_table` AS SELECT DISTINCT * FROM `Students`")) {
                createTemp.executeUpdate();
            }

            // Удаление оригинальной таблицы Students
            try (PreparedStatement dropOriginal = connection.prepareStatement("DROP TABLE `Students`")) {
                dropOriginal.executeUpdate();
            }

            // Переименование temp_table в Students
            try (PreparedStatement renameTable = connection.prepareStatement("RENAME TABLE `temp_table` TO `Students`")) {
                renameTable.executeUpdate();
            }

            // Подтверждаем транзакцию
            connection.commit();
        } catch (SQLException e) {
            // В случае ошибки откатываем изменения
            connection.rollback();
            throw e; // Перебрасываем исключение дальше

        }
    }
}
