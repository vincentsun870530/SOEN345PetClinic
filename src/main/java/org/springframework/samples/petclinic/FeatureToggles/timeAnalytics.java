package org.springframework.samples.petclinic.FeatureToggles;

public class timeAnalytics {
    public static long startTime = 0;
    public static long endTime = 0;

    public static long elapsedTimeNS(){
        return endTime - startTime;
    }

    public static double elapsedTimeMS(){
        return (double) elapsedTimeNS() / 1000000;
    }

    public static void resetTimeAnalystics() {
        startTime = 0;
        endTime = 0;
    }

}
