package org.springframework.samples.petclinic.ABTest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HomeBtnHelper {
    private static int homeBtnOneCount = 0;
    private static int homeBtnTwoCount = 0;
    private static Logger logVersionOneHomeBtn = LogManager.getLogger("logVersionOneHomeBtn");
    private static Logger logVersionTwoHomeBtn = LogManager.getLogger("logVersionTwoHomeBtn");

    public static int homeBtnOneClickCount(){
        homeBtnOneCount ++;
        //logVersionOneHomeBtn.info("Version 1 home button clicked " + homeBtnOneCount + " times");
        System.out.println("Version 1 home button clicked " + homeBtnOneCount + " times");
        return  homeBtnOneCount;
    }

    public static int homeBtnTwoClickCount(){
        homeBtnTwoCount ++;
        //logVersionTwoHomeBtn.info("Version 2 home button clicked " + homeBtnTwoCount + " times");
        System.out.println("Version 2 home button clicked " + homeBtnTwoCount + " times");
        return  homeBtnTwoCount;
    }

}
