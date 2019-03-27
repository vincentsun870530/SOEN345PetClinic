package org.springframework.samples.petclinic.sqlite;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
public class SQLiteOwnerHelper {

    private static final String INSERT_SQL = "INSERT INTO owners(first_name, last_name, address, city, telephone) VALUES(?, ?, ?, ?, ?)";
    private Connection connection = (SQLiteDBConnector.getInstance()).connect(); // GET CONNECTION.

    private static SQLiteOwnerHelper sqLiteOwnerHelper = null;

    private SQLiteOwnerHelper()
    {
        // for singleton
    }

    public static SQLiteOwnerHelper getInstance()
    {
        if (sqLiteOwnerHelper == null)
            sqLiteOwnerHelper = new SQLiteOwnerHelper();
        return sqLiteOwnerHelper;
    }


    public int insert(String firstName, String lastName, String address, String city, String telephone) {
        int numRowsInserted = 0;
        PreparedStatement ps = null;
        try {
            ps = this.connection.prepareStatement(INSERT_SQL);
            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ps.setString(3, address);
            ps.setString(4, city);
            ps.setString(5, telephone);
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
        //TODO use the UpdateOwner instead if u have time
        SQLiteDBConnector.getInstance().updateById("owners",colName,colValue,id);

    }

    //delete



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
