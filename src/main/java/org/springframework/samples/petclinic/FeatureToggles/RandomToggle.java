package org.springframework.samples.petclinic.FeatureToggles;

import java.util.concurrent.ThreadLocalRandom;

// Class created by : Dave Bhardwaj
// Credit to @Dave Bhardwaj

public class RandomToggle {

    public boolean randomToggle (float percent){
        float rnd = ThreadLocalRandom.current().nextFloat();
        System.out.println("Percent Testing " + percent + " Random float : " + rnd);
        if(rnd <= percent) {
            System.out.println("Toggle being set to false...");
            return false;
        }
        else {
            System.out.println("Toggle being set to true...");
            return true;
        }
    }
}