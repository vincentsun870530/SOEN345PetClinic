package org.springframework.samples.petclinic.consistencychecker;

import java.sql.ResultSet;
import java.util.List;

import org.springframework.samples.petclinic.owner.PetType;
import org.springframework.samples.petclinic.sqlite.SQLiteDBConnector;

public class TypeConsistencyChecker implements InConsistencyChecker{
    private static List<PetType> oldPetTypeData;
    private static List<PetType> newPetTypeData;

    public static void setOldData(List<PetType> data){ oldPetTypeData = data;}
    public static void setNewData(List<PetType>  data){ newPetTypeData = data;}

    public int consistencyChecker(){
        int inconsistency = 0;
        if (oldPetTypeData.size() == newPetTypeData.size()) {
            PetType oldPetType;
            PetType newPetType;
            int atID;
            for (int index = 0; index < oldPetTypeData.size(); index++) {
                oldPetType = oldPetTypeData.get(index);
                newPetType = newPetTypeData.get(index);
                if (oldPetType.getId() == newPetType.getId()) {
                    atID = newPetType.getId();
                    if (inconsistency != 0) {
                        System.out.println("OLD:" + oldPetType);
                        System.out.println("NEW:" + newPetType);
                    }
                    //need the number of columns (use hardcoded number or dynamically check the number of columns)
                    //for PetType,  columns 2
                    if (!oldPetType.getName().equals(newPetType.getName())) {
                        checkNewAndOldData(atID, oldPetType.getName(), newPetType.getName(), "name");
                        inconsistency++;
                    }
                } else {
                    System.out.println("Very inconsistent table (ID sequence not matching), please contact your DB admin: " + oldPetType.getId() + " != " + newPetType.getId());
                    inconsistency++;
                }
            }
        } else {
            System.out.println("Old and new DB table size don't match! " + oldPetTypeData.size() + " != " + newPetTypeData.size());
            inconsistency++;
        }
        return inconsistency;
    }

    public double calculateConsistencyChecker(int inconsistency) {
        int sizeOfPetType = oldPetTypeData.size();
        double consistency = (1 - (inconsistency/sizeOfPetType))*100;
        return Double.parseDouble(String.format("%.2f", consistency));
    }

    private void checkNewAndOldData(int id, String oldData, String newData, String columnName){
        checkNewAndOldData(id,oldData,newData,columnName,"types");
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
