package com.misfit.ta.ios.modelapi.signup;

import java.io.File;

import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ta.modelAPI.ModelAPI;
import com.misfit.ta.utils.ShortcutsTyper;

import com.misfit.ta.gui.Gui;
import com.misfit.ta.gui.SignUp;
import com.misfit.ta.ios.AutomationTest;

public class InvalidSignUpAPI extends ModelAPI {
    public InvalidSignUpAPI(AutomationTest automation, File model, boolean efsm, PathGenerator generator,
            boolean weight) {
        super(automation, model, efsm, generator, weight);
    }

    
    /**
     * This method implements the Edge 'e_Init'
     * 
     */
    public void e_Init() {
        //LaunchScreen.launch();
    }
    
    /**
     * This method implements the Edge 'e_Back'
     * 
     */
    public void e_Back() {
        Gui.tapPrevious();
        ShortcutsTyper.delayTime(300);
    }

    /**
     * This method implements the Edge 'e_ChooseSignUp'
     * 
     */
    public void e_ChooseSignUp() {
        SignUp.tapSignUp();
        ShortcutsTyper.delayTime(300);
    }

    /**
     * This method implements the Edge 'e_ConfirmAlert'
     * 
     */
    public void e_ConfirmAlert() {
        SignUp.tapOK();
        ShortcutsTyper.delayTime(500);
    }

    /**
     * This method implements the Edge 'e_FillExistedEmail'
     * 
     */
    public void e_FillExistedEmail() {
        SignUp.enterEmailPassword("thy@misfitwearables.com", "test12");
        ShortcutsTyper.delayTime(5000);
    }

    /**
     * This method implements the Edge 'e_FillInvalidEmail'
     * 
     */
    public void e_FillInvalidEmail() {
        SignUp.enterEmailPassword("test@com.", "test12");
        ShortcutsTyper.delayTime(500);
    }

    /**
     * This method implements the Edge 'e_FillInvalidPassword'
     * 
     */
    public void e_FillInvalidPassword() {
        SignUp.enterEmailPassword("test@test.com", "pass");
        ShortcutsTyper.delayTime(500);
    }

    
    
    
    /**
     * This method implements the Vertex 'v_InitialView'
     * 
     */
    public void v_InitialView() {
        // TODO:
    }

    /**
     * This method implements the Vertex 'v_SignUpStep1'
     * 
     */
    public void v_SignUpStep1() {
        Assert.assertTrue(SignUp.isSignUpAccountView(), "This is not sign up step1 view.");
        ShortcutsTyper.delayTime(500);
    }

    /**
     * This method implements the Vertex 'v_SignUpStep1DuplicatedEmail'
     * 
     */
    public void v_SignUpStep1DuplicatedEmail() {
        Assert.assertTrue(SignUp.hasExistedEmailMessage(), "This is not sign up duplicated email view.");
        ShortcutsTyper.delayTime(500);
    }

    /**
     * This method implements the Vertex 'v_SignUpStep1InvalidEmail'
     * 
     */
    public void v_SignUpStep1InvalidEmail() {
        Assert.assertTrue(SignUp.hasInvalidEmailMessage(), "This is not sign up invalid email view.");
        ShortcutsTyper.delayTime(500);
    }

    /**
     * This method implements the Vertex 'v_SignUpStep1InvalidPassword'
     * 
     */
    public void v_SignUpStep1InvalidPassword() {
        Assert.assertTrue(SignUp.hasInvalidPasswordMessage(), "This is not sign up invalid password view.");
        ShortcutsTyper.delayTime(500);
    }

}
