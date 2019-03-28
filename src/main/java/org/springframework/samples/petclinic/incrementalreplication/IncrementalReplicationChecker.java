package org.springframework.samples.petclinic.incrementalreplication;

import java.sql.*;
import java.time.LocalDate;

import org.springframework.samples.petclinic.sqlite.SQLiteDBConnector;
import org.springframework.samples.petclinic.visit.Visit;
import org.springframework.samples.petclinic.consistencychecker.OwnerConsistencyChecker;
import org.springframework.samples.petclinic.consistencychecker.PetConsistencyChecker;
import org.springframework.samples.petclinic.consistencychecker.VisitConsistencyChecker;
import org.springframework.samples.petclinic.mysql.MySQLJDBCDriverConnection;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.samples.petclinic.owner.PetType;
import org.springframework.samples.petclinic.owner.PetType;

public class IncrementalReplicationChecker {

    public static boolean isConsistency(int id, String tableName) {
        try {
            ResultSet oldDatabase = MySQLJDBCDriverConnection.selectById(tableName, id);
            ResultSet newDatabase = SQLiteDBConnector.getInstance().selectById(tableName, id);
            int isInconsistency = 0;
            System.out.println("TEST ID:" + id);
            switch(tableName) {
                case "owners":
                    Owner oldOwner = ownerRSet(oldDatabase);
                    Owner newOwner = ownerRSet(newDatabase);
                    isInconsistency = new OwnerConsistencyChecker().ownerCheckConsistency(oldOwner, newOwner);
                    System.out.println("IN SWITCH isInconsistency:" + isInconsistency);
                    break;
                case "pets": 
                    Pet oldPet = petRSet(oldDatabase);
                    Pet newPet = petRSet(newDatabase);
                    isInconsistency = new PetConsistencyChecker().petCheckConsistency(oldPet, newPet);
                    break;
                case "visits":
                    Visit oldVisit = visitRSet(oldDatabase);
                    Visit newVisit = visitRSet(newDatabase);
                    isInconsistency = new VisitConsistencyChecker().visitCheckConsistency(oldVisit, newVisit);
                    System.out.println("IN SWITCH isInconsistency:" + isInconsistency);
            }
            if(isInconsistency != 0) {
                System.out.println("isInconsistency:" + isInconsistency);
                printViolationMessage(id, tableName);
            } else {
                System.out.println("SUCCESS");
                return true;
            }
        } catch (SQLException exception) {
            System.out.println("ERROR IncrementalReplication/checkConsistency:" + exception.toString());
        }
        return false;
    }

    public static void printViolationMessage(int id, String tableName){
        System.out.println("The row " + id + " in the table " + tableName + "is inconsistent with the old database");
    }

    private static Owner ownerRSet(ResultSet rs) throws SQLException {
        Owner owner = null;
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
        }
        return owner;
    }

    private static Pet petRSet(ResultSet rs) throws SQLException {
        Pet pet = null;
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
        }
        return pet;
    }

    private static Visit visitRSet(ResultSet rs) throws SQLException {
        Visit visit = null;
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
        }
        System.out.println(visit.toString());
        return visit;
    }
    
}
