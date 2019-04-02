package org.springframework.samples.petclinic.sqlite;

import java.sql.*;

public class SQLiteIncrementalReplicationHelper {

    public final static String URL = "jdbc:sqlite:src/main/resources/db/sqlite/petclinic.sqlite3";

    private static SQLiteIncrementalReplicationHelper SQLiteIncrementReplicationHelper = null;

    private SQLiteIncrementalReplicationHelper()
    {
        // for singleton
    }

    public static SQLiteIncrementalReplicationHelper getInstance()
    {
        if (SQLiteIncrementReplicationHelper == null)
            SQLiteIncrementReplicationHelper = new SQLiteIncrementalReplicationHelper();
        return SQLiteIncrementReplicationHelper;
    }

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


    //Update the whole row
    public boolean updateRow(String dataArray[]) {
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
            query = returnUpdateQuery(dataArray);
            System.out.println(query+" @@@@@@@@@@@@@@@@@@@@@@@@@+@@@@@@@@@@@@@@@@@@");
            stmt.executeUpdate(query);
            close(stmt);
            return true;
        } catch (SQLException exception) {
            System.out.println(" Update Failed: " + exception.getMessage());
            return false;
        }
    }


/*    //Update the whole row
    public int updateRowReturnId(String dataArray[]) {
        int primaryKey=0;
        try {
            String query = "";
            Statement stmt = null;
            Connection connection = connect();
            stmt = connection.createStatement();
            System.out.println(dataArray.length+"######################################");
            query = returnUpdateQuery(dataArray);
            stmt.executeUpdate(query,PreparedStatement.RETURN_GENERATED_KEYS);

        } catch (SQLException exception) {
            System.out.println(" Update Failed: " + exception.getMessage());
        }
        return primaryKey;
    }*/

    private String returnUpdateQuery(String dataArray[]){
        String query = "";
        /**
         *  Switch statement for different tables
         *  dataArray[0] is for the table name
         *  dataArray[1] is for the id
         *  the rest of dataArray is the the value of the columns
         * */
        switch(dataArray[0]) {
            case "owners":
                query = "UPDATE owners SET first_name = \'" + dataArray[2] + "\', last_name = \'" + dataArray[3] + "\', address = \'" + dataArray[4] +
                        "\', city = \'" + dataArray[5] + "\', telephone = \'" + dataArray[6] + "\' WHERE id = " + dataArray[1];
                break;
            case "pets":
                query = "UPDATE pets SET name = \'" + dataArray[2] + "\', birth_date = \'" + dataArray[3] + "\', type_id = \'" + dataArray[4] +
                        "\', owner_id = \'" + dataArray[5] + "\' WHERE id = " + dataArray[1];
                break;
            case "vets":
                query = "UPDATE vets SET first_name = \'" + dataArray[2] + "\', last_name = \'" + dataArray[3] + "\' WHERE id = " + dataArray[1];
                break;
            case "visits":
                query = "UPDATE visits SET pet_id = \'" + dataArray[2] + "\', visit_date = \'" + dataArray[3] + "\', description = \'" + dataArray[4] +
                        "\' WHERE id = " + dataArray[1];
                break;
        }
        return query;
    }


    //Insert Data and return ID
    public int insertData(String dataArray[]) {
        int id = 0;
        try {
            String query = "";
            Connection connection = connect();
            PreparedStatement stmt = null;
            //Switch statement for different tables
            switch(dataArray[0]) {
                case "owners": 
                    query = "INSERT into owners(first_name, last_name, address, city, telephone) VALUES (\'" +
                            dataArray[1] + "\', \'" + dataArray[2] + "\', \'" + dataArray[3] + "\', \'" + dataArray[4] + "\', \'" + dataArray[5] + "\')";
                    break;
                case "pets": 
                    query = "INSERT into pets(name, birth_date, type_id, owner_id) VALUES (\'" +
                    dataArray[1] + "\', \'" + dataArray[2] + "\', \'" + dataArray[3] + "\', \'" + dataArray[4] + "\')";
                    break;
                case "vets": 
                    query = "INSERT into vets(first_name, last_name) VALUES (\'" +
                    dataArray[1] + "\', \'" + dataArray[2] + "\')";
                    break;
                case "visits": 
                    query = "INSERT into visits(pet_id, visit_date, description) VALUES (\'" +
                    dataArray[1] + "\', \'" + dataArray[2] + "\', \'" + dataArray[3] + "\')";
                    break;
            }
            stmt = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
			if(rs.next()){
				id = rs.getInt(1);
            }
            close(stmt);
        } catch (SQLException exception) {
            System.out.println(" Insert Failed: " + exception.getMessage());
        }
        return id;
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
