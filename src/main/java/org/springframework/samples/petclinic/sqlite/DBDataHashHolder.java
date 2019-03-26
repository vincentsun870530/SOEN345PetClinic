package org.springframework.samples.petclinic.sqlite;

import org.springframework.samples.petclinic.FeatureToggles.FeatureToggles;

import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBDataHashHolder {
    ArrayList<HashMap<String,String>> dataHolder;
    ArrayList<String> tableHeaders;

    public DBDataHashHolder()
    {
        this.dataHolder = new ArrayList<HashMap<String,String>>();
    }

    public DBDataHashHolder(ArrayList<HashMap<String,String>> dataHolder)
    {
        this.dataHolder = dataHolder;
    }

    public DBDataHashHolder(ResultSet resultSet)
    {
        if(dataHolder == null)
            this.dataHolder = new ArrayList<HashMap<String,String>>();
        setDataHolder(resultSet);
    }

    public void setDataHolder(ResultSet resultSet) {
        ResultSetMetaData rsmd;

        try {
            //temp store the table's col names
            rsmd = resultSet.getMetaData();
            int arraySize = rsmd.getColumnCount();
            debugPrintln("Col Size " + arraySize);
            tableHeaders = new ArrayList();
            for(int i=0;i<arraySize;i++){
                tableHeaders.add(rsmd.getColumnName(i+1));
            }

            //covert all rows as HashMap and add to the list
            while(resultSet.next()){
                HashMap row = new HashMap();
                for(int i=0;i<arraySize;i++) {
                    row.put(tableHeaders.get(i) ,resultSet.getString(i+1));
                }

                dataHolder.add(row);
            }
            //TODO change to logger info/debug
            debugPrintln("Converting succeeded -> From DBDataHashHolder");
        }catch (SQLException e){
            //TODO change to logger error
            System.out.println(" Converting Failed: " + e.getMessage() + " -> From DBDataHashHolder");
        }
    }

    public List<HashMap<String,String>> getData(){
        if(this.dataHolder.size() == 0){
            System.out.println("The List is empty -> From DBDataHashHolder");
            return null;
        }else {
            return this.dataHolder;
        }
    }

    public List<String> getHeaders(){
        if(this.dataHolder.size() == 0){
            System.out.println("The List is empty -> From DBDataHashHolder");
            return null;
        }else {
            return this.tableHeaders;
        }
    }

    public void display(){
        if(this.dataHolder.size() == 0){
            System.out.println("The List is empty -> From DBDataHashHolder");
        }else {
            boolean isFirst;
            for(int i = 0; i< this.dataHolder.size(); i++){
                //print col name
                if(i == 0){
                    isFirst =true;
                    for(String colName : tableHeaders) {
                        if(isFirst){
                            System.out.print(colName);
                            isFirst =false;
                        }else {
                            System.out.print("  |  " + colName);
                        }
                    }
                    System.out.println("");
                }

                isFirst =true;
                for(int j=0;j<tableHeaders.size();j++)
                {
                    if(isFirst){
                        System.out.print(dataHolder.get(i).get(tableHeaders.get(j)));
                        isFirst =false;
                    }else {
                        System.out.print("  |  " + dataHolder.get(i).get(tableHeaders.get(j)));
                    }
                }

                System.out.println("");
            }
        }
    }

    //only for temp debug
    private void debugPrintln(String str){
        if(FeatureToggles.isEnableDebuggingSystemOutPrint){
            System.out.println(str);
        }
    }
}
