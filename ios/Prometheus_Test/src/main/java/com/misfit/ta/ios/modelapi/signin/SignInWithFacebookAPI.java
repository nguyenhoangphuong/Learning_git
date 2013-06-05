package com.misfit.ta.ios.modelapi.signin;

import java.io.File;

import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ta.gui.HomeScreen;
import com.misfit.ta.gui.LaunchScreen;
import com.misfit.ta.gui.SignIn;
import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.modelAPI.ModelAPI;
import com.misfit.ta.utils.ShortcutsTyper;

public class SignInWithFacebookAPI extends ModelAPI {
	public SignInWithFacebookAPI(AutomationTest automation, File model,
			boolean efsm, PathGenerator generator, boolean weight) {
		super(automation, model, efsm, generator, weight);
	}

	/**
	 * This method implements the Edge 'e_Init'
	 * 
	 */
	public void e_Init() {
		LaunchScreen.launch();
	}

	/**
	 * This method implements the Edge 'e_SignInWithFacebook'
	 * 
	 */
	public void e_SignInWithFacebook() {
		
		// we use default Facebook account mfwcqa@gmail.com which is already logged in
		SignIn.tapLogInWithFacebook();
		ShortcutsTyper.delayTime(10000);
		
		// token expired, try to log in another time
		if(!HomeScreen.isToday())
		{
			SignIn.tapLogInWithFacebook();
			ShortcutsTyper.delayTime(10000);
		}
	}

	/**
	 * This method implements the Vertex 'v_HomeScreen'
	 * 
	 */
	public void v_HomeScreen() {
		 // check if this screen is home screen
    	Assert.assertTrue(HomeScreen.isToday(), "Current view is HomeScreen - Today");
	}

	/**
	 * This method implements the Vertex 'v_InitialView'
	 * 
	 */
	public void v_InitialView() {
		Assert.assertTrue(LaunchScreen.isAtInitialScreen(), "Curren view is InitialView");
	}

}
