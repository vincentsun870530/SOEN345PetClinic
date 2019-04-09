package org.springframework.samples.petclinic.system;

import org.springframework.samples.petclinic.FeatureToggles.FeatureToggles;
import org.springframework.samples.petclinic.loghelper.WelcomeLogHelper;
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

    
    @ModelAttribute("isEnableTabOwnerChange")
    public boolean isEnableTabOwnerChange() {
        return FeatureToggles.isEnableTabOwnerChange;
    }
}
