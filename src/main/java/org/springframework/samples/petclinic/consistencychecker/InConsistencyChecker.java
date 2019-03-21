package org.springframework.samples.petclinic.consistencychecker;

public interface InConsistencyChecker {
    public int consistencyChecker();
    public double calculateConsistencyChecker(int inconsistency);
    public void printViolationMessage();
}
