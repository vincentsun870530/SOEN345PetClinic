package org.springframework.samples.petclinic.FeatureToggles;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@WebServlet(urlPatterns = {"/featureToggle"})
public class FeatureToggles extends HttpServlet {

    //public static FeatureToggles objFeatureToggles = new FeatureToggles();

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
    public static boolean isEnableOwnerCreateIR = true;
    public static boolean isEnableOwnerEditIR = true;
    public static boolean isEnablePetAddIR = true;
    public static boolean isEnablePetEditIR = true;
    public static boolean isEnablePetVisitIR = true;
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

    // For a/b test on the Find Owners in the navbar
    public static boolean isEnableTabOwnerChange = true;
    public static boolean isEnableTabOwnerChangeRandom = true;

    // No Home Button
    public static boolean hasHomeButton = false;

    // Remove Find Owner Button
    public static boolean isEnabledLegacyFindOwnerButton = true;

    // Delete Owner Enabler (For Rollback Purpose)

    // A/B testing toggle
    public static boolean isEnableDeleteOwner = false;
    public static boolean isEnableDeleteOwnerRandom1 = true;
    public static boolean isEnableDeleteOwnerRandom2 = true;


    //For enable different delete Visit button A/B testing
    public static boolean isEnableDeleteVisit = true;

    public static boolean isEnableDeleteVisitRandom = true;


    // Delete Owner Toggle Value
    public static boolean deleteOwnerToggle = true;

    // Welcome Page Enabler (For Rollback Purpose)
    public static boolean isEnabledLegacyWelcomePage = false;
    // Welcome Page Toggle Value
    public static boolean welcomePageToggle = true;

    @GetMapping("/featureToggle")
    public String initFeatureToggleTable() {

        return null;

    }

    /**
     * public void changeToggles() {
     * document.getI
     * }
     **/

    //reference: https://stackoverflow.com/questions/31543454/how-to-take-an-html-value-and-make-it-a-java-variable
    @RequestMapping(value = "featureToggle", method = RequestMethod.POST)
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        String value = "Enable";

        // For package owner
        isEnableOwnerPage = value.equals(request.getParameter("ownerPage"));
        isEnableOwnerCreate = value.equals(request.getParameter("ownerCreate"));
        isEnableOwnerFind = value.equals(request.getParameter("ownerFind"));
        isEnableOwnerEdit = value.equals(request.getParameter("ownerEdit"));
        isEnablePetAdd = value.equals(request.getParameter("petAdd"));
        isEnablePetEdit = value.equals(request.getParameter("petEdit"));
        isEnablePetVisit = value.equals(request.getParameter("petVisit"));

        //Incremental Replication
        isEnableIR = value.equals(request.getParameter("incrementalReplication"));
        isEnableOwnerCreateIR = value.equals(request.getParameter("ownerCreateIR"));
        isEnableOwnerEditIR = value.equals(request.getParameter("ownerEditIR"));
        isEnablePetAddIR = value.equals(request.getParameter("petAddIR"));
        isEnablePetEditIR = value.equals(request.getParameter("petEditIR"));
        isEnablePetVisitIR = value.equals(request.getParameter("petVisitIR"));

        //For Date incremental
        isEnableIncrementDate = value.equals(request.getParameter("incrementDate"));

        // For package vet
        isEnableVetPage = value.equals(request.getParameter("vetPage"));

        // For shadow write
        isEnableShadowWrite = value.equals(request.getParameter("shadowWrite"));

        // For shadow read
        isEnableShadowRead = value.equals(request.getParameter("shadowRead"));

        //For temp debugging system.out.print
        isEnableDebuggingSystemOutPrint = value.equals(request.getParameter("debuggingSystemOutPrint"));

        // For a/b test on the Find Owners in the navbar
        isEnableTabOwnerChange = value.equals(request.getParameter("ownerTabChange"));
        isEnableTabOwnerChangeRandom = value.equals(request.getParameter("ownerTabChangeRandom"));

        // No Home Button
        hasHomeButton = value.equals(request.getParameter("homeButton"));

