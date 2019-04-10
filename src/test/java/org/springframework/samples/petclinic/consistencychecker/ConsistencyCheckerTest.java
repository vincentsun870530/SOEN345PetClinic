package org.springframework.samples.petclinic.consistencychecker;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.samples.petclinic.vet.Vet;
import org.springframework.samples.petclinic.visit.Visit;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class ConsistencyCheckerTest {

    ConsistencyChecker cC = new ConsistencyChecker();

    @Mock
    private ResultSet rS;

    @Test
    public void vetRSetTest() throws SQLException {

        when(rS.getInt(100)).thenReturn(1000);
        when(rS.getString("first_name")).thenReturn("First name 1");
        when(rS.getString("last_name")).thenReturn("Last name 1");

        Vet vet = new Vet();
        vet.setId(1000);
        vet.setFirstName("First name 1");
        vet.setLastName("Last name 1");

        List<Vet> vetList = new ArrayList<>();
        vetList.add(vet);

        rS.moveToInsertRow();
        rS.updateInt(1, 1000);
        rS.updateString(2, "First name 1");
        rS.updateString(3, "Last name 1");
        rS.insertRow();

        Assert.assertNotNull(vetList);
        // private method
        // Assert.assertEquals( , ConsistencyChecker.vetRSet(rS, vetList));

    }

    @Test
    public void visitConsCheckTest() {

    }

    @Test
    public void visitRSetTest() throws SQLException {

        when(rS.getInt(100)).thenReturn(1000);
        when(rS.getInt(200)).thenReturn(2000);
        when(rS.getString("visit_date")).thenReturn("2019-03-03");
        when(rS.getString("description")).thenReturn("1st description");

        when(rS.getInt(101)).thenReturn(1001);
        when(rS.getInt(202)).thenReturn(2002);
        when(rS.getString("visit 2")).thenReturn("2nd visit");
        when(rS.getString("description 2")).thenReturn("2nd description");

        Visit visit = new Visit();
        visit.setId(1000);
        visit.setPetId(2000);
        visit.setDate(LocalDate.of(2019, 03, 03));
        visit.setDescription("1st visit description");

        List<Visit> visitList = new ArrayList<>();
        visitList.add(visit);

        rS.moveToInsertRow();
        rS.updateInt(1, 1000);
        rS.updateInt(2, 2000);
        rS.updateString(3, "2019-03-03");
        rS.updateString(4, "1st description");
        rS.insertRow();


        Assert.assertNotNull(visitList);
        // private method
        // Assert.assertEquals( , ConsistencyChecker.visitRSet(rS, visitList));

    }

}
