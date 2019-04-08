/*
 * Copyright 2012-2018 the original author or authors.
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
package org.springframework.samples.petclinic.owner;

import org.springframework.samples.petclinic.FeatureToggles.FeatureToggles;
import org.springframework.samples.petclinic.incrementalreplication.IncrementalReplication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.samples.petclinic.incrementalreplication.IncrementalReplicationChecker;
import org.springframework.samples.petclinic.shadowRead.PetShadowRead;
import org.springframework.samples.petclinic.sqlite.SQLitePetHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 */
@Controller
@RequestMapping("/owners/{ownerId}")
class PetController {
    @Value("${spring.profiles.active}")
    private String activeProfile;

    private static final String VIEWS_PETS_CREATE_OR_UPDATE_FORM = "pets/createOrUpdatePetForm";
    private final PetRepository pets;
    private final OwnerRepository owners;
    private Pet pet;

    public PetController(PetRepository pets, OwnerRepository owners) {
        this.pets = pets;
        this.owners = owners;
    }

    @ModelAttribute("types")
    public Collection<PetType> populatePetTypes() {
        return this.pets.findPetTypes();
    }

    @ModelAttribute("owner")
    public Owner findOwner(@PathVariable("ownerId") int ownerId) {
        return this.owners.findById(ownerId);
    }

    @InitBinder("owner")
    public void initOwnerBinder(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }

    @InitBinder("pet")
    public void initPetBinder(WebDataBinder dataBinder) {
        dataBinder.setValidator(new PetValidator());
    }

    //@GetMapping("/pets/new")
    public String initCreationForm(Owner owner, ModelMap model) {
        return this.initCreationForm(owner,model,new Pet());
    }

    @GetMapping("/pets/new")
    public String initCreationForm(Owner owner, ModelMap model, Pet pet) {

        if (FeatureToggles.isEnablePetAdd) {

            this.pet = pet;

            owner.addPet(pet);
            model.put("pet", pet);

            //TODO change to logger info//
                //System.out.println("Shadow Read for visits passed from controller");
            return VIEWS_PETS_CREATE_OR_UPDATE_FORM;

        }
        return null;
    }

    @PostMapping("/pets/new")
    public String processCreationForm(Owner owner, @Valid Pet pet, BindingResult result, ModelMap model) {
        if (StringUtils.hasLength(pet.getName()) && pet.isNew() && owner.getPet(pet.getName(), true) != null){
            result.rejectValue("name", "duplicate", "already exists");
        }
        owner.addPet(pet);
        //TODO I don't think this should be here ***
//        if (activeProfile.equals("mysql")) {
//            if (isEnableShadowWrite) {
//                SQLitePetHelper.getInstance().insert(pet.getName(), pet.getBirthDate().toString(), pet.getType().getId(), owner.getId());
//
//            }
//            if (FeatureToggles.isEnablePetAddIR) {
//                IncrementalReplication.addToCreateList("pets," + pet.getName() + "," + pet.getBirthDate().toString() + "," + pet.getType().getId() + "," + owner.getId());
//                IncrementalReplication.incrementalReplication();
//            }
//        }
        if (result.hasErrors()) {
            model.put("pet", pet);
            return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
        } else {
            this.pets.save(pet);
            if (activeProfile.equals("mysql")) {
                if (FeatureToggles.isEnableShadowWrite) {
                    //System.out.println(pet.getName() + " insert");
                    int responseRowId = SQLitePetHelper.getInstance().insert(pet.getName(), pet.getBirthDate().toString(), pet.getType().getId(), owner.getId());
                    //System.out.println(responseRowId + "responseRowId");
                    FeatureToggles.isEnableIncrementDate = true;
                    // call incremental consistency check
                    boolean isConsistency = IncrementalReplicationChecker.isConsistency(responseRowId,"pets");
                    //System.out.println(isConsistency + "result");
                    // if incremental consistency check is not good call incremental replication
                    if (FeatureToggles.isEnableIR && isConsistency == false) {
                        FeatureToggles.isEnableIncrementDate = false;
                       // System.out.println("incremental replication!!!");
                       // System.out.println(pet.getBirthDate().toString()+"!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                        IncrementalReplication.addToCreateList("pets," + responseRowId + "," + pet.getName()+ "," + pet.getBirthDate().toString()+ "," + pet.getType().getId()+ "," + owner.getId());
                        IncrementalReplication.incrementalReplication();
                        FeatureToggles.isEnableIncrementDate = true;
                    }
                }
        }
            return "redirect:/owners/{ownerId}";
        }
    }

