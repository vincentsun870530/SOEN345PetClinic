package org.springframework.samples.petclinic.consistencychecker;

import java.util.List;

import org.springframework.samples.petclinic.sqlite.SQLiteDBConnector;
import org.springframework.samples.petclinic.vet.Vet;

public class VetConsistencyChecker implements InConsistencyChecker {

    private static List<Vet> oldVetsData;
    private static List<Vet> newVetsData;
    
    public static void setOldData(List<Vet> data) {
        oldVetsData = data;
    }

    public static void setNewData(List<Vet> data) {
        newVetsData = data;
    }

    public int consistencyChecker() {
        Vet oldVet;
        Vet newVet;
        int atID;
        int inconsistency = 0;
        for(int index=0; index < oldVetsData.size(); index++) {
            oldVet = oldVetsData.get(index);
            newVet = newVetsData.get(index);
            if (oldVet.getId() == newVet.getId()) {
                atID = oldVet.getId();
                if (inconsistency != 0) {
                    System.out.println("OLD:" + oldVet);
                    System.out.println("NEW:" + newVet);
                }
                //need the number of columns (use hardcoded number or dynamically check the number of columns)
                //for Vet, 3 columns
                if (!oldVet.getFirstName().equals(newVet.getFirstName())) {
                    checkNewAndOldData(atID, oldVet.getFirstName(), newVet.getFirstName(), "first_name");
                    inconsistency++;
                }
                if (!oldVet.getLastName().equals(newVet.getLastName())) {
                    checkNewAndOldData(atID, oldVet.getLastName(), newVet.getLastName(), "last_name");
                    inconsistency++;
                }
            } else {
                System.out.println("Very inconsistent table (ID sequence not matching), please contact your DB admin: " + oldVet.getId() + " != " + newVet.getId());
            }
        }
        return inconsistency;
    }

    public double calculateConsistencyChecker(int inconsistency) {
        int sizeOfVets = oldVetsData.size();
        double consistency = (1 - (inconsistency/sizeOfVets))*100;
        return Double.parseDouble(String.format("%.2f", consistency));
    }

    private void checkNewAndOldData(int id, String oldData, String newData, String columnName){
        checkNewAndOldData(id,oldData,newData,columnName,"vets");
    }

    private void checkNewAndOldData(int id, String oldData, String newData, String columnName, String tableName) {
        if(!(oldData.equals(newData))) {
            printViolationMessage(id, oldData, newData);
            SQLiteDBConnector.getInstance().updateById(tableName,columnName, oldData, id);
        }
    }

    public void printViolationMessage(int id, String oldData, String newData) {
        System.out.println("The row " + id + " on the new database," +
                            " does not match: New(" + newData + 
                            " is not equal to Old(" + oldData);
    }


}