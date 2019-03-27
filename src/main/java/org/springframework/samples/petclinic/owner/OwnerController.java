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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.samples.petclinic.mysql.MySQLJDBCDriverConnection;
import org.springframework.samples.petclinic.shadowRead.OwnerShadowRead;
import org.springframework.samples.petclinic.sqlite.SQLiteDBConnector;
import org.springframework.samples.petclinic.FeatureToggles.FeatureToggles;
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
            if (isEnableShadowWrite) {
                SQLiteOwnerHelper.getInstance().insert(owner.getFirstName(), owner.getLastName(), owner.getAddress(), owner.getCity(), owner.getTelephone());
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
//TODO shadow read
        if (FeatureToggles.isEnableOwnerPage) {
            model.put("owner", owner);
            return "owners/findOwners";
        }
        return null;
    }

    @GetMapping("/owners")
    public String processFindForm(Owner owner, BindingResult result, Map<String, Object> model) throws SQLException {

        if (FeatureToggles.isEnableOwnerFind) {
            System.out.println("inside");
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
                OwnerShadowRead ownerShadowReader = new OwnerShadowRead();
                int inconsistency_id = ownerShadowReader.checkOwner(owner);
                if(inconsistency_id > -1){
                    //TODO adapt incremental replication
                }

                return "redirect:/owners/" + owner.getId();
            } else {
                // multiple owners found
                //TODO shadow read as above but in a loop
                OwnerShadowRead ownerShadowReader = new OwnerShadowRead();
                /*
                 //TODO
                 //This enhanced for loop was commented out because it fails the tests
                 // Trying to solve this issue

                 for (Owner own : results) {
                        System.out.println(own.getId() + " Owner Id from controller");
                        //Shadow read return problem id
                        ownerShadowReader.checkOwner(own);
                       //if it's not good call incremental replication
                    }
                 */
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
        return this.showOwner(ownerId,new ModelAndView("owners/ownerDetails"));
    }

    public ModelAndView showOwner(@PathVariable("ownerId") int ownerId , ModelAndView modelAndView) {
        ModelAndView mav = modelAndView;
        mav.addObject(this.owners.findById(ownerId));
        return mav;
    }

}
