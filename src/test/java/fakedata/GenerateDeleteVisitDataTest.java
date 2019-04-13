package fakedata;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;
import org.springframework.samples.petclinic.ABTest.deleteVisitBtnHelper;
import org.springframework.samples.petclinic.FeatureToggles.FeatureToggles;

public class GenerateDeleteVisitDataTest {

    //@Test
    public void generateDeleteVisitDataTest() {

        int count = 0;
        FeatureToggles.isEnableDeleteVisit = true;

        while (count < 1000) {
            int rnd = RandomUtils.nextInt(0, 2);
            if (rnd == 0) {
                deleteVisitBtnHelper.countDeleteVisitBtnGreen();
            } else if (rnd == 1) {
                deleteVisitBtnHelper.countDeleteVisitBtnBlack();
            }
            count++;
        }

    }
}
