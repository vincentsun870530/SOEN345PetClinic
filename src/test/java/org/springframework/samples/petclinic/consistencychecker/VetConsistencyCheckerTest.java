package org.springframework.samples.petclinic.consistencychecker;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.samples.petclinic.vet.Vet;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.samples.petclinic.consistencychecker.VetConsistencyChecker.*;

@RunWith(SpringRunner.class)
public class VetConsistencyCheckerTest {

    Vet vet = new Vet();
    VetConsistencyChecker vCC = new VetConsistencyChecker();
    List<Vet> oldVetsList = new ArrayList<>();
    List<Vet> newVetsList = new ArrayList<>();

    @Test
    public void consistencyCheckerTest() {

        // oldVetsList.size() == 2, newVetsList.size() == 3
        oldVetsList.add(vet);
        oldVetsList.add(vet);
        newVetsList.add(vet);
        newVetsList.add(vet);
        newVetsList.add(vet);

        setOldData(oldVetsList);
        setNewData(newVetsList);

        // oldVetsList.size() < newVetsList.size()
        Assert.assertEquals(1, vCC.consistencyChecker());

        oldVetsList = new ArrayList<>();
        newVetsList = new ArrayList<>();

        Vet vet1 = new Vet();
        vet1.setId(100);
        vet1.setFirstName("First name 1");
        vet1.setLastName("Last name 1");
        Vet vet2 = new Vet();
        vet2.setId(101);
        vet2.setFirstName("First name 1");
        vet2.setLastName("Last name 1");
        Vet vet3 = new Vet();
        vet3.setId(100);
        vet3.setFirstName("First name 2");
        vet3.setLastName("Last name 1");
        Vet vet4 = new Vet();
        vet4.setId(100);
        vet4.setFirstName("First name 1");
        vet4.setLastName("Last name 2");
        Vet vet5 = new Vet();
        vet5.setId(100);
        vet5.setFirstName("First name 2");
        vet5.setLastName("First name 2");

        // no inconsistency
        oldVetsList.add(vet1);
        newVetsList.add(vet1);
        // 1 inconsistency
        oldVetsList.add(vet1);
        newVetsList.add(vet2);
        // 1 inconsistencies
        oldVetsList.add(vet1);
        newVetsList.add(vet3);
        // 1 inconsistencies
        oldVetsList.add(vet1);
        newVetsList.add(vet4);
        // 2 inconsistencies
        oldVetsList.add(vet1);
        newVetsList.add(vet5);

        setOldData(oldVetsList);
        setNewData(newVetsList);

        Assert.assertEquals(5, vCC.consistencyChecker());

    }

    @Test
    public void calculateConsistencyCheckerTest() {

        // for sizeOfVets
        for (int i = 0; i < 20; i++) {
            oldVetsList.add(vet);
        }

        setOldData(oldVetsList);

        Assert.assertEquals(85 , (int) vCC.calculateConsistencyChecker(3));

    }


}