package org.springframework.samples.petclinic.consistencychecker;

import java.util.List;

import org.springframework.samples.petclinic.owner.Owner;

public class ForkliftConsistencyChecker implements InConsistencyChecker {

    private List<Owner> oldOwnersData;
    private List<Owner> newOwnersData;
    
    public void setOldData(List<Owner> oldTableData) {
        this.oldOwnersData = oldTableData;
    }

    public void setNewData(List<Owner> newTableData) {
        this.newOwnersData = newTableData;
    }

    public int consistencyChecker() {
        //TODO work on the method
        Owner oldOwner;
        Owner newOwner;
        int atID;
        int inconsistency = 0;
        for(int index=0; index < oldOwnersData.size(); index++) {
            oldOwner = oldOwnersData.get(index);
            newOwner = newOwnersData.get(index);
            //need the number of columns (use hardcoded number or dynamically check the number of columns)
            //for Owner, 5 columns
            if(oldOwner.toString() != newOwner.toString()) {
                atID = newOwner.getId();
                checkFirstName(atID, oldOwner.getFirstName(), newOwner.getFirstName());
                // checkLastName(atID, oldOwner.getLastName(), newOwner.getLastName());
                // checkTelephone(atID, oldOwner.getTelephone(), newOwner.getTelephone());
                // checkAddress(atID, oldOwner.getAddress(), newOwner.getAddress());
                // checkCity(atID, oldOwner.getCity(), newOwner.getCity());
                inconsistency++;
            }   
        }
        return inconsistency;
    }

    public double calculateConsistencyChecker(int inconsistency) {
        //If doing for the owner's address record
        //List<Owner> owners = //retrieve owners information from database
        int sizeOfOwners = 10; //owners.size()
        double consistency = (1 - (inconsistency/sizeOfOwners))*100;
        return Double.parseDouble(String.format("%.2f", consistency));
    }

    private void checkFirstName(int id, String oldFirstName, String newFirstName) {
        if(oldFirstName != newFirstName) {
            printViolationMessage(id, oldFirstName, newFirstName);
        }
    }

    public void printViolationMessage(int id, String oldData, String newData) {
        System.out.println("The row " + id + " on the new database," +
                            " does not match: New(" + newData + 
                            " is not equal to Old(" + oldData);
    }
}