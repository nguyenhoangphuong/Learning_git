package com.misfit.ta.ios.modelapi.signin;

import java.io.File;

import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ta.modelAPI.ModelAPI;
import com.misfit.ta.utils.ShortcutsTyper;

import com.misfit.ta.gui.LaunchScreen;
import com.misfit.ta.gui.SignIn;
import com.misfit.ta.ios.AutomationTest;

public class InvalidSignInAPI extends ModelAPI {
    public InvalidSignInAPI(AutomationTest automation, File model, boolean efsm, PathGenerator generator, boolean weight) {
        super(automation, model, efsm, generator, weight);
    }

    /**
     * This method implements the Edge 'e_CancelResetPassword'
     * 
     */
    public void e_CancelResetPassword() {
        SignIn.tapCancel();
        ShortcutsTyper.delayTime(5000);
    }

    /**
     * This method implements the Edge 'e_ChooseForgotPassword'
     * 
     */
    public void e_ChooseTryAgain() {
        SignIn.tapTryAgain();
        ShortcutsTyper.delayTime(5000);
    }

    /**
     * This method implements the Edge 'e_ChooseForgotPassword'
     * 
     */
    public void e_ChooseForgotPassword() {
        SignIn.tapForgotPassword();
        ShortcutsTyper.delayTime(5000);
    }

    /**
     * This method implements the Edge 'e_ChooseSignIn'
     * 
     */
    public void e_ChooseSignIn() {
        SignIn.tapLogIn();
        ShortcutsTyper.delayTime(500);
    }

    /**
     * This method implements the Edge 'e_ConfirmAlert'
     * 
     */
    public void e_ConfirmAlert() {
        SignIn.tapOK();
        ShortcutsTyper.delayTime(2000);
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
//        SignIn.tapNext();
        ShortcutsTyper.delayTime(5000);
    }

    /**
     * This method implements the Edge 'e_FillInvalidEmail'
     * 
     */
    public void e_FillInvalidEmail() {
        SignIn.enterEmailPassword("test@a", "abc");
//        SignIn.tapNext();
        ShortcutsTyper.delayTime(5000);
    }

    /**
     * This method implements the Edge 'e_FillInvalidPassword'
     * 
     */
    public void e_FillInvalidPassword() {
        SignIn.enterEmailPassword("test147@thy.com", "abc");
//        SignIn.tapNext();
        ShortcutsTyper.delayTime(5000);
    }

    /**
     * This method implements the Edge 'e_FillNotExistedEmail'
     * 
     */
    public void e_FillNotExistedEmail() {
        SignIn.enterEmailPassword("test147@thyabc.com", "abc1333");
//        SignIn.tapNext();
        ShortcutsTyper.delayTime(5000);
    }

    /**
     * This method implements the Edge 'e_Init'
     * 
     */
    public void e_Init() {
    	LaunchScreen.launch(); 
    }

    /**
     * This method implements the Edge 'e_PressBack'
     * 
     */
    public void e_PressBack() {
        SignIn.tapPrevious();
        ShortcutsTyper.delayTime(3000);
    }

    /**
     * This method implements the Vertex 'v_ForgotPassword'
     * 
     */
    public void v_ForgotPassword() {
        Assert.assertTrue(SignIn.isForgotPasswordView(), "This is not forgot password view.");
        ShortcutsTyper.delayTime(3000);
    }

    /**
     * This method implements the Vertex 'v_IncorrectPassword'
     * 
     */
    public void v_IncorrectPassword() {
        Assert.assertTrue(SignIn.hasIncorrectLoginMessage(), "This is not incorrect password view.");
        ShortcutsTyper.delayTime(3000);
    }

    /**
     * This method implements the Vertex 'v_InitialView'
     * 
     */
    public void v_InitialView() {
        // TODO:
    }

    /**
     * This method implements the Vertex 'v_InvalidEmail'
     * 
     */
    public void v_InvalidEmail() {
        Assert.assertTrue(SignIn.hasInvalidEmailMessage(), "This is not invalid email view.");
        ShortcutsTyper.delayTime(500);
    }

    /**
     * This method implements the Vertex 'v_InvalidPassword'
     * 
     */
    public void v_InvalidPassword() {
        Assert.assertTrue(SignIn.hasInvalidPasswordMessage(), "This is not invalid password view.");
        ShortcutsTyper.delayTime(500);
    }

    /**
     * This method implements the Vertex 'v_NotExistedEmail'
     * 
     */
    public void v_NotExistedEmail() {
        Assert.assertTrue(SignIn.hasIncorrectLoginMessage(), "This is not incorrect email view.");
        ShortcutsTyper.delayTime(500);
    }

    /**
     * This method implements the Vertex 'v_SignInVisible'
     * 
     */
    public void v_SignInVisible() {
        Assert.assertTrue(SignIn.isLoginView(), "This is not sign in view.");
        ShortcutsTyper.delayTime(500);
    }

}
