package com.misfit.ta.ios.modelapi.settings;

import java.io.File;

import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ios.ViewUtils;
import com.misfit.ta.Gui;
import com.misfit.ta.gui.DefaultStrings;
import com.misfit.ta.gui.HomeScreen;
import com.misfit.ta.gui.HomeSettings;
import com.misfit.ta.gui.PrometheusHelper;
import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.modelAPI.ModelAPI;

public class DeviceSettingsAPI extends ModelAPI {

	public DeviceSettingsAPI(AutomationTest automation, File model, boolean efsm,
			PathGenerator generator, boolean weight) {
		super(automation, model, efsm, generator, weight);
	}

	/**
	 * This method implements the Edge 'e_cancel'
	 * 
	 */
	public void e_cancel() {
		HomeScreen.tapCancel();
	}

	/**
	 * This method implements the Edge 'e_goToDeviceSettings'
	 * 
	 */
	public void e_goToDeviceSettings() {
		HomeScreen.tapOpenSettingsTray();
		HomeScreen.tapDevices();
	}

	/**
	 * This method implements the Edge 'e_goToShineSettings'
	 * 
	 */
	public void e_goToShineSettings() {
		HomeScreen.tapActiveDevice();
	}

	/**
	 * This method implements the Edge 'e_input'
	 * 
	 */
	public void e_init() {
		PrometheusHelper.signUp();
	}

	/**
	 * This method implements the Vertex 'v_DevicesSettingScreen'
	 * 
	 */
	public void v_DevicesSettingScreen() {
		Assert.assertTrue(HomeSettings.isAtDevicesSettingScreen(), "Current view is Devices Screen");
	}

	/**
	 * This method implements the Vertex 'v_HomeScreen'
	 * 
	 */
	public void v_HomeScreen() {
		Assert.assertTrue(HomeScreen.isHomeScreen(), "Current view is HomeScreen");
	}

	/**
	 * This method implements the Vertex 'v_ShineSettingsScreen'
	 * 
	 */
	public void v_ShineSettingsScreen() {
		Assert.assertTrue(ViewUtils.isExistedView("UILabel", DefaultStrings.ShineLabel), "Current view is Shine Settings Screen");
	}

	/**
	 * This method implements the Vertex 'v_ShowMyShineDialogScreen'
	 * 
	 */
	public void v_ShowMyShineDialogScreen() {
		Assert.assertTrue(ViewUtils.isExistedView("UILabel", DefaultStrings.OKButton) && ViewUtils.isExistedView("UILabel", DefaultStrings.CancelButton), "Current view is Show My Shine Dialog");
	}

	/**
	 * This method implements the Edge 'v_goToShowMyShine'
	 * 
	 */
	public void e_goToShowMyShine() {
		HomeSettings.tapShowMyShine();
	}
	
	public void e_goToMisfitLabsScreen(){
		Gui.swipeUp(300);
		HomeSettings.tapTaggingAndSleep();
	}
	
	public void v_MisfitLabsScreen(){
		Assert.assertTrue(ViewUtils.isExistedView("UILabel", DefaultStrings.TaggingAndSleepTitle), "Current view is Misfit Labs Screen");
	}
	
	public void e_turnOffAutoSleep(){
		HomeScreen.switchAutoSleepTrackingOff();
	}
	
	public void v_MisfitLabsScreenUpdated(){
	}
}
