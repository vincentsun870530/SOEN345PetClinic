package org.springframework.samples.petclinic.consistencychecker;

import org.springframework.samples.petclinic.mysql.MySQLJDBCDriverConnection;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.samples.petclinic.owner.PetType;
import org.springframework.samples.petclinic.sqlite.SQLiteDBConnector;
import org.springframework.samples.petclinic.vet.Specialty;
import org.springframework.samples.petclinic.vet.Vet;
import org.springframework.samples.petclinic.visit.Visit;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ConsistencyChecker {

    public static void main(String[] args) {
        System.out.println("Owner Consistency Check");
        ownerConsCheck();
        System.out.println("\nPet Consistency Check");
        petConsCheck();
        System.out.println("\nSpecialty Consistency Check");
        specialtyConsCheck();
        System.out.println("\nType Consistency Check");
        typeConsCheck();
        System.out.println("\nVet Consistency Check");
        vetConsCheck();
        System.out.println("\nVisit Consistency Check");
        visitConsCheck();
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
                ownerOld.setId(id);
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

        System.out.println("Number(s) of inconsistencies: " + new OwnerConsistencyChecker().consistencyChecker());
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

        ResultSet rsOld = MySQLJDBCDriverConnection.selectAll("pets");
        List<Pet> petsListOld = new ArrayList<Pet>();
        Pet petOld;
        try {
            while (rsOld.next()) {
                int id = rsOld.getInt("id");
                String name = rsOld.getString("name");
                String birth_date = rsOld.getString("birth_date");
                int type_id = rsOld.getInt("type_id");
                int owner_id = rsOld.getInt("owner_id");

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

        System.out.println("Number(s) of inconsistencies: " + new PetConsistencyChecker().consistencyChecker());
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

        ResultSet rsOld = MySQLJDBCDriverConnection.selectAll("specialties");
        List<Specialty> specialtiesListOld = new ArrayList<Specialty>();
        Specialty specialtyOld;
        try {
            while (rsOld.next()) {
                int id = rsOld.getInt("id");
                String name = rsOld.getString("name");
                specialtyOld = new Specialty();

                specialtyOld.setId(id);
                specialtyOld.setName(name);

                specialtiesListOld.add(specialtyOld);
            }
            SpecialityConsistencyChecker.setOldData(specialtiesListNew);
        } catch (SQLException exception) {
            System.out.println(exception);
        }

        System.out.println("Number(s) of inconsistencies: " + new SpecialityConsistencyChecker().consistencyChecker());
    }

    public static void typeConsCheck() {
        ResultSet rsNew = new SQLiteDBConnector().selectAll("types");
        List<PetType> typeListNew = new ArrayList<PetType>();
        PetType typeNew;
        try {
            while (rsNew.next()) {
                int id = rsNew.getInt("id");
                String name = rsNew.getString("name");

                typeNew = new PetType();

                typeNew.setId(id);
                typeNew.setName(name);

                typeListNew.add(typeNew);
            }
            TypeConsistencyChecker.setNewData(typeListNew);
        } catch (SQLException exception) {
            System.out.println(exception);
        }

        ResultSet rsOld = MySQLJDBCDriverConnection.selectAll("types");
        List<PetType> typeListOld = new ArrayList<PetType>();
        PetType typeOld;
        try {
            while (rsOld.next()) {
                int id = rsOld.getInt("id");
                String name = rsOld.getString("name");

                typeOld = new PetType();

                typeOld.setId(id);
                typeOld.setName(name);

                typeListOld.add(typeOld);
            }
            TypeConsistencyChecker.setOldData(typeListOld);
        } catch (SQLException exception) {
            System.out.println(exception);
        }

        System.out.println("Number(s) of inconsistencies: " + new TypeConsistencyChecker().consistencyChecker());
    }

    public static void vetConsCheck() {
        ResultSet rsNew = new SQLiteDBConnector().selectAll("vets");
        List<Vet> vetListNew = new ArrayList<Vet>();
        Vet vetNew;
        try {
            while (rsNew.next()) {
                int id = rsNew.getInt("id");
                String first_name = rsNew.getString("first_name");
                String last_name = rsNew.getString("last_name");

                vetNew = new Vet();

                vetNew.setId(id);
                vetNew.setFirstName(first_name);
                vetNew.setLastName(last_name);

                vetListNew.add(vetNew);
            }
            VetConsistencyChecker.setNewData(vetListNew);
        } catch (SQLException exception) {
            System.out.println(exception);
        }

        ResultSet rsOld = MySQLJDBCDriverConnection.selectAll("vets");
        List<Vet> vetListOld = new ArrayList<Vet>();
        Vet vetOld;
        try {
            while (rsOld.next()) {
                int id = rsOld.getInt("id");
                String first_name = rsOld.getString("first_name");
                String last_name = rsOld.getString("last_name");

                vetOld = new Vet();

                vetOld.setId(id);
                vetOld.setFirstName(first_name);
                vetOld.setLastName(last_name);

                vetListOld.add(vetOld);
            }
            VetConsistencyChecker.setOldData(vetListOld);
        } catch (SQLException exception) {
            System.out.println(exception);
        }

        System.out.println("Number(s) of inconsistencies: " + new VetConsistencyChecker().consistencyChecker());
    }

    

    public static void visitConsCheck() {
        ResultSet rsNew = new SQLiteDBConnector().selectAll("visits");
        List<Visit> visitListNew = new ArrayList<Visit>();
        Visit visitNew;
        try {
            while (rsNew.next()) {

                int id = rsNew.getInt("id");
                int petId = rsNew.getInt("pet_id");
                String visitDate = rsNew.getString("visit_date");
                String description = rsNew.getString("description");

                visitNew = new Visit();
                visitNew.setId(id);
                visitNew.setPetId(petId);
                visitNew.setDate(LocalDate.parse(visitDate));
                visitNew.setDescription(description);

                visitListNew.add(visitNew);
            }
            new VisitConsistencyChecker().setNewData(visitListNew);
        } catch (SQLException exception) {
            System.out.println(exception);
        }

        ResultSet rsOld = MySQLJDBCDriverConnection.selectAll("visits");
        List<Visit> visitListOld = new ArrayList<Visit>();
        Visit visitOld;
        try {
            while (rsNew.next()) {

                int id = rsOld.getInt("id");
                int petId = rsOld.getInt("pet_id");
                String visitDate = rsOld.getString("visit_date");
                String description = rsOld.getString("description");

                visitOld = new Visit();
                visitOld.setId(id);
                visitOld.setPetId(petId);
                visitOld.setDate(LocalDate.parse(visitDate));
                visitOld.setDescription(description);

                visitListOld.add(visitOld);
            }
            new VisitConsistencyChecker().setOldData(visitListOld);
        } catch (SQLException exception) {
            System.out.println(exception);
        }

        System.out.println("Number(s) of inconsistencies: " + new VisitConsistencyChecker().consistencyChecker());
    }
}
