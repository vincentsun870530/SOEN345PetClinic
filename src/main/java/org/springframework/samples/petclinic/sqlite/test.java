package org.springframework.samples.petclinic.sqlite;

import java.sql.ResultSet;

public class test {
    public static void main(String[] args) {

        SQLiteResultSet.getVisits(new SQLiteDBConnector().selectAll("visits"));
//        ResultSet rs = new SQLiteDBConnector().selectAll("pets");
//
//        try {
//            while (rs.next()) {
//                //String strDate = dateFormat.format(rs.getDate("birth_date"));
//                System.out.println(rs.getInt("id") + "\t" +
//                        rs.getString("name") + "\t" +
//                        "\t" +
//                        rs.getInt("type_id") + "\t" +
//                        rs.getInt("owner_id"));
//
//
//                System.out.println(rs.getString("birth_date"));
//            }
//        }  catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//        try(r.next()) {
//            System.out.print(r.getString());
//        }

    }
}
