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

//import org.springframework.samples.petclinic.shadowRead.OwnerShadowRead;

import org.springframework.samples.petclinic.incrementalreplication.IncrementalReplicationChecker;
import org.springframework.samples.petclinic.shadowRead.VisitShadowRead;
import org.springframework.samples.petclinic.visit.Visit;
import org.springframework.samples.petclinic.visit.VisitRepository;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.samples.petclinic.sqlite.SQLiteVisitHelper;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 * @author Dave Syer
 */
@Controller
class VisitController {

    private final VisitRepository visits;
    private final PetRepository pets;
    private static Logger log = LoggerFactory.getLogger(VisitController.class);
    Visit visit;


    public VisitController(VisitRepository visits, PetRepository pets) {
        this.visits = visits;
        this.pets = pets;
    }

    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }

    /**
     * Called before each and every @RequestMapping annotated method.
     * 2 goals:
     * - Make sure we always have fresh data
     * - Since we do not use the session scope, make sure that Pet object always has an id
     * (Even though id is not part of the form fields)
     *
     * @param petId
     * @return Pet
     */

    //@ModelAttribute("visit")
    public Visit loadPetWithVisit(@PathVariable("petId") int petId, Map<String, Object> model) {
        return this.loadPetWithVisit(petId,model,new Visit());
    }

    @ModelAttribute("visit")
    public Visit loadPetWithVisit(@PathVariable("petId") int petId, Map<String, Object> model,Visit visit) {
        Pet pet = this.pets.findById(petId);
        model.put("pet", pet);
        this.visit = visit;
        pet.addVisit(visit);
        return visit;
    }

    // Spring MVC calls method loadPetWithVisit(...) before initNewVisitForm is called
    @GetMapping("/owners/*/pets/{petId}/visits/new")
    public String initNewVisitForm(@PathVariable("petId") int petId, Map<String, Object> model)
    {
        if (FeatureToggles.isEnablePetVisit)
            {
                // Shadow read
                if(FeatureToggles.isEnableShadowRead)
                {
                    VisitShadowRead visitShadowReader = new VisitShadowRead();
                    try {
                        List<Visit> visitList = this.visits.findByPetId(petId);
                        int inconsistencyShadowReadCounter = 0;
                        for (Visit visit : visitList) {
                            System.out.println(visit.getPetId() + "from controller");

                            //Shadow read return problem id
                            int inconsistency_id = visitShadowReader.checkVisit(visit);

                            //if it's not good call incremental replication
                            if (inconsistency_id > -1) {
                                // Incremental Replication
                                FeatureToggles.isEnableIncrementDate = false;
                                //System.out.println("incremental replication!!!");
                                IncrementalReplication.addToUpdateList("visits," + inconsistency_id + "," + visit.getPetId() + "," + visit.getDate().toString() + "," + visit.getDescription());
                                IncrementalReplication.incrementalReplication();
                                FeatureToggles.isEnableIncrementDate = true;
                                inconsistencyShadowReadCounter++;
                            }
                        }

                        if (inconsistencyShadowReadCounter == 0) {
                            log.info("Shadow Read for visits passed from controller");
                        }
                    }catch(Exception e){
                        e.getMessage();
                    }
                }
            return "pets/createOrUpdateVisitForm";
        }
        return null;
    }

    // Spring MVC calls method loadPetWithVisit(...) before processNewVisitForm is called
    @PostMapping("/owners/{ownerId}/pets/{petId}/visits/new")
    public String processNewVisitForm(@Valid Visit visit, BindingResult result) {
        if (result.hasErrors()) {
            return "pets/createOrUpdateVisitForm";
        } else {
            this.visits.save(visit);
            // shadow write
            if (FeatureToggles.isEnableShadowWrite) {

                //To make db difference comment out the isEnableIncrementDate toggle
                FeatureToggles.isEnableIncrementDate = false;
                System.out.println(visit.getDate() + " insert");
                int responseRowId = SQLiteVisitHelper.getInstance().insert(visit.getPetId(), visit.getDate().toString(), visit.getDescription());
                System.out.println(responseRowId+"responseRowId");
                FeatureToggles.isEnableIncrementDate = true;
                // call incremental consistency check
                boolean isConsistency = IncrementalReplicationChecker.isConsistency(responseRowId,"visits");
                System.out.println(isConsistency+"result");
                // if incremental consistency check is not good call incremental replication
                if (FeatureToggles.isEnableIR && isConsistency == false) {
                    FeatureToggles.isEnableIncrementDate = false;
                    //System.out.println("incremental replication!!!");
                    IncrementalReplication.addToCreateList("visits," + responseRowId + "," + visit.getPetId() + "," + visit.getDate().toString() + "," + visit.getDescription());
                    IncrementalReplication.incrementalReplication();
                    FeatureToggles.isEnableIncrementDate = true;
                }

            }
            return "redirect:/owners/{ownerId}";
        }
    }

    //
//    @RequestMapping(value = "/delete_visit", method = RequestMethod.POST)
//    public String handleDeleteUser(@ModelAttribute("visits") Visit visit) {
//        System.out.println(user.getPersonId());
//        System.out.println("test");
//        return "redirect:/external";
//    }


    // @RequestMapping(value = "/delete_visitor/{visitId}", method = RequestMethod.DELETE)
    @DeleteMapping("/owners/{ownerId}/pets/{petId}/visits/new")
    public String handleDeleteVisitor(@Valid Visit visit) {
       // @ModelAttribute("visit") Visit visit
       // Visit visit = this.visits.findById(Integer.parseInt(id));
        //@RequestParam(name="id")String id
        this.visits.delete(visit);

       // System.out.println(id);
        System.out.println("test");
        return "redirect:/external";
    }

}
