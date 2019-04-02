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
import java.util.List;

public class ConsistencyChecker {
    // public static void main(String[] args) {
    //     System.out.println("Owner Consistency Check");
    //     ownerConsCheck();
    //     System.out.println("\nPet Consistency Check");
    //     petConsCheck();
    //     System.out.println("\nSpecialty Consistency Check");
    //     specialtyConsCheck();
    //     System.out.println("\nType Consistency Check");
    //     typeConsCheck();
    //     System.out.println("\nVet Consistency Check");
    //     vetConsCheck();
    //     System.out.println("\nVisit Consistency Check");
    //     visitConsCheck();
    // }

    public static int ownerConsCheck() {
        // START OF CONSISTENCY CHECKER FOR OWNER
        ResultSet rsNew = SQLiteDBConnector.getInstance().selectAllASC("owners", "id");
        List<Owner> ownersListNew = new ArrayList<Owner>();
        Owner ownerNew;
        try {
            ownerRSet(rsNew, ownersListNew);
            OwnerConsistencyChecker.setNewData(ownersListNew);
        } catch (SQLException exception) {
            System.out.println(exception);
        }

        ResultSet rsOld = MySQLJDBCDriverConnection.selectAll("owners", "id");
        List<Owner> ownersListOld = new ArrayList<Owner>();
        Owner ownerOld;
        try {
            ownerRSet(rsOld, ownersListOld);
            OwnerConsistencyChecker.setOldData(ownersListOld);
        } catch (SQLException exception) {
            System.out.println(exception);
        }
        int numberOfInconsistencies = new OwnerConsistencyChecker().consistencyChecker();
        System.out.println("Number(s) of inconsistencies: " + numberOfInconsistencies);
        return numberOfInconsistencies;
        //END OF CONSISTENCY CHECKER FOR OWNER
    }

    private static void ownerRSet(ResultSet rs, List<Owner> ownersList) throws SQLException {
        Owner owner;
        while (rs.next()) {
            int id = rs.getInt("id");
            String firstName = rs.getString("first_name");
            String lastName = rs.getString("last_name");
            String address = rs.getString("address");
            String city = rs.getString("city");
            String telephone = rs.getString("telephone");

            owner = new Owner();
            owner.setId(id);
            owner.setFirstName(firstName);
            owner.setLastName(lastName);
            owner.setAddress(address);
            owner.setCity(city);
            owner.setTelephone(telephone);

            ownersList.add(owner);
        }
    }

    public static int numberOfOwnerRows() {
        return new OwnerConsistencyChecker().numberOfRows();
    }

    public static int petConsCheck() {
        ResultSet rsNew = SQLiteDBConnector.getInstance().selectAllASC("pets", "id");
        List<Pet> petsListNew = new ArrayList<Pet>();
        Pet petNew;
        Owner owner;
        PetType petType;
        try {
            petRSet(rsNew, petsListNew);
            PetConsistencyChecker.setNewData(petsListNew);
        } catch (SQLException exception) {
            System.out.println(exception);
        }

        ResultSet rsOld = MySQLJDBCDriverConnection.selectAll("pets", "id");
        List<Pet> petsListOld = new ArrayList<Pet>();
        Pet petOld;
        try {
            petRSet(rsOld, petsListOld);
            PetConsistencyChecker.setOldData(petsListNew);
        } catch (SQLException exception) {
            System.out.println(exception);
        }

        int numberOfInconsistencies = new PetConsistencyChecker().consistencyChecker();
        System.out.println("Number(s) of inconsistencies: " + numberOfInconsistencies);
        return numberOfInconsistencies;
    }

