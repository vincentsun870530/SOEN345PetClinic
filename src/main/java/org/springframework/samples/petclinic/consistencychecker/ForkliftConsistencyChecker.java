package org.springframework.samples.petclinic.consistencychecker;

public class ForkliftConsistencyChecker implements InConsistencyChecker {

    public int consistencyChecker() {
        //TODO work on the method
        //
        return 0;
    }
    public double calculateConsistencyChecker(int inconsistency) {
        //If doing for the owner's address record
        //List<Owner> owners = //retrieve owners information from database
        int sizeOfOwners = 10; //owners.size()
        double consistency = (1 - (inconsistency/sizeOfOwners))*100;
        return Double.parseDouble(String.format("%.2f", consistency));
    }
    public void printViolationMessage() {
        //TODO print information about the inconsistent row
        System.out.println("The row is inconsistent");
    }
}