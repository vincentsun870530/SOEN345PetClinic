package org.springframework.samples.petclinic.consistencychecker;

import java.time.LocalDate;
import java.util.List;

import org.springframework.samples.petclinic.sqlite.SQLiteDBConnector;
import org.springframework.samples.petclinic.visit.Visit;

public class VisitConsistencyChecker implements InConsistencyChecker {

    private static List<Visit> oldVisitsData;
    private static List<Visit> newVisitsData;
    
    public static void setOldData(List<Visit> oldTableData) {
        // this.oldVisitsData = oldTableData;
        oldVisitsData = oldTableData;
    }

    public static void setNewData(List<Visit> newTableData) {
        // this.newVisitsData = newTableData;
        newVisitsData = newTableData;
    }

    public int consistencyChecker() {
        int inconsistency = 0;
        if (oldVisitsData.size() == newVisitsData.size()) {
            Visit oldVisit;
            Visit newVisit;
            int atID;
            for (int index = 0; index < oldVisitsData.size(); index++) {
                oldVisit = oldVisitsData.get(index);
                newVisit = newVisitsData.get(index);

                inconsistency += visitCheckConsistency(oldVisit, newVisit);
                // if (newVisit.getId() == oldVisit.getId()) {
                //     atID = newVisit.getId();
                //     if (inconsistency != 0) {
                //         System.out.println("OLD:" + oldVisit);
                //         System.out.println("NEW:" + newVisit);
                //     }
                //     //need the number of columns (use hardcoded number or dynamically check the number of columns)
                //     //for Owner,  columns
                //     if (!oldVisit.getDescription().equals(newVisit.getDescription())) {
                //         checkNewAndOldData(atID, oldVisit.getDescription(), newVisit.getDescription(), "description");
                //         inconsistency++;
                //     }
                //     if (!oldVisit.getDate().equals(newVisit.getDate())) {
                //         checkDateNewAndOldData(atID, oldVisit.getDate(), newVisit.getDate(), "visit_date");
                //         inconsistency++;
                //     }
                //     if (!oldVisit.getPetId().equals(newVisit.getPetId())) {
                //         checkNewAndOldData(atID, oldVisit.getPetId().toString(), newVisit.getPetId().toString(), "pet_id");
                //         inconsistency++;
                //     }
                // } else {
                //     System.out.println("Very inconsistent table (ID sequence not matching), please contact your DB admin: " + oldVisit.getId() + " != " + newVisit.getId());
                //     inconsistency++;
                // }
            }
        } else {
            System.out.println("Old and new DB table size don't match! " + oldVisitsData.size() + " != " + newVisitsData.size());
            inconsistency++;
        }
        return inconsistency;
    }

    public int visitCheckConsistency(Visit oldVisit, Visit newVisit) {
       int inconsistency = 0;
       try{
       if (newVisit.getId() == oldVisit.getId()) {
            int atID = newVisit.getId();
            if (inconsistency != 0) {
                System.out.println("OLD:" + oldVisit);
                System.out.println("NEW:" + newVisit);
            }
            //need the number of columns (use hardcoded number or dynamically check the number of columns)
            //for Owner,  columns
            if (!oldVisit.getDescription().equals(newVisit.getDescription())) {
                checkNewAndOldData(atID, oldVisit.getDescription(), newVisit.getDescription(), "description");
                inconsistency++;
            }
            if (!oldVisit.getDate().equals(newVisit.getDate())) {
                checkDateNewAndOldData(atID, oldVisit.getDate(), newVisit.getDate(), "visit_date");
                inconsistency++;
            }
            if (!oldVisit.getPetId().equals(newVisit.getPetId())) {
                checkNewAndOldData(atID, oldVisit.getPetId().toString(), newVisit.getPetId().toString(), "pet_id");
                inconsistency++;
            }
        } else {
            System.out.println("Very inconsistent table (ID sequence not matching), please contact your DB admin: " + oldVisit.getId() + " != " + newVisit.getId());
            inconsistency++;
        }}catch (Exception e){
           System.out.println(e.getMessage());
       }
        return inconsistency;
    }


    public double calculateConsistencyChecker(int inconsistency) {
        int sizeOfVisits = oldVisitsData.size();
        double consistency = (1 - (inconsistency/sizeOfVisits))*100;
        return Double.parseDouble(String.format("%.2f", consistency));
    }

    public int numberOfRows() {
        return oldVisitsData.size();
    }

    private void checkNewAndOldData(int id, String oldData, String newData, String columnName){
        checkNewAndOldData(id,oldData,newData,columnName,"visits");
    }

    private void checkNewAndOldData(int id, String oldData, String newData, String columnName, String tableName) {
        if(!(oldData.equals(newData))) {
            printViolationMessage(id, oldData, newData);
            SQLiteDBConnector.getInstance().updateById(tableName,columnName, oldData, id);
        }
    }

    private void checkDateNewAndOldData(int id, LocalDate oldDate, LocalDate newDate, String columnName) {
        checkDateNewAndOldData(id,oldDate,newDate,columnName,"visits");
    }

    private void checkDateNewAndOldData(int id, LocalDate oldDate, LocalDate newDate, String columnName, String tableName) {
        if(oldDate.isEqual(newDate) == false) {
            printViolationMessage(id, oldDate.toString(), newDate.toString());

            SQLiteDBConnector.getInstance().updateById(tableName,columnName, oldDate.toString(), id);

        }
    }

//    private void checkIDNewAndOldData(int id, int oldId, int newId) {
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