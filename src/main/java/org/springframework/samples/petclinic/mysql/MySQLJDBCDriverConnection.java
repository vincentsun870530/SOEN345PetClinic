package org.springframework.samples.petclinic.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQLJDBCDriverConnection {

    public static Connection connect() {
        String url = "jdbc:mysql://127.0.0.1:3306/petclinic";
        String user = "root";
        String password = "root";
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connect to MySQL");
        } catch(SQLException exception) {
            System.out.println("MySQLJDBCDriverConnection/connect:"+exception);
        };
        return connection;
    }

    // Return all data
    public static ResultSet selectAll(String tableName) {
        String query = "SELECT * FROM " + tableName;
        ResultSet resultSet = null;
        return getResultSet(query, resultSet);
    }

    // Return all data ASC
    public static ResultSet selectAll(String tableName, String column1) {
        String query = "SELECT * FROM " + tableName + " ORDER BY " + column1 + " ASC;";
        ResultSet resultSet = null;
        return getResultSet(query, resultSet);
    }

    private static ResultSet getResultSet(String query, ResultSet resultSet) {
        try {
            Connection connection = connect();
            Statement statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            // FOR TESTING PURPOSE, TO BE REMOVED
            // int count = 1;
            // while(resultSet.next()) {
            //     System.out.println(count + "." + resultSet.getString("name"));
            // }
            //////////////////////////////////////

        } catch (SQLException exception) {
            System.out.println("MySQLJDBCDriverConnection/selectAll:"+exception);
        }
        return resultSet;
    }

    //select one item 
    public static ResultSet selectById(String tableName, int id){
        if(tableName.equals("vet_specialties")) {
            return getVetSpecialitiesResultSet(tableName, id, connect());
        } else {
            System.out.println(tableName+"!!!!!!!!!!!!!!!!!!!");
            return getResultSet(tableName, id, connect());
        }
    }

    public static ResultSet getResultSet(String tableName, int id, Connection connect) {
        String query = "SELECT * FROM " + tableName + " WHERE id = " + id;
        ResultSet resultSet = null;
        try  {
            Connection connection = connect;
            Statement stmt = connection.createStatement();

            resultSet = stmt.executeQuery(query);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return resultSet;
    }

    public static ResultSet getVetSpecialitiesResultSet(String tableName, int id, Connection connect) {
        String query = "SELECT * FROM " + tableName + " WHERE vet_id = " + id;
        ResultSet resultSet = null;
        try  {
            Connection connection = connect;
            Statement stmt = connection.createStatement();

            resultSet = stmt.executeQuery(query);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return resultSet;
    }

    // public static void updateRow(int id, String tableName, String columnName, String data) {
    //     String query = "UPDATE " + tableName + " SET " + columnName + "=" + data + " WHERE id=" + id;
    //         try {
    //             Connection connection = connect();
    //             PreparedStatement pStatement = connection.prepareStatement(query);
    //             pStatement.executeUpdate();
    //         } catch (SQLException exception) {
    //             System.out.println("MySQLJDBCDriverConnection/updateRowFor" + tableName + ":" + exception);
    //         }
    // }
}