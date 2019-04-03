package org.springframework.samples.petclinic.consistencychecker;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.samples.petclinic.visit.Visit;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.samples.petclinic.consistencychecker.VisitConsistencyChecker.*;

@RunWith(MockitoJUnitRunner.class)
public class VisitConsistencyCheckerTest {

    // private List<Visit> oldVisitsData;
    // private List<Visit> newVisitsData;
    VisitConsistencyChecker vCC = new VisitConsistencyChecker();
    List<Visit> oldVisitList = new ArrayList<>();
    List<Visit> newVisitList = new ArrayList<>();
    private final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    private final PrintStream printStream = System.out;

    @Mock
    VisitConsistencyChecker vCCmock;

    @Before
    public void setUpStreams() {
        // for system.out.println test
        System.setOut(new PrintStream(byteArrayOutputStream));
    }

    @After
    public void restoreStreams() {
        System.setOut(printStream);
    }

    @Test
    public void consistencyCheckerTest() {

        Visit visit = new Visit();

        // oldVetsList.size() == 2, newVetsList.size() == 3
        oldVisitList.add(visit);
        oldVisitList.add(visit);
        newVisitList.add(visit);
        newVisitList.add(visit);
        newVisitList.add(visit);
        setOldData(oldVisitList);
        setNewData(newVisitList);

        // oldVisitList.size() < newVisitList.size()
        Assert.assertEquals(1, vCC.consistencyChecker());
    }

    @Test
    public void visitCheckConsistencyTest() {

        Visit visit1 = new Visit();
        visit1.setId(100);
        visit1.setDescription("visit 1");
        visit1.setPetId(1000);
        Visit visit2 = new Visit();
        visit2.setId(101);
        visit2.setDescription("visit 1");
        visit2.setPetId(1000);
        Visit visit3 = new Visit();
        visit3.setId(100);
        visit3.setDescription("visit 2");
        visit3.setPetId(1000);
        Visit visit4 = new Visit();
        visit4.setId(100);
        visit4.setDescription("visit 1");
        visit4.setPetId(1001);
        Visit visit5 = new Visit();
        visit5.setId(100);
        visit5.setDescription("visit 2");
        visit5.setPetId(1001);

        // 0 inconsistency
        oldVisitList.add(visit1);
        newVisitList.add(visit1);
        // 1 inconsistency
        oldVisitList.add(visit1);
        newVisitList.add(visit2);
        // 1 inconsistency
        oldVisitList.add(visit1);
        newVisitList.add(visit3);
        // 1 inconsistency
        oldVisitList.add(visit1);
        newVisitList.add(visit4);
        // 2 inconsistencies
        oldVisitList.add(visit1);
        newVisitList.add(visit5);

        setOldData(oldVisitList);
        setNewData(newVisitList);

        Assert.assertEquals(5, vCC.consistencyChecker());

    }

    /** Above has better version
    @Test
    public void visitCheckConsistencyFailTest() {

        Visit old1 = new Visit();
        old1.setId(100);
        old1.setDescription("test 1");
        old1.setPetId(1000);
        Visit new1 = new Visit();
        new1.setId(200);
        new1.setDescription("test 1");
        new1.setPetId(1000);
        Visit new2 = new Visit();
        new2.setId(100);
        new2.setDescription("test 2");
        new2.setPetId(1000);
        Visit new3 = new Visit();
        new3.setId(100);
        new3.setDescription("test 2");
        new3.setPetId(2000);

        Assert.assertEquals(1, vCC.visitCheckConsistency(old1, new1));
        Assert.assertEquals(1, vCC.visitCheckConsistency(old1, new2));
        Assert.assertEquals(2, vCC.visitCheckConsistency(old1, new3));

    } **/

    @Test
    public void calculateConsistencyCheckerTest() {

        Visit visit = new Visit();
        List<Visit> oldListVisitData = new ArrayList<Visit>();

        for (int i = 0; i < 20; i++) {
            oldListVisitData.add(visit);
        }

        setOldData(oldListVisitData);

        Assert.assertEquals(95 , (int) vCC.calculateConsistencyChecker(1));

    }

    @Test
    public void checkNewAndOldDataTest() {



    }

    @Test
    public void checkDataNewAndOldDataTest() {

        //when(vCCmock.printViolationMessage(1, "old1", "new1")).thenReturn("The row " +
        //        "1 on the new dtabase, does not match: New(new1 is not equal to Old(old1\n");

    }

    @Test
    public void printViolationMessageTest() {

        vCC.printViolationMessage(5, "123test", "test123");

        // TODO
        // Assert.assertEquals("The row 5 on the new database, does not match: New(123test is not equal to Old(test123\n", printStream.toString());

    }
}
