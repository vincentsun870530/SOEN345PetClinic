package org.springframework.samples.petclinic.incrementalreplication;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import org.springframework.samples.petclinic.consistencychecker.OwnerConsistencyChecker;
import org.springframework.samples.petclinic.consistencychecker.PetConsistencyChecker;
import org.springframework.samples.petclinic.consistencychecker.VisitConsistencyChecker;
import org.springframework.samples.petclinic.mysql.MySQLJDBCDriverConnection;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.samples.petclinic.owner.PetType;
import org.springframework.samples.petclinic.sqlite.SQLiteDBConnector;
import org.springframework.samples.petclinic.sqlite.SQLiteIncrementalReplicationHelper;
import org.springframework.samples.petclinic.visit.Visit;

public class IncrementalReplication {

    /** updateArray will contain ID and table name separated by a comma*/
    private static ArrayList<String> updateArray;

    /** Could use ArrayList of array if needed
     *  Else use ArrayList of String for a simple implementation
     *  If ArrayList of String: put whole object in one string, separate by commas
     */
    private static ArrayList<String> createArray; 

    private static ArrayList<String> createIRArray; 
    
    public static void addToUpdateList(String data) {
        if(updateArray == null) {
            updateArray = new ArrayList<String>();
        }
        updateArray.add(data);
        System.out.println("Data saved to update list");
    }

    public static void addToCreateList(String data) {
        if(createArray == null) {
            createArray = new ArrayList<String>();
        }
        createArray.add(data);
        System.out.println("Data saved to create list");
    }

    public static void addToCreateIRList(String data) {
        if(createIRArray == null) {
            createIRArray = new ArrayList<String>();
        }
        createIRArray.add(data);
        System.out.println("Data saved to create Incremental Replication list");
    }

    public static void incrementalReplication() {
        String data = null;
        String[] splittedData = null;

        Connection connectionMySQL = MySQLJDBCDriverConnection.connect();
        Connection connectionSQLite = SQLiteDBConnector.getInstance().connect();

        if(updateArray != null) {
            for(int index=0; index<updateArray.size(); index++) {
                    data = updateArray.get(index);
                    splittedData = data.split(",");
                    int id = Integer.parseInt(splittedData[1].replace(" ", ""));
                    SQLiteIncrementalReplicationHelper.getInstance().updateRow(splittedData);
                    IncrementalReplicationChecker.isConsistency(id, splittedData[0]);

            }
        }

        // To ensure that the data and splittedData are reset to insert new clean data
        data = null;
        splittedData = null;
        if(createArray != null) {
            for(int index=0; index<createArray.size(); index++) {
                data = createArray.get(index);
                splittedData = data.split(",");
                String tableName = splittedData[0];
                int primaryKey = Integer.parseInt(splittedData[1]);
            /*    for(int k =0  ; k<splittedData.length;k++){
                    System.out.println(splittedData[k]+k+"!!!!!!!!!!!!!!!@@@@######");
                }*/
                //System.out.println(splittedData.length+"@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
                boolean isSuccess = SQLiteIncrementalReplicationHelper.getInstance().updateRow(splittedData);

                if(isSuccess) {
                    IncrementalReplicationChecker.isConsistency(primaryKey, tableName);
                } else {
                    System.out.println("Error in incrementalReplication(): ID(" + primaryKey + ") not found in table(" + tableName + ")");
                }
            }
        }


        // Insert new data using incremental replication only
        data = null;
        splittedData = null;
        if(createIRArray != null) {
            for(int index=0; index<createIRArray.size(); index++) {
                data = createIRArray.get(index);
                splittedData = data.split(",");
                String tableName = splittedData[0];
                int id = SQLiteIncrementalReplicationHelper.getInstance().insertData(splittedData);
                 if(id != 0) {	 
                    IncrementalReplicationChecker.isConsistency(id, tableName);
                } else {
                    System.out.println("Error in incrementalReplication(): ID(" + id + ") not found in table(" + tableName + ")");}
            }
        }
    }
}