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
        String sql = "UPDATE " + tableName + " SET " + colName
                + " = \'" + colValue + "\' WHERE id = " + id;
        Statement stmt =null;
        try  {

            Connection conn = connect();
            stmt = conn.createStatement();

            stmt.executeUpdate(sql);

            System.out.println("Updated "+tableName+" on "+colName+" with ID = " + id );

        } catch (SQLException e) {
            System.out.println(" Update Failed: " + e.getMessage());
        }
        
        return;
    }

    //Update the whole row
    public void updateRow(String dataArray[]) {
        try {
            String query = "";
            Statement stmt = null;
            Connection connection = connect();
            stmt = connection.createStatement();
            /**
             *  Switch statement for different tables
             *  dataArray[0] is for the table name
             *  dataArray[1] is for the id
             *  the rest of dataArray is the the value of the columns
             * */
            switch(dataArray[0]) {
                case "owners": 
                    // query = "UPDATE owners SET first_name = ?, last_name = ?, address = ?, city = ?, telephone = ? WHERE id = ?";
                    // jdbcTemplateObj.update(query, dataArray[2], dataArray[3], dataArray[4], dataArray[5], dataArray[6], dataArray[1]);
                    query = "UPDATE owners SET first_name = \'" + dataArray[2] + "\', last_name = \'" + dataArray[3] + "\', address = \'" + dataArray[4] + 
                            "\', city = \'" + dataArray[5] + "\', telephone = \'" + dataArray[6] + "\' WHERE id = " + dataArray[1];
                    break;
                case "pets": 
                    // query = "UPDATE pets SET name = ?, birth_date = ?, type_id = ?, owner_id = ?) WHERE id = ?";
                    // jdbcTemplateObj.update(query, dataArray[2], dataArray[3], dataArray[4], dataArray[5], dataArray[1]);
                    query = "UPDATE pets SET name = \'" + dataArray[2] + "\', birth_date = \'" + dataArray[3] + "\', type_id = \'" + dataArray[4] + 
                    "\', owner_id = \'" + dataArray[5] + "\' WHERE id = " + dataArray[1];
                    break;
                case "vets": 
                    // query = "UPDATE vets SET first_name = ?, last_name = ? WHERE id = ?";
                    // jdbcTemplateObj.update(query, dataArray[2], dataArray[3], dataArray[1]);
                    query = "UPDATE vets SET first_name = \'" + dataArray[2] + "\', last_name = \'" + dataArray[3] + "\' WHERE id = " + dataArray[1];
                    break;
                case "visits": 
                    // query = "UPDATE visits SET pet_id = ?, visit_date = ?, description = ? WHERE id = ?";
                    // jdbcTemplateObj.update(query, dataArray[2], dataArray[3], dataArray[4], dataArray[1]);
                    query = "UPDATE visits SET pet_id = \'" + dataArray[2] + "\', visit_date = \'" + dataArray[3] + "\', description = \'" + dataArray[4] + 
                    "\' WHERE id = " + dataArray[1];
                    break;
            }
            stmt.executeUpdate(query);
        } catch (SQLException exception) {
            System.out.println(" Update Failed: " + exception.getMessage());
        }
    }

    public int insertData(String dataArray[]) {
        int id = 0;
        try {
            String query = "";
            // Statement stmt = null;
            Connection connection = connect();
            // stmt = connection.createStatement();
            PreparedStatement stmt = null;
            //Switch statement for different tables
            switch(dataArray[0]) {
                case "owners": 
                    // query = "INSERT into owners (first_name, last_name, address, city, telephone) VALUES (?, ?, ?, ?, ?)";
                    // jdbcTemplateObj.update(query, dataArray[1], dataArray[2], dataArray[3], dataArray[4], dataArray[5]);
                    query = "INSERT into owners(first_name, last_name, address, city, telephone) VALUES (\'" +
                            dataArray[1] + "\', \'" + dataArray[2] + "\', \'" + dataArray[3] + "\', \'" + dataArray[4] + "\', \'" + dataArray[5] + "\')";
                    break;
                case "pets": 
                    // query = "INSERT into pets (name, birth_date, type_id, owner_id) VALUES (?, ?, ?, ?)";
                    // jdbcTemplateObj.update(query, dataArray[1], dataArray[2], dataArray[3], dataArray[4]);
                    query = "INSERT into pets(name, birth_date, type_id, owner_id) VALUES (" +
                    dataArray[1] + ", " + dataArray[2] + ", " + dataArray[3] + ", " + dataArray[4] + ")";
                    break;
                case "vets": 
                    // query = "INSERT into vets (first_name, last_name) VALUES (?, ?)";
                    // jdbcTemplateObj.update(query, dataArray[1], dataArray[2]);
                    query = "INSERT into vets(first_name, last_name) VALUES (" +
                    dataArray[1] + ", " + dataArray[2] + ")";
                    break;
                case "visits": 
                    // query = "INSERT into visits (pet_id, visit_date, description) VALUES (?, ?, ?)";
                    // jdbcTemplateObj.update(query, dataArray[1], dataArray[2], dataArray[3]);
                    query = "INSERT into visits(pet_id, visit_date, description) VALUES (" +
                    dataArray[1] + ", " + dataArray[2] + ", " + dataArray[3] + ")";
                    break;
            }

            // THIS PART INSERT TWICE
            // stmt = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            // stmt.execute();

            // THIS PART THROWS A NULL POINTER EXCEPTION
            // stmt.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);

            // stmt.executeQuery();
            System.out.println("QUERY:" + query);
            ResultSet rs = stmt.getGeneratedKeys();
			if(rs.next()){
				id = rs.getInt(1);
                System.out.println("THE ID IS " + id);
			}
        } catch (SQLException exception) {
            System.out.println(" Insert Failed: " + exception.getMessage());
        }
        return id;
    }

}