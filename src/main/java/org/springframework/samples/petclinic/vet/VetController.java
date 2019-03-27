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

import org.springframework.samples.petclinic.FeatureToggles.FeatureToggles;
import org.springframework.samples.petclinic.shadowRead.VetShadowRead;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.samples.petclinic.FeatureToggles.FeatureToggles;
import java.util.Collection;
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
                //Collection<Vet> vetShadowList = this.vetrepository.findAll();
                int inconsistencyShadowReadCounter = 0;

                for (Vet vet : vetList) {
                    System.out.println(vet.getFirstName() + "from controller");

                    //Shadow read return problem id
                    int inconsistency_id = vetShadowReader.checkVet(vet);

                    //if it's not good call incremental replication
                    if(inconsistency_id>-1){
                        //TODO adapt the Increamental Replication
                        inconsistencyShadowReadCounter++;
                    }
                }
                if(inconsistencyShadowReadCounter == 0){
                    //TODO change to logger info
                    System.out.println("Shadow Read for vets passed from controller");
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
            int inconsistencyShadowReadCounter = 0;

            for (Vet vet : vetList) {
                System.out.println(vet.getFirstName() + "from controller");

                //Shadow read return problem id
                int inconsistency_id = vetShadowReader.checkVet(vet);

                //if it's not good call incremental replication
                if(inconsistency_id>-1){
                    //TODO adapt the Increamental Replication
                    inconsistencyShadowReadCounter++;
                }
            }
            if(inconsistencyShadowReadCounter == 0){
                //TODO change to logger info
                System.out.println("Shadow Read for vets passed from controller");
            }

        }
        return vets;
    }

}
