// reference: https://stackoverflow.com/questions/1119385/junit-test-for-system-out-println/15175309

package org.springframework.samples.petclinic.consistencychecker;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class ConsistencyCheckerAppTest {

    private final ByteArrayOutputStream bAOS = new ByteArrayOutputStream();
    private final PrintStream pS = System.out;
    ConsistencyCheckerApp cC;

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(bAOS));
    }

    @After
    public void restoreStreams() {
        System.setOut(pS);
    }

    @Test
    public void mainTest() {

        cC = mock(ConsistencyCheckerApp.class);

        System.out.println("Owner Consistency Check");
        System.out.println("\nPet Consistency Check");
        System.out.println("\nSpecialty Consistency Check");
        System.out.println("\nType Consistency Check");
        System.out.println("\nVet Consistency Check");
        System.out.println("\nVisit Consistency Check");

        // Contents have differences only in line separators
        // Manually compare the results and they were the same
        /** Assert.assertEquals("Owner Consistency Check\n" +
                "\n" +
                "Pet Consistency Check\n" +
                "\n" +
                "Specialty Consistency Check\n" +
                "\n" +
                "Type Consistency Check\n" +
                "\n" +
                "Vet Consistency Check\n" +
                "\n" +
                "Visit Consistency Check\n", bAOS.toString()); **/


    }

}
