package org.springframework.samples.petclinic.system;

import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;
import org.springframework.samples.petclinic.ABTest.OwnerLogHelper;
import org.springframework.samples.petclinic.ABTest.WelcomeLogHelper;
import org.springframework.samples.petclinic.FeatureToggles.FeatureToggles;

public class GenerateOwnerTabDataTest {

    @Test
    public void generateOwnerTabDataTest() {

        int count = 0;
        FeatureToggles.isEnableTabOwnerChange = true;

        while (count < 1000) {
            int rnd = RandomUtils.nextInt(0, 2);
            if (rnd == 0 && FeatureToggles.isEnableTabOwnerChange) {
                OwnerLogHelper.countOwnerTabOne();
                WelcomeLogHelper.countUserVerOne();
            }
            else if (rnd == 1 && FeatureToggles.isEnableTabOwnerChange){
                OwnerLogHelper.countOwnerTabTwo();
                WelcomeLogHelper.countUserVerTwo();
            }
            count++;
        }
    }
}
