package org.springframework.samples.petclinic.consistencychecker;

import java.time.LocalDate;
import java.util.List;
import org.springframework.samples.petclinic.sqlite.SQLiteDBConnector;
import org.springframework.samples.petclinic.mysql.MySQLJDBCDriverConnection;
import org.springframework.samples.petclinic.owner.Pet;

public class PetConsistencyChecker implements InConsistencyChecker {

    private List<Pet> oldPetsData;
    private List<Pet> newPetsData;
    
    public void setOldData(List<Pet> oldTableData) {
        this.oldPetsData = oldTableData;
    }

    public void setNewData(List<Pet> newTableData) {
        this.newPetsData = newTableData;
    }

    public int consistencyChecker() {
        Pet oldPet;
        Pet newPet;
        int atID;
        int inconsistency = 0;
        for(int index=0; index < oldPetsData.size(); index++) {
            oldPet = oldPetsData.get(index);
            newPet = newPetsData.get(index);
            //need the number of columns (use hardcoded number or dynamically check the number of columns)
            //for Owner,  columns
            if(oldPet.toString() != newPet.toString()) {
                atID = newPet.getId();
                checkNewAndOldData(atID, oldPet.getName(), newPet.getName(),"name");
                checkDateNewAndOldData(atID, oldPet.getBirthDate(), newPet.getBirthDate(), "birth_date");
                checkNewAndOldData(atID, oldPet.getType().getId().toString(), newPet.getOwner().getId().toString(), "owner_id");
                inconsistency++;
            }   
        }
        return inconsistency;
    }

    public double calculateConsistencyChecker(int inconsistency) {
        int sizeOfPets = oldPetsData.size();
        double consistency = (1 - (inconsistency/sizeOfPets))*100;
        return Double.parseDouble(String.format("%.2f", consistency));
    }

    private void checkNewAndOldData(int id, String oldData, String newData, String columnName){
        checkNewAndOldData(id,oldData,newData,columnName,"pets");
    }

    private void checkNewAndOldData(int id, String oldData, String newData, String columnName, String tableName) {
        if(!(oldData.equals(newData))) {
            printViolationMessage(id, oldData, newData);
            new SQLiteDBConnector().updateById(tableName,columnName, oldData, id);
        }
    }

    private void checkDateNewAndOldData(int id, LocalDate oldDate, LocalDate newDate, String columnName) {
        checkDateNewAndOldData(id,oldDate,newDate,columnName,"pets");
    }

    private void checkDateNewAndOldData(int id, LocalDate oldDate, LocalDate newDate, String columnName, String tableName) {
        if(oldDate.isEqual(newDate) == false) {
            printViolationMessage(id, oldDate.toString(), newDate.toString());

            new SQLiteDBConnector().updateById(tableName,columnName, oldDate.toString(), id);

        }
    }

    //TODO how can u check ID, it is a primary key
//    private void checkIDNewAndOldData(int id, int oldId, int newId, String columnName) {
//        if((oldId == newId) == false) {
//            printViolationMessage(id, Integer.toString(oldId), Integer.toString(newId));
//
//            // TODO update the new database
//            // INSERT CODE HER FOR UPDATING TO THE NEW DATABASE
//
//        }
//    }

    public void printViolationMessage(int id, String oldData, String newData) {
        System.out.println("The row " + id + " on the new database," +
                            " does not match: New(" + newData + 
                            " is not equal to Old(" + oldData);
    }


}