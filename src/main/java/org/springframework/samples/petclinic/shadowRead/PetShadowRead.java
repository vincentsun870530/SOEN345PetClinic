// Contributed along with Felix

package org.springframework.samples.petclinic.shadowRead;

import org.springframework.samples.petclinic.mysql.MySQLJDBCDriverConnection;
import org.springframework.samples.petclinic.sqlite.SQLiteDBConnector;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.samples.petclinic.sqlite.SQLitePetHelper;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PetShadowRead {
    private int readInconsistencies = 0;

    SQLiteDBConnector sqLiteDbConnector = SQLiteDBConnector.getInstance();
    MySQLJDBCDriverConnection mySQLJDBCDriverConnector = new MySQLJDBCDriverConnection();
    SQLitePetHelper sqLitePetHelper = SQLitePetHelper.getInstance();

    public void checkPet(Pet oldPet){
        System.out.println( " From Pet Shadow Read" + "  -start");
        ResultSet newPetResult = SQLiteDBConnector.getInstance().selectById("pets", oldPet.getId());

        ArrayList<HashMap> newPetPack = new ArrayList();
        List<Pet> newPetList;
        newPetList = SQLitePetHelper.getInstance().getModelList(newPetResult);
        boolean isInconsistent = false;
        String strInconsistant = "";

        //sqLiteDbConnector.connect();
        try{
            System.out.println( " From Pet Shadow Read" + "  -try");

            for (int i= 0; i < newPetList.size(); i++){

                System.out.println( " From Pet Shadow Read" + "  -while");

                String name = newPetResult.getString("name");
                System.out.println( " From Pet Shadow Read " + name);
                System.out.println( " From Pet Shadow Read " + oldPet.getName());

                String birth_date = newPetResult.getString("birth_date");
                System.out.println( " From Pet Shadow Read " + birth_date);
                System.out.println( " From Pet Shadow Read " + oldPet.getBirthDate().toString());

                String type_id = newPetResult.getString("type_id");
                System.out.println( " From Pet Shadow Read " + type_id);
                System.out.println( " From Pet Shadow Read " + oldPet.getType());

                String owner_id = newPetResult.getString("owner_id");
                System.out.println( " From Pet Shadow Read " + owner_id);
                System.out.println( " From Pet Shadow Read " + oldPet.getOwner().toString());

                HashMap inconsistencyRow = new HashMap();
                inconsistencyRow.put("id",oldPet.getId());
                inconsistencyRow.put("name",null);
                inconsistencyRow.put("birth_date",null);
                inconsistencyRow.put("type_id",null);
                inconsistencyRow.put("owner_id",null);

                System.out.println( " From Pet Shadow Read" + "  -pass check 1");

                if(!oldPet.getName().equals(name)){
                    isInconsistent=true;

                    strInconsistant += "Shadow Read Inconsistency found: From table pets, at id "+oldPet.getId()+
                            ", at column name: "
                            + oldPet.getName() +
                            " | " + name + " \n";

                    inconsistencyRow.replace("name",oldPet.getName());
                }

                System.out.println( " From Pet Shadow Read" + "  -pass check 2");

                if(!oldPet.getBirthDate().toString().equals(birth_date)){
                    isInconsistent=true;

                    strInconsistant += "Shadow Read Inconsistency found: From table pets, at id "+oldPet.getId()+
                            ", at column birth_date: "
                            + oldPet.getBirthDate().toString() +
                            " | " + birth_date + " \n";

                    inconsistencyRow.replace("birth_date",oldPet.getBirthDate().toString());
                }

                if(!oldPet.getType().equals(type_id)){
                    isInconsistent=true;

                    strInconsistant += "Shadow Read Inconsistency found: From table pets, at id "+oldPet.getId()+
                            ", at column type_id: " + oldPet.getType() +
                            " | " + type_id + " \n";

                    inconsistencyRow.replace("type_id",oldPet.getType());
                }

                if(!oldPet.getOwner().equals(owner_id)){
                    isInconsistent=true;

                    strInconsistant += "Shadow Read Inconsistency found: From table pets, at id "+oldPet.getId()+
                            ", at column type_id: " + oldPet.getOwner() +
                            " | " + type_id + " \n";

                    inconsistencyRow.replace("owner_id",oldPet.getOwner());
                }

                if(isInconsistent){
                    readInconsistencies++;
                    System.out.println(strInconsistant);
                    System.out.println( "Shadow Read Inconsistency count: " + readInconsistencies + " from Pet Shadow Read");
                    newPetPack.add(inconsistencyRow);
                }else {
                    System.out.println( "Shadow Read Inconsistency count: " + readInconsistencies + " from Pet Shadow Read");
                    System.out.println( "Shadow Read successfully from Pet Shadow Read");
                }
            }

            System.out.println( "Done while loop from Pet Shadow Read");

            for(int i = 0; i<newPetPack.size();i++)
            {
                System.out.println( "start updating from Pet Shadow Read");
                int id = Integer.parseInt(newPetPack.get(i).get("id").toString());

                if(newPetPack.get(i).get("name") != null) {

                    sqLitePetHelper.updateColById("name", newPetPack.get(i).get("name").toString(), id);
                }
                if(newPetPack.get(i).get("birth_date") != null) {

                    sqLitePetHelper.updateColById("birth_date", newPetPack.get(i).get("birth_date").toString(), id);
                }
                if(newPetPack.get(i).get("type_id") != null) {

                    sqLitePetHelper.updateColById("type_id", newPetPack.get(i).get("type_id").toString(), id);
                }
                if(newPetPack.get(i).get("updateColById") != null) {

                    sqLitePetHelper.updateColById("owner_id", newPetPack.get(i).get("owner_id").toString(), id);
                }
            }

        }catch (Exception e){
            System.out.println(e.getMessage() + " Error from Pet Shadow Read");
        }

    }

    public int getReadInconsistencies() {
        return readInconsistencies;
    }
    

}
