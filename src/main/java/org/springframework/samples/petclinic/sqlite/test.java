package org.springframework.samples.petclinic.sqlite;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

public class test {
    public static void main(String[] args) {

        //SQLiteResultSet.getPets(new SQLiteDBConnector().selectAll("pets"));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String ts = sdf.format(timestamp);
        System.out.println(ts);
        SQLitePetHelper.getInstance().insert("1",ts,5,2);
       // System.out.println(LocalDate.now());
    }
}
