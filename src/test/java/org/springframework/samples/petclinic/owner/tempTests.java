package org.springframework.samples.petclinic.owner;

import org.junit.Test;

import static org.mockito.BDDMockito.given;

public class tempTests {
    @Test
    public void testController(){
        OwnerRepository owners;
        Owner george = new Owner();
        george.setId(1);
        george.setFirstName("George");
        george.setLastName("Franklin");
        george.setAddress("110 W. Liberty St.");
        george.setCity("Madison");
        george.setTelephone("6085551023");
        //given(owners.findById(1)).willReturn(george);


    }

}
