package org.springframework.samples.petclinic.sqlite;

import java.sql.*;

/**
 *
 * @author sqlitetutorial.net
 */

public class SQLiteHelper {
    public final static String URL = "jdbc:sqlite:src/main/resources/db/sqlite/petclinic.sqlite3";

    public static void getDataFromTable(String data) {
        // SQLite connection string
        String url = URL;

        // SQL statement
        String sql = "SELECT " + data + " FROM owners;";

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            System.out.println(rs.getString("first_name"));

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void selectAllFromTable(String table){
        String sql = "SELECT * FROM " + table;

        try (Connection conn = DriverManager.getConnection(SQLiteHelper.URL);
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            switch(table) {
                case "owners":
                    SQLiteResultSet.getOwners(rs);
                    break;
                case "pets":
                    SQLiteResultSet.getPets(rs);
                    break;
                case "specialties":
                    SQLiteResultSet.getSpecialties(rs);
                    break;
                case "types":
                    SQLiteResultSet.getTypes(rs);
                    break;
                case "vet_specialties":
                    SQLiteResultSet.getVetSpecialties(rs);
                    break;
                case "vets":
                    SQLiteResultSet.getVets(rs);
                    break;
                case "visits":
                    SQLiteResultSet.getVisits(rs);
                    break;
                default:
                    break;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        selectAllFromTable("owners");
        selectAllFromTable("pets");
        selectAllFromTable("specialties");
        selectAllFromTable("types");
        selectAllFromTable("vet_specialties");
        selectAllFromTable("vets");
        selectAllFromTable("visits");
    }
}