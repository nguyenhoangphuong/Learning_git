package com.misfit.ta.ios.modelapi.signin;

import java.io.File;

import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ta.modelAPI.ModelAPI;
import com.misfit.ta.utils.ShortcutsTyper;

import com.misfit.ta.gui.SignIn;
import com.misfit.ta.ios.AutomationTest;

public class ForgotPasswordAPI extends ModelAPI {
    public ForgotPasswordAPI(AutomationTest automation, File model, boolean efsm, PathGenerator generator,
            boolean weight) {
        super(automation, model, efsm, generator, weight);
    }

    /**
     * This method implements the Edge 'e_ConfirmAlert'
     * 
     */
    public void e_ConfirmAlert() {
        SignIn.tapTryAgain();
        ShortcutsTyper.delayTime(00);
    }

    /**
     * This method implements the Edge 'e_InputInvalidEmail'
     * 
     */
    public void e_InputInvalidEmail() {
        SignIn.enterEmail("test.t.c");
        SignIn.tapSubmit();
        ShortcutsTyper.delayTime(1000);
    }

    /**
     * This method implements the Edge 'e_InputNotExistedEmail'
     * 
     */
    public void e_InputNotExistedEmail() {
        SignIn.enterEmail("abc@thy.comabc");
        SignIn.tapSubmit();
        ShortcutsTyper.delayTime(1000);
    }

	/**
	 * This method implements the Edge 'e_Submit'
	 * 
	 */
	public void e_Submit() {
		SignIn.tapSubmit();
		ShortcutsTyper.delayTime(1000);
	}

    /**
     * This method implements the Edge 'e_gotoForgotPassword'
     * 
     */
    public void e_gotoForgotPassword() {
        // TODO:
    }

    /**
     * This method implements the Edge 'e_tapForgotPassword'
     * 
     */
    public void e_tapForgotPassword() {
        SignIn.tapForgotPassword();
        ShortcutsTyper.delayTime(300);
    }

    /**
     * This method implements the Vertex 'v_ForgotPassword'
     * 
     */
    public void v_ForgotPassword() {
        Assert.assertTrue(SignIn.isForgotPasswordView(), "This is not forgot password view.");
        ShortcutsTyper.delayTime(300);
    }

	/**
	 * This method implements the Vertex 'v_InvalidEmailView'
	 * 
	 */
	public void v_InvalidEmailView() {
		Assert.assertTrue(SignIn.hasInvalidEmailMessage(),
				"This is not forgot password invalid email view.");
		ShortcutsTyper.delayTime(300);
	}

	/**
	 * This method implements the Vertex 'v_NotExistedEmailView'
	 * 
	 */
	public void v_NotExistedEmailView() {
//		Assert.assertTrue(SignIn.hasNotAssociatedEmailMessage(),
//				"This is not forgot password not existed email view.");
//		ShortcutsTyper.delayTime(300);
	}

	/**
	 * This method implements the Vertex 'v_SignInView'
	 * 
	 */
	public void v_SignInView() {
		Assert.assertTrue(SignIn.isLoginView(),
				"This is not forgot password log in view.");
		ShortcutsTyper.delayTime(300);
	}

}
