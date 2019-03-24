package org.springframework.samples.petclinic.sqlite;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class test {
    public static void main(String[] args) {

        //SQLiteResultSet.getPets(new SQLiteDBConnector().selectAll("pets"));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String ts = sdf.format(timestamp);
        System.out.println(ts);
        // Test Pet Helper
        SQLitePetHelper.getInstance().insert("1",ts,5,2);
        // Test Visit Helper
        SQLiteVisitHelper.getInstance().insert(10,ts,"This is a test");
       // System.out.println(LocalDate.now());
    }
}
