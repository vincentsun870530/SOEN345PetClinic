package org.springframework.samples.petclinic.sqlite;
import org.springframework.samples.petclinic.owner.Owner;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
public class SQLiteOwnerHelper {

    private static final String INSERT_SQL = "INSERT INTO owners(first_name, last_name, address, city, telephone) VALUES(?, ?, ?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE owners SET first_name = ?, last_name = ?, address = ?, city = ?, telephone = ?, WHERE id = ?";
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
    public int update(Owner owner) {
        int numRowsInserted = 0;
        PreparedStatement statement = null;
        try{
            statement = this.connection.prepareStatement(UPDATE_SQL);
            statement.setString(1, owner.getFirstName());
            statement.setString(2, owner.getLastName());
            statement.setString(3, owner.getAddress());
            statement.setString(4, owner.getCity());
            statement.setString(5, owner.getTelephone());
            statement.setInt(6, owner.getId());
            numRowsInserted = statement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Exception in updating owner" + ex);
        }
        return numRowsInserted;
    }
    public void updateColById(String colName, String colValue, int id)
    {
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
