package org.springframework.samples.petclinic.consistencychecker;

import org.springframework.samples.petclinic.vet.Specialty;
import org.springframework.samples.petclinic.sqlite.SQLiteDBConnector;

import java.util.List;

public class SpecialityConsistencyChecker implements InConsistencyChecker{
    private static List<Specialty> oldSpecialityData;
    private static List<Specialty> newSpecialtyData;

    public static void setOldData(List<Specialty> data){oldSpecialityData = data;}
    public static void setNewData(List<Specialty>  data){newSpecialtyData = data;}

    public int consistencyChecker(){
        Specialty oldSpeciality;
        Specialty newSpecialty;
        int atID;
        int inconsistency = 0;
        for(int index=0; index < oldSpecialityData.size(); index++) {
            oldSpeciality = oldSpecialityData.get(index);
            newSpecialty = newSpecialtyData.get(index);
            //need the number of columns (use hardcoded number or dynamically check the number of columns)
            //for PetType,  columns 2
            if(oldSpeciality.toString() != newSpecialty.toString()) {
                atID = newSpecialty.getId();
                checkNewAndOldData(atID, oldSpeciality.getName(), newSpecialty.getName(),"name");
                inconsistency++;
            }
        }
        return inconsistency;
    }

    public double calculateConsistencyChecker(int inconsistency) {
        int sizeOfSpecialty = oldSpecialityData.size();
        double consistency = (1 - (inconsistency/sizeOfSpecialty))*100;
        return Double.parseDouble(String.format("%.2f", consistency));
    }

    private void checkNewAndOldData(int id, String oldData, String newData, String columnName){
        checkNewAndOldData(id,oldData,newData,columnName,"specialties");
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