    private static void petRSet(ResultSet rs, List<Pet> petsList) throws SQLException {
        Pet pet;
        Owner owner;
        PetType petType;
        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            String birth_date = rs.getString("birth_date");
            int type_id = rs.getInt("type_id");
            int owner_id = rs.getInt("owner_id");

            pet = new Pet();
            owner = new Owner();
            owner.setId(owner_id);
            petType = new PetType();
            petType.setId(type_id);

            pet.setId(id);
            pet.setName(name);
            pet.setBirthDate(LocalDate.parse(birth_date));
            pet.setType(petType);
            pet.setOwner(owner);

            petsList.add(pet);
        }
    }

    public static int numberOfPetRows() {
        return new PetConsistencyChecker().numberOfRows();
    }

    public static int specialtyConsCheck() {
        ResultSet rsNew = SQLiteDBConnector.getInstance().selectAllASC("specialties", "id");
        List<Specialty> specialtiesListNew = new ArrayList<Specialty>();
        Specialty specialtyNew;
        try {
            specialtyRSet(rsNew, specialtiesListNew);
            SpecialityConsistencyChecker.setNewData(specialtiesListNew);
        } catch (SQLException exception) {
            System.out.println(exception);
        }

        ResultSet rsOld = MySQLJDBCDriverConnection.selectAll("specialties", "id");
        List<Specialty> specialtiesListOld = new ArrayList<Specialty>();
        Specialty specialtyOld;
        try {
            specialtyRSet(rsOld, specialtiesListOld);
            SpecialityConsistencyChecker.setOldData(specialtiesListNew);
        } catch (SQLException exception) {
            System.out.println(exception);
        }

        int numberOfInconsistencies = new SpecialityConsistencyChecker().consistencyChecker();
        System.out.println("Number(s) of inconsistencies: " + numberOfInconsistencies);
        return numberOfInconsistencies;
    }

    private static void specialtyRSet(ResultSet rs, List<Specialty> specialtiesList) throws SQLException {
        Specialty specialty;
        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            specialty = new Specialty();

            specialty.setId(id);
            specialty.setName(name);

            specialtiesList.add(specialty);
        }
    }

    public static int numberOfSpecialityRows() {
        return new SpecialityConsistencyChecker().numberOfRows();
    }

    public static int typeConsCheck() {
        ResultSet rsNew = SQLiteDBConnector.getInstance().selectAllASC("types", "id");
        List<PetType> typeListNew = new ArrayList<PetType>();
        PetType typeNew;
        try {
            typeRSet(rsNew, typeListNew);
            TypeConsistencyChecker.setNewData(typeListNew);
        } catch (SQLException exception) {
            System.out.println(exception);
        }

        ResultSet rsOld = MySQLJDBCDriverConnection.selectAll("types", "id");
        List<PetType> typeListOld = new ArrayList<PetType>();
        PetType typeOld;
        try {
            typeRSet(rsOld, typeListOld);
            TypeConsistencyChecker.setOldData(typeListOld);
        } catch (SQLException exception) {
            System.out.println(exception);
        }

        int numberOfInconsistencies = new TypeConsistencyChecker().consistencyChecker();
        System.out.println("Number(s) of inconsistencies: " + numberOfInconsistencies);
        return numberOfInconsistencies;
    }

    private static void typeRSet(ResultSet rs, List<PetType> typeList) throws SQLException {
        PetType type;
        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");

            type = new PetType();

            type.setId(id);
            type.setName(name);

            typeList.add(type);
        }
    }

    public static int numberOfTypeRows() {
        return new TypeConsistencyChecker().numberOfRows();
    }

    public static int vetConsCheck() {
        ResultSet rsNew = SQLiteDBConnector.getInstance().selectAllASC("vets", "id");
        List<Vet> vetListNew = new ArrayList<Vet>();
        Vet vetNew;
        try {
            vetRSet(rsNew, vetListNew);
            VetConsistencyChecker.setNewData(vetListNew);
        } catch (SQLException exception) {
            System.out.println(exception);
        }

        ResultSet rsOld = MySQLJDBCDriverConnection.selectAll("vets", "id");
        List<Vet> vetListOld = new ArrayList<Vet>();
        Vet vetOld;
        try {
            vetRSet(rsOld, vetListOld);
            VetConsistencyChecker.setOldData(vetListOld);
        } catch (SQLException exception) {
            System.out.println(exception);
        }

        int numberOfInconsistencies = new VetConsistencyChecker().consistencyChecker();
        System.out.println("Number(s) of inconsistencies: " + numberOfInconsistencies);
        return numberOfInconsistencies;
    }

    private static void vetRSet(ResultSet rs, List<Vet> vetList) throws SQLException {
        Vet vet;
        while (rs.next()) {
            int id = rs.getInt("id");
            String first_name = rs.getString("first_name");
            String last_name = rs.getString("last_name");

            vet = new Vet();

            vet.setId(id);
            vet.setFirstName(first_name);
            vet.setLastName(last_name);

            vetList.add(vet);
        }
    }

    public static int numberOfVetRows() {
        return new VetConsistencyChecker().numberOfRows();
    }


    public static int visitConsCheck() {
        ResultSet rsNew = SQLiteDBConnector.getInstance().selectAllASC("visits", "id");
        List<Visit> visitListNew = new ArrayList<Visit>();
        Visit visitNew;
        try {
            visitRSet(rsNew, visitListNew);
            new VisitConsistencyChecker().setNewData(visitListNew);
        } catch (SQLException exception) {
            System.out.println(exception);
        }

        ResultSet rsOld = MySQLJDBCDriverConnection.selectAll("visits", "id");
        List<Visit> visitListOld = new ArrayList<Visit>();
        Visit visitOld;
        try {
            visitRSet(rsOld, visitListOld);
            new VisitConsistencyChecker().setOldData(visitListOld);
        } catch (SQLException exception) {
            System.out.println(exception);
        }

        int numberOfInconsistencies = new VisitConsistencyChecker().consistencyChecker();
        System.out.println("Number(s) of inconsistencies: " + numberOfInconsistencies);
        return numberOfInconsistencies;
    }

    private static void visitRSet(ResultSet rs, List<Visit> visitList) throws SQLException {
        Visit visit;
        while (rs.next()) {

            int id = rs.getInt("id");
            int petId = rs.getInt("pet_id");
            String visitDate = rs.getString("visit_date");
            String description = rs.getString("description");

            visit = new Visit();
            visit.setId(id);
            visit.setPetId(petId);
            visit.setDate(LocalDate.parse(visitDate));
            visit.setDescription(description);

            visitList.add(visit);
        }
    }

    public static int numberOfVisitRows() {
        return new VisitConsistencyChecker().numberOfRows();
    }
    
    public static double calculateTotalConsistency(int inconsistencies, int rows) {
        double consistency = (1 - ((double)inconsistencies/(double)rows))*100;
        return Double.parseDouble(String.format("%.2f", consistency));
    }
}
