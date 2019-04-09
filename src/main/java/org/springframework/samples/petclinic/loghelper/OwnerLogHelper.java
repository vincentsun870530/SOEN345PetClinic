package org.springframework.samples.petclinic.loghelper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class OwnerLogHelper {
    private static int countClicksOwnerTabVerOne = 0;
    private static int countClicksOwnerTabVerTwo = 0;
    private static Logger logOwnerTabVerOne = LogManager.getLogger("logOwnerTabVerOne");
    private static Logger logOwnerTabVerTwo = LogManager.getLogger("logOwnerTabVerTwo");

    public static int countOwnerTabOne(){
        countClicksOwnerTabVerOne ++;
        logOwnerTabVerOne.info("New version(Owners) tab have been clicked " + countClicksOwnerTabVerOne + " times");
        System.out.println("New version(Owners) tab have been clicked " + countClicksOwnerTabVerOne + " times");
        return countClicksOwnerTabVerOne;
    }

    public static int countOwnerTabTwo(){
        countClicksOwnerTabVerTwo ++;
        logOwnerTabVerTwo.info("Old version(Find Owners) tab have been clicked " + countClicksOwnerTabVerTwo + " times");
        System.out.println("Old version(Find Owners) tab have been clicked " + countClicksOwnerTabVerTwo + " times");
        return countClicksOwnerTabVerTwo;
    }

}