package org.springframework.samples.petclinic.consistencychecker;

import java.time.LocalDate;
import java.util.List;

import org.springframework.samples.petclinic.visit.Visit;

public class VisitConsistencyChecker implements InConsistencyChecker {

    private List<Visit> oldVisitsData;
    private List<Visit> newVisitsData;
    
    public void setOldData(List<Visit> oldTableData) {
        this.oldVisitsData = oldTableData;
    }

    public void setNewData(List<Visit> newTableData) {
        this.newVisitsData = newTableData;
    }

    public int consistencyChecker() {
        Visit oldVisit;
        Visit newVisit;
        int atID;
        int inconsistency = 0;
        for(int index=0; index < oldVisitsData.size(); index++) {
            oldVisit = oldVisitsData.get(index);
            newVisit = newVisitsData.get(index);
            //need the number of columns (use hardcoded number or dynamically check the number of columns)
            //for Owner,  columns
            if(oldVisit.toString() != newVisit.toString()) {
                atID = newVisit.getId();
                checkNewAndOldData(atID, oldVisit.getDescription(), newVisit.getDescription());
                checkDateNewAndOldData(atID, oldVisit.getDate(), newVisit.getDate());
                checkIDNewAndOldData(atID, oldVisit.getPetId(), newVisit.getPetId());
                inconsistency++;
            }   
        }
        return inconsistency;
    }

    public double calculateConsistencyChecker(int inconsistency) {
        int sizeOfOwners = oldVisitsData.size();
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