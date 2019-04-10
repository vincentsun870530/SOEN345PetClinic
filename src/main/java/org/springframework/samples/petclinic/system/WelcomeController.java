package org.springframework.samples.petclinic.system;

import org.springframework.samples.petclinic.FeatureToggles.FeatureToggles;
import org.springframework.samples.petclinic.FeatureToggles.RandomToggle;
import org.springframework.samples.petclinic.ABTest.WelcomeLogHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
class WelcomeController {

    @GetMapping("/")
    public String welcome() {
        if(FeatureToggles.isEnableTabOwnerChange == true) {
            WelcomeLogHelper.countUserVerOne();
        } else {
            WelcomeLogHelper.countUserVerTwo();
        }
        return "welcome";
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
