package org.springframework.samples.petclinic.FeatureToggles;

public class FeatureToggles {

    // For package owner
    public static boolean isEnableOwnerPage = true;
    public static boolean isEnableOwnerCreate = true;
    public static boolean isEnableOwnerFind = true ;
    public static boolean isEnableOwnerEdit = true;
    public static boolean isEnablePetAdd = true;
    public static boolean isEnablePetEdit = true;
    public static boolean isEnablePetVisit = true;

    //Incremental Replication
    public static boolean isEnableIR = true;
    public static boolean isEnableOwnerCreateIR = false;
    public static boolean isEnableOwnerEditIR = false;
    public static boolean isEnablePetAddIR = false;
    public static boolean isEnablePetEditIR = false;
    public static boolean isEnablePetVisitIR = false;
    //For Date incremental
    public static boolean isEnableIncrementDate = true;

    // For package vet
    public static boolean isEnableVetPage = true;

    // For shadow write
    public static boolean isEnableShadowWrite = true;

    // For shadow read
    public static boolean isEnableShadowRead = true;

    //For temp debugging system.out.print
    public static boolean isEnableDebuggingSystemOutPrint = true;

}
