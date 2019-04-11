package org.springframework.samples.petclinic.consistencychecker;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.test.context.junit4.SpringRunner;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import static org.springframework.samples.petclinic.consistencychecker.OwnerConsistencyChecker.*;

@RunWith(SpringRunner.class)
public class OwnerConsistencyCheckerTest {

    Owner owner = new Owner();
    OwnerConsistencyChecker oCC = new OwnerConsistencyChecker();

    List<Owner> oldOwnerList = new ArrayList<>();
    List<Owner> newOwnerList = new ArrayList<>();

    @Test
    public void consistencyCheckerTest() {

        // oldOwnerList.size() == 2, newOwnerList.size() == 3
        oldOwnerList.add(owner);
        oldOwnerList.add(owner);
        newOwnerList.add(owner);
        newOwnerList.add(owner);
        newOwnerList.add(owner);

        setOldData(oldOwnerList);
        setNewData(newOwnerList);

        // oldOwnerList.size() < newOwnerList.size()
        Assert.assertEquals(1, oCC.consistencyChecker());

        oldOwnerList = new ArrayList<>();
        newOwnerList = new ArrayList<>();

        Owner owner1 = new Owner();
        owner1.setId(100);
        owner1.setFirstName("First name 1");
        owner1.setLastName("Last name 1");
        owner1.setAddress("Address 1");
        owner1.setCity("City 1");
        owner1.setTelephone("111");
        Owner owner2 = new Owner();
        owner2.setId(101);
        owner2.setFirstName("First name 1");
        owner2.setLastName("Last name 1");
        owner2.setAddress("Address 1");
        owner2.setCity("City 1");
        owner2.setTelephone("111");
        Owner owner3 = new Owner();
        owner3.setId(100);
        owner3.setFirstName("First name 2");
        owner3.setLastName("Last name 2");
        owner3.setAddress("Address 2");
        owner3.setCity("City 2");
        owner.setTelephone("222");

        // no inconsistency
        oldOwnerList.add(owner1);
        newOwnerList.add(owner1);
        // 1 inconsistency
        oldOwnerList.add(owner1);
        newOwnerList.add(owner2);
        // 5 inconsistencies
        oldOwnerList.add(owner1);
        newOwnerList.add(owner3);


        setOldData(oldOwnerList);
        setNewData(newOwnerList);

        Assert.assertEquals(6, oCC.consistencyChecker());

    }

    @Test
    public void calculateConsistencyCheckerTest() {

        for (int i = 0; i < 20; i++) {
            oldOwnerList.add(owner);
        }

        setOldData(oldOwnerList);

        Assert.assertEquals(65 , (int) oCC.calculateConsistencyChecker(7));

    }


}