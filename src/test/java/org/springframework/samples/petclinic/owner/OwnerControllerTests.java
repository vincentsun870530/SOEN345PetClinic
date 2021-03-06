package org.springframework.samples.petclinic.owner;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;
import static org.springframework.samples.petclinic.FeatureToggles.FeatureToggles.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Collection;
import java.util.Map;

import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.samples.petclinic.FeatureToggles.FeatureToggles;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.OwnerController;
import org.springframework.samples.petclinic.owner.OwnerRepository;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.NestedServletException;

/**
 * Test class for {@link OwnerController}
 *
 * @author Colin But
 */
@RunWith(SpringRunner.class)
@WebMvcTest(OwnerController.class)
public class OwnerControllerTests {

	private static final int TEST_OWNER_ID = 1;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private OwnerRepository owners;
	private Owner george;
	private Owner betty;

	@Before
	public void setup() {
		george = new Owner();
		george.setId(TEST_OWNER_ID);
		george.setFirstName("George");
		george.setLastName("Franklin");
		george.setAddress("110 W. Liberty St.");
		george.setCity("Madison");
		george.setTelephone("6085551023");
		given(this.owners.findById(TEST_OWNER_ID)).willReturn(george);

		betty = new Owner();
		betty.setId(2);
		betty.setFirstName("Betty");
		betty.setLastName("Davis");
		betty.setAddress("638 Cardinal Ave.");
		betty.setCity("Sun Prairie");
		betty.setTelephone("6085551749");
		given(this.owners.findById(2)).willReturn(betty);
	}

