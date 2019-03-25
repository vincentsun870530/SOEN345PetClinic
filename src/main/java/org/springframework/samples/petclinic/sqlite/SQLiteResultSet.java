package org.springframework.samples.petclinic.sqlite;

import java.sql.*;

public class SQLiteResultSet {
    public static void getOwners(ResultSet rs) {
        try {
            while (rs.next()) {
                System.out.println(rs.getInt("id") + "\t" +
                        rs.getString("first_name") + "\t" +
                        rs.getString("last_name") + "\t" +
                        rs.getString("address") + "\t" +
                        rs.getString("city") + "\t" +
                        rs.getString("telephone"));
            }
        }  catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void getPets(ResultSet rs) {
        try {
            while (rs.next()) {
                System.out.println(rs.getInt("id") + "\t" +
                        rs.getString("name") + "\t" +
                        rs.getString("birth_date") + "\t" +
                        rs.getInt("type_id") + "\t" +
                        rs.getInt("owner_id"));
            }
        }  catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void getSpecialties(ResultSet rs) {
        getTypes(rs);
    }

    public static void getTypes(ResultSet rs) {
        try {
            while (rs.next()) {
                System.out.println(rs.getInt("id") + "\t" +
                        rs.getString("name"));
            }
        }  catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void getVetSpecialties(ResultSet rs) {
        try {
            while (rs.next()) {
                System.out.println(rs.getInt("vet_id") + "\t" +
                        rs.getInt("specialty_id"));
            }
        }  catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void getVets(ResultSet rs) {
        try {
            while (rs.next()) {
                System.out.println(rs.getInt("id") + "\t" +
                        rs.getString("first_name") + "\t" +
                        rs.getString("last_name"));
            }
        }  catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void getVisits(ResultSet rs) {
        try {
            while (rs.next()) {
                System.out.println(rs.getInt("id") + "\t" +
                        rs.getInt("pet_id") + "\t" +
                        rs.getString("visit_date") + "\t" +
                        rs.getString("description"));
            }
        }  catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }




}
