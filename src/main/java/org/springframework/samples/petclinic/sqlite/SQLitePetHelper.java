// contributed along with Felix

package org.springframework.samples.petclinic.sqlite;

import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.samples.petclinic.owner.PetType;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SQLitePetHelper {
    private static final String INSERT_SQL = "INSERT INTO pets(name, birth_date, type_id, owner_id) VALUES(?, ?, ?, ?)";
    private Connection connection = (SQLiteDBConnector.getInstance()).connect();

    private static SQLitePetHelper sqLitePetHelper = null;

    private SQLitePetHelper() {}

    public static SQLitePetHelper getInstance()
    {
        if (sqLitePetHelper == null)
            sqLitePetHelper = new SQLitePetHelper();
        return sqLitePetHelper;
    }


    public int insert(String name, String birth_date, int type_id, int owner_id) {
        int numRowsInserted = 0;
        PreparedStatement ps = null;
        try {
            ps = this.connection.prepareStatement(INSERT_SQL);
            ps.setString(1, name);
            ps.setString(2,birth_date);
            ps.setInt(3, type_id);
            ps.setInt(4, owner_id);
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
        SQLiteDBConnector.getInstance().updateById("pets",colName,colValue,id);
    }

    //Retrieve from ResultSet
    public List<Pet> getModelList(ResultSet rs){
        ArrayList<Pet> petList = new ArrayList<Pet>();
        try{
            while(rs.next()){
                Pet pet = new Pet();
                System.out.println(rs.getInt("id"));
                pet.setId(rs.getInt("id"));
                System.out.println(rs.getString("name"));
                pet.setName(rs.getString("name"));
                System.out.println(rs.getString("birth_date"));
                pet.setBirthDate(LocalDate.parse(rs.getString("birth_date")));
                System.out.println(rs.getString("type_id"));
                PetType typeIdHolder = new PetType();
                typeIdHolder.setId(Integer.parseInt(rs.getString("type_id")));
                pet.setType(typeIdHolder);
                System.out.println(rs.getString("owner_id"));
                Owner ownerIdHolder = new Owner();
                ownerIdHolder.setId(Integer.parseInt(rs.getString("owner_id")));
                pet.setOwner(ownerIdHolder);

                petList.add(pet);
            }
        }catch(SQLException e){
            e.printStackTrace();
            System.out.println(e.getMessage() + " Retrieve Error From pet Helper");
        }
        return petList;
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