    public String processCreationFormTest(Owner owner, @Valid Pet pet, BindingResult result, ModelMap model) {
        if (StringUtils.hasLength(pet.getName()) && pet.isNew() && owner.getPet(pet.getName(), true) != null){
            result.rejectValue("name", "duplicate", "already exists");
        }
        owner.addPet(pet);
        if (result.hasErrors()) {
            model.put("pet", pet);
            return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
        } else {
            this.pets.save(pet);
            return "redirect:/owners/{ownerId}";
        }
    }

    @GetMapping("/pets/{petId}/edit")
    public String initUpdateForm(@PathVariable("petId") int petId, ModelMap model) {

        if (FeatureToggles.isEnablePetEdit) {
            Pet pet = this.pets.findById(petId);
            //shadow read for pet
            PetShadowRead petShadowRead = new PetShadowRead();
            try {
                //Shadow read return problem id
                if (pet != null) {
                    //this is an if filter for test since the test did not mock or setup all the details of the pet;
                    //TODO add mocked or setup dedetails for test
                    if (pet.getType() != null) {
                        int inconsistency_id = petShadowRead.checkPet(pet);
                        //if it's not good call incremental replication
                        if (inconsistency_id > -1) {
                            //increamental replication
                            IncrementalReplication.addToUpdateList("pets," + inconsistency_id + "," + pet.getName() + "," + pet.getBirthDate().toString() + "," + pet.getType().getId() + "," + pet.getOwner().getId());
                            IncrementalReplication.incrementalReplication();
                        } else {
                            System.out.println("Shadow Read for visits passed from controller");
                        }
                    }
                }
            }catch(Exception e){
                e.getMessage();
            }
            model.put("pet", pet);
            return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
        }
        return null;
    }

    @PostMapping("/pets/{petId}/edit")
    public String processUpdateForm(@Valid Pet pet, BindingResult result, Owner owner, ModelMap model) {
        if (result.hasErrors()) {
            pet.setOwner(owner);
            model.put("pet", pet);
            return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
        } else {
            owner.addPet(pet);
            this.pets.save(pet);

            if (FeatureToggles.isEnableShadowWrite) {
                SQLitePetHelper.getInstance().insert(pet.getName(), pet.getBirthDate().toString(), pet.getType().getId(), owner.getId());
                //System.out.println(pet.getName() + " update");
                int responseRowId = SQLitePetHelper.getInstance().update(pet, owner);
                //System.out.println(responseRowId + "responseRowId");
                FeatureToggles.isEnableIncrementDate = true;
                // call incremental consistency check
                boolean isConsistency = IncrementalReplicationChecker.isConsistency(responseRowId,"pets");
                //System.out.println(isConsistency + "result");
                // if incremental consistency check is not good call incremental replication
                if (FeatureToggles.isEnableIR && isConsistency == false) {
                    FeatureToggles.isEnableIncrementDate = false;
                    //System.out.println("incremental replication!!!");
                    IncrementalReplication.addToUpdateList("pets," + responseRowId + "," + pet.getName()+ "," + pet.getBirthDate().toString()+ "," + pet.getType().getId()+ "," + owner.getId());
                    IncrementalReplication.incrementalReplication();
                    FeatureToggles.isEnableIncrementDate = true;
                }
            }

//            if (FeatureToggles.isEnablePetEditIR) {
//                IncrementalReplication.addToUpdateList("pets," + pet.getId() + "," + pet.getName() + "," + pet.getBirthDate().toString() + "," + pet.getType().getId() + "," + owner.getId());
//                IncrementalReplication.incrementalReplication();
//
//            }
            return "redirect:/owners/{ownerId}";
        }
    }
}
