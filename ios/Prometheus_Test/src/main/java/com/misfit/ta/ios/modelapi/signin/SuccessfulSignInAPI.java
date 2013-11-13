package com.misfit.ta.ios.modelapi.signin;

import java.io.File;

import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ta.modelAPI.ModelAPI;

import com.misfit.ta.gui.DefaultStrings;
import com.misfit.ta.gui.HomeScreen;
import com.misfit.ta.gui.LaunchScreen;
import com.misfit.ta.gui.PrometheusHelper;
import com.misfit.ta.gui.SignIn;
import com.misfit.ta.ios.AutomationTest;

public class SuccessfulSignInAPI extends ModelAPI {
	
	public SuccessfulSignInAPI(AutomationTest automation, File model,
			boolean efsm, PathGenerator generator, boolean weight) {
		super(automation, model, efsm, generator, weight);
	}

	
	
	public void e_Init() {
		LaunchScreen.launch();
	}

	public void e_ChooseSignIn() {
		SignIn.tapLogIn();
	}

	public void e_FillCorrectEmailPassword() {
		
		SignIn.enterEmailPassword("nhhai16991@gmail.com", "qqqqqq");
		PrometheusHelper.waitForViewToDissappear("UILabel", DefaultStrings.SignInTitle);
		PrometheusHelper.handleTutorial();
	}

	
		
	public void v_InitialView() {
		Assert.assertTrue(LaunchScreen.isAtInitialScreen(),
				"Current view is InitialView");
	}

	public void v_HomeScreen() {
		Assert.assertTrue(HomeScreen.isToday(),
				"Current view is HomeScreen - Today");
	}

	public void v_SignInVisible() {
		Assert.assertTrue(SignIn.isLoginView(), "This is not sign in view.");
	}

}
