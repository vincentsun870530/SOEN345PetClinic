package org.springframework.samples.petclinic.shadowRead;

import org.springframework.samples.petclinic.sqlite.SQLiteDBConnector;
import org.springframework.samples.petclinic.mysql.MySQLJDBCDriverConnection;
import org.springframework.samples.petclinic.sqlite.SQLiteResultSet;
import org.springframework.samples.petclinic.visit.Visit;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class VisitShadowRead {
    private int readInconsistencies = 0;
    //int testC = 0;
    SQLiteDBConnector sqLiteDbConnector = SQLiteDBConnector.getInstance();
    MySQLJDBCDriverConnection mySQLJDBCDriverConnector = new MySQLJDBCDriverConnection();
    //private Visit oldVisit;
    //private Visit newVisit;

    public void checkVisit(Visit oldVisit){
        System.out.println( " From Visit Shadow Read" + "  -start");
        ResultSet newVisitResult = SQLiteDBConnector.getInstance().selectById("visits", oldVisit.getId());

        ArrayList<HashMap> newVisitPack = new ArrayList();
        List<ArrayList> newVisitList = null;

        boolean isInconsistent = false;
        String strInconsis = "";

        //sqLiteDbConnector.connect();
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
                HashMap inconsistencyRow = new HashMap();
                inconsistencyRow.put("id",oldVisit.getId());
                inconsistencyRow.put("pet_id",null);
                inconsistencyRow.put("visit_date",null);
                inconsistencyRow.put("description",null);
                System.out.println( " From Visit Shadow Read" + "  -pass check 1");
                if(!oldVisit.getPetId().toString().equals(pet_id)){
                    isInconsistent=true;

                    strInconsis += "Shadow Read Inconsistency found: From table visits, at id "+oldVisit.getId()+
                            ", at column pet_id: "
                            + oldVisit.getPetId().toString() +
                            " | " + pet_id + " \n";

                    inconsistencyRow.replace("pet_id",oldVisit.getPetId());
                }

                System.out.println( " From Visit Shadow Read" + "  -pass check 2");
                if(!oldVisit.getDate().toString().equals(visit_date)){
                    isInconsistent=true;
                    strInconsis += "Shadow Read Inconsistency found: From table visits, at id "+oldVisit.getId()+
                            ", at column visit_date: "
                            + oldVisit.getDate().toString() +
                            " | " + visit_date + " \n";

                    //System.out.println(strInconsis);
                    //SQLiteDBConnector.getInstance().updateById("visits","visit_date",oldVisit.getDate().toString(),oldVisit.getId());
                    inconsistencyRow.replace("visit_date",oldVisit.getDate().toString());
                }
                if(!oldVisit.getDescription().equals(description)){
                    isInconsistent=true;

                    strInconsis += "Shadow Read Inconsistency found: From table visits, at id "+oldVisit.getId()+
                            ", at column description: " + oldVisit.getDescription() +
                            " | " + description + " \n";
                    //System.out.println(strInconsis);
                    //SQLiteDBConnector.getInstance().updateById("visits","description",oldVisit.getDescription(),oldVisit.getId());
                    inconsistencyRow.replace("description",oldVisit.getDescription());
                }

                if(isInconsistent){
                    readInconsistencies++;
                    System.out.println(strInconsis);
                    System.out.println( "Shadow Read Inconsistency count: " + readInconsistencies + " From Visit Shadow Read");
                    newVisitPack.add(inconsistencyRow);
                }else {
                    System.out.println( "Shadow Read Inconsistency count: " + readInconsistencies + " From Visit Shadow Read");
                    System.out.println( "Shadow Read successfully From Visit Shadow Read");
                }
            }
            System.out.println( "done while loop From Visit Shadow Read");
            for(int i = 0; i<newVisitPack.size();i++)
            {
                System.out.println( "start updating From Visit Shadow Read");
                int id = Integer.parseInt(newVisitPack.get(i).get("id").toString());
                if(newVisitPack.get(i).get("pet_id") != null) {

                    sqLiteDbConnector.updateById("visits", "pet_id", newVisitPack.get(i).get("pet_id").toString(), id);
                }
                if(newVisitPack.get(i).get("visit_date") != null) {

                    sqLiteDbConnector.updateById("visits", "visit_date", newVisitPack.get(i).get("visit_date").toString(), id);
                }
                if(newVisitPack.get(i).get("description") != null) {

                    sqLiteDbConnector.updateById("visits", "description", newVisitPack.get(i).get("description").toString(), id);
                }
            }

        }catch (Exception e){
            System.out.println(e.getMessage() + " Error From Visit Shadow Read");
        }

    }

    public int getReadInconsistencies() {
        return readInconsistencies;
    }
    

}
