package org.springframework.samples.petclinic.consistencychecker;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.samples.petclinic.vet.Specialty;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.samples.petclinic.consistencychecker.SpecialityConsistencyChecker.*;

@RunWith(SpringRunner.class)
public class SpecialityConsistencyCheckerTest {

    Specialty specialty = new Specialty();
    SpecialityConsistencyChecker sCC = new SpecialityConsistencyChecker();

    List<Specialty> oldSpecialityList = new ArrayList<>();
    List<Specialty> newSpecialityTest = new ArrayList<>();

    @Test
    public void consistencyCheckerTest() {

        // oldSpecialityList.size() == 2, newSpecialityTest.size() == 3
        oldSpecialityList.add(specialty);
        oldSpecialityList.add(specialty);
        newSpecialityTest.add(specialty);
        newSpecialityTest.add(specialty);
        newSpecialityTest.add(specialty);

        setOldData(oldSpecialityList);
        setNewData(newSpecialityTest);

        // oldSpecialityList.size() < newSpecialityTest.size()
        Assert.assertEquals(1, sCC.consistencyChecker());

        oldSpecialityList = new ArrayList<>();
        newSpecialityTest = new ArrayList<>();

        Specialty speciality1 = new Specialty();
        speciality1.setId(100);
        speciality1.setName("Speciality 1");
        Specialty speciality2 = new Specialty();
        speciality2.setId(101);
        speciality2.setName("Speciality 1");
        Specialty speciality3 = new Specialty();
        speciality3.setId(100);
        speciality3.setName("Speciality 2");

        // no inconsistency
        oldSpecialityList.add(speciality1);
        newSpecialityTest.add(speciality1);
        // 1 inconsistency
        oldSpecialityList.add(speciality1);
        newSpecialityTest.add(speciality2);
        // 1 inconsistency
        oldSpecialityList.add(speciality1);
        newSpecialityTest.add(speciality3);


        setOldData(oldSpecialityList);
        setNewData(newSpecialityTest);

        Assert.assertEquals(2, sCC.consistencyChecker());
    }

    @Test
    public void calculateConsistencyCheckerTest() {


        for (int i = 0; i < 4; i++) {
            oldSpecialityList.add(specialty);
        }

        setOldData(oldSpecialityList);

        Assert.assertEquals(75 , (int) sCC.calculateConsistencyChecker(1));

    }

}