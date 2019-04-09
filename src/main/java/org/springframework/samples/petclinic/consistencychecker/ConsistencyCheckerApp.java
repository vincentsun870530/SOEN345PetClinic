package org.springframework.samples.petclinic.consistencychecker;

import org.springframework.scheduling.annotation.Async;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import static java.util.concurrent.TimeUnit.SECONDS;

public class ConsistencyCheckerApp {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    //    public static void main(String[] args) {
    @Async
    public void runConsitencyChecker() {
        int numberOfInconsistencies = 0;
        int numberOfRows = 0;

        System.out.println("Owner Consistency Check");
        numberOfInconsistencies += ConsistencyChecker.ownerConsCheck();
        numberOfRows += ConsistencyChecker.numberOfOwnerRows();
        System.out.println("\nPet Consistency Check");
        numberOfInconsistencies += ConsistencyChecker.petConsCheck();
        numberOfRows += ConsistencyChecker.numberOfPetRows();
        System.out.println("\nSpecialty Consistency Check");
        numberOfInconsistencies += ConsistencyChecker.specialtyConsCheck();
        numberOfRows += ConsistencyChecker.numberOfSpecialityRows();
        System.out.println("\nType Consistency Check");
        numberOfInconsistencies += ConsistencyChecker.typeConsCheck();
        numberOfRows += ConsistencyChecker.numberOfTypeRows();
        System.out.println("\nVet Consistency Check");
        numberOfInconsistencies +=  ConsistencyChecker.vetConsCheck();
        numberOfRows += ConsistencyChecker.numberOfVetRows();
        System.out.println("\nVisit Consistency Check");
        numberOfInconsistencies += ConsistencyChecker.visitConsCheck();
        numberOfRows += ConsistencyChecker.numberOfVisitRows();

        System.out.println("\nTotal Consistency rate:" + ConsistencyChecker.calculateTotalConsistency(numberOfInconsistencies, numberOfRows) + " %");
    }

    public void runForAnHour() {
        final Runnable beeper = new Runnable() {
            public void run() { runConsitencyChecker(); }
        };
        final ScheduledFuture<?> beeperHandle = scheduler.scheduleAtFixedRate(beeper, 10, 10, SECONDS);
        scheduler.schedule(new Runnable() {
            public void run() { beeperHandle.cancel(true); }
        }, 60 * 60, SECONDS);
    }
}
