package org.springframework.samples.petclinic.ABTest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class deleteVisitBtnHelper {
    private static int countDeleteVisitBtnGreen = 0;
    private static int countDeleteVisitBtnBlack = 0;
    private static Logger logVersionGreenBtn = LogManager.getLogger("logVersionGreenBtn");
    private static Logger logVersionBlackBtn = LogManager.getLogger("logVersionBlackBtn");

    public static int countDeleteVisitBtnGreen(){
        countDeleteVisitBtnGreen ++;
        logVersionGreenBtn.info("Version green delete visit button clicked " + countDeleteVisitBtnGreen + " times");
        //System.out.println("Version 1 home button clicked " + countDeleteVisitBtnGreen + " times");
        return  countDeleteVisitBtnGreen;
    }

    public static int countDeleteVisitBtnBlack(){
        countDeleteVisitBtnBlack ++;
        logVersionBlackBtn.info("Version black delete visit button clicked " + countDeleteVisitBtnBlack + " times");
        //System.out.println("Version 2 home button clicked " + countDeleteVisitBtnBlack + " times");
        return  countDeleteVisitBtnBlack;
    }
}
