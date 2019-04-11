package org.springframework.samples.petclinic.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.samples.petclinic.FeatureToggles.FeatureToggles;
import org.springframework.web.bind.annotation.ModelAttribute;

public class User {
    private static Logger analytics = LogManager.getLogger("Home Buttone Toggle");

    @ModelAttribute("hasHomeButton")
    public boolean hasHomeButton () {
        analytics.info("Home Button: " + FeatureToggles.hasHomeButton);
        return FeatureToggles.hasHomeButton;
    }
}
