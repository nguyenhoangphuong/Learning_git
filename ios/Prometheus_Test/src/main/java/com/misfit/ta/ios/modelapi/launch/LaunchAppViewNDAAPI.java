package com.misfit.ta.ios.modelapi.launch;

import java.io.File;
import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ios.ViewUtils;
import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.modelAPI.ModelAPI;
import com.misfit.ta.gui.LaunchScreen;

public class LaunchAppViewNDAAPI extends ModelAPI {
	public LaunchAppViewNDAAPI(AutomationTest automation, File model,
			boolean efsm, PathGenerator generator, boolean weight) {
		super(automation, model, efsm, generator, weight);
	}

	/**
	 * This method implements the Edge 'e_dismissHardwareTutorials'
	 * 
	 */
	public void e_dismissHardwareTutorials() {
		LaunchScreen.closeHardwareTutorial();
	}

	/**
	 * This method implements the Edge 'e_start'
	 * 
	 */
	public void e_start() {
		// nothing to do here :P
	}

	/**
	 * This method implements the Edge 'e_tapAgree'
	 * 
	 */
	public void e_tapAgree() {
		LaunchScreen.tapAgreeInNDA();
	}

	/**
	 * This method implements the Edge 'e_tapDeviceSetup'
	 * 
	 */
	public void e_tapDeviceSetup() {
		LaunchScreen.tapSetUpYourShine();
	}

	/**
	 * This method implements the Edge 'e_tapViewNDA'
	 * 
	 */
	public void e_tapViewNDA() {
		LaunchScreen.tapViewNDA();
	}

	/**
	 * This method implements the Vertex 'v_NDAView'
	 * 
	 */
	public void v_NDAView() {
		Assert.assertTrue(ViewUtils.isExistedView("UIButtonLabel", "AGREE") &&
				ViewUtils.isExistedView("UIButtonLabel", "DISAGREE"),
				"NDA is displayed");
	}

	/**
	 * This method implements the Vertex 'v_hardwareTutorialView'
	 * 
	 */
	public void v_hardwareTutorialView() {
		// still have not found a correct way to verify this screen being shown
	}

	/**
	 * This method implements the Vertex 'v_haveShineView'
	 * 
	 */
	public void v_haveShineView() {
		Assert.assertTrue(LaunchScreen.isAtLaunchScreen(),
				"Launch screen is displayed");
	}

	/**
	 * This method implements the Vertex 'v_launchView'
	 * 
	 */
	public void v_launchView() {
		// nothing to do here
	}

}
