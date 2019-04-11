package org.springframework.samples.petclinic.ABTest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class OwnerLogHelper {
    private static int countClicksOwnerTabVerOne = 0;
    private static int countClicksOwnerTabVerTwo = 0;
    private static Logger logOwnerTabVerOne = LogManager.getLogger("logOwnerTabVerOneOwner");
    private static Logger logOwnerTabVerTwo = LogManager.getLogger("logOwnerTabVerTwoOwner");

    public static int countOwnerTabOne(){
        System.setProperty("log4j.configurationFile", "log4j2-owner.xml");
        countClicksOwnerTabVerOne ++;
        logOwnerTabVerOne.info("New version(Owners) tab have been clicked " + countClicksOwnerTabVerOne + " times");
        System.out.println("New version(Owners) tab have been clicked " + countClicksOwnerTabVerOne + " times");
        return countClicksOwnerTabVerOne;
    }

    public static int countOwnerTabTwo(){
        System.setProperty("log4j.configurationFile", "log4j2-owner.xml");
        countClicksOwnerTabVerTwo ++;
        logOwnerTabVerTwo.info("Old version(Find Owners) tab have been clicked " + countClicksOwnerTabVerTwo + " times");
        System.out.println("Old version(Find Owners) tab have been clicked " + countClicksOwnerTabVerTwo + " times");
        return countClicksOwnerTabVerTwo;
    }

}