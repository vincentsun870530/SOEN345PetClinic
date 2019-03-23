package org.springframework.samples.petclinic.sqlite;

import java.sql.*;
import java.util.List;
import org.springframework.samples.petclinic.owner.*;

/**
 *
 * @author sqlitetutorial.net
 */

public class SQLiteDBConnector {
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

    //region create table
    public static void createOwnerTable() {
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

    public static void createPetTable() {
        // SQLite connection string
        String url = URL;

        // SQL statement
        String sql = "SELECT * FROM pets;";

        try (Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void createVetTable() {
        // SQLite connection string
        String url = URL;

        // SQL statement
        String sql = "SELECT * FROM vets;";

        try (Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void createVisitTable() {
        // SQLite connection string
        String url = URL;

        // SQL statement
        String sql = "SELECT * FROM visits;";

        try (Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void createSpecialityTable() {
        // SQLite connection string
        String url = URL;

        // SQL statement
        String sql = "SELECT * FROM specialties;";

        try (Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void createVetSpecialityTable() {
        // SQLite connection string
        String url = URL;

        // SQL statement
        String sql = "SELECT * FROM vet_specialties;";

        try (Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void createTypeTable() {
        // SQLite connection string
        String url = URL;

        // SQL statement
        String sql = "SELECT * FROM types;";

        try (Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    //endregion

    //selectAll
    public static ResultSet selectAll(String tableName){
        String sql = "SELECT * FROM " + tableName;
        ResultSet rs = null;
        try (Connection conn = DriverManager.getConnection(URL);
        Statement stmt  = conn.createStatement()){
            
            rs = stmt.executeQuery(sql);
           
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return rs;
    }
    //select one item 
    public static ResultSet selectById(String tableName, int id){
        String sql = "SELECT * FROM " + tableName + " WHERE id = " + id;
        ResultSet rs = null;
        try (Connection conn = DriverManager.getConnection(URL);
        Statement stmt  = conn.createStatement()){
            
            rs = stmt.executeQuery(sql);
           
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return rs;
    }

/**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        selectAll("owners");
    }


}