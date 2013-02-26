package com.misfit.ta.android.modelapi;

import java.io.File;

import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ta.android.AutomationTest;
import com.misfit.ta.android.gui.SignIn;
import com.misfit.ta.modelAPI.ModelAPI;

public class SignInAPI extends ModelAPI {
    public SignInAPI(AutomationTest automation, File model, boolean efsm, PathGenerator generator, boolean weight) {
        super(automation, model, efsm, generator, weight);
    }

    /**
     * This method implements the Edge 'e_ChooseSignUp'
     * 
     */
    public void e_ChooseSignIn() {
        SignIn.chooseSignIn();

    }

    /**
     * This method implements the Edge 'e_Init'
     * 
     */
    public void e_Init() {
        
    }

    /**
     * This method implements the Edge 'e_PressBack'
     * 
     */
    public void e_PressBack() {
        SignIn.pressBack();
    }

    /**
     * This method implements the Vertex 'v_InitView'
     * 
     */
    public void v_InitView() {
        Assert.assertTrue(SignIn.isInitViewVisible(), "Init view is not visible");
    }

    /**
     * This method implements the Vertex 'v_UserInfo'
     * 
     */
    public void v_SignInVisible() {
        Assert.assertTrue(SignIn.isSignInVisible(), "Sign in is not visible");
    }

}
