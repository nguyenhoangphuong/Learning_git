package com.misfit.ta.ios.modelapi.signin;

import java.io.File;

import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ta.gui.DefaultStrings;
import com.misfit.ta.gui.HomeScreen;
import com.misfit.ta.gui.LaunchScreen;
import com.misfit.ta.gui.PrometheusHelper;
import com.misfit.ta.gui.SignIn;
import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.modelAPI.ModelAPI;

public class SignInWithFacebookAPI extends ModelAPI {
	public SignInWithFacebookAPI(AutomationTest automation, File model,
			boolean efsm, PathGenerator generator, boolean weight) {
		super(automation, model, efsm, generator, weight);
	}


	public void e_Init() {
	}

	public void e_SignInWithFacebook() {
		
		// we use default Facebook account mfwcqa@gmail.com which is already logged in
		SignIn.tapLogInWithFacebook();
		PrometheusHelper.waitForViewToDissappear("UIButtonLabel", DefaultStrings.SignUpButton, 10);
		
		// token expired, try to log in another time
		if(LaunchScreen.isAtInitialScreen())
		{
			SignIn.tapLogInWithFacebook();
			PrometheusHelper.waitForViewToDissappear("UIButtonLabel", DefaultStrings.SignUpButton, 10);
		}
	}


	
	public void v_HomeScreen() {
    	Assert.assertTrue(HomeScreen.isToday(), "Current view is HomeScreen - Today");
	}

	public void v_InitialView() {
		Assert.assertTrue(LaunchScreen.isAtInitialScreen(), "Curren view is InitialView");
	}

}