        // Remove Find Owner Button
        isEnabledLegacyFindOwnerButton = value.equals(request.getParameter("newOwnerFindButton"));

        // Delete Owner Enabler (For Rollback Purpose)
        // A/B testing toggle
        isEnableDeleteOwner = value.equals(request.getParameter("ownerDelete"));
        isEnableDeleteOwnerRandom1 = value.equals(request.getParameter("ownerDeleteRandom1"));
        isEnableDeleteOwnerRandom2 = value.equals(request.getParameter("ownerDeleteRandom2"));
        // Delete Owner Toggle Value
        deleteOwnerToggle = value.equals(request.getParameter("ownerDeleteToggle"));

        isEnableDeleteVisit =  value.equals(request.getParameter("visitDelete"));

        // Welcome Page Enabler (For Rollback Purpose)
        isEnabledLegacyWelcomePage = value.equals(request.getParameter("newWelcomePage"));
        // Welcome Page Toggle Value
        welcomePageToggle = value.equals(request.getParameter("welcomePageToggle"));

        // redirect page
        response.sendRedirect("");

    }

    public static boolean isIsEnableOwnerPage() {
        return isEnableOwnerPage;
    }

    public static boolean isIsEnableOwnerCreate() {
        return isEnableOwnerCreate;
    }

    public static boolean isIsEnableOwnerFind() {
        return isEnableOwnerFind;
    }

    public static boolean isIsEnableOwnerEdit() {
        return isEnableOwnerEdit;
    }

    public static boolean isIsEnablePetAdd() {
        return isEnablePetAdd;
    }

    public static boolean isIsEnablePetEdit() {
        return isEnablePetEdit;
    }

    public static boolean isIsEnablePetVisit() {
        return isEnablePetVisit;
    }

    public static boolean isIsEnableIR() {
        return isEnableIR;
    }

    public static boolean isIsEnableOwnerCreateIR() {
        return isEnableOwnerCreateIR;
    }

    public static boolean isIsEnableOwnerEditIR() {
        return isEnableOwnerEditIR;
    }

    public static boolean isIsEnablePetAddIR() {
        return isEnablePetAddIR;
    }

    public static boolean isIsEnablePetEditIR() {
        return isEnablePetEditIR;
    }

    public static boolean isIsEnablePetVisitIR() {
        return isEnablePetVisitIR;
    }

    public static boolean isIsEnableIncrementDate() {
        return isEnableIncrementDate;
    }

    public static boolean isIsEnableVetPage() {
        return isEnableVetPage;
    }

    public static boolean isIsEnableShadowWrite() {
        return isEnableShadowWrite;
    }

    public static boolean isIsEnableShadowRead() {
        return isEnableShadowRead;
    }

    public static boolean isIsEnableDebuggingSystemOutPrint() {
        return isEnableDebuggingSystemOutPrint;
    }

    public static boolean isIsEnableTabOwnerChange() {
        return isEnableTabOwnerChange;
    }

    public static boolean isIsEnableTabOwnerChangeRandom() {
        return isEnableTabOwnerChangeRandom;
    }

    public static boolean isHasHomeButton() {
        return hasHomeButton;
    }

    public static boolean isIsEnabledLegacyFindOwnerButton() {
        return isEnabledLegacyFindOwnerButton;
    }

    public static boolean isIsEnableDeleteOwnerRandom1() {
        return isEnableDeleteOwnerRandom1;
    }

    public static boolean isIsEnableDeleteOwnerRandom2() {
        return isEnableDeleteOwnerRandom2;
    }

    public static boolean isIsEnableDeleteOwner() {
        return isEnableDeleteOwner;

    }
    public static boolean isISEnableDeleteVisit() {
        return isEnableDeleteVisit;
    }


    public static boolean isDeleteOwnerToggle() {
        return deleteOwnerToggle;
    }

    public static boolean isIsEnabledLegacyWelcomePage() {
        return isEnabledLegacyWelcomePage;
    }

    public static boolean isWelcomePageToggle() {
        return welcomePageToggle;
    }
}

