//package org.springframework.samples.petclinic.consistencychecker;
//
//import org.springframework.samples.petclinic.sqlite.SQLiteDBConnector;
//import org.springframework.samples.petclinic.vet.Specialty;
//
//import java.lang.reflect.Array;
//import java.util.HashMap;
//import java.util.List;
//
//public class VetSpecialityConsistencyChecker implements InConsistencyChecker{
//    private List<Array> oldVetSpecialityData;
//    private List<Array> newVetSpecialtyData;
//
//    public void setOldData(List<Array> oldVetSpecialityData){this.oldVetSpecialityData = oldVetSpecialityData;}
//    public void setNewData(List<Array>  newVetSpecialtyData){this.newVetSpecialtyData = newVetSpecialtyData;}
//
//    public int consistencyChecker(){
//        Array oldVetSpeciality = ();
//        Array newVetSpecialty;
//        int atID;
//        int inconsistency = 0;
//        for(int index=0; index < oldVetSpecialityData.size(); index++) {
//            oldVetSpeciality = oldVetSpecialityData.get(index);
//            newVetSpecialty = newVetSpecialtyData.get(index);
//            //need the number of columns (use hardcoded number or dynamically check the number of columns)
//            //for PetType,  columns 2
//            if(oldVetSpeciality.toString() != newVetSpecialty.toString()) {
//                atID = oldVetSpeciality.;
//                checkNewAndOldData(atID, oldVetSpeciality.getName(), newVetSpecialty.getName(),"name");
//                inconsistency++;
//            }
//        }
//        return inconsistency;
//    }
//
//    public double calculateConsistencyChecker(int inconsistency) {
//        int sizeOfSpecialty = oldSpecialityData.size();
//        double consistency = (1 - (inconsistency/sizeOfSpecialty))*100;
//        return Double.parseDouble(String.format("%.2f", consistency));
//    }
//
//    private void checkNewAndOldData(int id, String oldData, String newData, String columnName){
//        checkNewAndOldData(id,oldData,newData,columnName,"specialties");
//    }
//
//    private void checkNewAndOldData(int id, String oldData, String newData, String columnName, String tableName) {
//        if(oldData != newData) {
//            printViolationMessage(id, oldData, newData);
//            new SQLiteDBConnector().updateById(tableName,columnName, oldData, id);
//        }
//    }
//
//    public void printViolationMessage(int id, String oldData, String newData) {
//        System.out.println("The row " + id + " on the new database," +
//                " does not match: New(" + newData +
//                " is not equal to Old(" + oldData);
//    }
//
//
//}
