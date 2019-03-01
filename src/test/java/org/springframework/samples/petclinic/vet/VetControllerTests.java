package org.springframework.samples.petclinic.vet;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.*;
import static org.mockito.Mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Collection;
import java.util.Map;
import static org.junit.Assert.assertTrue;
/**
 * Test class for the {@link VetController}
 */
@RunWith(SpringRunner.class)
@WebMvcTest(VetController.class)
public class VetControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VetRepository vets;

    @Before
    public void setup() {
        Vet james = new Vet();
        james.setFirstName("James");
        james.setLastName("Carter");
        james.setId(1);

        Vet helen = new Vet();
        helen.setFirstName("Helen");
        helen.setLastName("Leary");
        helen.setId(2);

        Specialty radiology = new Specialty();
        //Specialty radiology = mock(Specialty.class);
        //when(radiology.getId()).thenReturn(1);
        //when(radiology.getName()).thenReturn("radiology");

        radiology.setId(1);
        radiology.setName("radiology");
        helen.addSpecialty(radiology);
        given(this.vets.findAll()).willReturn(Lists.newArrayList(james, helen));
    }

    @Test
    public void testShowVetListHtml() throws Exception {
        mockMvc.perform(get("/vets.html"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("vets"))
            .andExpect(view().name("vets/vetList"));
    }

    @Test
    public void testShowResourcesVetList() throws Exception {
        ResultActions actions = mockMvc.perform(get("/vets")
            .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        actions.andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$.vetList[0].id").value(1));
    }
    @Test
    public void test_mockito_ShowVetList() throws Exception{
        Vets vets = mock(Vets.class);
        Map<String, Object>  mock_model = mock(Map.class);
        VetRepository vetRepository = mock(VetRepository.class);
        VetController vetController = new VetController(vetRepository);

        String str = vetController.showVetList(mock_model,vets);

        verify(vets).getVetList();
        verify(mock_model).put("vets", vets);

        assertTrue(str == "vets/vetList");
    }

    @Test
    public void test_mockito_showResourcesVetList() throws Exception{
        Vets vets = mock(Vets.class);
        VetRepository vetRepository = mock(VetRepository.class);

        VetController vetController = new VetController(vetRepository);

        vetController.showResourcesVetList(vets);
        //verify(vetRepository).findAll();
        verify(vets).getVetList();


    }

}
