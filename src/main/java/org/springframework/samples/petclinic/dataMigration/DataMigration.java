package org.springframework.samples.petclinic.dataMigration;

import org.springframework.samples.petclinic.consistencychecker.ConsistencyCheckerApp;

import java.io.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class DataMigration {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public static void main (String[] args) {

        // TODO: Check for OS and Create unix version since this implementation will only work on windows
        // Run Forklift
        try {
            ProcessBuilder builder = new ProcessBuilder(
                    "cmd.exe", "/c", "\"src\\main\\resources\\db\\sqlite\\forklift_step.sh\"");
            builder.redirectErrorStream(true);
            Process p = builder.start();
            BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while (true) {
                line = r.readLine();
                if (line == null) {
                    break;
                }
                System.out.println(line);
            }
        }catch(IOException e){
            System.out.println(e);
        }

        //run app for an hour
        runConsitencyChecker();

    }


    public static void runConsitencyChecker() {
        // Run Consistency Checker
        try {
            ConsistencyCheckerApp consistencyChecker = new ConsistencyCheckerApp();
            consistencyChecker.runForAnHour();
        } catch(Exception e){
            System.out.println("An error occured running consistency Check app " + e);
        }
    }

}