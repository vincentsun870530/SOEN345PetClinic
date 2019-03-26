package org.springframework.samples.petclinic.incrementalreplication;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import org.springframework.samples.petclinic.consistencychecker.OwnerConsistencyChecker;
import org.springframework.samples.petclinic.consistencychecker.PetConsistencyChecker;
import org.springframework.samples.petclinic.consistencychecker.VisitConsistencyChecker;
import org.springframework.samples.petclinic.mysql.MySQLJDBCDriverConnection;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.samples.petclinic.owner.PetType;
import org.springframework.samples.petclinic.sqlite.SQLiteDBConnector;
import org.springframework.samples.petclinic.visit.Visit;

public class IncrementalReplication {

    /** updateArray will contain ID and table name separated by a comma*/
    private static ArrayList<String> updateArray;

    /** Could use ArrayList of array if needed
     *  Else use ArrayList of String for a simple implementation
     *  If ArrayList of String: put whole object in one string, separate by commas
     */
    private static ArrayList<String> createArray; 
    
    public static void addToUpdateList(String data) {
        if(updateArray == null) {
            updateArray = new ArrayList<String>();
        }
        updateArray.add(data);
        System.out.println("Data saved to update list");
    }

    public static void addToCreateList(String data) {
        if(createArray == null) {
            createArray = new ArrayList<String>();
        }
        createArray.add(data);
        System.out.println("Data saved to create list");
    }

    public static void incrementalReplication() {
        String data = null;
        String[] splittedData = null;


        //TODO ADD SQLITE CONNECTION 
        Connection connectionMySQL = MySQLJDBCDriverConnection.connect();
        Connection connectionSQLite = SQLiteDBConnector.getInstance().connect();


        if(updateArray != null) {
            for(int index=0; index<updateArray.size(); index++) {
                    data = updateArray.get(index);
                    splittedData = data.split(",");

                    // Could use the array value directly to the query
                    // String idString = splittedData[0];
                    // String tableName = splittedData[1];
                    // String columnName = splittedData[2];
                    // String value = splittedData[3];

                    // int id = Integer.parseInt(idString);

                    // SQLiteDBConnector.getInstance().updateById(tableName, columnName, value, id);

                    // checkConsistency(id, tableName, columnName, value);
                    int id = Integer.parseInt(splittedData[1].replace(" ", ""));
                    SQLiteDBConnector.getInstance().updateRow(splittedData);
                    // checkConsistency(id, splittedData[0], splittedData);
                    checkConsistency(id, splittedData[0]);

            }
        }

        // To ensure that the data and splittedData are reset to insert new clean data
        data = null;
        splittedData = null;
        if(createArray != null) {
            for(int index=0; index<createArray.size(); index++) {
                data = createArray.get(index);
                splittedData = data.split(",");
                String tableName = splittedData[0];

                // String tableName = splittedData[0];
                int id = SQLiteDBConnector.getInstance().insertData(splittedData);
                if(id != 0) {
                    // checkConsistency(id, tableName, splittedData);
                    checkConsistency(id, tableName);
                } else {
                    System.out.println("Error in incrementalReplication(): ID(" + id + ") not found in table(" + tableName + ")");
                }

            }
        }
    }

    // private static void checkConsistency(int id, String tableName, String columnName, String value) {
    //     String oldDatabase = (MySQLJDBCDriverConnection.selectById(tableName, id)).toString();
    //     String newDatabase = (SQLiteDBConnector.getInstance().selectById(tableName, id)).toString();

    //     if(oldDatabase != newDatabase) {
    //         printViolationMessage(id, tableName);
    //         SQLiteDBConnector.getInstance().updateById(tableName, columnName, value, id);
    //     }
    // }

    private static void checkConsistency(int id, String tableName) {
        // String oldDatabase = (MySQLJDBCDriverConnection.selectById(tableName, id)).toString();
        // String newDatabase = (SQLiteDBConnector.getInstance().selectById(tableName, id)).toString();

        // if(oldDatabase != newDatabase) {
        //     printViolationMessage(id, tableName);
        //     SQLiteDBConnector.getInstance().updateRow(array);
        // }

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
            }
            if(isInconsistency != 0) {
                System.out.println("isInconsistency:" + isInconsistency);
                printViolationMessage(id, tableName);
            } else {
                System.out.println("SUCCESS");
            }
        } catch (SQLException exception) {
            System.out.println("ERROR IncrementalReplication/checkConsistency:" + exception.toString());
        }
    }

    public static void printViolationMessage(int id, String tableName){ //, String oldData, String newData) {
        // System.out.println("The row " + id + " on the new database," +
        //                     " does not match: New(" + newData + 
        //                     " is not equal to Old(" + oldData);
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
        return visit;
    }

    /**
     * TODO ADD CALLS TO THOSE METHODS IN ALL CONTROLLERS FOR UPDATE AND CREATE
     */


}