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
        PetType oldPetType;
        PetType newPetType;
        int atID;
        int inconsistency = 0;
        for(int index=0; index < oldPetTypeData.size(); index++) {
            oldPetType = oldPetTypeData.get(index);
            newPetType = newPetTypeData.get(index);
            //need the number of columns (use hardcoded number or dynamically check the number of columns)
            //for PetType,  columns 2
            if(!oldPetType.toString().equals(newPetType.toString())) {
                atID = newPetType.getId();
                checkNewAndOldData(atID, oldPetType.getName(), newPetType.getName(),"name");
                inconsistency++;
            }
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
