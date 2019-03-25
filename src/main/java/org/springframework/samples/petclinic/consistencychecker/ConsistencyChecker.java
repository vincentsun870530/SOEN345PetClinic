package org.springframework.samples.petclinic.consistencychecker;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.samples.petclinic.mysql.MySQLJDBCDriverConnection;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.samples.petclinic.sqlite.SQLiteDBConnector;

public class ConsistencyChecker {

        public static void main(String[] args) {

            // START OF CONSISTENCY CHECKER FOR OWNER
            ResultSet rsNew = new SQLiteDBConnector().selectAll("owners");
            List<Owner> ownersListNew = new ArrayList<Owner>();
            Owner ownerNew;
            try {
                while (rsNew.next()) {
                    int id = rsNew.getInt("id");
                    String firstName = rsNew.getString("first_name");
                    String lastName = rsNew.getString("last_name");
                    String address = rsNew.getString("address");
                    String city = rsNew.getString("city");
                    String telephone = rsNew.getString("telephone");

                    ownerNew = new Owner();
                    ownerNew.setId(id);
                    ownerNew.setFirstName(firstName);
                    ownerNew.setLastName(lastName);
                    ownerNew.setAddress(address);
                    ownerNew.setCity(city);
                    ownerNew.setTelephone(telephone);

                    ownersListNew.add(ownerNew);
                }
                OwnerConsistencyChecker.setNewData(ownersListNew);
            } catch (SQLException exception) {

            }

            ResultSet rsOld = MySQLJDBCDriverConnection.selectAll("owners");
            List<Owner> ownersListOld = new ArrayList<Owner>();
            Owner ownerOld;
            try {
                while (rsOld.next()) {
                    int id = rsOld.getInt("id");
                    String firstName = rsOld.getString("first_name");
                    String lastName = rsOld.getString("last_name");
                    String address = rsOld.getString("address");
                    String city = rsOld.getString("city");
                    String telephone = rsOld.getString("telephone");

                    ownerOld = new Owner();
                    ownerOld.setFirstName(firstName);
                    ownerOld.setLastName(lastName);
                    ownerOld.setAddress(address);
                    ownerOld.setCity(city);
                    ownerOld.setTelephone(telephone);

                    ownersListOld.add(ownerOld);
                }
                OwnerConsistencyChecker.setOldData(ownersListOld);
            } catch (SQLException exception) {

            }

            new OwnerConsistencyChecker().consistencyChecker();
            //END OF CONSISTENCY CHECKER FOR OWNER

            // START OF CONSISTENCY CHECKER FOR OWNER
            ResultSet rsNewPets = new SQLiteDBConnector().selectAll("pets");
            List<Pet> ownersListNewPets = new ArrayList<Pet>();
            Owner petNew;
            try {
                while (rsNew.next()) {
                    int idPet = rsNewPets.getInt("id");
                    String namePet = rsNewPets.getString("name");
                    String birthdatePet = rsNewPets.getString("birth_date");
                    int typeIdPet = rsNewPets.getInt("type_id");
                    int ownerIdPet = rsNewPets.getInt("owner_id");

                    // petNew = new Owner();
                    // petNew.setId(idPet);
                    // petNew.set(namePet);
                    // petNew.setLastName(birthdatePet);
                    // petNew.setAddress(birthdatePet);
                    // petNew.setCity(city);
                    // petNew.setTelephone(telephone);

                    // ownersListNewPets.add(petNew);
                }
                // PetConsistencyChecker.setNewData(ownersListNew);
            } catch (SQLException exception) {

            }

            // ResultSet rsOld = MySQLJDBCDriverConnection.selectAll("owners");
            // List<Owner> ownersListOld = new ArrayList<Owner>();
            // Owner ownerOld;
            // try {
            //     while (rsOld.next()) {
            //         int id = rsOld.getInt("id");
            //         String firstName = rsOld.getString("first_name");
            //         String lastName = rsOld.getString("last_name");
            //         String address = rsOld.getString("address");
            //         String city = rsOld.getString("city");
            //         String telephone = rsOld.getString("telephone");

            //         ownerOld = new Owner();
            //         ownerOld.setFirstName(firstName);
            //         ownerOld.setLastName(lastName);
            //         ownerOld.setAddress(address);
            //         ownerOld.setCity(city);
            //         ownerOld.setTelephone(telephone);

            //         ownersListOld.add(ownerOld);
            //     }
            //     OwnerConsistencyChecker.setOldData(ownersListOld);
            // } catch (SQLException exception) {

            // }

            new PetConsistencyChecker().consistencyChecker();
            //END OF CONSISTENCY CHECKER FOR OWNER

    }
}
