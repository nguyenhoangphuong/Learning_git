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

public class ForgotPasswordAPI extends ModelAPI {
	public ForgotPasswordAPI(AutomationTest automation, File model,
			boolean efsm, PathGenerator generator, boolean weight) {
		super(automation, model, efsm, generator, weight);
	}

	private String[] invalidEmails = {"aa", "aaa@", ".aaa@a.a", "aa@@a.a", "aaa@a..a", "aaa@a.a."};
	
	/**
	 * This method implements the Edge 'e_gotoForgotPassword'
	 * 
	 */
	public void e_gotoForgotPassword() 
	{
		LaunchScreen.launch();
		SignIn.tapLogIn();
		ShortcutsTyper.delayOne();
		SignIn.tapForgotPassword();
		ShortcutsTyper.delayOne();
	}
	
	/**
	 * This method implements the Edge 'e_ConfirmAlert'
	 * 
	 */
	public void e_ConfirmAlert() {
		SignIn.tapCancel();
		ShortcutsTyper.delayTime(1000);
	}

	public void e_TapOK() {
		SignIn.tapOK();
		ShortcutsTyper.delayTime(1000);
	}

	/**
	 * This method implements the Edge 'e_InputInvalidEmail'
	 * 
	 */
	public void e_InputInvalidEmail() {
		SignIn.enterEmailForResetPassword(invalidEmails[PrometheusHelper.randInt(0, invalidEmails.length)]);
		SignIn.tapSubmitResetPassword();
		ShortcutsTyper.delayTime(1000);
	}

	/**
	 * This method implements the Edge 'e_InputNotExistedEmail'
	 * 
	 */
	public void e_InputNotExistedEmail() {
		SignIn.enterEmailForResetPassword(MVPApi.generateUniqueEmail());
		SignIn.tapSubmitResetPassword();
		ShortcutsTyper.delayTime(5000);
	}

	/**
	 * This method implements the Edge 'e_Submit'
	 * 
	 */
	public void e_Submit() {
		SignIn.enterEmailForResetPassword("mfwcqa.automation@gmail.com");
		SignIn.tapSubmitResetPassword();
		ShortcutsTyper.delayTime(5000);
	}

	/**
	 * This method implements the Edge 'e_tapForgotPassword'
	 * 
	 */
	public void e_tapForgotPassword() {
		SignIn.tapForgotPassword();
		ShortcutsTyper.delayTime(1000);
	}

	
	
	
	/**
	 * This method implements the Vertex 'v_ForgotPassword'
	 * 
	 */
	public void v_ForgotPassword() {
		Assert.assertTrue(SignIn.isForgotPasswordView(), "This is not forgot password view.");
	}

	/**
	 * This method implements the Vertex 'v_InvalidEmailView'
	 * 
	 */
	public void v_InvalidEmailView() {
		Assert.assertTrue(SignIn.hasForgotPasswordInvalidEmailMessage(), "This is not forgot password invalid email view.");
	}

	/**
	 * This method implements the Vertex 'v_EmailSentView'
	 * 
	 */
	public void v_EmailSentView() {
		Assert.assertTrue(SignIn.hasEmailSentMessage(), "This is not forgot password email sent view.");
	}

	/**
	 * This method implements the Vertex 'v_NotExistedEmailView'
	 * 
	 */
	public void v_NotExistedEmailView() {
		Assert.assertTrue(SignIn.hasNotAssociatedEmailMessage(), "This is not forgot password not existed email view.");
	}

	/**
	 * This method implements the Vertex 'v_SignInView'
	 * 
	 */
	public void v_SignInView() {
		Assert.assertTrue(SignIn.isLoginView(), "This is not forgot password log in view.");
	}

}
