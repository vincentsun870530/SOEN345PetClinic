package org.springframework.samples.petclinic.sqlite;

import org.springframework.samples.petclinic.visit.Visit;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SQLiteVisitHelper {
    private static final String INSERT_SQL = "INSERT INTO visits(pet_id, visit_date, description) VALUES(?, ?, ?)";
    private Connection connection = (SQLiteDBConnector.getInstance()).connect(); // GET CONNECTION.

    private static SQLiteVisitHelper sqLiteVisitHelper = null;

    private SQLiteVisitHelper()
    {
        // for singleton
    }

    public static SQLiteVisitHelper getInstance()
    {
        if (sqLiteVisitHelper == null)
            sqLiteVisitHelper = new SQLiteVisitHelper();
        return sqLiteVisitHelper;
    }


    public int insert(int pet_id, String visit_date, String description) {
        int numRowsInserted = 0;
        PreparedStatement ps = null;
        try {
            ps = this.connection.prepareStatement(INSERT_SQL);
            ps.setInt(1, pet_id);
            ps.setString(2,visit_date);
            ps.setString(3, description);
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
        SQLiteDBConnector.getInstance().updateById("visits",colName,colValue,id);
    }

    //delete


    //Retrieve from ResultSet
    public List<Visit> getModelList(ResultSet rs){
        ArrayList<Visit> visitList = new ArrayList<Visit>();
        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try{
            while(rs.next()){
                Visit visit = new Visit();
                System.out.println(rs.getInt("id"));
                visit.setId(rs.getInt("id"));
                System.out.println(rs.getString("pet_id"));
                visit.setPetId(Integer.parseInt(rs.getString("pet_id")));
                System.out.println(LocalDate.parse(rs.getString("visit_date")));
                visit.setDate(LocalDate.parse(rs.getString("visit_date")));
                System.out.println(rs.getString("description"));
                visit.setDescription(rs.getString("description"));
                visitList.add(visit);
            }
        }catch(SQLException e){
            e.printStackTrace();
            System.out.println(e.getMessage() + " Retrieve Error From Visit Helper");
        }
        return visitList;
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
