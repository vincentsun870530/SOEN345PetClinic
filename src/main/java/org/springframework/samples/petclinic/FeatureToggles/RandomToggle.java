package org.springframework.samples.petclinic.FeatureToggles;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ThreadLocalRandom;

public class RandomToggle {
    private static Logger analytics = LogManager.getLogger("Random Toggle");

    public boolean randomToggle (float percent){
        float rnd = ThreadLocalRandom.current().nextFloat();
        if(rnd <= percent) {
            analytics.info("False");
            return false;
        }
        else {
            analytics.info("True");
            return true;
        }
    }
}
