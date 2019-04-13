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

import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.samples.petclinic.FeatureToggles.timeAnalytics;
import org.springframework.samples.petclinic.mysql.MySQLJDBCDriverConnection;
import org.springframework.samples.petclinic.shadowRead.OwnerShadowRead;
import org.springframework.samples.petclinic.shadowRead.PetShadowRead;
import org.springframework.samples.petclinic.shadowRead.PetTypeShadowRead;
import org.springframework.samples.petclinic.sqlite.SQLiteDBConnector;
import org.springframework.samples.petclinic.FeatureToggles.FeatureToggles;
import org.springframework.samples.petclinic.incrementalreplication.IncrementalReplication;
import org.springframework.samples.petclinic.incrementalreplication.IncrementalReplicationChecker;
import org.springframework.samples.petclinic.sqlite.SQLiteOwnerHelper;
import org.springframework.samples.petclinic.visit.Visit;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.springframework.samples.petclinic.FeatureToggles.FeatureToggles.isEnableShadowRead;
import static org.springframework.samples.petclinic.FeatureToggles.FeatureToggles.isEnableShadowWrite;


/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 */
@Controller
class OwnerController {

    private static final String VIEWS_OWNER_CREATE_OR_UPDATE_FORM = "owners/createOrUpdateOwnerForm";
    private final OwnerRepository owners;
    private Owner owner;
    private Collection<Owner> results;
    private static Logger log = LoggerFactory.getLogger(OwnerController.class);
    private static org.apache.logging.log4j.Logger timeLogAnalytics = LogManager.getLogger("WelcomeFeature");
    
    @Autowired
    public OwnerController(OwnerRepository clinicService) {
        this.owners = clinicService;
    }

