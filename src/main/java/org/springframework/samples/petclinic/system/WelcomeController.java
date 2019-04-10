package org.springframework.samples.petclinic.system;


import javafx.scene.control.Toggle;
import org.springframework.samples.petclinic.FeatureToggles.RandomToggle;
import org.springframework.samples.petclinic.FeatureToggles.timeAnalytics;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.samples.petclinic.FeatureToggles.FeatureToggles;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Instant;
import org.springframework.samples.petclinic.FeatureToggles.FeatureToggles;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
class WelcomeController {

    private static Logger analytics = LogManager.getLogger("Welcome Toggle");

    @GetMapping("/")
    public String welcome() {

        RandomToggle rndToggle = new RandomToggle();
        // Legacy Mode 75% of the time V2 25% of the timeS
        FeatureToggles.welcomePageToggle = rndToggle.randomToggle(0.30f);
        return toggleWelcome();
    }

    private String toggleWelcome() {
        //Start Time stamp to test ms spent on welcome page
        timeAnalytics.resetTimeAnalystics();
        timeAnalytics.startTime = System.nanoTime();
        if (FeatureToggles.isEnabledLegacyWelcomePage) {
            if (FeatureToggles.welcomePageToggle == false) {
                analytics.info("Welcome Page Version 2");
                return "welcome-v2";
            } else {
                analytics.info("Welcome Page Version 1");
                return "welcome";
            }
        } else {
            analytics.info("Default Welcome Page (Toggle Disabled");
            return "welcome";
        }
    }
}
