package fakedata;

import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;
import org.springframework.samples.petclinic.ABTest.DeleteOwnerBtnHelper;
import org.springframework.samples.petclinic.FeatureToggles.FeatureToggles;

public class GenerateDeleteOwnerDataTest {

    //@Test
    public void generateDeleteOwnerDataTest() {

        int count = 0;
        FeatureToggles.isEnableFeature1 = true;

        while (count < 1000) {
            int rnd = RandomUtils.nextInt(0, 2);
            if (rnd == 0) {
                DeleteOwnerBtnHelper.countDeleteOwnerBtnOne();
            }
            else if (rnd == 1){
                DeleteOwnerBtnHelper.countDeleteOwnerBtnTwo();
            }
            count++;
        }
    }
}
