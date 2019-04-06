package org.springframework.samples.petclinic.FeatureToggles;

import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FeatureToggles extends HttpServlet {

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

    @GetMapping("/featureToggle.html")
    public String initFeatureToggleTable() {


        return null;

    }

    /**
     * public void changeToggles() {
     * document.getI
     * }
     **/

    //reference: https://stackoverflow.com/questions/31543454/how-to-take-an-html-value-and-make-it-a-java-variable
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        String value = "Enable";

        isEnableOwnerPage = value.equals(request.getParameter("ownerPage"));
        //objFeatureToggles.setIsEnableOwnerPage(isEnableOwnerPage);
        isEnableOwnerCreate = value.equals(request.getParameter("ownerCreate"));
        //setIsEnableOwnerCreate(isEnableOwnerCreate);
        isEnableOwnerFind = value.equals(request.getParameter("ownerFind"));
        //setIsEnableOwnerFind(isEnableOwnerFind);

        //System.out.println(isEnableOwnerPage);

    }

}
