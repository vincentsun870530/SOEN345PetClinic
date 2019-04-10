package org.springframework.samples.petclinic.owner;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.springframework.samples.petclinic.ABTest.DeleteOwnerBtnHelper.countDeleteOwnerBtnOne;

import org.junit.Before;
import org.junit.Test;
import org.springframework.samples.petclinic.ABTest.DeleteOwnerBtnHelper;
import org.springframework.samples.petclinic.FeatureToggles.FeatureToggles;
import org.springframework.ui.Model;

import java.sql.SQLException;


public class RollbackDeleteOwnerTest {
    OwnerController ownerController;
    OwnerRepository owners;
    Model model;
    Owner owner;
    DeleteOwnerBtnHelper deleteOwnerBtnHelper;
    private static final int TEST_OWNER_ID = 2;

    @Before
    public void setUp() {

        owner = mock(Owner.class);
        model = mock(Model.class);
        owners= mock(OwnerRepository.class);
        deleteOwnerBtnHelper = mock(DeleteOwnerBtnHelper.class);
    }

    @Test
    public void TestRollbackDeleteOwner() throws SQLException {
        ownerController = new OwnerController(owners);
        //Delete owner Button Version 2 is off/dark but Button Version 1 is on
        FeatureToggles.isEnableDeleteOwner = false;
        ownerController.DeleteOwnerTwo(TEST_OWNER_ID, model);
        try {
            String str1 = ownerController.DeleteOwnerTwo(TEST_OWNER_ID, model);
            String str2 = "owners/findOwners";
            assertEquals(str1, str2);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //Delete owner Button Version 2 is on
        FeatureToggles.isEnableDeleteOwner = true;
        try {
            when(owners.findById(TEST_OWNER_ID)).thenReturn(owner);
            when(owner.getId()).thenReturn(TEST_OWNER_ID);
            verify(owners , times(1)).findById(TEST_OWNER_ID);
            verify(owners , times(1)).deleteById(owner.getId());
            verify(model, times(1)).addAttribute(owner);

            String str1 = ownerController.DeleteOwnerTwo(TEST_OWNER_ID, model);
            String str2 = "owners/deleteBtnVersionOne";
            assertEquals(str1, str2);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //turn off the feature, no discount
    }
    @Test
    public void rollbacktest(){
        FeatureToggles.isEnableDeleteOwner = false;
        if(FeatureToggles.isEnableDeleteOwner ==false){
            DeleteOwnerBtnHelper.countDeleteOwnerBtnOne();
        }else{
            DeleteOwnerBtnHelper.countDeleteOwnerBtnTwo();
        }

    }

}
