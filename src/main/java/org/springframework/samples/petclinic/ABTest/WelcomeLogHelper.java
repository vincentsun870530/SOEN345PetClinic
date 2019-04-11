package org.springframework.samples.petclinic.ABTest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WelcomeLogHelper {
    private static int countClicksUsersVerOne = 0;
    private static int countClicksUsersVerTwo = 0;
    private static Logger logUserVerOne = LogManager.getLogger("logOwnerTabVerOneWelcome");
    private static Logger loguserVerTwo = LogManager.getLogger("logOwnerTabVerTwoWelcome");

    public static int countUserVerOne(){
        countClicksUsersVerOne ++;
        logUserVerOne.info("New version of user have clicked the home page " + countClicksUsersVerOne + " times");
        System.out.println("New version of user have clicked the home page " + countClicksUsersVerOne + " times");
        return countClicksUsersVerOne;
    }

    public static int countUserVerTwo(){
        countClicksUsersVerTwo ++;
        loguserVerTwo.info("Old version of user have clicked the home page " + countClicksUsersVerTwo + " times");
        System.out.println("Old version of user have clicked the home page " + countClicksUsersVerTwo + " times");
        return countClicksUsersVerTwo;
    }

}