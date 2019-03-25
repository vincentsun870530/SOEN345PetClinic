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
        Connection connectionSQLite = SQLiteDBConnector.getInstance().connect();


        if(updateArray != null) {
            for(int index=0; index<updateArray.size(); index++) {
                    data = updateArray.get(index);
                    splittedData = data.split(",");

                    // Could use the array value directly to the query
                    // String idString = splittedData[0];
                    // String tableName = splittedData[1];
                    // String columnName = splittedData[2];
                    // String value = splittedData[3];

                    // int id = Integer.parseInt(idString);

                    // SQLiteDBConnector.getInstance().updateById(tableName, columnName, value, id);

                    // checkConsistency(id, tableName, columnName, value);

                    SQLiteDBConnector.getInstance().updateRow(splittedData);
                    checkConsistency(Integer.parseInt(splittedData[1]), splittedData[0], splittedData);

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

                // String tableName = splittedData[0];
                int id = SQLiteDBConnector.getInstance().insertData(splittedData);
                if(id != 0) {
                    checkConsistency(id, tableName, splittedData);
                } else {
                    System.out.println("Error in incrementalReplication(): ID(" + id + ") not found in table(" + tableName + ")");
                }

            }
        }
    }

    // private static void checkConsistency(int id, String tableName, String columnName, String value) {
    //     String oldDatabase = (MySQLJDBCDriverConnection.selectById(tableName, id)).toString();
    //     String newDatabase = (SQLiteDBConnector.getInstance().selectById(tableName, id)).toString();

    //     if(oldDatabase != newDatabase) {
    //         printViolationMessage(id, tableName);
    //         SQLiteDBConnector.getInstance().updateById(tableName, columnName, value, id);
    //     }
    // }

    private static void checkConsistency(int id, String tableName, String array[]) {
        String oldDatabase = (MySQLJDBCDriverConnection.selectById(tableName, id)).toString();
        String newDatabase = (SQLiteDBConnector.getInstance().selectById(tableName, id)).toString();

        if(oldDatabase != newDatabase) {
            printViolationMessage(id, tableName);
            SQLiteDBConnector.getInstance().updateRow(array);
        }
    }

    public static void printViolationMessage(int id, String tableName){ //, String oldData, String newData) {
        // System.out.println("The row " + id + " on the new database," +
        //                     " does not match: New(" + newData + 
        //                     " is not equal to Old(" + oldData);
        System.out.println("The row " + id + " in the table " + tableName + "is inconsistent with the old database");
    }

    /**
     * TODO ADD CALLS TO THOSE METHODS IN ALL CONTROLLERS FOR UPDATE AND CREATE
     */


}