package org.springframework.samples.petclinic.shadowRead;

import org.springframework.samples.petclinic.FeatureToggles.FeatureToggles;
import org.springframework.samples.petclinic.sqlite.SQLiteDBConnector;
import org.springframework.samples.petclinic.mysql.MySQLJDBCDriverConnection;
import org.springframework.samples.petclinic.sqlite.SQLiteResultSet;
import org.springframework.samples.petclinic.sqlite.SQLiteVisitHelper;
import org.springframework.samples.petclinic.visit.Visit;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class VisitShadowRead {
    private int readInconsistencies = 0;
    //int testC = 0;
    SQLiteDBConnector sqLiteDbConnector = SQLiteDBConnector.getInstance();
    MySQLJDBCDriverConnection mySQLJDBCDriverConnector = new MySQLJDBCDriverConnection();
    SQLiteVisitHelper sqLiteVisitHelper = SQLiteVisitHelper.getInstance();
    public int checkVisit(Visit oldVisit){
        System.out.println( " From Visit Shadow Read" + "  -start");
        ResultSet newVisitResult = SQLiteDBConnector.getInstance().selectById("visits", oldVisit.getId());
        int inconsistencyId = -1;
        ArrayList<HashMap> newVisitCorrectionPack = new ArrayList();
        List<Visit> newVisitList;
        boolean isInconsistent = false;
        String strInconsis = "";

        //sqLiteDbConnector.connect();
        try {
            newVisitList = SQLiteVisitHelper.getInstance().getModelList(newVisitResult);
            System.out.println(" From Visit Shadow Read" + "  -try");
            for (int i = 0; i < newVisitList.size(); i++) {
                System.out.println(" From Visit Shadow Read" + "  -while");
                String pet_id = newVisitList.get(i).getPetId().toString();
                System.out.println(" From Visit Shadow Read " + pet_id);
                System.out.println(" From Visit Shadow Read " + oldVisit.getPetId().toString());
                //turn off repository date correction toggle for new db date
                FeatureToggles.isEnableIncrementDate = false;
                String visit_date = newVisitList.get(i).getDate().toString();
                //turn the toggle back on
                FeatureToggles.isEnableIncrementDate = true;
                System.out.println(" From Visit Shadow Read " + visit_date);
                System.out.println(" From Visit Shadow Read " + oldVisit.getDate().toString());
                String description = newVisitList.get(i).getDescription();
                System.out.println(" From Visit Shadow Read " + description);
                System.out.println(" From Visit Shadow Read " + oldVisit.getDescription());
                HashMap inconsistencyRow = new HashMap();
                inconsistencyRow.put("id", oldVisit.getId());
                inconsistencyRow.put("pet_id", null);
                inconsistencyRow.put("visit_date", null);
                inconsistencyRow.put("description", null);
                System.out.println(" From Visit Shadow Read" + "  -pass check 1");
                if (!oldVisit.getPetId().toString().equals(pet_id)) {
                    isInconsistent = true;

                    strInconsis += "Shadow Read Inconsistency found: From table visits, at id " + oldVisit.getId() +
                            ", at column pet_id: "
                            + oldVisit.getPetId().toString() +
                            " | " + pet_id + " \n";

                    inconsistencyRow.replace("pet_id", oldVisit.getPetId());
                }

                System.out.println(" From Visit Shadow Read" + "  -pass check 2");
                if (!oldVisit.getDate().toString().equals(visit_date)) {
                    isInconsistent = true;
                    strInconsis += "Shadow Read Inconsistency found: From table visits, at id " + oldVisit.getId() +
                            ", at column visit_date: "
                            + oldVisit.getDate().toString() +
                            " | " + visit_date + " \n";

                    //System.out.println(strInconsis);
                    //SQLiteDBConnector.getInstance().updateById("visits","visit_date",oldVisit.getDate().toString(),oldVisit.getId());
                    inconsistencyRow.replace("visit_date", oldVisit.getDate().toString());
                }
                if (!oldVisit.getDescription().equals(description)) {
                    isInconsistent = true;

                    strInconsis += "Shadow Read Inconsistency found: From table visits, at id " + oldVisit.getId() +
                            ", at column description: " + oldVisit.getDescription() +
                            " | " + description + " \n";
                    //System.out.println(strInconsis);
                    //SQLiteDBConnector.getInstance().updateById("visits","description",oldVisit.getDescription(),oldVisit.getId());
                    inconsistencyRow.replace("description", oldVisit.getDescription());
                }

                if (isInconsistent) {
                    readInconsistencies++;
                    System.out.println(strInconsis);
                    System.out.println("Shadow Read Inconsistency count: " + readInconsistencies + " From Visit Shadow Read");
                    newVisitCorrectionPack.add(inconsistencyRow);
                    inconsistencyId = oldVisit.getId();
                } else {
                    System.out.println("Shadow Read Inconsistency count: " + readInconsistencies + " From Visit Shadow Read");
                    System.out.println("Shadow Read successfully From Visit Shadow Read");
                }
            }
            System.out.println("done while loop From Visit Shadow Read");
//            for(int i = 0; i<newVisitCorrectionPack.size();i++)
//            {
//                System.out.println( "start updating From Visit Shadow Read");
//                int id = Integer.parseInt(newVisitCorrectionPack.get(i).get("id").toString());
//                if(newVisitCorrectionPack.get(i).get("pet_id") != null) {
//
//                    sqLiteVisitHelper.updateColById("pet_id", newVisitCorrectionPack.get(i).get("pet_id").toString(), id);
//                }
//                if(newVisitCorrectionPack.get(i).get("visit_date") != null) {
//
//                    sqLiteVisitHelper.updateColById("visit_date", newVisitCorrectionPack.get(i).get("visit_date").toString(), id);
//                }
//                if(newVisitCorrectionPack.get(i).get("description") != null) {
//
//                    sqLiteVisitHelper.updateColById("description", newVisitCorrectionPack.get(i).get("description").toString(), id);
//                }
//              }

        }catch (Exception e){
            System.out.println(e.getMessage() + " Error From Visit Shadow Read");
        }
        return inconsistencyId;
    }

    public int getReadInconsistencies() {
        return readInconsistencies;
    }


}
