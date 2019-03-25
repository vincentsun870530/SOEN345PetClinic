package org.springframework.samples.petclinic.shadowRead;

import org.springframework.samples.petclinic.sqlite.SQLiteDBConnector;
import org.springframework.samples.petclinic.visit.Visit;

import java.sql.ResultSet;
import java.sql.SQLException;

public class VisitShadowRead {
    private int readInconsistencies = 0;
    int testC = 0;
    SQLiteDBConnector sqLiteDbConnector = SQLiteDBConnector.getInstance();

    //private Visit oldVisit;
    //private Visit newVisit;

    public void checkVisit(Visit oldVisit){
        System.out.println( " From Visit Shadow Read" + "  -start");
        ResultSet newVisitResult = SQLiteDBConnector.getInstance().selectById("visits", oldVisit.getId());
        System.out.println( " From Visit Shadow Read" + "  -start2");
        //SQLiteResultSet.getVisits(newVisitResult);
        boolean isInconsistent = false;
        String strInconsis = "";
        try{
            System.out.println( " From Visit Shadow Read" + "  -try");
            while(newVisitResult.next()){
                System.out.println( " From Visit Shadow Read" + "  -while");
                String pet_id = newVisitResult.getString("pet_id");
                System.out.println( " From Visit Shadow Read " + pet_id);
                System.out.println( " From Visit Shadow Read " + oldVisit.getPetId().toString());
                String visit_date = newVisitResult.getString("visit_date");
                System.out.println( " From Visit Shadow Read " + visit_date);
                System.out.println( " From Visit Shadow Read " + oldVisit.getDate().toString());
                String description = newVisitResult.getString("description");
                System.out.println( " From Visit Shadow Read " + description);
                System.out.println( " From Visit Shadow Read " + oldVisit.getDescription());
                testC++;
                if(!oldVisit.getPetId().toString().equals(pet_id)){
                    isInconsistent=true;

                    strInconsis += "Shadow Read Inconsistency found: From table visits, at id "+oldVisit.getId()+
                            ", at column pet_id: "
                            + oldVisit.getPetId().toString() +
                            " | " + pet_id + " \n";
                    //System.out.println(strInconsis);
                    sqLiteDbConnector.updateById("visits","pet_id",oldVisit.getPetId().toString(),oldVisit.getId());
                }
                if(!oldVisit.getDate().toString().equals(visit_date)){
                    isInconsistent=true;
                    sqLiteDbConnector.updateById("visits","visit_date",oldVisit.getDate().toString(),oldVisit.getId());
                    strInconsis += "Shadow Read Inconsistency found: From table visits, at id "+oldVisit.getId()+
                            ", at column visit_date: "
                            + oldVisit.getDate().toString() +
                            " | " + visit_date + " \n";

                    //System.out.println(strInconsis);
                    sqLiteDbConnector.updateById("visits","visit_date",oldVisit.getDate().toString(),oldVisit.getId());
                }
                if(!oldVisit.getDescription().equals(description)){
                    isInconsistent=true;

                    strInconsis += "Shadow Read Inconsistency found: From table visits, at id "+oldVisit.getId()+
                            ", at column visit_date: " + oldVisit.getDescription() +
                            " | " + description + " \n";
                    //System.out.println(strInconsis);
                    sqLiteDbConnector.updateById("visits","description",oldVisit.getDescription(),oldVisit.getId());
                }

                if(isInconsistent){
                    readInconsistencies++;
                    System.out.println(strInconsis);
                    System.out.println( "Shadow Read Inconsistency count: " + readInconsistencies + " From Visit Shadow Read");
                }else {
                    System.out.println( "Shadow Read Inconsistency count: " + readInconsistencies + " From Visit Shadow Read");
                    System.out.println( "Shadow Read successfully From Visit Shadow Read");
                }
            }
        }catch (SQLException e){
            System.out.println(e.getMessage() + " Error From Visit Shadow Read");
        }

    }

    public int getReadInconsistencies() {
        return readInconsistencies;
    }
    

}
