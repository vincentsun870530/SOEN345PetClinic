package org.springframework.samples.petclinic.sqlite;

public class test {
    public static void main(String[] args) {

        //SQLiteResultSet.getPets(new SQLiteDBConnector().selectAll("pets"));
        SQLiteOwnerHelper.getInstance().insert("1","2","#","4","5");

    }
}
