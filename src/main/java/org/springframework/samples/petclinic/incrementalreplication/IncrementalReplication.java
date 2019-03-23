package org.springframework.samples.petclinic.incrementalreplication;

import java.util.ArrayList;

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


        if(updateArray != null) {
            for(int index=0; index<updateArray.size(); index++) {
                    data = updateArray.get(index);
                    splittedData = data.split(",");

                    // Could use the array value directly to the query
                    String id = splittedData[0];
                    String tableName = splittedData[1];

                    //TODO ADD QUERY TO NEW DATABASE (UPDATE COMMAND)
                    //TODO COMPARE OLD DATA WITH NEW DATA

            }
        }

        // To ensure that the data and splittedData are reset to insert new clean data
        data = null;
        splittedData = null;
        if(createArray != null) {
            for(int index=0; index<createArray.size(); index++) {
                data = createArray.get(index);
                splittedData = data.split(",");

                //TODO ADD QUERY TO NEW DATABASE (CREATE COMMAND)


            }
        }
    }

    /**
     * TODO ADD CALLS TO THOSE METHODS IN ALL CONTROLLERS FOR UPDATE AND CREATE
     */


}