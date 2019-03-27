package org.springframework.samples.petclinic.consistencychecker;

import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.sqlite.SQLiteDBConnector;

import java.util.List;

public class OwnerConsistencyChecker implements InConsistencyChecker {

    private static List<Owner> oldOwnersData;
    private static List<Owner> newOwnersData;
    
    public static void setOldData(List<Owner> oldTableData) {
        // this.oldOwnersData = oldTableData;
        oldOwnersData = oldTableData;
    }

    public static void setNewData(List<Owner> newTableData) {
        // this.newOwnersData = newTableData;
        newOwnersData = newTableData;
    }

    public int consistencyChecker() {
        int inconsistency = 0;
        if (oldOwnersData.size() == newOwnersData.size()) {
            Owner oldOwner;
            Owner newOwner;
            int atID;
            for (int index = 0; index < oldOwnersData.size(); index++) {
                oldOwner = oldOwnersData.get(index);
                newOwner = newOwnersData.get(index);

                inconsistency += ownerCheckConsistency(oldOwner, newOwner);
                // if (oldOwner.getId() == newOwner.getId()) {
                //     atID = newOwner.getId();
                //     if (inconsistency != 0) {
                //         System.out.println("OLD:" + oldOwnersData.get(index));
                //         System.out.println("NEW:" + newOwnersData.get(index));
                //     }
                //     //need the number of columns (use hardcoded number or dynamically check the number of columns)
                //     //for Owner, 5 columns
                //     if (oldOwner.getId() != newOwner.getId()) {
                //         System.out.println("\t" + oldOwner.getId() + "!=" + newOwner.getId());
                //         inconsistency++;
                //     }
                //     if (!oldOwner.getFirstName().equals(newOwner.getFirstName())) {
                //         checkNewAndOldData(atID, oldOwner.getFirstName(), newOwner.getFirstName(), "first_name");
                //         inconsistency++;
                //     }
                //     if (!oldOwner.getLastName().equals(newOwner.getLastName())) {
                //         checkNewAndOldData(atID, oldOwner.getLastName(), newOwner.getLastName(), "last_name");
                //         inconsistency++;
                //     }
                //     if (!oldOwner.getAddress().equals(newOwner.getAddress())) {
                //         checkNewAndOldData(atID, oldOwner.getAddress(), newOwner.getAddress(), "address");
                //         inconsistency++;
                //     }
                //     if (!oldOwner.getCity().equals(newOwner.getCity())) {
                //         checkNewAndOldData(atID, oldOwner.getCity(), newOwner.getCity(), "city");
                //         inconsistency++;
                //     }
                //     if (!oldOwner.getTelephone().equals(newOwner.getTelephone())) {
                //         checkNewAndOldData(atID, oldOwner.getTelephone(), newOwner.getTelephone(), "telephone");
                //         inconsistency++;
                //     }
                // } else {
                //     System.out.println("Very inconsistent table (ID sequence not matching), please contact your DB admin: " + oldOwner.getId() + " != " + newOwner.getId());
                //     inconsistency++;
                // }
            }
        } else {
            System.out.println("Old and new DB table size don't match! " + oldOwnersData.size() + " != " + newOwnersData.size());
            inconsistency++;
        }
        return inconsistency;
    }

    public int ownerCheckConsistency(Owner oldOwner, Owner newOwner) {
        int inconsistency = 0;
        if (oldOwner.getId() == newOwner.getId()) {
            int atID = newOwner.getId();
            //need the number of columns (use hardcoded number or dynamically check the number of columns)
            //for Owner, 5 columns
            if (oldOwner.getId() != newOwner.getId()) {
                System.out.println("\t" + oldOwner.getId() + "!=" + newOwner.getId());
                inconsistency++;
            }
            if (!oldOwner.getFirstName().equals(newOwner.getFirstName())) {
                checkNewAndOldData(atID, oldOwner.getFirstName(), newOwner.getFirstName(), "first_name");
                inconsistency++;
            }
            if (!oldOwner.getLastName().equals(newOwner.getLastName())) {
                checkNewAndOldData(atID, oldOwner.getLastName(), newOwner.getLastName(), "last_name");
                inconsistency++;
            }
            if (!oldOwner.getAddress().equals(newOwner.getAddress())) {
                checkNewAndOldData(atID, oldOwner.getAddress(), newOwner.getAddress(), "address");
                inconsistency++;
            }
            if (!oldOwner.getCity().equals(newOwner.getCity())) {
                checkNewAndOldData(atID, oldOwner.getCity(), newOwner.getCity(), "city");
                inconsistency++;
            }
            if (!oldOwner.getTelephone().equals(newOwner.getTelephone())) {
                checkNewAndOldData(atID, oldOwner.getTelephone(), newOwner.getTelephone(), "telephone");
                inconsistency++;
            }
        } else {
            System.out.println("Very inconsistent table (ID sequence not matching), please contact your DB admin: " + oldOwner.getId() + " != " + newOwner.getId());
            inconsistency++;
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
        if(!(oldData.equals(newData))) {
            printViolationMessage(id, oldData, newData);
            SQLiteDBConnector.getInstance().updateById(tableName,columnName, oldData, id);
        }
    }

    public void printViolationMessage(int id, String oldData, String newData) {
        System.out.println("The row " + id + " on the new database," +
                            " does not match: New(" + newData + 
                            ") is not equal to Old(" + oldData + ")");
    }
}