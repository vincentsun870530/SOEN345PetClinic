package org.springframework.samples.petclinic.sqlite;

public class test {
    public static void main(String[] args) {

        SQLiteResultSet.getOwners(new SQLiteDBConnector().selectAll("owners"));

    }
}