    public OwnerController(OwnerRepository clinicService, Owner owner, Collection<Owner> results) {
        this.owners = clinicService;
        this.owner = owner;
        this.results = results;
    }


    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }


    public String initCreationForm(Map<String, Object> model) {
        return this.initCreationForm(model, new Owner());
    }

    // dependency injection
    @GetMapping("/owners/new")
    public String initCreationForm(Map<String, Object> model, Owner owner) {

        if (FeatureToggles.isEnableOwnerCreate == true) {
            this.owner = owner;
            model.put("owner", owner);
            return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
        }
        return null;
    }

    @PostMapping("/owners/new")
    public String processCreationForm(@Valid Owner owner, BindingResult result) {
        if (result.hasErrors()) {
            return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
        } else {
            this.owners.save(owner);
            //Shadow write
            if (FeatureToggles.isEnableShadowWrite) {
                //To make db difference
                System.out.println(owner.getFirstName() + " " + owner.getLastName() + " insert");
                int responseRowId = SQLiteOwnerHelper.getInstance().insert(owner.getFirstName(), owner.getLastName(), owner.getAddress(), owner.getCity(), owner.getTelephone());
                System.out.println(responseRowId + "responseRowId");
                FeatureToggles.isEnableIncrementDate = true;
                // call incremental consistency check
                boolean isConsistency = IncrementalReplicationChecker.isConsistency(responseRowId, "owners");
                System.out.println(isConsistency + "result");
                // if incremental consistency check is not good call incremental replication
                if (FeatureToggles.isEnableIR && isConsistency == false) {
                    FeatureToggles.isEnableIncrementDate = false;
                    //System.out.println("incremental replication!!!");
                    IncrementalReplication.addToCreateList("owners," + responseRowId + "," + owner.getFirstName() + "," + owner.getLastName() + "," + owner.getAddress() + "," + owner.getCity() + "," + owner.getTelephone());
                    IncrementalReplication.incrementalReplication();
                    FeatureToggles.isEnableIncrementDate = true;
                }
            }

            return "redirect:/owners/" + owner.getId();
        }
    }


    public String initFindForm(Map<String, Object> model) {
        return this.initFindForm(model, new Owner());
    }

    // dependency injection
    @GetMapping("/owners/find")
    public String initFindForm(Map<String, Object> model, Owner owner) {
        if(FeatureToggles.Feature2){
            timeAnalytics.endTime = System.nanoTime();
            timeLogAnalytics.info("Elapsed Time (ms) : " + timeAnalytics.elapsedTimeMS() + " Legacy Welcome : " + FeatureToggles.welcomePageToggle);
            timeAnalytics.resetTimeAnalystics();
        }

        if (FeatureToggles.isEnableOwnerPage) {
            model.put("owner", owner);
            return "owners/findOwners";
        }
        return null;
    }

    @GetMapping("/owners")
    public String processFindForm(Owner owner, BindingResult result, Map<String, Object> model) {
        if (FeatureToggles.isEnableOwnerFind) {
            // allow parameterless GET request for /owners to return all records
            if (owner.getLastName() == null) {
                owner.setLastName(""); // empty string signifies broadest possible search
            }

            // find owners by last name
            //this.results = this.owners.findByLastName(owner.getLastName());
            setResults(owner);
            System.out.println(results.toString());

            if (results.isEmpty()) {
                // no owners found
                result.rejectValue("lastName", "notFound", "not found");
                return "owners/findOwners";
            } else if (results.size() == 1) {
                // 1 owner found
                owner = results.iterator().next();

                //shadow read for owner
                if (isEnableShadowRead == true) {
                    OwnerShadowRead ownerShadowReader = new OwnerShadowRead();
                    log.trace(owner.getId() + " Owner Id from controller");
                    //Shadow read return problem id
                    int inconsistency_id = ownerShadowReader.checkOwner(owner);
                    //if it's not good call incremental replication
                    if (inconsistency_id > -1) {
                        IncrementalReplication.addToUpdateList("owners" + "," + owner.getId() + "," + owner.getFirstName() + "," + owner.getLastName() + "," + owner.getAddress() + "," + owner.getCity() + "," + owner.getTelephone());
                        IncrementalReplication.incrementalReplication();
                    }
                }
                return "redirect:/owners/" + owner.getId();
            } else {
                // multiple owners found
                if (FeatureToggles.isEnableShadowRead) {
                    OwnerShadowRead ownerShadowReader = new OwnerShadowRead();
                    try {
                        //shadow read for owner
                        for (Owner own : results) {
                            log.trace(own.getId() + " Owner Id from controller");
                            //Shadow read return problem id
                            int inconsistency_id = ownerShadowReader.checkOwner(own);
                            //if it's not good call incremental replication
                            if (inconsistency_id > -1) {
                                IncrementalReplication.addToUpdateList("owners" + "," + owner.getId() + "," + owner.getFirstName() + "," + owner.getLastName() + "," + owner.getAddress() + "," + owner.getCity() + "," + owner.getTelephone());
                                IncrementalReplication.incrementalReplication();
                            }
                        }
                    } catch (Exception e) {
                        e.getMessage();
                    }
                }
                model.put("selections", results);
                return "owners/ownersList";

            }
        }
        return null;
    }

    public void setResults(Owner owner) {
        this.results = this.owners.findByLastName(owner.getLastName());
    }

    @GetMapping("/owners/{ownerId}/edit")
    public String initUpdateOwnerForm(@PathVariable("ownerId") int ownerId, Model model) throws SQLException {

        if (FeatureToggles.isEnableOwnerEdit) {
            Owner owner = this.owners.findById(ownerId);
            model.addAttribute(owner);
            return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
        }
        return null;
    }

    @PostMapping("/owners/{ownerId}/edit")
    public String processUpdateOwnerForm(@Valid Owner owner, BindingResult result, @PathVariable("ownerId") int ownerId) {
        if (result.hasErrors()) {
            return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
        } else {
            owner.setId(ownerId);
            this.owners.save(owner);
            if (FeatureToggles.isEnableOwnerEditIR) {
                IncrementalReplication.addToUpdateList("owners" + "," + (owner.getId()).toString() + "," + owner.getFirstName() + "," + owner.getLastName() + "," + owner.getAddress() + "," + owner.getCity() + "," + owner.getTelephone());
                IncrementalReplication.incrementalReplication();
            }
//            if (FeatureToggles.isEnableShadowWrite) {
//                //To make db difference
//                System.out.println(owner.getFirstName() + " " + owner.getLastName() + " update");
//                // Implement an update Form
//                int responseRowId = SQLiteOwnerHelper.getInstance().update(owner);
//                System.out.println(responseRowId + "responseRowId");
//                FeatureToggles.isEnableIncrementDate = true;
//                // call incremental consistency check
//                boolean isConsistency = IncrementalReplicationChecker.isConsistency(responseRowId,"visits");
//                System.out.println(isConsistency+"result");
//                // if incremental consistency check is not good call incremental replication
//                if (FeatureToggles.isEnableIR && isConsistency == false) {
//                    FeatureToggles.isEnableIncrementDate = false;
//                    //System.out.println("incremental replication!!!");
//                    IncrementalReplication.addToUpdateList("owners," + (owner.getId()).toString() + "," + owner.getFirstName() + "," + owner.getLastName() + "," + owner.getAddress() + "," + owner.getCity() + "," + owner.getTelephone());
//                    IncrementalReplication.incrementalReplication();
//                    FeatureToggles.isEnableIncrementDate = true;
//                }
//            }

            return "redirect:/owners/{ownerId}";
        }
    }

    /**
     * Custom handler for displaying an owner.
     *
     * @param ownerId the ID of the owner to display
     * @return a ModelMap with the model attributes for the view
     */

    @GetMapping("/owners/{ownerId}")
    public ModelAndView showOwner(@PathVariable("ownerId") int ownerId) {
        return this.showOwner(ownerId, new ModelAndView("owners/ownerDetails"));
    }

    public ModelAndView showOwner(@PathVariable("ownerId") int ownerId, ModelAndView modelAndView) {
        ModelAndView mav = modelAndView;

        Owner owner = this.owners.findById(ownerId);
        List<Pet> pets = owner.getPets();

        //shadow read for pet
        PetShadowRead petShadowRead = new PetShadowRead();
        PetTypeShadowRead petTypeShadowRead = new PetTypeShadowRead();
        try {
            for (Pet pet : pets) {
                if (pet != null) {
                    PetType petType = pet.getType();
                    if (petType != null) {
                        //region shadow read for pet type
                        int petType_inconsistency_id = petTypeShadowRead.checkPetType(petType);
                        if (petType_inconsistency_id > -1) {
                            petTypeShadowRead.incrementalReplicationAdapter(petType);
                        }
                        //endregion shadow read for pet type end

                        int pet_inconsistency_id = petShadowRead.checkPet(pet);
                        if (pet_inconsistency_id > -1) {
                            //increamental replication
                            IncrementalReplication.addToUpdateList("pets," + pet_inconsistency_id
                                    + "," + pet.getName() + "," + pet.getBirthDate().toString()
                                    + "," + pet.getType().getId() + "," + pet.getOwner().getId());
                            IncrementalReplication.incrementalReplication();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.getMessage();
        }

        mav.addObject(owner);
        return mav;
    }

}


























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
 *//*

package org.springframework.samples.petclinic.owner;

import org.apache.logging.log4j.LogManager;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.samples.petclinic.FeatureToggles.RandomToggle;
import org.springframework.samples.petclinic.FeatureToggles.timeAnalytics;
import org.springframework.samples.petclinic.mysql.MySQLJDBCDriverConnection;
import org.apache.logging.log4j.Logger;
import org.springframework.samples.petclinic.FeatureToggles.FeatureToggles;
import org.springframework.samples.petclinic.FeatureToggles.RandomToggle;
import org.springframework.samples.petclinic.incrementalreplication.IncrementalReplication;
import org.springframework.samples.petclinic.incrementalreplication.IncrementalReplicationChecker;
import org.springframework.samples.petclinic.shadowRead.OwnerShadowRead;
import org.springframework.samples.petclinic.shadowRead.PetShadowRead;
import org.springframework.samples.petclinic.shadowRead.PetTypeShadowRead;
import org.springframework.samples.petclinic.incrementalreplication.IncrementalReplication;
import org.springframework.samples.petclinic.incrementalreplication.IncrementalReplicationChecker;
import org.springframework.samples.petclinic.ABTest.OwnerLogHelper;
import org.springframework.samples.petclinic.sqlite.SQLiteOwnerHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.springframework.samples.petclinic.ABTest.DeleteOwnerBtnHelper.countDeleteOwnerBtnOne;
import static org.springframework.samples.petclinic.ABTest.DeleteOwnerBtnHelper.countDeleteOwnerBtnTwo;
import static org.springframework.samples.petclinic.FeatureToggles.FeatureToggles.isEnableOwnerPage;
import static org.springframework.samples.petclinic.FeatureToggles.FeatureToggles.isEnableShadowRead;

*/
/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 * <p>
 * Custom handler for displaying an owner.
 * @param ownerId the ID of the owner to display
 * @return a ModelMap with the model attributes for the view
 *//*

@Controller
class OwnerController {

    private static final String VIEWS_OWNER_CREATE_OR_UPDATE_FORM = "owners/createOrUpdateOwnerForm";
    private final OwnerRepository owners;
    private final PetRepository pets = null;
    private Owner owner;
    private Collection<Owner> results;
    //private static Logger log = LoggerFactory.getLogger(OwnerController.class);
    private static Logger log = LogManager.getLogger(OwnerController.class);
    private static org.apache.logging.log4j.Logger timeLogAnalytics = LogManager.getLogger("WelcomeFeature");

    @Autowired
   public OwnerController(OwnerRepository clinicService) {
       this.owners = clinicService;
    }

    public OwnerController(OwnerRepository clinicService,Owner owner, Collection<Owner> results) {
        this.owners = clinicService;
        this.owner = owner;
        this.results = results;
    }


    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }


    public String initCreationForm(Map<String, Object> model) {
        return this.initCreationForm(model,new Owner());
    }
    // dependency injection
    @GetMapping("/owners/new")
    public String initCreationForm(Map<String, Object> model , Owner owner) {

        if(FeatureToggles.isEnableOwnerCreate) {
            this.owner = owner;
            model.put("owner", owner);
            return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
        }
        return null;
    }

    @PostMapping("/owners/new")
    public String processCreationForm(@Valid Owner owner, BindingResult result) {
        if (result.hasErrors()) {
            return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
        } else {
            this.owners.save(owner);
            //Shadow write
            if (FeatureToggles.isEnableShadowWrite) {
                //To make db difference
                System.out.println(owner.getFirstName() + " " + owner.getLastName() + " insert");
                int responseRowId = SQLiteOwnerHelper.getInstance().insert(owner.getFirstName(), owner.getLastName(), owner.getAddress(), owner.getCity(), owner.getTelephone());
                System.out.println(responseRowId + "responseRowId");
                FeatureToggles.isEnableIncrementDate = true;
                // call incremental consistency check
                boolean isConsistency = IncrementalReplicationChecker.isConsistency(responseRowId,"owners");
                System.out.println(isConsistency+"result");
                // if incremental consistency check is not good call incremental replication
                if (FeatureToggles.isEnableIR && isConsistency == false) {
                    FeatureToggles.isEnableIncrementDate = false;
                    //System.out.println("incremental replication!!!");
                    IncrementalReplication.addToCreateList("owners," + responseRowId + "," + owner.getFirstName()+ "," + owner.getLastName()+ "," + owner.getAddress()+ "," + owner.getCity()+ "," + owner.getTelephone());
                    IncrementalReplication.incrementalReplication();
                    FeatureToggles.isEnableIncrementDate = true;
                }
            }

            return "redirect:/owners/" + owner.getId();
        }
    }


    public String initFindForm(Map<String, Object> model) {
       return this.initFindForm(model, new Owner());
    }

    // dependency injection
    @GetMapping("/owners/find")
    public String initFindForm(Map<String, Object> model , Owner owner) {
        // TODO toggle?
        */
/*  timeAnalytics.endTime = System.nanoTime();
        timeLogAnalytics.info("Elapsed Time (ms) : " + timeAnalytics.elapsedTimeMS() + " Legacy Welcome : " + FeatureToggles.welcomePageToggle);
        timeAnalytics.resetTimeAnalystics();*//*

        if (FeatureToggles.isEnableOwnerPage) {
            model.put("owner", owner);
            if(FeatureToggles.isEnableTabOwnerChange == true && FeatureToggles.isEnableTabOwnerChangeRandom == true) {
                OwnerLogHelper.countOwnerTabOne();
            } else {
                OwnerLogHelper.countOwnerTabTwo();
            }
            return "owners/findOwners";
            }
        return null;
    }

    @GetMapping("/owners")
    public String processFindForm(Owner owner, BindingResult result, Map<String, Object> model) {
        if (FeatureToggles.isEnableOwnerFind) {
            // allow parameterless GET request for /owners to return all records
            if (owner.getLastName() == null) {
                owner.setLastName(""); // empty string signifies broadest possible search
            }

            // find owners by last name
            //this.results = this.owners.findByLastName(owner.getLastName());
            setResults(owner);
            System.out.println(results.toString());
            // TODO toggle
           */
/* RandomToggle rndToggle = new RandomToggle();
            FeatureToggles.isEnabledLegacyFindOwnerButton = rndToggle.randomToggle(0.50f);*//*

            if (results.isEmpty()) {
                // no owners found
                result.rejectValue("lastName", "notFound", "not found");
                if(FeatureToggles.isEnabledLegacyFindOwnerButton == true) return "owners/findOwners";
                else return "owners/findOwners-V2";
            } else if (results.size() == 1) {
                // 1 owner found
                owner = results.iterator().next();

                //shadow read for owner
                if(isEnableShadowRead) {
                    OwnerShadowRead ownerShadowReader = new OwnerShadowRead();
                    log.trace(owner.getId() + " Owner Id from controller");
                    //Shadow read return problem id
                    int inconsistency_id = ownerShadowReader.checkOwner(owner);
                    //if it's not good call incremental replication
                    if (inconsistency_id > -1) {
                        IncrementalReplication.addToUpdateList("owners" + "," + owner.getId() + "," + owner.getFirstName() + "," + owner.getLastName() + "," + owner.getAddress() + "," + owner.getCity() + "," + owner.getTelephone());
                        IncrementalReplication.incrementalReplication();
                    }
                }
                return "redirect:/owners/" + owner.getId();
            } else {
                // multiple owners found
                    if (FeatureToggles.isEnableShadowRead) {
                        OwnerShadowRead ownerShadowReader = new OwnerShadowRead();
                        try {
                            //shadow read for owner
                            for (Owner own : results) {
                                log.trace(own.getId() + " Owner Id from controller");
                                //Shadow read return problem id
                                int inconsistency_id = ownerShadowReader.checkOwner(own);
                                //if it's not good call incremental replication
                                if (inconsistency_id > -1) {
                                    IncrementalReplication.addToUpdateList("owners" + "," + owner.getId() + "," + owner.getFirstName() + "," + owner.getLastName() + "," + owner.getAddress() + "," + owner.getCity() + "," + owner.getTelephone());
                                    IncrementalReplication.incrementalReplication();
                                }
                            }
                        }catch(Exception e){
                            e.getMessage();
                        }
                    }
                        model.put("selections", results);
                        return "owners/ownersList";

            }
        }
        return null;
    }

    public void setResults(Owner owner){
        this.results = this.owners.findByLastName(owner.getLastName());
    }

    @GetMapping("/owners/{ownerId}/edit")
    public String initUpdateOwnerForm(@PathVariable("ownerId") int ownerId, Model model) throws SQLException {

        if (FeatureToggles.isEnableOwnerEdit) {
            Owner owner = this.owners.findById(ownerId);
            model.addAttribute(owner);
            return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
        }
        return null;
    }

    @PostMapping("/owners/{ownerId}/edit")
    public String processUpdateOwnerForm(@Valid Owner owner, BindingResult result, @PathVariable("ownerId") int ownerId) {
        if (result.hasErrors()) {
            return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
        } else {
            owner.setId(ownerId);
            this.owners.save(owner);
            if (FeatureToggles.isEnableOwnerEditIR) {
                IncrementalReplication.addToUpdateList("owners" + "," + (owner.getId()).toString() + "," + owner.getFirstName() + "," + owner.getLastName() + "," + owner.getAddress() + "," + owner.getCity() + "," + owner.getTelephone());
                IncrementalReplication.incrementalReplication();
            }
//            if (FeatureToggles.isEnableShadowWrite) {
//                //To make db difference
//                System.out.println(owner.getFirstName() + " " + owner.getLastName() + " update");
//                // Implement an update Form
//                int responseRowId = SQLiteOwnerHelper.getInstance().update(owner);
//                System.out.println(responseRowId + "responseRowId");
//                FeatureToggles.isEnableIncrementDate = true;
//                // call incremental consistency check
//                boolean isConsistency = IncrementalReplicationChecker.isConsistency(responseRowId,"visits");
//                System.out.println(isConsistency+"result");
//                // if incremental consistency check is not good call incremental replication
//                if (FeatureToggles.isEnableIR && isConsistency == false) {
//                    FeatureToggles.isEnableIncrementDate = false;
//                    //System.out.println("incremental replication!!!");
//                    IncrementalReplication.addToUpdateList("owners," + (owner.getId()).toString() + "," + owner.getFirstName() + "," + owner.getLastName() + "," + owner.getAddress() + "," + owner.getCity() + "," + owner.getTelephone());
//                    IncrementalReplication.incrementalReplication();
//                    FeatureToggles.isEnableIncrementDate = true;
//                }
//            }

            return "redirect:/owners/{ownerId}";
        }
    }

    */
/**
 * Custom handler for displaying an owner.
 *
 * @param ownerId the ID of the owner to display
 * @return a ModelMap with the model attributes for the view
 *//*


    @GetMapping("/owners/{ownerId}")
    public ModelAndView showOwner(@PathVariable("ownerId") int ownerId) {
        return this.showOwner(ownerId,new ModelAndView("owners/ownerDetails"));
    }

    public ModelAndView showOwner(@PathVariable("ownerId") int ownerId , ModelAndView modelAndView) {
        ModelAndView mav = modelAndView;

        Owner owner = this.owners.findById(ownerId);
        List<Pet> pets = owner.getPets();

        //shadow read for pet
        PetShadowRead petShadowRead = new PetShadowRead();
        PetTypeShadowRead petTypeShadowRead = new PetTypeShadowRead();
        try {
            for(Pet pet: pets) {
                if (pet != null) {
                    PetType petType = pet.getType();
                    if(petType != null) {
                        //region shadow read for pet type
                        int petType_inconsistency_id = petTypeShadowRead.checkPetType(petType);
                        if (petType_inconsistency_id > -1) {
                            petTypeShadowRead.incrementalReplicationAdapter(petType);
                        }
                        //endregion shadow read for pet type end

                        int pet_inconsistency_id = petShadowRead.checkPet(pet);
                        if (pet_inconsistency_id > -1) {
                            //increamental replication
                            IncrementalReplication.addToUpdateList("pets," + pet_inconsistency_id
                                    + "," + pet.getName() + "," + pet.getBirthDate().toString()
                                    + "," + pet.getType().getId() + "," + pet.getOwner().getId());
                            IncrementalReplication.incrementalReplication();
                        }
                    }
                }
            }
        }catch(Exception e){
            e.getMessage();
        }

        mav.addObject(owner);
        return mav;
    }

    // To simulate different users using the new tab feature
    @ModelAttribute("isEnableTabOwnerChangeRandom")
    public boolean isEnableTabOwnerChangeRandom() {
        RandomToggle rndToggle = new RandomToggle();
        FeatureToggles.isEnableTabOwnerChangeRandom = rndToggle.randomToggle(0.50f);
        return  FeatureToggles.isEnableTabOwnerChangeRandom;
    }

    @ModelAttribute("isEnableTabOwnerChange")
    public boolean isEnableTabOwnerChange() {
        return FeatureToggles.isEnableTabOwnerChange;
    }

    // Pass the toggle to the layout to show/hide the button Version 1
    @ModelAttribute("isEnableDeleteOwnerRandom1")
    public boolean isEnableDeleteOwnerRandom1() {
        RandomToggle rndToggle = new RandomToggle();
        FeatureToggles.isEnableDeleteOwnerRandom1 = rndToggle.randomToggle(0.50f);
        return  FeatureToggles.isEnableDeleteOwnerRandom1;
    }

    // Pass the toggle to the layout
    // Pass the toggle to the layout to show/hide the button Version 2
    @ModelAttribute("isEnableDeleteOwnerRandom2")
    public boolean isEnableDeleteOwnerRandom2() {
        RandomToggle rndToggle = new RandomToggle();
        FeatureToggles.isEnableDeleteOwnerRandom2 = rndToggle.randomToggle(0.50f);
        return  FeatureToggles.isEnableDeleteOwnerRandom2;
    }

    // Pass the toggle isDisableDeleteOwner to the layout to turn off the whole feature
    @ModelAttribute("isEnableDeleteOwner")
    public boolean isEnableDeleteOwner() {
        return  FeatureToggles.isEnableDeleteOwner;
    }

    @ModelAttribute("isEnableDeleteVisit")
    public boolean isEnableDeleteVisit(){
        return FeatureToggles.isEnableDeleteVisit;
    }

    @ModelAttribute("isEnableDeleteVisitRandom")
    public boolean isEnableDeleteVisitRandom() {
        RandomToggle rndToggle = new RandomToggle();
        FeatureToggles.isEnableDeleteVisitRandom = rndToggle.randomToggle(0.50f);
        return  FeatureToggles.isEnableDeleteVisitRandom;
    }

    // Delete owner that doesn't have pets version One
    // if the owner has pets then the pets should be deleted first
    // then you can delete the owner to conserve the database integrity (child-parent)
    @GetMapping("/owners/{ownerId}/deleteBtnVersionOne")
    public String DeleteOwnerOne(@PathVariable("ownerId") int ownerId, Model model) throws SQLException {
        if (FeatureToggles.deleteOwnerToggle) {
            Owner owner = this.owners.findById(ownerId);
            this.owners.deleteById(owner.getId());
            model.addAttribute(owner);
            countDeleteOwnerBtnOne();
            return "owners/deleteBtnVersionOne";
        }
        return "owners/findOwners";
    }

    // Delete owner that doesn't have pets version Two
    // if the owner has pets then the pets should be deleted first
    // then you can delete the owner to conserve the database integrity (child-parent)
    @GetMapping("/owners/{ownerId}/deleteBtnVersionTwo")
    public String DeleteOwnerTwo(@PathVariable("ownerId") int ownerId, Model model) throws SQLException {
        if (FeatureToggles.deleteOwnerToggle) {
            Owner owner = this.owners.findById(ownerId);
            this.owners.deleteById(owner.getId());
            model.addAttribute(owner);
            countDeleteOwnerBtnTwo();
            return "owners/deleteBtnVersionTwo";
        }
        return "owners/findOwners";
    }



}
*/
