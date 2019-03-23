package org.springframework.samples.petclinic.sqlite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteDBConnection {

    public Connection connect() {
        // SQLite connection string
        String dbFilePath = System.getProperty("user.dir") + "/src/main/resources/db/sqlite/sqlite-data.db";
        String sqliteUrl = "jdbc:sqlite:"+dbFilePath;
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(sqliteUrl);
         System.out.println("Connected to database");
        } catch (SQLException error) {
            System.out.println(error.getMessage());
        }
        return connection;
    }
}