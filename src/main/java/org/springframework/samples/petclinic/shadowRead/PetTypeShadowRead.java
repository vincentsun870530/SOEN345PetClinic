package org.springframework.samples.petclinic.shadowRead;

import org.springframework.samples.petclinic.FeatureToggles.FeatureToggles;
import org.springframework.samples.petclinic.incrementalreplication.IncrementalReplication;
import org.springframework.samples.petclinic.mysql.MySQLJDBCDriverConnection;
import org.springframework.samples.petclinic.sqlite.SQLiteDBConnector;
import org.springframework.samples.petclinic.sqlite.SQLitePetHelper;
import org.springframework.samples.petclinic.owner.PetType;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PetTypeShadowRead {

    private static int PET_TYPE_READ_INCONSISTENCY = 0;
    SQLiteDBConnector sqLiteDbConnector = SQLiteDBConnector.getInstance();
    MySQLJDBCDriverConnection mySQLJDBCDriverConnector = new MySQLJDBCDriverConnection();
    SQLitePetHelper sqLiteVetHelper = SQLitePetHelper.getInstance();

    public int checkPetType(PetType oldPetType)  {
        System.out.println( " From PetType Shadow Read" + "  -start");

        ResultSet newPetTypeResult = SQLiteDBConnector.getInstance().selectById("types", oldPetType.getId());
        int inconsistencyId = -1;
        List<PetType> newPetTypeList;
        boolean isInconsistent = false;
        String strInconsis = "";

        try {
            newPetTypeList =SQLitePetHelper.getInstance().getPetTypeModelList(newPetTypeResult);
            System.out.println(" From PetType Shadow Read" + "  -try");
            for (int i = 0; i < newPetTypeList.size(); i++) {
                System.out.println(" From PetType Shadow Read" + "  -while");

                String name = newPetTypeList.get(i).getName();
                System.out.println(" From PetType Shadow Read " + name);
                System.out.println(" From PetType Shadow Read " + oldPetType.getName());

                System.out.println(" From PetType Shadow Read" + "  -pass check 1");

                if (!oldPetType.getName().equals(name)) {
                    isInconsistent = true;

                    strInconsis += "Shadow Read Inconsistency found: From table types, at id " + oldPetType.getId() +
                            ", at column name: "
                            + oldPetType.getName() +
                            " | " + name + " \n";
                }

                System.out.println(" From PetType Shadow Read" + "  -pass check 2");

                if (isInconsistent) {
                    PET_TYPE_READ_INCONSISTENCY++;
                    System.out.println(strInconsis);
                    System.out.println("Shadow Read Inconsistency count: " + PET_TYPE_READ_INCONSISTENCY + " From PetType Shadow Read");
                    inconsistencyId = oldPetType.getId();
                    System.out.println(inconsistencyId);
                } else {
                    System.out.println("Shadow Read Inconsistency count: " + PET_TYPE_READ_INCONSISTENCY + " From PetType Shadow Read");
                    System.out.println("Shadow Read successfully From PetType Shadow Read");
                }
            }


        } catch (Exception e){
            System.out.println(e.getMessage() + " Error From PetType Shadow Read");
        }
        return inconsistencyId;
    }

    public static int getPetTypeReadInconsistency(){
        return PET_TYPE_READ_INCONSISTENCY;
    }

    public void incrementalReplicationAdapter(PetType petType){

        IncrementalReplication.addToUpdateList("types" + "," + petType.getId() + "," + petType.getName());
        IncrementalReplication.incrementalReplication();

    }

}
