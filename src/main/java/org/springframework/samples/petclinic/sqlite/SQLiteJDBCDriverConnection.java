package org.springframework.samples.petclinic.sqlite;

import java.sql.*;

/**
 *
 * @author sqlitetutorial.net
 */

public class SQLiteJDBCDriverConnection {
    public final static String URL = "jdbc:sqlite:src/main/resources/db/sqlite/petclinic.sqlite3";
    /**
     * Connect to a sample database
     */
    private Connection connect() {
        // SQLite connection string
        String url = URL;
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public static void createNewTable() {
        // SQLite connection string
        String url = URL;

        // SQL statement
        String sql = "SELECT * FROM owners;";

        try (Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void selectAll(){
        String sql = "SELECT * FROM owners";

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // loop through the result set
            while (rs.next()) {
                System.out.println(rs.getInt("id") +  "\t" +
                        rs.getString("first_name") + "\t" +
                        rs.getString("last_name") + "\t" +
                        rs.getString("address") + "\t" +
                        rs.getString("city") + "\t" +
                        rs.getDouble("telephone"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        selectAll();
    }
}