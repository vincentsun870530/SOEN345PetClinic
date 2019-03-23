package org.springframework.samples.petclinic.consistencychecker;

import java.util.List;

import org.springframework.samples.petclinic.owner.Owner;

public interface InConsistencyChecker {
    // public void setOldData(List<Owner> oldTableData);
    // public void setNewData(List<Owner> newTableData);
    public int consistencyChecker();
    public double calculateConsistencyChecker(int inconsistency);
    public void printViolationMessage(int id, String oldFirstName, String newFirstNa);
}
