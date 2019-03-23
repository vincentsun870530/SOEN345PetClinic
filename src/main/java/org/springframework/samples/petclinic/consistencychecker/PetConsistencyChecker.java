package org.springframework.samples.petclinic.consistencychecker;

import java.time.LocalDate;
import java.util.List;

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
                checkNewAndOldData(atID, oldPet.getName(), newPet.getName());
                checkDateNewAndOldData(atID, oldPet.getBirthDate(), newPet.getBirthDate());
                checkIDNewAndOldData(atID, oldPet.getType().getId(), newPet.getOwner().getId());
                inconsistency++;
            }   
        }
        return inconsistency;
    }

    public double calculateConsistencyChecker(int inconsistency) {
        int sizeOfOwners = oldPetsData.size();
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

    private void checkDateNewAndOldData(int id, LocalDate oldDate, LocalDate newDate) {
        if(oldDate.isEqual(newDate) == false) {
            printViolationMessage(id, oldDate.toString(), newDate.toString());

            // TODO update the new database
            // INSERT CODE HER FOR UPDATING TO THE NEW DATABASE

        }
    }

    private void checkIDNewAndOldData(int id, int oldId, int newId) {
        if((oldId == newId) == false) {
            printViolationMessage(id, Integer.toString(oldId), Integer.toString(newId));

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