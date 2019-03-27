package org.springframework.samples.petclinic.consistencychecker;

public class ConsistencyCheckerApp {
    public static void main(String[] args) {
        System.out.println("Owner Consistency Check");
        ConsistencyChecker.ownerConsCheck();
        System.out.println("\nPet Consistency Check");
        ConsistencyChecker.petConsCheck();
        System.out.println("\nSpecialty Consistency Check");
        ConsistencyChecker.specialtyConsCheck();
        System.out.println("\nType Consistency Check");
        ConsistencyChecker.typeConsCheck();
        System.out.println("\nVet Consistency Check");
        ConsistencyChecker.vetConsCheck();
        System.out.println("\nVisit Consistency Check");
        ConsistencyChecker.visitConsCheck();
    }
}
