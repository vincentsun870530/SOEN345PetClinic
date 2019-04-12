package org.springframework.samples.petclinic.featureToggle;

import org.apache.logging.log4j.LogManager;
import org.junit.Test;
import org.springframework.samples.petclinic.FeatureToggles.FeatureToggles;
import org.springframework.samples.petclinic.FeatureToggles.RandomToggle;

import java.util.Random;


public class welcomePageTest {

    private static org.apache.logging.log4j.Logger timeLogAnalytics = LogManager.getLogger("WelcomeFeature");


    @Test
    public void testWelcomePage() {
        int count = 1000;
        int low = 1000;
        int high = 2000;

        FeatureToggles.isEnabledLegacyWelcomePage = true;
        RandomToggle rndToggle = new RandomToggle();


        Random r = new Random();
        while(count != 0) {
            FeatureToggles.welcomePageToggle = rndToggle.randomToggle(0.30f);
            int result = r.nextInt(high-low) + low;
            timeLogAnalytics.info("Elapsed Time (ms) : " + result + " Legacy Welcome : " + FeatureToggles.welcomePageToggle);
            count--;
        }

        
    }


}
