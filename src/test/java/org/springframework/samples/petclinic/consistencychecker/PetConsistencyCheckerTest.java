package org.springframework.samples.petclinic.consistencychecker;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.samples.petclinic.owner.PetType;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.samples.petclinic.consistencychecker.PetConsistencyChecker.*;

@RunWith(SpringRunner.class)
public class PetConsistencyCheckerTest {

    Pet pet = new Pet();
    PetConsistencyChecker pCC = new PetConsistencyChecker();

    List<Pet> oldPetList = new ArrayList<>();
    List<Pet> newPetList = new ArrayList<>();

    @Test
    public void consistencyCheckerTest() {

        // oldPetList.size() == 2, newPetList.size() == 3
        oldPetList.add(pet);
        oldPetList.add(pet);
        newPetList.add(pet);
        newPetList.add(pet);
        newPetList.add(pet);

        setOldData(oldPetList);
        setNewData(newPetList);

        // oldPetList.size() < newPetList.size()
        Assert.assertEquals(1, pCC.consistencyChecker());

        oldPetList = new ArrayList<>();
        newPetList = new ArrayList<>();

        PetType pType1 = new PetType();
        pType1.setId(111);
        PetType pType2 = new PetType();
        pType2.setName("dog");
        pType2.setId(222);
        Owner owner1 = new Owner();
        owner1.setId(111);
        Owner owner2 = new Owner();
        owner2.setId(222);

        /** tried mock to set petType
        Pet petMock1 = mock(Pet.class);
        when(petMock1.getId()).thenReturn(100);
        when(petMock1.getName()).thenReturn("Pet 1");
        when(petMock1.getBirthDate()).thenReturn(LocalDate.of(2019, 3, 3));
        when(petMock1.getType()).thenReturn(pType1);

        Pet petMock2 = mock(Pet.class);
        when(petMock2.getId()).thenReturn(100);
        when(petMock2.getName()).thenReturn("Pet 1");
        when(petMock2.getBirthDate()).thenReturn(LocalDate.of(2019, 3, 3));
        when(petMock2.getType()).thenReturn(pType2);

        oldPetList.add(petMock1);
        newPetList.add(petMock2); **/

        Pet pet1 = new Pet();
        pet1.setId(100);
        pet1.setName("Pet 1");
        pet1.setBirthDate(LocalDate.of(2019, 3, 3));
        pet1.setOwner(owner1);
        pet1.setType(pType1);
        Pet pet2 = new Pet();
        pet2.setId(101);
        pet2.setName("Pet 1");
        pet2.setBirthDate(LocalDate.of(2019, 3, 3));
        pet2.setType(pType2);
        pet2.setOwner(owner2);
        Pet pet3 = new Pet();
        pet3.setId(100);
        pet3.setName("Pet 2");
        pet3.setBirthDate(LocalDate.of(2019, 3, 3));
        pet3.setType(pType1);
        pet3.setOwner(owner1);
        Pet pet4 = new Pet();
        pet4.setId(100);
        pet4.setName("Pet 1");
        pet4.setBirthDate(LocalDate.of(2019, 4, 4));
        pet4.setType(pType1);
        pet4.setOwner(owner1);
        Pet pet5 = new Pet();
        pet5.setId(100);
        pet5.setName("Pet 1");
        pet5.setBirthDate(LocalDate.of(2019, 3, 3));
        pet5.setType(pType2);
        pet5.setOwner(owner2);
        Pet pet6 = new Pet();
        pet6.setId(100);
        pet6.setName("Pet 2");
        pet6.setBirthDate(LocalDate.of(2019, 4, 4));
        pet6.setType(pType2);
        pet6.setOwner(owner2);

        //System.out.println(pet1.getBirthDate());

        // no inconsistency
        oldPetList.add(pet1);
        newPetList.add(pet1);
        // 1 inconsistency
        oldPetList.add(pet1);
        newPetList.add(pet2);
        // 1 inconsistency
        oldPetList.add(pet1);
        newPetList.add(pet3);
        // 1 inconsistency
        oldPetList.add(pet1);
        newPetList.add(pet4);
        // 2 inconsistencies
        oldPetList.add(pet1);
        newPetList.add(pet5);
        // 4 inconsistencies
        oldPetList.add(pet1);
        newPetList.add(pet6);


        setOldData(oldPetList);
        setNewData(newPetList);

        Assert.assertEquals(9, pCC.consistencyChecker());



    }

    @Test
    public void calculateConsistencyCheckerTest() {

        for (int i = 0; i < 10; i++) {
            oldPetList.add(pet);
        }

        setOldData(oldPetList);

        Assert.assertEquals(70 , (int) pCC.calculateConsistencyChecker(3));

    }


}