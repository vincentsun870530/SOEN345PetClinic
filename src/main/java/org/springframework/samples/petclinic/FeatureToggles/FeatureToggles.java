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
    public static boolean isEnableDebuggingSystemOutPrint = false;

    // For a/b test on the Find Owners in the navbar
    //public static boolean isEnableTabOwnerChange = true;


    // No Home Button
    public static boolean hasHomeButton = false;

    // Remove Find Owner Button
    public static boolean isEnabledLegacyFindOwnerButton = false;

    public static boolean welcomePageToggle = false;

    //  A/B test Feature1
    public static boolean isEnableFeature1Random1 = false;
    public static boolean isEnableFeature1Random2 = false;
    public static boolean isEnableFeature1 = false;

    // A/B test Feature2
    public static boolean Feature2 = false;

    // A/B test Feature3
    public static boolean Feature3 = false;
    public static boolean isEnableTabOwnerChangeRandom = false;

    // A/B test Feature4
    public static boolean isEnableFeature4 = false;
    public static boolean isEnableDeleteVisitRandom = false;

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
        //objFeatureToggles.setIsEnableOwnerPage(value.equals(request.getParameter("ownerPage")));
        isEnableOwnerCreate = value.equals(request.getParameter("ownerCreate"));
        //setIsEnableOwnerCreate(isEnableOwnerCreate);
        isEnableOwnerFind = value.equals(request.getParameter("ownerFind"));
        //setIsEnableOwnerFind(isEnableOwnerFind);
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

        //isEnableFeature1 = value.equals(request.getParameter("ownerDelete"));

        //  A/B test Feature1
        isEnableFeature1 = value.equals(request.getParameter("feature1"));

        //  A/B test Feature2
        Feature2 = value.equals(request.getParameter("feature2"));

        //  A/B test Feature3
        Feature3 = value.equals(request.getParameter("feature3"));

        //  A/B test Feature4
        isEnableFeature4 = value.equals(request.getParameter("feature4"));
        // A/B testing toggle for owner tab
        //isEnableTabOwnerChange = value.equals(request.getParameter("ownerTab"));

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

    public static boolean isIsEnableFeature4(){
        return isEnableFeature4;
    }

    public static boolean isIsEnableFeature1() {
        return isEnableFeature1;
    }

    public static boolean isFeature2() {
        return Feature2;
    }

    public static boolean isFeature3() {
        return Feature3;
    }

    /*public static boolean isIsEnableTabOwnerChange() {
        return isEnableTabOwnerChange;
    }*/

  /*  //public static boolean isEnabledLegacyWelcomePage() {
        return isEnabledLegacyWelcomePage;
    }*/
}

