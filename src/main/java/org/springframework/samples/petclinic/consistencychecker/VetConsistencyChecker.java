package org.springframework.samples.petclinic.consistencychecker;

import java.util.List;

import org.springframework.samples.petclinic.sqlite.SQLiteDBConnector;
import org.springframework.samples.petclinic.vet.Vet;

public class VetConsistencyChecker implements InConsistencyChecker {

    private List<Vet> oldVetsData;
    private List<Vet> newVetsData;
    
    public void setOldData(List<Vet> oldTableData) {
        this.oldVetsData = oldTableData;
    }

    public void setNewData(List<Vet> newTableData) {
        this.newVetsData = newTableData;
    }

    public int consistencyChecker() {
        Vet oldVet;
        Vet newVet;
        int atID;
        int inconsistency = 0;
        for(int index=0; index < oldVetsData.size(); index++) {
            oldVet = oldVetsData.get(index);
            newVet = newVetsData.get(index);
            //need the number of columns (use hardcoded number or dynamically check the number of columns)
            //for Vet, 3 columns
            if(oldVet.toString() != newVet.toString()) {
                atID = newVet.getId();
                checkNewAndOldData(atID, oldVet.getFirstName(), newVet.getFirstName(),"first_name");
                checkNewAndOldData(atID, oldVet.getLastName(), newVet.getLastName(),"last_name");
                inconsistency++;
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
        if(oldData != newData) {
            printViolationMessage(id, oldData, newData);
            new SQLiteDBConnector().updateById(tableName,columnName, oldData, id);
        }
    }

    public void printViolationMessage(int id, String oldData, String newData) {
        System.out.println("The row " + id + " on the new database," +
                            " does not match: New(" + newData + 
                            " is not equal to Old(" + oldData);
    }


}