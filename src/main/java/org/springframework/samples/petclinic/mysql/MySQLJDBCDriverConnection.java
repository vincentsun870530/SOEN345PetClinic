package org.springframework.samples.petclinic.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQLJDBCDriverConnection {

    public Connection connect() {
        String url = "jdbc:mysql://127.0.0.1:3306/petclinic";
        String user = "root";
        String password = "petclinic";
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connect to MySQL");
        } catch(SQLException exception) {
            System.out.println("MySQLJDBCDriverConnection/connect:"+exception);
        };
        return connection;
    }

    public ResultSet selectAll(String tableName) {
        String query = "SELECT * FROM " + tableName;
        ResultSet resultSet = null;
        try {
            Connection connection = connect();
            Statement statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            // FOR TESTING PURPOSE, TO BE REMOVED
            int count = 1;
            while(resultSet.next()) {
                System.out.println(count + "." + resultSet.getString("name"));
            }
            //////////////////////////////////////

        } catch (SQLException exception) {
            System.out.println("MySQLJDBCDriverConnection/selectAll:"+exception);
        }
        return resultSet;
    }
}