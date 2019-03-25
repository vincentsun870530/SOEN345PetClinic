package org.springframework.samples.petclinic.consistencychecker;

import org.springframework.samples.petclinic.mysql.MySQLJDBCDriverConnection;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.samples.petclinic.owner.PetType;
import org.springframework.samples.petclinic.sqlite.SQLiteDBConnector;
import org.springframework.samples.petclinic.vet.Specialty;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ConsistencyChecker {

    public static void main(String[] args) {
        ownerConsCheck();
        petConsCheck();
        specialtyConsCheck();
    }

    public static void ownerConsCheck() {
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
            System.out.println(exception);
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
            System.out.println(exception);
        }

        new OwnerConsistencyChecker().consistencyChecker();
        //END OF CONSISTENCY CHECKER FOR OWNER
    }

    public static void petConsCheck() {
        ResultSet rsNew = new SQLiteDBConnector().selectAll("pets");
        List<Pet> petsListNew = new ArrayList<Pet>();
        Pet petNew;
        Owner owner;
        PetType petType;
        try {
            while (rsNew.next()) {
                int id = rsNew.getInt("id");
                String name = rsNew.getString("name");
                String birth_date = rsNew.getString("birth_date");
                int type_id = rsNew.getInt("type_id");
                int owner_id = rsNew.getInt("owner_id");

                petNew = new Pet();
                owner = new Owner();
                owner.setId(owner_id);
                petType = new PetType();
                petType.setId(type_id);

                petNew.setId(id);
                petNew.setName(name);
                petNew.setBirthDate(LocalDate.parse(birth_date));
                petNew.setType(petType);
                petNew.setOwner(owner);

                petsListNew.add(petNew);
            }
            PetConsistencyChecker.setNewData(petsListNew);
        } catch (SQLException exception) {
            System.out.println(exception);
        }

        ResultSet rsOld = MySQLJDBCDriverConnection.selectAll("owners");
        List<Pet> petsListOld = new ArrayList<Pet>();
        Pet petOld;
        try {
            while (rsNew.next()) {
                int id = rsNew.getInt("id");
                String name = rsNew.getString("name");
                String birth_date = rsNew.getString("birth_date");
                int type_id = rsNew.getInt("type_id");
                int owner_id = rsNew.getInt("owner_id");

                petOld = new Pet();
                owner = new Owner();
                owner.setId(owner_id);
                petType = new PetType();
                petType.setId(type_id);

                petOld.setId(id);
                petOld.setName(name);
                petOld.setBirthDate(LocalDate.parse(birth_date));
                petOld.setType(petType);
                petOld.setOwner(owner);

                petsListOld.add(petOld);
            }
            PetConsistencyChecker.setOldData(petsListNew);
        } catch (SQLException exception) {
            System.out.println(exception);
        }

        new PetConsistencyChecker().consistencyChecker();
    }

    public static void specialtyConsCheck() {
        ResultSet rsNew = new SQLiteDBConnector().selectAll("specialties");
        List<Specialty> specialtiesListNew = new ArrayList<Specialty>();
        Specialty specialtyNew;
        try {
            while (rsNew.next()) {
                int id = rsNew.getInt("id");
                String name = rsNew.getString("name");

                specialtyNew = new Specialty();

                specialtyNew.setId(id);
                specialtyNew.setName(name);

                specialtiesListNew.add(specialtyNew);
            }
            SpecialityConsistencyChecker.setNewData(specialtiesListNew);
        } catch (SQLException exception) {
            System.out.println(exception);
        }

        ResultSet rsOld = MySQLJDBCDriverConnection.selectAll("owners");
        List<Specialty> specialtiesListOld = new ArrayList<Specialty>();
        Specialty specialtyOld;
        try {
            while (rsNew.next()) {
                int id = rsNew.getInt("id");
                String name = rsNew.getString("name");
                specialtyOld = new Specialty();

                specialtyOld.setId(id);
                specialtyOld.setName(name);

                specialtiesListOld.add(specialtyOld);
            }
            SpecialityConsistencyChecker.setOldData(specialtiesListNew);
        } catch (SQLException exception) {
            System.out.println(exception);
        }

        new SpecialityConsistencyChecker().consistencyChecker();
    }
}
