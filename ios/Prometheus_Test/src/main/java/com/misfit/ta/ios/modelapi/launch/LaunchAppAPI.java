package com.misfit.ta.ios.modelapi.launch;

import java.io.File;
import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.modelAPI.ModelAPI;
import com.misfit.ta.gui.LaunchScreen;

public class LaunchAppAPI extends ModelAPI {
	public LaunchAppAPI(AutomationTest automation, File model, boolean efsm,
			PathGenerator generator, boolean weight) {
		super(automation, model, efsm, generator, weight);
	}

	/**
	 * This method implements the Edge 'e_start'
	 * 
	 */
	public void e_start() {
		// nothing to do here :P
	}

	/**
	 * This method implements the Edge 'e_tapAgreeOnPopup'
	 * 
	 */
	public void e_tapAgreeOnPopup() {
		LaunchScreen.tapAgreeOnPopup();
	}

	/**
	 * This method implements the Edge 'e_tapIHaveAShine'
	 * 
	 */
	public void e_tapIHaveAShine() {
		LaunchScreen.tapIHaveAShine();
	}

	/**
	 * This method implements the Vertex 'v_haveShineView'
	 * 
	 */
	public void v_haveShineView() {
		this.v_initialView();
	}

	/**
	 * This method implements the Vertex 'v_initialView'
	 * 
	 */
	public void v_initialView() {
		Assert.assertTrue(LaunchScreen.isAtLaunchScreen(),
				"Launch screen is displayed");
	}

	/**
	 * This method implements the Vertex 'v_launchView'
	 * 
	 */
	public void v_launchView() {
		// nothing to do here :P
	}

}
