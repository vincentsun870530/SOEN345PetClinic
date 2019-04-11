package org.springframework.samples.petclinic.owner;

import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;
import org.springframework.samples.petclinic.ABTest.DeleteOwnerBtnHelper;
import org.springframework.samples.petclinic.FeatureToggles.FeatureToggles;

public class GenerateOwnerDataTest {

    @Test
    public void generateOwnerDataTest() {

        int count = 0;
        FeatureToggles.isEnableDeleteOwner = true;

        while (count < 1000) {
            int rnd = RandomUtils.nextInt(0, 2);
            if (rnd == 0 && FeatureToggles.isEnableDeleteOwnerRandom1) {
                DeleteOwnerBtnHelper.countDeleteOwnerBtnOne();
            }
            else if (rnd == 1 && FeatureToggles.isEnableDeleteOwnerRandom2){
                DeleteOwnerBtnHelper.countDeleteOwnerBtnTwo();
            }
            count++;
        }
    }
}
