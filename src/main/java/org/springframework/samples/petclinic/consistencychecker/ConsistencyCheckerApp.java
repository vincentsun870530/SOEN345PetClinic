package org.springframework.samples.petclinic.consistencychecker;

public class ConsistencyCheckerApp {
    public static void main(String[] args) {
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

        System.out.println("\nTotal (double)inconsistency rate:" + ConsistencyChecker.calculateTotalConsistency(numberOfInconsistencies, numberOfRows));
    }
}
