package com.misfit.ta.ios.modelapi.signin;

import java.io.File;

import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ta.modelAPI.ModelAPI;
import com.misfit.ta.utils.ShortcutsTyper;

import com.misfit.ta.backend.api.internalapi.MVPApi;
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
	
	public void e_gotoForgotPassword() 
	{
		LaunchScreen.launch();
		SignIn.tapLogIn();
		ShortcutsTyper.delayTime(200);
		SignIn.tapForgotPassword();
	}
	
	public void e_ConfirmAlert() {
		SignIn.tapCancel();
	}

	public void e_TapOK() {
		SignIn.tapOK();
	}

	public void e_InputInvalidEmail() {
		SignIn.enterEmailForResetPassword(invalidEmails[PrometheusHelper.randInt(0, invalidEmails.length)]);
		SignIn.tapSubmitResetPassword();
		PrometheusHelper.waitForAlert();
	}

	public void e_InputNotExistedEmail() {
		SignIn.enterEmailForResetPassword(MVPApi.generateUniqueEmail());
		SignIn.tapSubmitResetPassword();
		PrometheusHelper.waitForAlert();
	}

	public void e_Submit() {
		SignIn.enterEmailForResetPassword("mfwcqa.automation@gmail.com");
		SignIn.tapSubmitResetPassword();
		PrometheusHelper.waitForAlert();
	}

	public void e_tapForgotPassword() {
		SignIn.tapForgotPassword();
	}

		
	
	public void v_ForgotPassword() {
		Assert.assertTrue(SignIn.isForgotPasswordView(), "This is not forgot password view.");
	}

	public void v_InvalidEmailView() {
		Assert.assertTrue(SignIn.hasForgotPasswordInvalidEmailMessage(), "This is not forgot password invalid email view.");
	}

	public void v_EmailSentView() {
		Assert.assertTrue(SignIn.hasEmailSentMessage(), "This is not forgot password email sent view.");
	}

	public void v_NotExistedEmailView() {
		Assert.assertTrue(SignIn.hasNotAssociatedEmailMessage(), "This is not forgot password not existed email view.");
	}

	public void v_SignInView() {
		Assert.assertTrue(SignIn.isLoginView(), "This is not forgot password log in view.");
	}

}
