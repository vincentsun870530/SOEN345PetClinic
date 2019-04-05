package org.springframework.samples.petclinic.system;


import org.springframework.samples.petclinic.FeatureToggles.RandomToggle;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.samples.petclinic.FeatureToggles.FeatureToggles;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Controller
class WelcomeController {

    private static Logger analytics = LogManager.getLogger("Analytics");

    @GetMapping("/")
    public String welcome() {

        RandomToggle rndToggle = new RandomToggle();
        // Legacy Mode 75% of the time V2 25% of the timeS
        FeatureToggles.isEnabledLegacyWelcomePage = rndToggle.randomToggle(0.25f);
        return toggleWelcome();
    }

    private String toggleWelcome() {
        if(FeatureToggles.isEnabledLegacyWelcomePage == false) {
            analytics.info("Version 2 of welcome page started ");
            return "welcome-v2";
        }
        else {
            analytics.info("Version 1 of welcome page started ");

            return "welcome";
        }
    }
}
