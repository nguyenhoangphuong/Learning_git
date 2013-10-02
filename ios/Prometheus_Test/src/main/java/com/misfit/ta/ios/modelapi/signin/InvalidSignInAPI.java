package com.misfit.ta.ios.modelapi.signin;

import java.io.File;

import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ta.modelAPI.ModelAPI;

import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.gui.LaunchScreen;
import com.misfit.ta.gui.PrometheusHelper;
import com.misfit.ta.gui.SignIn;
import com.misfit.ta.ios.AutomationTest;

public class InvalidSignInAPI extends ModelAPI {
    public InvalidSignInAPI(AutomationTest automation, File model, boolean efsm, PathGenerator generator, boolean weight) {
        super(automation, model, efsm, generator, weight);
    }

    private String[] invalidEmails = {"aa", "aaa@", ".aaa@a.a", "aa@@a.a", "aaa@a..a", "aaa@a.a."};
    private String[] invalidPasswords = {"qwert", "12345."};
    
    public void e_Init() {
    	LaunchScreen.launch(); 
    }
    
    public void e_ChooseSignIn() {
        SignIn.tapLogIn();
    }
    
    public void e_FillIncorrectPassword() {
        SignIn.enterEmailPassword("mfwcqa.automation@gmail.com", "test11");
        PrometheusHelper.waitForAlert();
    }

    public void e_FillInvalidEmail() {
    	String email = invalidEmails[PrometheusHelper.randInt(0, invalidEmails.length)];
        SignIn.enterEmailPassword(email, "qwerty1");
        PrometheusHelper.waitForAlert();
    }

    public void e_FillInvalidPassword() {
    	String password = invalidPasswords[PrometheusHelper.randInt(0, invalidPasswords.length)];
        SignIn.enterEmailPassword("valid@email.com", password);
        PrometheusHelper.waitForAlert();
    }

    public void e_FillNotExistedEmail() {
        SignIn.enterEmailPassword(MVPApi.generateUniqueEmail(), "abc1333");
        PrometheusHelper.waitForAlert();
    }
    
    public void e_CancelResetPassword() {
        SignIn.tapCancelResetPassword();
    }

    public void e_ChooseTryAgain() {
        SignIn.tapTryAgain();
    }

    public void e_ChooseForgotPassword() {
        SignIn.tapForgotPassword();
    }

    public void e_ConfirmAlert() {
        SignIn.tapOK();
    }

    public void e_ConfirmForgotPassword() {
        SignIn.tapIForgot();
    }

    public void e_PressBack() {
        SignIn.tapPrevious();
    }

    
    
    
    public void v_InitialView() {
    }

    public void v_ForgotPassword() {
        Assert.assertTrue(SignIn.isForgotPasswordView(), "This is not forgot password view.");
    }

    public void v_IncorrectPassword() {
        Assert.assertTrue(SignIn.hasIncorrectLoginMessage(), "This is not incorrect password view.");
    }

    public void v_InvalidEmail() {
        Assert.assertTrue(SignIn.hasSignInInvalidEmailMessage(), "This is not invalid email view.");
    }

    public void v_InvalidPassword() {
        Assert.assertTrue(SignIn.hasInvalidPasswordMessage(), "This is not invalid password view.");
    }

    public void v_NotExistedEmail() {
        Assert.assertTrue(SignIn.hasIncorrectLoginMessage(), "This is not incorrect email view.");
    }

    public void v_SignInVisible() {
        Assert.assertTrue(SignIn.isLoginView(), "This is not sign in view.");
    }

}
