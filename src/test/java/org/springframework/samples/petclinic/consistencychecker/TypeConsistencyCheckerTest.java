package org.springframework.samples.petclinic.consistencychecker;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.samples.petclinic.owner.PetType;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.samples.petclinic.consistencychecker.TypeConsistencyChecker.*;

@RunWith(SpringRunner.class)
public class TypeConsistencyCheckerTest {

    PetType petType = new PetType();
    TypeConsistencyChecker tCC = new TypeConsistencyChecker();

    List<PetType> oldPetTypeList = new ArrayList<>();
    List<PetType> newPetTypeList = new ArrayList<>();

    @Test
    public void consistencyCheckerTest() {

        // oldPetTypeList.size() == 2, newPetTypeList.size() == 3
        oldPetTypeList.add(petType);
        oldPetTypeList.add(petType);
        newPetTypeList.add(petType);
        newPetTypeList.add(petType);
        newPetTypeList.add(petType);

        setOldData(oldPetTypeList);
        setNewData(newPetTypeList);

        // oldPetTypeList.size() < newPetTypeList.size()
        Assert.assertEquals(1, tCC.consistencyChecker());

        oldPetTypeList = new ArrayList<>();
        newPetTypeList = new ArrayList<>();

        PetType petType1 = new PetType();
        petType1.setId(100);
        petType1.setName("Pet Type 1");
        PetType petType2 = new PetType();
        petType2.setId(101);
        petType2.setName("Pet Type 1");
        PetType petType3 = new PetType();
        petType3.setId(100);
        petType3.setName("Pet Type 2");

        // no inconsistency
        oldPetTypeList.add(petType1);
        newPetTypeList.add(petType1);
        // 1 inconsistency
        oldPetTypeList.add(petType1);
        newPetTypeList.add(petType2);
        // 1 inconsistency
        oldPetTypeList.add(petType1);
        newPetTypeList.add(petType3);


        setOldData(oldPetTypeList);
        setNewData(newPetTypeList);

        Assert.assertEquals(2, tCC.consistencyChecker());
    }

    @Test
    public void calculateConsistencyCheckerTest() {

        for (int i = 0; i < 5; i++) {
            oldPetTypeList.add(petType);
        }

        setOldData(oldPetTypeList);

        Assert.assertEquals(80 , (int) tCC.calculateConsistencyChecker(1));

    }


}