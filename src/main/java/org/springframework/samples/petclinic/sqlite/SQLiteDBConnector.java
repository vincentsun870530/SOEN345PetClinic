package org.springframework.samples.petclinic.sqlite;

import java.sql.*;
import java.sql.Connection;
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

    //region create table
    public void createOwnerTable() {
       
        // SQL statement
        String sql = "SELECT * FROM owners;";

        executeQuery(sql);
    }

    public void createPetTable() {
       
        // SQL statement
        String sql = "SELECT * FROM pets;";
        executeQuery(sql);
    }

    public void createVetTable() {
        // SQL statement
        String sql = "SELECT * FROM vets;";

        executeQuery(sql);
    }

    public void createVisitTable() {
        // SQL statement
        String sql = "SELECT * FROM visits;";

        executeQuery(sql);
    }

    public void createSpecialityTable() {
        // SQL statement
        String sql = "SELECT * FROM specialties;";

        executeQuery(sql);
    }

    private void executeQuery(String sql) {
        try  {
            Connection conn = connect();
            Statement stmt = conn.createStatement();
            stmt.execute(sql);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void createVetSpecialityTable() {
        // SQL statement
        String sql = "SELECT * FROM vet_specialties;";
        executeQuery(sql);
    }

    public void createTypeTable() {
        // SQL statement
        String sql = "SELECT * FROM types;";
        executeQuery(sql);
    }
    //endregion

    //selectAll
    public ResultSet selectAll(String tableName){
        String sql = "SELECT * FROM " + tableName;
        ResultSet rs = null;
        return getResultSet(sql, rs);
    }

    //selectAll
    public ResultSet selectAll(String tableName, String column1){
        String sql = "SELECT * FROM " + tableName + " ORDER BY " + column1 + " ASC;";
        ResultSet rs = null;
        return getResultSet(sql, rs);
    }


    //selectAll
    public ResultSet selectAllASC(String tableName, String column1){
        String sql = "SELECT * FROM " + tableName + " ORDER BY " + column1 + " ASC;";
        ResultSet rs = null;
        return getResultSet(sql, rs);
    }
    
    //select one item 
    public ResultSet selectById(String tableName, int id){
        String sql = "SELECT * FROM " + tableName + " WHERE id = " + id;
        ResultSet rs = null;
        return getResultSet(sql, rs);
    }

    private ResultSet getResultSet(String sql, ResultSet rs) {
        try  {
            Connection conn = connect();
            Statement stmt = conn.createStatement();

            rs = stmt.executeQuery(sql);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return rs;
    }

    //update one item
    public void updateById(String tableName, String colName, String colValue, int id){
        if(dataSource != null)
        {

            String sql = "UPDATE " + tableName + " SET  " + colName
                    + " = " + colValue + " WHERE id = " + id;
            
            jdbcTemplateObj.update(sql);

            System.out.println("Updated "+tableName+" on "+colName+" with ID = " + id );
            
        }else{
            System.out.println("Update Failed: DateResource is not set");
        }
        
        return;
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