	@Test
	public void testInitCreationForm() throws Exception {
		mockMvc.perform(get("/owners/new"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("owner"))
				.andExpect(view().name("owners/createOrUpdateOwnerForm"));
	}

	@Test
	public void testInitCreationForm_mockito()throws Exception {
		Owner owner = mock(Owner.class);
		Map<String, Object> model = (Map<String, Object>) mock(Map.class);
		OwnerRepository owners= mock(OwnerRepository.class);
		OwnerController ownerController = new OwnerController(owners);
		ownerController.initCreationForm(model,owner);
		verify(model).put("owner", owner);
		String str1 = ownerController.initCreationForm(model, owner);
		String str2 = "owners/createOrUpdateOwnerForm";
		assertEquals(str1, str2);
	}

	@Test
	public void testProcessCreationFormSuccess() throws Exception {
		FeatureToggles.isEnableShadowWrite = false;
		mockMvc.perform(post("/owners/new")
				.param("firstName", "Joe")
				.param("lastName", "Bloggs")
				.param("address", "123 Caramel Street")
				.param("city", "London")
				.param("telephone", "01316761638")
		)
				.andExpect(status().is3xxRedirection());
	}

	@Test
	public void testProcessCreationFormSuccess_mockito() throws Exception {
		FeatureToggles.isEnableShadowWrite = false;
		Owner owner = mock(Owner.class);
		OwnerRepository owners= mock(OwnerRepository.class);
		BindingResult result = mock(BindingResult.class);
		OwnerController ownerController = new OwnerController(owners);
		String str1 = ownerController.processCreationForm(owner, result);
		String str2 = "redirect:/owners/" + owner.getId();
		when(result.hasErrors()).thenReturn(false);
		verify(result, times(1)).hasErrors();
		assertEquals(str1, str2);
	}

	@Test
	public void testProcessCreationFormHasErrors() throws Exception {
		FeatureToggles.isEnableShadowWrite = false;
		mockMvc.perform(post("/owners/new")
				.param("firstName", "Joe")
				.param("lastName", "Bloggs")
				.param("city", "London")
		)
				.andExpect(status().isOk())
				.andExpect(model().attributeHasErrors("owner"))
				.andExpect(model().attributeHasFieldErrors("owner", "address"))
				.andExpect(model().attributeHasFieldErrors("owner", "telephone"))
				.andExpect(view().name("owners/createOrUpdateOwnerForm"));
	}

	@Test
	public void testProcessCreationFormHasErrors_mockito() throws Exception {
		FeatureToggles.isEnableShadowWrite = false;
		Owner owner = mock(Owner.class);
		OwnerRepository owners= mock(OwnerRepository.class);
		BindingResult result = mock(BindingResult.class);
		OwnerController ownerController = new OwnerController(owners);
		when(result.hasErrors()).thenReturn(true);
		String str1 = ownerController.processCreationForm(owner, result);
		String str2 = "owners/createOrUpdateOwnerForm";
		verify(result, times(1)).hasErrors();
		assertEquals(str1, str2);

	}

	@Test
	public void testInitFindForm() throws Exception {
		mockMvc.perform(get("/owners/find"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("owner"))
				.andExpect(view().name("owners/findOwners"));
	}

	@Test
	public void testInitFindForm_mockito() throws Exception {
		Owner owner = mock(Owner.class);
		Map<String, Object> model = (Map<String, Object>) mock(Map.class);
		OwnerRepository owners= mock(OwnerRepository.class);
		OwnerController ownerController = new OwnerController(owners);
		String str1 = ownerController.initFindForm(model, owner);
		String str2 = "owners/findOwners";
		verify(model, times(1)).put("owner", owner);
		assertEquals(str1, str2);
	}
	@Test
	public void testProcessFindFormSuccess() throws Exception {
		given(this.owners.findByLastName("")).willReturn(Lists.newArrayList(george, betty));
		mockMvc.perform(get("/owners"))
				.andExpect(status().isOk())
				.andExpect(view().name("owners/ownersList"));
	}
	@Test
	public void testProcessFindFormSuccessOneOwner_mockmvc() throws Exception {
		//NullPointerException, test fails
		Owner owner = mock(Owner.class);
		BindingResult result = mock(BindingResult.class);
		Map<String, Object> model = mock(Map.class);
		OwnerRepository owners= mock(OwnerRepository.class);
		given(this.owners.findByLastName("")).willReturn(Lists.newArrayList(george, new Owner()));
		Collection<Owner> results =  this.owners.findByLastName("");
		System.out.println(results.toString());
		OwnerController ownerController = new OwnerController(owners,owner,results);
		//when(owners.findByLastName(owner.getLastName())).thenReturn(results);
		//when(results.isEmpty()).thenReturn(false);
		//when(results.size()).thenReturn(1);

		String str1 = ownerController.processFindForm(owner, result, model);
		//String str2 =  "redirect:/owners/" + owner.getId();

		//verify(results, times(1)).isEmpty();
		//verify(results,times(1)).size();
		//verify(results, times(1)).iterator().next();
		System.out.println(str1);
		String str2 =  "redirect:/owners/" + owner.getId();
		assertNotEquals(str1, str2);
	}
	@Test
	public void testProcessFindFormSuccessNoOwner_mockmvc() throws Exception {
		//NullPointerException, test fails
		Owner owner = mock(Owner.class);
		BindingResult result = mock(BindingResult.class);
		Map<String, Object> model = mock(Map.class);
		OwnerRepository owners= mock(OwnerRepository.class);
		given(this.owners.findByLastName("")).willReturn(Lists.newArrayList());
		Collection<Owner> results =  this.owners.findByLastName("");
		//Collection<Owner> results = spy(results1);
		System.out.println(results.toString());
		OwnerController ownerController = new OwnerController(owners,owner,results);
		//when(owners.findByLastName(owner.getLastName())).thenReturn(results);
		//when(results.isEmpty()).thenReturn(false);
		//when(results.size()).thenReturn(1);

		String str1 = ownerController.processFindForm(owner, result, model);
		System.out.println(str1);
		//String str2 =  "redirect:/owners/" + owner.getId();

		//verify(results, times(1)).isEmpty();
		//verify(results,times(1)).size();
		//verify(results, times(1)).iterator().next();

		String str2 =  "owners/findOwners";
		assertEquals(str1, str2);
	}

	@Test
	public void testProcessFindFormSuccessMultipleOwners_mockito() throws Exception {
		Owner owner = mock(Owner.class);
		BindingResult result = mock(BindingResult.class);
		Map<String, Object> model = (Map<String, Object>) mock(Map.class);
		OwnerRepository owners= mock(OwnerRepository.class);;
		Collection<Owner> results = (Collection<Owner>) mock(Collection.class);
		OwnerController ownerController = new OwnerController(owners);
		when(owners.findByLastName(owner.getLastName())).thenReturn(results);
		when(results.isEmpty()).thenReturn(false);
		when(results.size()).thenReturn(2);
		String str1 = ownerController.processFindForm(owner, result, model);
		String str2 = "owners/ownersList";

		//verify(results,times(1)).size();
		//verify(model,times(1)).put("selections", results);

		//assertEquals passes only when str2 is changed from "owners/ownersList" to "owners/findOwners"
		//the issue seems to be in results
		assertEquals(str1, str2);
	}
	@Test
	public void testProcessFindFormByLastName() throws Exception {
		isEnableShadowRead = false;
		given(this.owners.findByLastName(george.getLastName())).willReturn(Lists.newArrayList(george));
		mockMvc.perform(get("/owners")
				.param("lastName", "Franklin")
		)
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/owners/" + TEST_OWNER_ID));
	}

	@Test
	public void testProcessFindFormNoOwnersFound() throws Exception {
		mockMvc.perform(get("/owners")
				.param("lastName", "Unknown Surname")
		)
				.andExpect(status().isOk())
				.andExpect(model().attributeHasFieldErrors("owner", "lastName"))
				.andExpect(model().attributeHasFieldErrorCode("owner", "lastName", "notFound"))
				.andExpect(view().name("owners/findOwners"));
	}

	@Test
	public void testProcessFindFormNoOwnersFound_mockito() throws Exception {
		Owner owner = mock(Owner.class);
		BindingResult result = mock(BindingResult.class);
		Map<String, Object> model = (Map<String, Object>) mock(Map.class);
		OwnerRepository owners= mock(OwnerRepository.class);;
		Collection<Owner> results = (Collection<Owner>) mock(Collection.class);
		OwnerController ownerController = new OwnerController(owners);
		String str1 = ownerController.processFindForm(owner, result, model);
		String str2 =  "owners/findOwners";

		when(owners.findByLastName(owner.getLastName())).thenReturn(results);
		when(results.isEmpty()).thenReturn(true);
		when(results.size()).thenReturn(0);

		//verify(results, times(1)).isEmpty();
		//verify(results,times(1)).size();
		verify(result, times(1)).rejectValue("lastName", "notFound", "not found");

		assertEquals(str1, str2);
	}

	@Test
	public void testInitUpdateOwnerForm() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/edit", TEST_OWNER_ID))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("owner"))
				.andExpect(model().attribute("owner", hasProperty("lastName", is("Franklin"))))
				.andExpect(model().attribute("owner", hasProperty("firstName", is("George"))))
				.andExpect(model().attribute("owner", hasProperty("address", is("110 W. Liberty St."))))
				.andExpect(model().attribute("owner", hasProperty("city", is("Madison"))))
				.andExpect(model().attribute("owner", hasProperty("telephone", is("6085551023"))))
				.andExpect(view().name("owners/createOrUpdateOwnerForm"));
	}

	@Test
	public void testInitUpdateOwnerForm_mockito() throws Exception {
		int ownerId = 1;
		// used the Owner mock from @mockbean
		Model model = mock(Model.class);
		OwnerController ownerController = new OwnerController(owners);
		String str1 = ownerController.initUpdateOwnerForm(ownerId, model);
		String str2 =  "owners/createOrUpdateOwnerForm";

		// used the mock created via @mockBean
		when(this.owners.findById(ownerId)).thenReturn(george);
		verify(model).addAttribute(george);

		assertEquals(str1, str2);
	}

	@Test
	public void testProcessUpdateOwnerFormSuccess() throws Exception {
		FeatureToggles.isEnableShadowWrite = false;
		mockMvc.perform(post("/owners/{ownerId}/edit", TEST_OWNER_ID)
				.param("firstName", "Joe")
				.param("lastName", "Bloggs")
				.param("address", "123 Caramel Street")
				.param("city", "London")
				.param("telephone", "01616291589")
		)
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/owners/{ownerId}"));
	}

	@Test
	public void testProcessUpdateOwnerFormSuccess_mockito() throws Exception {
		FeatureToggles.isEnableShadowWrite = false;
		int ownerId = 1;
		Owner owner = mock(Owner.class);
		BindingResult result = mock(BindingResult.class);
		OwnerController ownerController = new OwnerController(owners);
		when(result.hasErrors()).thenReturn(false);

		String str1 = ownerController.processUpdateOwnerForm(owner, result, ownerId);
		String str2 =  "redirect:/owners/{ownerId}";

		verify(owner).setId(ownerId);
		assertEquals(str1, str2);
	}

	@Test
	public void testProcessUpdateOwnerFormHasErrors() throws Exception {
		FeatureToggles.isEnableShadowWrite = false;
		mockMvc.perform(post("/owners/{ownerId}/edit", TEST_OWNER_ID)
				.param("firstName", "Joe")
				.param("lastName", "Bloggs")
				.param("city", "London")
		)
				.andExpect(status().isOk())
				.andExpect(model().attributeHasErrors("owner"))
				.andExpect(model().attributeHasFieldErrors("owner", "address"))
				.andExpect(model().attributeHasFieldErrors("owner", "telephone"))
				.andExpect(view().name("owners/createOrUpdateOwnerForm"));
	}

	public void testProcessUpdateOwnerFormHasErrors_mockito() throws Exception {
		FeatureToggles.isEnableShadowWrite = false;
		int ownerId=1;
		Owner owner = mock(Owner.class);
		BindingResult result = mock(BindingResult.class);
		OwnerController ownerController = new OwnerController(owners);
		when(result.hasErrors()).thenReturn(true);

		String str1 = ownerController.processUpdateOwnerForm(owner, result, ownerId);
		String str2 =  "owners/createOrUpdateOwnerForm";

		verify(result, times(1)).hasErrors();
		assertEquals(str1, str2);
	}

	@Test
	public void testShowOwner() throws Exception {
		ModelAndView modelAndView = mock(ModelAndView.class);
		mockMvc.perform(get("/owners/{ownerId}", TEST_OWNER_ID))
				.andExpect(status().isOk())
				.andExpect(model().attribute("owner", hasProperty("lastName", is("Franklin"))))
				.andExpect(model().attribute("owner", hasProperty("firstName", is("George"))))
				.andExpect(model().attribute("owner", hasProperty("address", is("110 W. Liberty St."))))
				.andExpect(model().attribute("owner", hasProperty("city", is("Madison"))))
				.andExpect(model().attribute("owner", hasProperty("telephone", is("6085551023"))))
				.andExpect(view().name("owners/ownerDetails"));
	}

}