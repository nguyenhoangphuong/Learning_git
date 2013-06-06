package com.misfit.ta.ios.modelapi.signin;

import java.io.File;

import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ta.modelAPI.ModelAPI;
import com.misfit.ta.utils.ShortcutsTyper;

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
    private String[] invalidPasswords = {"qwerty", "123456", "qwert.", "12345."};
    
    /**
     * This method implements the Edge 'e_Init'
     * 
     */
    public void e_Init() {
    	LaunchScreen.launch(); 
    }
    
    /**
     * This method implements the Edge 'e_CancelResetPassword'
     * 
     */
    public void e_CancelResetPassword() {
        SignIn.tapCancelResetPassword();
        ShortcutsTyper.delayTime(1000);
    }

    /**
     * This method implements the Edge 'e_ChooseForgotPassword'
     * 
     */
    public void e_ChooseTryAgain() {
        SignIn.tapTryAgain();
        ShortcutsTyper.delayTime(1000);
    }

    /**
     * This method implements the Edge 'e_ChooseForgotPassword'
     * 
     */
    public void e_ChooseForgotPassword() {
        SignIn.tapForgotPassword();
        ShortcutsTyper.delayTime(1000);
    }

    /**
     * This method implements the Edge 'e_ChooseSignIn'
     * 
     */
    public void e_ChooseSignIn() {
        SignIn.tapLogIn();
        ShortcutsTyper.delayTime(1000);
    }

    /**
     * This method implements the Edge 'e_ConfirmAlert'
     * 
     */
    public void e_ConfirmAlert() {
        SignIn.tapOK();
        ShortcutsTyper.delayTime(1000);
    }

    /**
     * This method implements the Edge 'e_ConfirmForgotPassword'
     * 
     */
    public void e_ConfirmForgotPassword() {
        SignIn.tapIForgot();
        ShortcutsTyper.delayTime(1000);
    }

    /**
     * This method implements the Edge 'e_FillIncorrectPassword'
     * 
     */
    public void e_FillIncorrectPassword() {
        SignIn.enterEmailPassword("test147@thy.com", "test11");
        ShortcutsTyper.delayTime(5000);
    }

    /**
     * This method implements the Edge 'e_FillInvalidEmail'
     * 
     */
    public void e_FillInvalidEmail() {
    	String email = invalidEmails[PrometheusHelper.randInt(0, invalidEmails.length)];
        SignIn.enterEmailPassword(email, "qwerty1");
        ShortcutsTyper.delayTime(1000);
    }

    /**
     * This method implements the Edge 'e_FillInvalidPassword'
     * 
     */
    public void e_FillInvalidPassword() {
    	String password = invalidPasswords[PrometheusHelper.randInt(0, invalidPasswords.length)];
        SignIn.enterEmailPassword("valid@email.com", password);
        ShortcutsTyper.delayTime(1000);
    }

    /**
     * This method implements the Edge 'e_FillNotExistedEmail'
     * 
     */
    public void e_FillNotExistedEmail() {
        SignIn.enterEmailPassword(MVPApi.generateUniqueEmail(), "abc1333");
        ShortcutsTyper.delayTime(5000);
    }

    /**
     * This method implements the Edge 'e_PressBack'
     * 
     */
    public void e_PressBack() {
        SignIn.tapPrevious();
        ShortcutsTyper.delayTime(1000);
    }

    
    
    /**
     * This method implements the Vertex 'v_InitialView'
     * 
     */
    public void v_InitialView() {
    }
    
    /**
     * This method implements the Vertex 'v_ForgotPassword'
     * 
     */
    public void v_ForgotPassword() {
        Assert.assertTrue(SignIn.isForgotPasswordView(), "This is not forgot password view.");
    }

    /**
     * This method implements the Vertex 'v_IncorrectPassword'
     * 
     */
    public void v_IncorrectPassword() {
        Assert.assertTrue(SignIn.hasIncorrectLoginMessage(), "This is not incorrect password view.");
    }

    /**
     * This method implements the Vertex 'v_InvalidEmail'
     * 
     */
    public void v_InvalidEmail() {
        Assert.assertTrue(SignIn.hasInvalidEmailMessage(), "This is not invalid email view.");
    }

    /**
     * This method implements the Vertex 'v_InvalidPassword'
     * 
     */
    public void v_InvalidPassword() {
        Assert.assertTrue(SignIn.hasInvalidPasswordMessage(), "This is not invalid password view.");
    }

    /**
     * This method implements the Vertex 'v_NotExistedEmail'
     * 
     */
    public void v_NotExistedEmail() {
        Assert.assertTrue(SignIn.hasIncorrectLoginMessage(), "This is not incorrect email view.");
    }

    /**
     * This method implements the Vertex 'v_SignInVisible'
     * 
     */
    public void v_SignInVisible() {
        Assert.assertTrue(SignIn.isLoginView(), "This is not sign in view.");
    }

}
