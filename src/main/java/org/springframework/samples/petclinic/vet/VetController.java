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
package org.springframework.samples.petclinic.vet;

import org.apache.logging.log4j.LogManager;
import org.springframework.samples.petclinic.FeatureToggles.FeatureToggles;
import org.springframework.samples.petclinic.FeatureToggles.RandomToggle;
import org.springframework.samples.petclinic.FeatureToggles.timeAnalytics;
import org.springframework.samples.petclinic.incrementalreplication.IncrementalReplication;
import org.springframework.samples.petclinic.shadowRead.SpecialtyShadowRead;
import org.springframework.samples.petclinic.shadowRead.VetShadowRead;
import org.springframework.samples.petclinic.shadowRead.VetSpecialtyShadowRead;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.samples.petclinic.FeatureToggles.FeatureToggles;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Juergen Hoeller
 * @author Mark Fisher
 * @author Ken Krebs
 * @author Arjen Poutsma
 */
@Controller
class VetController {

    private final VetRepository vetrepository;
    Vets vets;
    private static org.apache.logging.log4j.Logger timeLogAnalytics = LogManager.getLogger("WelcomeFeature");

    public VetController(VetRepository clinicService) {
        this.vetrepository = clinicService;
    }

    //@GetMapping("/vets.html")
    public String showVetList(Map<String, Object> model) {
        // Here we are returning an object of type 'Vets' rather than a collection of Vet
        // objects so it is simpler for Object-Xml mapping
        return this.showVetList(model,new Vets());
    }

    @GetMapping("/vets.html")
    public String showVetList(Map<String, Object> model , Vets vets) {

        timeAnalytics.endTime = System.nanoTime();
        timeLogAnalytics.info("Elapsed Time (ms) : " + timeAnalytics.elapsedTimeMS());
        timeAnalytics.resetTimeAnalystics();
        if (FeatureToggles.isEnableVetPage) {
            // Here we are returning an object of type 'Vets' rather than a collection of Vet
            // objects so it is simpler for Object-Xml mapping
            this.vets = vets;
            Collection<Vet> vetList = this.vetrepository.findAll();
            vets.getVetList().addAll(vetList);
            model.put("vets", vets);

            // Shadow read
            if(FeatureToggles.isEnableShadowRead)
            {
                VetShadowRead vetShadowReader = new VetShadowRead();
                SpecialtyShadowRead specialtyShadowRead = new SpecialtyShadowRead();
                VetSpecialtyShadowRead vetSpecialtyShadowRead = new VetSpecialtyShadowRead();
                //Collection<Vet> vetShadowList = this.vetrepository.findAll();
                try {
                    int inconsistencyShadowReadCounter = 0;

                    for (Vet vet : vetList) {
                        //TODO change to logger debug
                        System.out.println(vet.getFirstName() + " from controller");
                        ArrayList<String> vetSpecialtyList = new ArrayList<String>();
                        vetSpecialtyList.add(vet.getId().toString());
                        //Shadow read return problem id | shadow read for specialty first
                        List<Specialty> specialtyList = vet.getSpecialties();
                        for(Specialty specialty : specialtyList) {

                            int specialty_inconsistency_id = specialtyShadowRead.checkSpecialty(specialty);
                            if (specialty_inconsistency_id > -1) {
                                String updateQuery = "specialties," + specialty_inconsistency_id + "," + specialty.getName();
                                IncrementalReplication.addToUpdateList(updateQuery);
                                IncrementalReplication.incrementalReplication();
                            }
                            vetSpecialtyList.add(specialty.getId().toString());
                        }

                        //Shadow read for vet_specialties joint table
                        vetSpecialtyShadowRead.checkVetSpecialty(vetSpecialtyList);

                        //Shadow read return problem id
                        int inconsistency_id = vetShadowReader.checkVet(vet);

                        //if it's not good call incremental replication
                        if (inconsistency_id > -1) {
                            // Increamental Replication
                            IncrementalReplication.addToUpdateList("vets," + inconsistency_id + "," + vet.getFirstName() + "," + vet.getLastName());
                            IncrementalReplication.incrementalReplication();
                            inconsistencyShadowReadCounter++;
                        }
                    }
                    if (inconsistencyShadowReadCounter == 0) {
                        //TODO change to logger info
                        System.out.println("Shadow Read for vets passed from controller");
                    }
                }catch(Exception e){
                    e.getMessage();
                }

            }

            return "vets/vetList";
        }
        return null;
    }

    //@GetMapping({ "/vets" })
    public @ResponseBody Vets showResourcesVetList() {
        // Here we are returning an object of type 'Vets' rather than a collection of Vet
        // objects so it is simpler for JSon/Object mapping
        return this.showResourcesVetList(new Vets());
    }

    @GetMapping({ "/vets" })
    public @ResponseBody Vets showResourcesVetList(Vets vets) {
        // Here we are returning an object of type 'Vets' rather than a collection of Vet
        // objects so it is simpler for JSon/Object mapping
        this.vets = vets;
        Collection<Vet> vetList = this.vetrepository.findAll();
        vets.getVetList().addAll(vetList);
        // Shadow read
        if(FeatureToggles.isEnableShadowRead)
        {
            VetShadowRead vetShadowReader = new VetShadowRead();
            //Collection<Vet> vetShadowList = this.vetrepository.findAll();
            try {
                int inconsistencyShadowReadCounter = 0;

                for (Vet vet : vetList) {
                    System.out.println(vet.getFirstName() + "from controller");

                    //Shadow read return problem id
                    int inconsistency_id = vetShadowReader.checkVet(vet);

                    //if it's not good call incremental replication
                    if (inconsistency_id > -1) {
                        //Increamental Replication
                        IncrementalReplication.addToUpdateList("vets," + inconsistency_id + "," + vet.getFirstName() + "," + vet.getLastName());
                        IncrementalReplication.incrementalReplication();
                        inconsistencyShadowReadCounter++;
                    }
                }
                if (inconsistencyShadowReadCounter == 0) {
                    //TODO change to logger info
                    System.out.println("Shadow Read for vets passed from controller");
                }
            }catch(Exception e){
                e.getMessage();
            }
        }
        return vets;
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

}
