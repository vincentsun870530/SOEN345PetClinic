package org.springframework.samples.petclinic.sqlite;

import java.sql.*;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.samples.petclinic.owner.*;

/**
 *
 * @author sqlitetutorial.net
 */

public class SQLiteDBConnector {
    public final static String URL = "jdbc:sqlite:src/main/resources/db/sqlite/petclinic.sqlite3";
    private DataSource dataSource = null;
    private JdbcTemplate jdbcTemplateObj=null;

    private static SQLiteDBConnector sqLiteDBConnector = null;
    //private ResultSet resultSet;

    private SQLiteDBConnector()
    {
       // for singleton
    }

    public static SQLiteDBConnector getInstance()
    {
        if (sqLiteDBConnector == null)
            sqLiteDBConnector = new SQLiteDBConnector();

        return sqLiteDBConnector;
    }

    /**
     * Connect to a sample database
     */
    public Connection connect() {
        // SQLite connection string
        String url = URL;
        String user = "sa";
        String password = "sa";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connect to SQLite");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public void setDataSource(DataSource dataSource){
        this.dataSource = dataSource;
        this.jdbcTemplateObj = new JdbcTemplate(dataSource);
    }

    //selectAll
    public ResultSet selectAll(String tableName){
        String sql = "SELECT * FROM " + tableName;
        ResultSet rs = null;
        Connection conn = connect();
        try  {
            if(conn.isClosed())
            System.out.println("conn closed: " + sql);
            Statement stmt = conn.createStatement();
            
            rs = stmt.executeQuery(sql);

           //resultSet = rs;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return rs;
    }
    
    //select one item 
    public ResultSet selectById(String tableName, int id){
        String sql = "SELECT * FROM " + tableName + " WHERE id = " + id;
        ResultSet rs = null;
        Connection conn = connect();
        try  {
            if(conn.isClosed())
                System.out.println("conn closed: " + sql);
            Statement stmt = conn.createStatement();
            
            rs = stmt.executeQuery(sql);

            //resultSet = rs;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return rs;
    }

    //update one item
    public void updateById(String tableName, String colName, String colValue, int id){
        //String sql = "UPDATE " + tableName + " SET " + colName + " = \'" + colValue + "\' WHERE id = " + id;
        Connection conn = connect();
        String sql = "UPDATE " + tableName + " SET " + colName + " = ? WHERE id = ?";
        PreparedStatement  stmt =null;

        try  {
            if(conn.isClosed())
                System.out.println("conn closed: " + sql);
            stmt = conn.prepareStatement(sql);
            //stmt.setString(1,tableName);
            //stmt.setString(1,colName);
            stmt.setString(1,colValue);
            stmt.setInt(2,id);
            stmt.executeUpdate();
            //conn.close();
            System.out.println("Updated "+tableName+" on "+colName+" with ID = " + id );

        } catch (SQLException e) {
            System.out.println(" Update Failed: " + e.getMessage());
        }
        
        //return;
    }

    public void insertData(String tableName, String dataArray[]) {
        String query = "";
        switch(tableName) {
            case "owners": 
                query = "INSERT into owners (first_name, last_name, address, city, telephone) VALUES (?, ?, ?, ?, ?)";
                jdbcTemplateObj.update(query, dataArray[0], dataArray[1], dataArray[2], dataArray[3], dataArray[4]);
                break;
            case "pets": 
                query = "INSERT into pets (name, birth_date, type_id, owner_id) VALUES (?, ?, ?, ?)";
                jdbcTemplateObj.update(query, dataArray[0], dataArray[1], dataArray[2], dataArray[3]);
                break;
            case "vets": 
                query = "INSERT into vets (first_name, last_name) VALUES (?, ?)";
                jdbcTemplateObj.update(query, dataArray[0], dataArray[1]);
                break;
            case "visits": 
                query = "INSERT into visits (pet_id, visit_date, description) VALUES (?, ?, ?)";
                jdbcTemplateObj.update(query, dataArray[0], dataArray[1], dataArray[2]);
                break;
        }
    }

}