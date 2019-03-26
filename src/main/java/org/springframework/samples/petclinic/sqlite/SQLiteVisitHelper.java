package org.springframework.samples.petclinic.sqlite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

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
