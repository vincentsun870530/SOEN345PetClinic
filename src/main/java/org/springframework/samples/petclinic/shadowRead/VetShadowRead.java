package org.springframework.samples.petclinic.shadowRead;

import org.springframework.samples.petclinic.FeatureToggles.FeatureToggles;
import org.springframework.samples.petclinic.mysql.MySQLJDBCDriverConnection;
import org.springframework.samples.petclinic.sqlite.SQLiteDBConnector;
import org.springframework.samples.petclinic.sqlite.SQLiteVetHelper;
import org.springframework.samples.petclinic.vet.Vet;


import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VetShadowRead {
    private int readInconsistencies = 0;
    SQLiteDBConnector sqLiteDbConnector = SQLiteDBConnector.getInstance();
    MySQLJDBCDriverConnection mySQLJDBCDriverConnector = new MySQLJDBCDriverConnection();
    SQLiteVetHelper sqLiteVetHelper = SQLiteVetHelper.getInstance();

    public int checkVet(Vet oldVet){
        System.out.println( " From Vet Shadow Read" + "  -start");

        ResultSet newVetResult = SQLiteDBConnector.getInstance().selectById("vet", oldVet.getId());
        int inconsistencyId = -1;
        ArrayList<HashMap> newVetCorrectionPack = new ArrayList();
        List<Vet> newVetList;
        boolean isInconsistent = false;
        String strInconsis = "";

        try {
            newVetList =SQLiteVetHelper.getInstance().getModelList(newVetResult);
            System.out.println(" From Vet Shadow Read" + "  -try");
            for (int i = 0; i < newVetList.size(); i++) {
                System.out.println(" From Vet Shadow Read" + "  -while");

                String first_name = newVetList.get(i).getFirstName();
                System.out.println(" From Vet Shadow Read " + first_name);
                System.out.println(" From Vet Shadow Read " + oldVet.getFirstName());



                String last_name = newVetList.get(i).getLastName();
                System.out.println(" From Vet Shadow Read " + last_name);
                System.out.println(" From Vet Shadow Read " + oldVet.getLastName());

                HashMap inconsistencyRow = new HashMap();
                inconsistencyRow.put("id", oldVet.getId());
                inconsistencyRow.put("first_name", null);
                inconsistencyRow.put("last_name", null);
                System.out.println(" From Vet Shadow Read" + "  -pass check 1");

                if (!oldVet.getLastName().equals(last_name)) {
                    isInconsistent = true;

                    strInconsis += "Shadow Read Inconsistency found: From table vets, at id " + oldVet.getId() +
                            ", at column last_name: "
                            + oldVet.getLastName() +
                            " | " + last_name + " \n";

                    inconsistencyRow.replace("last_name", oldVet.getLastName());
                }

                System.out.println(" From Vet Shadow Read" + "  -pass check 2");

                if (!oldVet.getFirstName().equals(first_name)) {
                    isInconsistent = true;

                    strInconsis += "Shadow Read Inconsistency found: From table vets, at id " + oldVet.getId() +
                            ", at column last_name: " + oldVet.getFirstName() +
                            " | " + first_name + " \n";
                   inconsistencyRow.replace("first_name", oldVet.getFirstName());
                }

                if (isInconsistent) {
                    readInconsistencies++;
                    System.out.println(strInconsis);
                    System.out.println("Shadow Read Inconsistency count: " + readInconsistencies + " From Vet Shadow Read");
                    newVetCorrectionPack.add(inconsistencyRow);
                    inconsistencyId = oldVet.getId();
                } else {
                    System.out.println("Shadow Read Inconsistency count: " + readInconsistencies + " From Vet Shadow Read");
                    System.out.println("Shadow Read successfully From Vet Shadow Read");
                }
            }






        } catch (Exception e){
        System.out.println(e.getMessage() + " Error From Vet Shadow Read");
    }
        return inconsistencyId;









    }

}
