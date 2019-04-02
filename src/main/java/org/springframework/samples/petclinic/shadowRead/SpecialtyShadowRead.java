package org.springframework.samples.petclinic.shadowRead;

import org.springframework.samples.petclinic.FeatureToggles.FeatureToggles;
import org.springframework.samples.petclinic.mysql.MySQLJDBCDriverConnection;
import org.springframework.samples.petclinic.sqlite.SQLiteDBConnector;
import org.springframework.samples.petclinic.sqlite.SQLiteVetHelper;
import org.springframework.samples.petclinic.vet.Specialty;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SpecialtyShadowRead {

    private int readInconsistencies = 0;
    SQLiteDBConnector sqLiteDbConnector = SQLiteDBConnector.getInstance();
    MySQLJDBCDriverConnection mySQLJDBCDriverConnector = new MySQLJDBCDriverConnection();
    SQLiteVetHelper sqLiteVetHelper = SQLiteVetHelper.getInstance();


    public int checkSpecialty(Specialty oldSpecialty)  {
        System.out.println( " From Specialty Shadow Read" + "  -start");

        ResultSet newSpecialtyResult = SQLiteDBConnector.getInstance().selectById("specialties", oldSpecialty.getId());
        int inconsistencyId = -1;
        List<Specialty> newSpecialtyList;
        boolean isInconsistent = false;
        String strInconsis = "";

        try {
            newSpecialtyList =SQLiteVetHelper.getInstance().getSpecialtyModelList(newSpecialtyResult);
            System.out.println(" From Specialty Shadow Read" + "  -try");
            for (int i = 0; i < newSpecialtyList.size(); i++) {
                System.out.println(" From Specialty Shadow Read" + "  -while");

                String name = newSpecialtyList.get(i).getName();
                System.out.println(" From Specialty Shadow Read " + name);
                System.out.println(" From Specialty Shadow Read " + oldSpecialty.getName());

                System.out.println(" From Specialty Shadow Read" + "  -pass check 1");

                if (!oldSpecialty.getName().equals(name)) {
                    isInconsistent = true;

                    strInconsis += "Shadow Read Inconsistency found: From table specialties, at id " + oldSpecialty.getId() +
                            ", at column name: "
                            + oldSpecialty.getName() +
                            " | " + name + " \n";
                }

                System.out.println(" From Specialty Shadow Read" + "  -pass check 2");

                if (isInconsistent) {
                    readInconsistencies++;
                    System.out.println(strInconsis);
                    System.out.println("Shadow Read Inconsistency count: " + readInconsistencies + " From Specialty Shadow Read");
                    inconsistencyId = oldSpecialty.getId();
                    System.out.println(inconsistencyId);
                } else {
                    System.out.println("Shadow Read Inconsistency count: " + readInconsistencies + " From Specialty Shadow Read");
                    System.out.println("Shadow Read successfully From Specialty Shadow Read");
                }
            }


        } catch (Exception e){
            System.out.println(e.getMessage() + " Error From Specialty Shadow Read");
        }
        return inconsistencyId;
    }

}
