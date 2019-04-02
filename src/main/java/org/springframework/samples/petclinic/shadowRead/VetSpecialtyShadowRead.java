package org.springframework.samples.petclinic.shadowRead;

import org.springframework.samples.petclinic.FeatureToggles.FeatureToggles;
import org.springframework.samples.petclinic.incrementalreplication.IncrementalReplication;
import org.springframework.samples.petclinic.mysql.MySQLJDBCDriverConnection;
import org.springframework.samples.petclinic.sqlite.SQLiteDBConnector;
import org.springframework.samples.petclinic.sqlite.DBDataHashHolder;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VetSpecialtyShadowRead {
    private static int VET_SPECIALTY_READ_INCONSISTENCY = 0;
    SQLiteDBConnector sqLiteDbConnector = SQLiteDBConnector.getInstance();
    MySQLJDBCDriverConnection mySQLJDBCDriverConnector = new MySQLJDBCDriverConnection();
    DBDataHashHolder sqLiteVetSpecialtyData = new DBDataHashHolder();
    ArrayList<HashMap<String,String>> inconsistentData;

    boolean isInconsistent = false;
    public void checkVetSpecialty(List<String> oldVetSpecialities){
        System.out.println( " From Vet Specialty Shadow Read" + "  -start");

        ResultSet newVetSpecialitiesResult = SQLiteDBConnector.getInstance().selectVetSpecialtyById("vet_specialties", Integer.parseInt(oldVetSpecialities.get(0)));
        int inconsistencyId = -1;
        ArrayList<String> newVetSpecialities = new ArrayList<String>();
        isInconsistent = false;
        String strInconsis = "";

        try{
            sqLiteVetSpecialtyData.setDataHolder(newVetSpecialitiesResult);
            sqLiteVetSpecialtyData.display();
            newVetSpecialities.add(oldVetSpecialities.get(0));
            System.out.println(" From Vet Specialty Shadow Read" + "  -try");
            for(HashMap<String,String> hashMap: sqLiteVetSpecialtyData.getData())
            {
                System.out.print(" | " + hashMap.get("specialty_id"));
                newVetSpecialities.add(hashMap.get("specialty_id"));
            }
            System.out.println("\n --------------------------------------");
            System.out.println(oldVetSpecialities+"\n --------------------------------------");
            inconsistentData = new ArrayList<HashMap<String, String>>();
            for(int i = 1; i<oldVetSpecialities.size(); i++)
            {
                boolean isExist = false;
                String old_specialty_id = oldVetSpecialities.get(i);
                for(int j = 1; j<newVetSpecialities.size();j++)
                {
                    if(oldVetSpecialities.get(i).equals(newVetSpecialities.get(j)))
                    {
                        oldVetSpecialities.set(i,"-1");
                        newVetSpecialities.set(j,"-1");
                        isExist = true;
                        //break;
                    }
                }

                if(isExist == false){
                    VET_SPECIALTY_READ_INCONSISTENCY++;
                    isInconsistent=true;
                    HashMap<String,String> inconsistencyHash = new HashMap<String,String>();
                    inconsistencyHash.put("old_data",old_specialty_id);
                    inconsistencyHash.put("new_data","-1");
                    inconsistentData.add(inconsistencyHash);

                 }
            }
            System.out.println( "--------------   check 1      ----------------");

            for(int i = 0; i<inconsistentData.size(); i++){
                for(int j = 1; j<newVetSpecialities.size(); j++){
                    if(Integer.parseInt(newVetSpecialities.get(j))> -1)
                    {
                        HashMap<String,String> temp = new HashMap<String,String>();
                        temp.put("old_data",inconsistentData.get(i).get("old_data"));
                        temp.put("new_data",newVetSpecialities.get(j));
                        inconsistentData.set(i,temp);
                        System.out.println("Inconsistency occurs at vet id = "
                                +oldVetSpecialities.get(0)
                                +inconsistentData+"\n --------------------------------------");
                    }
                }
            }
            //System.out.println(inconsistentData+"\n --------------------------------------");
            incrementalReplicationAdapter(oldVetSpecialities.get(0));

        } catch (Exception e){
            System.out.println(e.getMessage() + " Error From Vet Specialty Shadow Read");
        }
    }

    public void incrementalReplicationAdapter(String id){

        if(isInconsistent == true) {
            //if it is inconsistent add query list
            for(int i = 0; i<inconsistentData.size(); i++) {
                if(Integer.parseInt(inconsistentData.get(i).get("new_data")) > -1) {
                    String str = "vet_specialties"
                            + "," + id
                            + "," + inconsistentData.get(i).get("old_data")
                            + "," + inconsistentData.get(i).get("new_data");
                    System.out.println(str +" -------- From Vet Specialty Shadow Read");
                    //TODO solve the bug to implament IncrementalReplication
                    //IncrementalReplication.addToUpdateList(str);
                    sqLiteDbConnector.updateVetSpecialty("vet_specialties","specialty_id",
                            Integer.parseInt(inconsistentData.get(i).get("old_data")), Integer.parseInt(id),
                            Integer.parseInt(inconsistentData.get(i).get("new_data")));
                }
            }


            //IncrementalReplication.incrementalReplication();
        }

    }
}
