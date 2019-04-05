package org.springframework.samples.petclinic.system;


import org.springframework.samples.petclinic.FeatureToggles.RandomToggle;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.samples.petclinic.FeatureToggles.FeatureToggles;
import java.util.concurrent.ThreadLocalRandom;

@Controller
class WelcomeController {

    @GetMapping("/")
    public String welcome() {
        RandomToggle rndToggle = new RandomToggle();
        FeatureToggles.isEnabledLegacyWelcomePage = rndToggle.randomToggle(0.25f);
        return toggleWelcome();
    }

    private String toggleWelcome() {
        if(FeatureToggles.isEnabledLegacyWelcomePage == false) {
            System.out.println("Running welcome V2");
            return "welcome-v2";
        } else {
            System.out.println("Running welcome V1");
            return "welcome";
        }
    }
}
