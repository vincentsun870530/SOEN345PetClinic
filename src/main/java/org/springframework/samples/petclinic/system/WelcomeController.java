package org.springframework.samples.petclinic.system;


import javafx.scene.control.Toggle;
import org.springframework.samples.petclinic.FeatureToggles.timeAnalytics;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Instant;
import org.springframework.samples.petclinic.FeatureToggles.FeatureToggles;
import org.springframework.samples.petclinic.FeatureToggles.RandomToggle;
import org.springframework.samples.petclinic.ABTest.WelcomeLogHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
class WelcomeController {

    private static Logger analytics = LogManager.getLogger("Welcome Toggle");

    @GetMapping("/")
    public String welcome() {
        if(FeatureToggles.Feature3) {
            if (FeatureToggles.isEnableTabOwnerChangeRandom) {
                WelcomeLogHelper.countUserVerOne();
            } else {
                WelcomeLogHelper.countUserVerTwo();
            }
        }
        return toggleWelcome();
    }

    private String toggleWelcome() {
        //Start Time stamp to test ms spent on welcome page
        if(FeatureToggles.Feature2){
            timeAnalytics.resetTimeAnalystics();
            timeAnalytics.startTime = System.nanoTime();
        }

        if (FeatureToggles.Feature2) {
            RandomToggle rndToggle = new RandomToggle();
            FeatureToggles.welcomePageToggle = rndToggle.randomToggle(0.30f);
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

    // To simulate different users using the new tab feature
    @ModelAttribute("isEnableTabOwnerChangeRandom")
    public boolean isEnableTabOwnerChangeRandom() {
        RandomToggle rndToggle = new RandomToggle();
        FeatureToggles.isEnableTabOwnerChangeRandom = rndToggle.randomToggle(0.50f);
        return  FeatureToggles.isEnableTabOwnerChangeRandom;
    }
    
    @ModelAttribute("isEnableFeature3")
    public boolean isEnableFeature3() {
        return FeatureToggles.Feature3;
    }
}
