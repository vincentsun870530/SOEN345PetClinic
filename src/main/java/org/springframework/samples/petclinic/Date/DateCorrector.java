package org.springframework.samples.petclinic.Date;

import java.time.LocalDate;

public class DateCorrector {

    public static LocalDate correctDate(LocalDate localDate){
        LocalDate tempLocalDate = localDate;
        System.out.println(tempLocalDate);
        tempLocalDate = tempLocalDate.plusDays(1);
        System.out.println(tempLocalDate);
        return tempLocalDate;
    }

    //For test
    /*public static void main(String[] args) {
        DateCorrector dateCorrector =new DateCorrector();
        LocalDate now =LocalDate.now();
        System.out.println(dateCorrector.correctDate(now));
    }*/
}
