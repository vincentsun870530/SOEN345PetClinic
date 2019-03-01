/*
 * Copyright 2016-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.vet;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.util.SerializationUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author Dave Syer
 *
 */
public class VetTests {

    Vet vet;

    @Before
    public void initialize() {
        vet = new Vet();
    }

    @Test
    public void testSerialization() {
        //Vet vet = new Vet();
        vet.setFirstName("Zaphod");
        vet.setLastName("Beeblebrox");
        vet.setId(123);
        Vet other = (Vet) SerializationUtils
                .deserialize(SerializationUtils.serialize(vet));
        assertThat(other.getFirstName()).isEqualTo(vet.getFirstName());
        assertThat(other.getLastName()).isEqualTo(vet.getLastName());
        assertThat(other.getId()).isEqualTo(vet.getId());
    }
    @Test
    public void addSpecialtiesTest(){
        //Vet vet = new Vet();

        Specialty specialty = mock(Specialty.class);
        when(specialty.getName()).thenReturn("test");
        vet.addSpecialty(specialty);

        assertEquals(vet.getNrOfSpecialties(), 1);
    }
    @Test
    public void getSpecialtiesTest(){
        //Vet vet = new Vet();
        Specialty specialty1 = mock(Specialty.class);
        when(specialty1.getName()).thenReturn("a");
        Specialty specialty2 = mock(Specialty.class);
        when(specialty2.getName()).thenReturn("b");
        vet.addSpecialty(specialty1);
        vet.addSpecialty(specialty2);
        //Real list
        /*
        List<Specialty> sortedSpecs =new ArrayList<>();
        sortedSpecs.add(0, specialty1);
        sortedSpecs.add(1, specialty2);*/
        List<Specialty> sortedSpecs =mock(ArrayList.class);
        when(sortedSpecs.get(0)).thenReturn(specialty1);
        when(sortedSpecs.get(1)).thenReturn(specialty2);
        assertEquals(vet.getSpecialties().get(0),sortedSpecs.get(0));
        assertEquals(vet.getSpecialties().get(1),sortedSpecs.get(1));
        Specialty specialty3 = mock(Specialty.class);
        when(specialty3.getName()).thenReturn("a");
        Specialty specialty4 = mock(Specialty.class);
        when(specialty4.getName()).thenReturn("b");
        vet.addSpecialty(specialty3);
        vet.addSpecialty(specialty4);

       //Real list
       /* List<Specialty> unSortedSpecs =mock(List.class);
        sortedSpecs.add(0, specialty4);
        sortedSpecs.add(1, specialty3);*/

        List<Specialty> unSortedSpecs =mock(List.class);
        when(sortedSpecs.get(0)).thenReturn(specialty4);
        when(sortedSpecs.get(1)).thenReturn(specialty3);
        assertNotEquals(vet.getSpecialties().get(0),unSortedSpecs.get(0));
        assertNotEquals(vet.getSpecialties().get(1),unSortedSpecs.get(1));
    }

}