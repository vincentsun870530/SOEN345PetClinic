package org.springframework.samples.petclinic.consistencychecker;

import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.samples.petclinic.sqlite.SQLiteDBConnector;

import java.time.LocalDate;
import java.util.List;

public class PetConsistencyChecker implements InConsistencyChecker {

    private static List<Pet> oldPetsData;
    private static List<Pet> newPetsData;
    
    public static void setOldData(List<Pet> data) {
        oldPetsData = data;
    }

    public static void setNewData(List<Pet> data) {
        newPetsData = data;
    }

    public int consistencyChecker() {
        int inconsistency = 0;
        if (oldPetsData.size() == newPetsData.size()) {
            Pet oldPet;
            Pet newPet;
            int atID;
            for (int index = 0; index < oldPetsData.size(); index++) {
                oldPet = oldPetsData.get(index);
                newPet = newPetsData.get(index);
                
                inconsistency += petCheckConsistency(oldPet, newPet);
                // if (oldPet.getId() == newPet.getId()) {
                //     atID = newPet.getId();
                //     if (inconsistency != 0) {
                //         System.out.println("OLD:" + oldPetsData.get(index));
                //         System.out.println("NEW:" + newPetsData.get(index));
                //     }
                //     //need the number of columns (use hardcoded number or dynamically check the number of columns)
                //     //for Owner,  columns
                //     if (!oldPet.getName().equals(newPet.getName())) {
                //         checkNewAndOldData(atID, oldPet.getName(), newPet.getName(), "name");
                //         inconsistency++;
                //     }
                //     if (!oldPet.getBirthDate().equals(newPet.getBirthDate())) {
                //         checkDateNewAndOldData(atID, oldPet.getBirthDate(), newPet.getBirthDate(), "birth_date");
                //         inconsistency++;
                //     }
                //     if (!oldPet.getType().equals(newPet.getType())) {
                //         checkNewAndOldData(atID, oldPet.getType().getId().toString(), newPet.getOwner().getId().toString(), "owner_id");
                //         inconsistency++;
                //     }
                // } else {
                //     System.out.println("Very inconsistent table (ID sequence not matching), please contact your DB admin: " + oldPet.getId() + " != " + newPet.getId());
                //     inconsistency++;
                // }
            }
        } else {
            System.out.println("Old and new DB table size don't match! " + oldPetsData.size() + " != " + newPetsData.size());
            inconsistency++;
        }
        return inconsistency;
    }

    public int petCheckConsistency(Pet oldPet, Pet newPet) {
        int inconsistency = 0;
        try {
            if (oldPet.getId() == newPet.getId()) {
                int atID = newPet.getId();
                if (!oldPet.getName().equals(newPet.getName())) {
                    System.out.println("old pet"+oldPet.getName());
                    System.out.println("new pet"+newPet.getName());
                    checkNewAndOldData(atID, oldPet.getName(), newPet.getName(), "name");
                    inconsistency++;
                    System.out.println(inconsistency+" rate@");
                }
                if (!oldPet.getBirthDate().equals(newPet.getBirthDate())) {
                    System.out.println("old pet"+oldPet.getBirthDate().toString());
                    System.out.println("new pet"+newPet.getBirthDate().toString());
                    checkDateNewAndOldData(atID, oldPet.getBirthDate(), newPet.getBirthDate(), "birth_date");
                    inconsistency++;
                    System.out.println(inconsistency+" rate@");
                }
                if (!oldPet.getType().getId().equals(newPet.getType().getId())) {

                    //System.out.println("old pet"+oldPet.getType());
                    //System.out.println("new pet"+newPet.getType());
                    checkNewAndOldData(atID, oldPet.getType().getId().toString(), newPet.getType().getId().toString(), "type_id");
                    inconsistency++;
                    //System.out.println(inconsistency+" rate@");
                }
                if (!oldPet.getOwner().getId().equals(newPet.getOwner().getId())) {

                    //System.out.println("old pet"+oldPet.getOwner());
                    //System.out.println("new pet"+newPet.getOwner());
                    checkNewAndOldData(atID, oldPet.getOwner().getId().toString(), newPet.getOwner().getId().toString(), "owner_id");
                    inconsistency++;
                    //System.out.println(inconsistency+" rate@");
                }
            } else {
                System.out.println("Very inconsistent table (ID sequence not matching), please contact your DB admin: " + oldPet.getId() + " != " + newPet.getId());
                inconsistency++;
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        return inconsistency;
    }

    public double calculateConsistencyChecker(int inconsistency) {
        int sizeOfPets = oldPetsData.size();
        double consistency = (1 - (inconsistency/sizeOfPets))*100;
        return Double.parseDouble(String.format("%.2f", consistency));
    }

    public int numberOfRows() {
        return oldPetsData.size();
    }

    private void checkNewAndOldData(int id, String oldData, String newData, String columnName){
        checkNewAndOldData(id,oldData,newData,columnName,"pets");
    }

    private void checkNewAndOldData(int id, String oldData, String newData, String columnName, String tableName) {
        if(!(oldData.equals(newData))) {
            printViolationMessage(id, oldData, newData);
            SQLiteDBConnector.getInstance().updateById(tableName,columnName, oldData, id);
        }
    }

    private void checkDateNewAndOldData(int id, LocalDate oldDate, LocalDate newDate, String columnName) {
        checkDateNewAndOldData(id,oldDate,newDate,columnName,"pets");
    }

    private void checkDateNewAndOldData(int id, LocalDate oldDate, LocalDate newDate, String columnName, String tableName) {
        if(oldDate.isEqual(newDate) == false) {
            printViolationMessage(id, oldDate.toString(), newDate.toString());

            SQLiteDBConnector.getInstance().updateById(tableName,columnName, oldDate.toString(), id);

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