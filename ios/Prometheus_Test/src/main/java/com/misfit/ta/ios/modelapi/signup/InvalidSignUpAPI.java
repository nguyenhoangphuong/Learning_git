package com.misfit.ta.ios.modelapi.signup;

import java.io.File;

import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ta.modelAPI.ModelAPI;

import com.misfit.ta.gui.Gui;
import com.misfit.ta.gui.LaunchScreen;
import com.misfit.ta.gui.PrometheusHelper;
import com.misfit.ta.gui.SignUp;
import com.misfit.ta.ios.AutomationTest;

public class InvalidSignUpAPI extends ModelAPI {
    public InvalidSignUpAPI(AutomationTest automation, File model, boolean efsm, PathGenerator generator,
            boolean weight) {
        super(automation, model, efsm, generator, weight);
    }

    
    public void e_Init() {
        LaunchScreen.launch();
    }

    public void e_Back() {
        Gui.tapPrevious();
    }

    public void e_ChooseSignUp() {
        SignUp.tapSignUp();
    }

    public void e_ConfirmAlert() {
        SignUp.tapOK();
    }

    public void e_FillExistedEmail() {
        SignUp.enterEmailPassword("mfwcqa.automation@gmail.com", "qwerty1");
        PrometheusHelper.waitForAlert();
    }

    public void e_FillInvalidEmail() {
        SignUp.enterEmailPassword("test@com.", "test12");
        PrometheusHelper.waitForAlert();
    }

    public void e_FillInvalidPassword() {
        SignUp.enterEmailPassword("test@test.com", "pass");
        PrometheusHelper.waitForAlert();
    }

    
    
    

    public void v_InitialView() {
        Assert.assertTrue(LaunchScreen.isAtInitialScreen(), "Current view is InitialScreen");
    }

    public void v_SignUpStep1() {
        Assert.assertTrue(SignUp.isSignUpAccountView(), "This is not sign up step1 view.");
    }

    public void v_SignUpStep1DuplicatedEmail() {
        Assert.assertTrue(SignUp.hasExistedEmailMessage(), "This is not sign up duplicated email view.");
    }

    public void v_SignUpStep1InvalidEmail() {
        Assert.assertTrue(SignUp.hasSignUpInvalidEmailMessage(), "This is not sign up invalid email view.");
    }

    public void v_SignUpStep1InvalidPassword() {
        Assert.assertTrue(SignUp.hasInvalidPasswordMessage(), "This is not sign up invalid password view.");
    }

}
