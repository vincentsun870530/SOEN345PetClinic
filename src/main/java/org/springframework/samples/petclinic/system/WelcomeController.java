package org.springframework.samples.petclinic.system;


import org.springframework.samples.petclinic.FeatureToggles.FeatureToggles;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
class WelcomeController {

    @GetMapping("/")
    public String welcome() {
        return "welcome";
    }

    // Passes the toggle variable to the layout for A/B testing
    @ModelAttribute("isEnableHomeBtn")
    public boolean isEnableHomeBtn() {
        return FeatureToggles.isEnableHomeBtn;
    }

}
