package org.springframework.samples.petclinic.consistencychecker;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.samples.petclinic.vet.Vet;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
public class VetSpecialityConsistencyCheckerTest {

    VetConsistencyChecker vCC = new VetConsistencyChecker();

    @Test
    public void consistencyCheckerTest() {

        Vet vet1 = new Vet();
        List<int[]> oldList = new ArrayList<>();
        int[] data = new int[5];
        oldList.add(data);
        //vCC.setOldData(oldList);

    }


}