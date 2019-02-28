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

import org.junit.Test;

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

    @Test
    public void testSerialization() {
        Vet vet = new Vet();
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
    public void  addSpecialtiesTest(){
        Vet vet = new Vet();

        Specialty specialty = mock(Specialty.class);
        when(specialty.getName()).thenReturn("test");
        vet.addSpecialty(specialty);

        assertEquals(vet.getNrOfSpecialties(), 1);
    }
    @Test
    public void getSpecialtiesTest(){
        Vet vet = new Vet();
        Specialty specialty1 = mock(Specialty.class);
        when(specialty1.getName()).thenReturn("a");
        Specialty specialty2 = mock(Specialty.class);
        when(specialty2.getName()).thenReturn("b");
        vet.addSpecialty(specialty1);
        vet.addSpecialty(specialty2);
        List<Specialty> sortedSpecs =new ArrayList<>();
        sortedSpecs.add(0, specialty1);
        sortedSpecs.add(1, specialty2);
        assertEquals(vet.getSpecialties(),sortedSpecs);

        Specialty specialty3 = mock(Specialty.class);
        when(specialty3.getName()).thenReturn("a");
        Specialty specialty4 = mock(Specialty.class);
        when(specialty4.getName()).thenReturn("b");
        vet.addSpecialty(specialty3);
        vet.addSpecialty(specialty4);
        List<Specialty> unSortedSpecs =new ArrayList<>();
        sortedSpecs.add(0, specialty4);
        sortedSpecs.add(1, specialty3);
        assertNotEquals(vet.getSpecialties(),unSortedSpecs);

    }

}
/*
Analysis for vet package:
1. Vets class has two method. One is for getVetList, and another is for setVetList. Since getter and setter are no need to be tested, this class doesn’t have any tests. 
2.  Specialty class is extended from NamedEntity class, which has getName(), setName(), and toString() methods.
3. VetController class has showVetList() and ShowresourceList(). showVetList() is for Object-Xml mapping, and ShowresourceList() is for JSon/Object mapping. There are already tests for this controller class, which use mockMvc to test the controller logic without connecting to the real web server.
4. VetRepository is an interface, since we only test concrete classes, there is no test for this class.
5. Vet class have methods getSpecialtiesInternal(), setSpecialtiesInternal(), getSpecialties(), getNrOfSpecialties(), and addSpecialty().  getSpecialtiesInternal() and  setSpecialtiesInternal() are protected methods, and they are only for internal use. New tests will be added for the rest methods.

 */