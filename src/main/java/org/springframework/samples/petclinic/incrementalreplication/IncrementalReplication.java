package org.springframework.samples.petclinic.incrementalreplication;

import java.sql.Connection;
import java.util.ArrayList;

import org.springframework.samples.petclinic.mysql.MySQLJDBCDriverConnection;
import org.springframework.samples.petclinic.sqlite.SQLiteDBConnector;

public class IncrementalReplication {

    /** updateArray will contain ID and table name separated by a comma*/
    private static ArrayList<String> updateArray;

    /** Could use ArrayList of array if needed
     *  Else use ArrayList of String for a simple implementation
     *  If ArrayList of String: put whole object in one string, separate by commas
     */
    private static ArrayList<String> createArray; 
    
    public static void addToUpdateList(String data) {
        if(updateArray == null) {
            updateArray = new ArrayList<String>();
        }
        updateArray.add(data);
    }

    public static void addToCreateList(String data) {
        if(createArray == null) {
            createArray = new ArrayList<String>();
        }
        createArray.add(data);
    }

    public static void incrementalReplication() {
        String data = null;
        String[] splittedData = null;


        //TODO ADD SQLITE CONNECTION 
        Connection connectionMySQL = MySQLJDBCDriverConnection.connect();
        Connection connectionSQLite = new SQLiteDBConnector().connect();


        if(updateArray != null) {
            for(int index=0; index<updateArray.size(); index++) {
                    data = updateArray.get(index);
                    splittedData = data.split(",");

                    // Could use the array value directly to the query
                    String idString = splittedData[0];
                    String tableName = splittedData[1];
                    String columnName = splittedData[2];
                    String value = splittedData[3];

                    int id = Integer.parseInt(idString);

                    new SQLiteDBConnector().updateById(tableName, columnName, value, id);

                    checkConsistency(id, tableName, columnName, value);

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
                new SQLiteDBConnector().insertData(tableName, splittedData);

            }
        }
    }

    private static void checkConsistency(int id, String tableName, String columnName, String value) {
        String oldDatabase = (MySQLJDBCDriverConnection.selectById(tableName, id)).toString();
        String newDatabase = (new SQLiteDBConnector().selectById(tableName, id)).toString();

        if(oldDatabase != newDatabase) {
            printViolationMessage(id);
            new SQLiteDBConnector().updateById(tableName, columnName, value, id);
        }
    }

    public static void printViolationMessage(int id){ //, String oldData, String newData) {
        // System.out.println("The row " + id + " on the new database," +
        //                     " does not match: New(" + newData + 
        //                     " is not equal to Old(" + oldData);
        System.out.println("The row " + id + " is inconsistent with the old database");
    }

    /**
     * TODO ADD CALLS TO THOSE METHODS IN ALL CONTROLLERS FOR UPDATE AND CREATE
     */


}