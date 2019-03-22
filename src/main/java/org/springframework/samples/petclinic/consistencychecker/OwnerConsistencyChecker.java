package org.springframework.samples.petclinic.consistencychecker;

import java.util.List;

import org.springframework.samples.petclinic.owner.Owner;

public class OwnerConsistencyChecker implements InConsistencyChecker {

    private List<Owner> oldOwnersData;
    private List<Owner> newOwnersData;
    
    public void setOldData(List<Owner> oldTableData) {
        this.oldOwnersData = oldTableData;
    }

    public void setNewData(List<Owner> newTableData) {
        this.newOwnersData = newTableData;
    }

    public int consistencyChecker() {
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
                checkNewAndOldData(atID, oldOwner.getFirstName(), newOwner.getFirstName());
                checkNewAndOldData(atID, oldOwner.getLastName(), newOwner.getLastName());
                checkNewAndOldData(atID, oldOwner.getTelephone(), newOwner.getTelephone());
                checkNewAndOldData(atID, oldOwner.getAddress(), newOwner.getAddress());
                checkNewAndOldData(atID, oldOwner.getCity(), newOwner.getCity());
                inconsistency++;
            }   
        }
        return inconsistency;
    }

    public double calculateConsistencyChecker(int inconsistency) {
        int sizeOfOwners = oldOwnersData.size();
        double consistency = (1 - (inconsistency/sizeOfOwners))*100;
        return Double.parseDouble(String.format("%.2f", consistency));
    }

    private void checkNewAndOldData(int id, String oldData, String newData) {
        if(oldData != newData) {
            printViolationMessage(id, oldData, newData);

            // TODO update the new database
            // INSERT CODE HER FOR UPDATING TO THE NEW DATABASE

        }
    }

    public void printViolationMessage(int id, String oldData, String newData) {
        System.out.println("The row " + id + " on the new database," +
                            " does not match: New(" + newData + 
                            " is not equal to Old(" + oldData);
    }


}