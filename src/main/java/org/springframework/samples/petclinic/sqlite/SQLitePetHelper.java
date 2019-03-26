package org.springframework.samples.petclinic.sqlite;

import java.sql.*;
import java.time.LocalDate;

public class SQLitePetHelper {

    private static final String INSERT_SQL = "INSERT INTO pets(name, birth_date, type_id, owner_id) VALUES(?, ?, ?, ?)";
    private Connection connection = (SQLiteDBConnector.getInstance()).connect(); // GET CONNECTION.

    private static SQLitePetHelper sqLitePetHelper = null;

    private SQLitePetHelper()
    {
        // for singleton
    }

    public static SQLitePetHelper getInstance()
    {
        if (sqLitePetHelper == null)
            sqLitePetHelper = new SQLitePetHelper();
        return sqLitePetHelper;
    }


    public int insert(String name, String date, int typeId, int ownerId) {
        int numRowsInserted = 0;
        PreparedStatement ps = null;
        try {
            ps = this.connection.prepareStatement(INSERT_SQL);
            ps.setString(1, name);
            ps.setString(2,date);
            ps.setInt(3, typeId);
            ps.setInt(4, ownerId);
            numRowsInserted = ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(ps);
        }
        return numRowsInserted;
    }

    //update

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
