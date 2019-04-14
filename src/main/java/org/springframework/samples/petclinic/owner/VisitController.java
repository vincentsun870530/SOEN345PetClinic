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
import org.springframework.samples.petclinic.FeatureToggles.RandomToggle;
import org.springframework.samples.petclinic.incrementalreplication.IncrementalReplication;
import org.springframework.samples.petclinic.ABTest.deleteVisitBtnHelper;
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
import java.sql.SQLException;
import org.springframework.ui.Model;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static org.springframework.samples.petclinic.ABTest.deleteVisitBtnHelper.countDeleteVisitBtnBlack;
import static org.springframework.samples.petclinic.ABTest.deleteVisitBtnHelper.countDeleteVisitBtnGreen;

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
    private static Logger log = LogManager.getLogger(VisitController.class);
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
        // A/B testing feature
        if(FeatureToggles.isEnableDeleteVisit){
        //pet.addVisit(visit);
        }else{
            pet.addVisit(visit);
        }
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

    @ModelAttribute("isEnableDeleteVisit")
    @GetMapping("/owners/{ownerId}/pets/{petId}/visit/{visitId}/deleteVisitGreen")
    public String handleDeleteVisitGreen(@PathVariable("visitId") int visitId,@PathVariable("petId") int petId,@PathVariable("ownerId") int ownerId, Model model) throws SQLException  {
        if(FeatureToggles.isEnableDeleteVisit){
            System.out.println(visitId);
            Visit visit = this.visits.findById(visitId);
            System.out.println(visit);
            this.visits.deleteById(visit.getId());
            model.addAttribute(visit);
            countDeleteVisitBtnGreen();
            return "owners/deleteVisitGreen"  ;
        }
        return "owners/ownerDetails";
    }
    @ModelAttribute("isEnableDeleteVisit")
    @GetMapping("/owners/{ownerId}/pets/{petId}/visit/{visitId}/deleteVisitBlack")
    public String handleDeleteVisitBlack(@PathVariable("visitId") int visitId,@PathVariable("petId") int petId,@PathVariable("ownerId") int ownerId, Model model) throws SQLException  {
        if(FeatureToggles.isEnableDeleteVisit){
            System.out.println(visitId);
            Visit visit = this.visits.findById(visitId);
            System.out.println(visit);
            this.visits.deleteById(visit.getId());
            model.addAttribute(visit);
            countDeleteVisitBtnBlack();
            return "owners/deleteVisitBlack";
        }
       return "owners/ownerDetails";

    }


    // To simulate different users using the new tab feature
    @ModelAttribute("isEnableTabOwnerChangeRandom")
    public boolean isEnableTabOwnerChangeRandom() {
        RandomToggle rndToggle = new RandomToggle();
        FeatureToggles.isEnableTabOwnerChangeRandom = rndToggle.randomToggle(0.50f);
        return  FeatureToggles.isEnableTabOwnerChangeRandom;
    }

    @ModelAttribute("isEnableFeature3")
    public boolean isEnableTabOwnerChange() {
        return FeatureToggles.Feature3;
    }


}
