package org.springframework.samples.petclinic.consistencychecker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.samples.petclinic.mysql.MySQLJDBCDriverConnection;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.sqlite.SQLiteDBConnector;

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
                checkNewAndOldData(atID, oldOwner.getFirstName(), newOwner.getFirstName(), "first_name");
                checkNewAndOldData(atID, oldOwner.getLastName(), newOwner.getLastName(), "last_name");
                checkNewAndOldData(atID, oldOwner.getAddress(), newOwner.getAddress(), "address");
                checkNewAndOldData(atID, oldOwner.getCity(), newOwner.getCity(), "city");
                checkNewAndOldData(atID, oldOwner.getTelephone(), newOwner.getTelephone(), "telephone");
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

    private void checkNewAndOldData(int id, String oldData, String newData, String columnName){
        checkNewAndOldData(id,oldData,newData,columnName,"owners");
    }

    private void checkNewAndOldData(int id, String oldData, String newData, String columnName, String tableName) {
        if(oldData != newData) {
            printViolationMessage(id, oldData, newData);
            new SQLiteDBConnector().updateById(tableName,columnName, oldData, id);
            //MySQLJDBCDriverConnection.updateRow(id, "owners", columnName, oldData);

        }
    }

    public void printViolationMessage(int id, String oldData, String newData) {
        System.out.println("The row " + id + " on the new database," +
                            " does not match: New(" + newData + 
                            " is not equal to Old(" + oldData);
    }

    // public static void main(String[] args) {
    //     OwnerConsistencyChecker occ = new OwnerConsistencyChecker();
    //     occ.consistencyChecker();
    // }


}