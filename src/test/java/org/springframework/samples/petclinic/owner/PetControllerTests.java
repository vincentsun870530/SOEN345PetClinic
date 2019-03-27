package org.springframework.samples.petclinic.owner;

import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.env.Environment;
import org.springframework.samples.petclinic.FeatureToggles.FeatureToggles;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;

import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the {@link PetController}
 *
 * @author Colin But
 */
@RunWith(SpringRunner.class)
@ActiveProfiles({ "test" })
@WebMvcTest(value = PetController.class,
    includeFilters = @ComponentScan.Filter(
                            value = PetTypeFormatter.class,
                            type = FilterType.ASSIGNABLE_TYPE))
public class PetControllerTests {

    private static final int TEST_OWNER_ID = 1;
    private static final int TEST_PET_ID = 1;


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PetRepository pets;

    @MockBean
    private OwnerRepository owners;

    @Before
    public void setup() {
        PetType cat = new PetType();
        cat.setId(3);
        cat.setName("hamster");
        given(this.pets.findPetTypes()).willReturn(Lists.newArrayList(cat));
        given(this.owners.findById(TEST_OWNER_ID)).willReturn(new Owner());
        given(this.pets.findById(TEST_PET_ID)).willReturn(new Pet());

    }
    
    @Test
    public void testInitCreationForm() throws Exception {
        mockMvc.perform(get("/owners/{ownerId}/pets/new", TEST_OWNER_ID))
            .andExpect(status().isOk())
            .andExpect(view().name("pets/createOrUpdatePetForm"))
            .andExpect(model().attributeExists("pet"));
    }
    
    @Test
    public void test_mockito_InitCreationForm() throws Exception{
    	Owner owner = mock(Owner.class);
    	ModelMap model = mock(ModelMap.class);
    	Pet pet = mock(Pet.class);
    	PetRepository pr = mock(PetRepository.class);
    	OwnerRepository or = mock(OwnerRepository.class);
    	PetController petController = new PetController(pr, or);
    	
    	String str = petController.initCreationForm(owner, model, pet);
    	
    	verify(owner).addPet(pet);
    	verify(model).put("pet", pet);
    	
    	assertTrue(str == "pets/createOrUpdatePetForm");
    }

    @Test
    public void testProcessCreationFormSuccess() throws Exception {
        mockMvc.perform(post("/owners/{ownerId}/pets/new", TEST_OWNER_ID)
            .param("name", "Betty")
            .param("type", "hamster")
            .param("birthDate", "2015-02-12")
        )
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/owners/{ownerId}"));
    }

    @Test
    public void testProcessCreationFormHasErrors() throws Exception {
        mockMvc.perform(post("/owners/{ownerId}/pets/new", TEST_OWNER_ID)
            .param("name", "Betty")
            .param("birthDate", "2015-02-12")
        )
            .andExpect(model().attributeHasNoErrors("owner"))
            .andExpect(model().attributeHasErrors("pet"))
            .andExpect(model().attributeHasFieldErrors("pet", "type"))
            .andExpect(model().attributeHasFieldErrorCode("pet", "type", "required"))
            .andExpect(status().isOk())
            .andExpect(view().name("pets/createOrUpdatePetForm"));
    }
    
    @Test
    public void test_mockito_ProcessCreationFormSuccess() throws Exception{
    	Owner owner = mock(Owner.class);
    	ModelMap model = mock(ModelMap.class);
    	Pet pet = mock(Pet.class);
    	BindingResult result = mock(BindingResult.class);
    	PetRepository pr = mock(PetRepository.class);
    	OwnerRepository or = mock(OwnerRepository.class);
    	PetController petController = new PetController(pr, or);
    	
    	when(result.hasErrors()).thenReturn(false);
    	String str = petController.processCreationFormTest(owner, pet, result, model);
    	
    	verify(pet,atLeast(1)).getName();
    	verify(pet,atMost(2)).getName();
    	verify(owner,atMost(1)).getPet(pet.getName(), true);
    	verify(result,times(0)).rejectValue("name", "duplicate", "already exists");
    	verify(owner).addPet(pet);
    	verify(result).hasErrors();
    	//verify(model,times(0)).put("pet", pet);
    	verifyZeroInteractions(model);
    	assertTrue(str == "redirect:/owners/{ownerId}");
    	
    }
    
    @Test
    public void test_mockito_ProcessCreationFormHasErrors() throws Exception{
    	Owner owner = mock(Owner.class);
    	ModelMap model = mock(ModelMap.class);
    	Pet pet = mock(Pet.class);
    	BindingResult result = mock(BindingResult.class);
    	PetRepository pr = mock(PetRepository.class);
    	OwnerRepository or = mock(OwnerRepository.class);
    	PetController petController = new PetController(pr, or);
    	
    	when(pet.getName()).thenReturn("Kitty");
    	when(pet.isNew()).thenReturn(true);
    	when(owner.getPet("Kitty", true)).thenReturn(pet);
    	when(result.hasErrors()).thenReturn(true);
    	String str = petController.processCreationFormTest(owner, pet, result, model);
    	
    	verify(pet,times(2)).getName();
    	verify(pet,times(1)).isNew();
    	verify(owner,times(1)).getPet(pet.getName(), true);
    	verify(result,times(1)).rejectValue("name", "duplicate", "already exists");
    	verify(owner).addPet(pet);
    	verify(result).hasErrors();
    	verify(model).put("pet", pet);
    	
    	assertTrue(str == "pets/createOrUpdatePetForm");
    }

    @Test
    public void testInitUpdateForm() throws Exception {
        //switch off Date incremental, keep original test
        FeatureToggles.isEnableIncrementDate = false;
        mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/edit", TEST_OWNER_ID, TEST_PET_ID))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("pet"))
            .andExpect(view().name("pets/createOrUpdatePetForm"));

        FeatureToggles.isEnableIncrementDate = true;
    }

    @Test
    public void testProcessUpdateFormSuccess() throws Exception {
        mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/edit", TEST_OWNER_ID, TEST_PET_ID)
            .param("name", "Betty")
            .param("type", "hamster")
            .param("birthDate", "2015-02-12")
        )
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/owners/{ownerId}"));
    }

    @Test
    public void testProcessUpdateFormHasErrors() throws Exception {
        //switch off Date incremental, keep original test
        FeatureToggles.isEnableIncrementDate = false;
        mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/edit", TEST_OWNER_ID, TEST_PET_ID)
            .param("name", "Betty")
            .param("birthDate", "2015/02/12")
        )
            .andExpect(model().attributeHasNoErrors("owner"))
            .andExpect(model().attributeHasErrors("pet"))
            .andExpect(status().isOk())
            .andExpect(view().name("pets/createOrUpdatePetForm"));
    }

}
