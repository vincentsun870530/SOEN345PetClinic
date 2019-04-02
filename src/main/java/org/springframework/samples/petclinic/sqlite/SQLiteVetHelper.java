package org.springframework.samples.petclinic.sqlite;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.samples.petclinic.vet.Specialty;
import org.springframework.samples.petclinic.vet.Vet;

public class SQLiteVetHelper {

    private static final String INSERT_SQL = "INSERT INTO vets(first_name, last_name) VALUES( ?, ?)";
    private Connection connection = (SQLiteDBConnector.getInstance()).connect(); // GET CONNECTION.

    private static SQLiteVetHelper sqLiteVetHelper = null;

    private SQLiteVetHelper()
    {
        // for singleton
    }

    public static SQLiteVetHelper getInstance()
    {
        if (sqLiteVetHelper == null)
            sqLiteVetHelper = new SQLiteVetHelper();
        return sqLiteVetHelper;
    }


    public int insert(String first_name, String last_name) {
        int numRowsInserted = 0;
        PreparedStatement ps = null;
        try {
            ps = this.connection.prepareStatement(INSERT_SQL);
            ps.setString(1, first_name);
            ps.setString(2,last_name);
            numRowsInserted = ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(ps);
        }
        return numRowsInserted;
    }

    //update
    public void updateColById(String colName, String colValue, int id)
    {
        SQLiteDBConnector.getInstance().updateById("vets",colName,colValue,id);
    }

    //delete


    //Retrieve from ResultSet
    public List<Vet> getModelList(ResultSet rs){
        ArrayList<Vet> vetList = new ArrayList<Vet>();

        try{
            while(rs.next()){
                Vet vet = new Vet();
                System.out.println(rs.getInt("id"));
                vet.setId(rs.getInt("id"));

                vet.setFirstName(rs.getString("first_name"));
                vet.setFirstName(rs.getString("first_name"));

                System.out.println(rs.getString("last_name"));
                vet.setLastName(rs.getString("last_name"));
                vetList.add(vet);
            }
        }catch(SQLException e){
            e.printStackTrace();
            System.out.println(e.getMessage() + " Retrieve Error From Vet Helper");
        }
        return vetList;
    }

    public List<Specialty> getSpecialtyModelList(ResultSet rs){
        ArrayList<Specialty> specialtyList = new ArrayList<Specialty>();

        try{
            while(rs.next()){
                Specialty specialty = new Specialty();
                System.out.println(rs.getInt("id"));
                specialty.setId(rs.getInt("id"));

                System.out.println(rs.getString("name"));

                specialty.setName(rs.getString("name"));

                specialtyList.add(specialty);
            }
        }catch(SQLException e){
            e.printStackTrace();
            System.out.println(e.getMessage() + " Retrieve Error From Vet Helper");
        }
        return specialtyList;
    }



    private void close(Statement statement) {
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
