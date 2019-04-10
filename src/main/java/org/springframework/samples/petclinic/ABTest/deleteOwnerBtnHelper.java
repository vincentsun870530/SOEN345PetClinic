package org.springframework.samples.petclinic.ABTest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class deleteOwnerBtnHelper {
    private static int countDeleteOwnerBtnOne = 0;
    private static int countDeleteOwnerBtnTwo = 0;
    private static Logger logVersionOneHomeBtn = LogManager.getLogger("logVersionOneHomeBtn");
    private static Logger logVersionTwoHomeBtn = LogManager.getLogger("logVersionTwoHomeBtn");

    public static int countDeleteOwnerBtnOne(){
        countDeleteOwnerBtnOne ++;
        logVersionOneHomeBtn.info("Version 1 delete owner button clicked " + countDeleteOwnerBtnOne + " times");
        //System.out.println("Version 1 home button clicked " + countDeleteOwnerBtnOne + " times");
        return  countDeleteOwnerBtnOne;
    }

    public static int countDeleteOwnerBtnTwo(){
        countDeleteOwnerBtnTwo ++;
        logVersionTwoHomeBtn.info("Version 2 delete owner button clicked " + countDeleteOwnerBtnTwo + " times");
        //System.out.println("Version 2 home button clicked " + countDeleteOwnerBtnTwo + " times");
        return  countDeleteOwnerBtnTwo;
    }

}
