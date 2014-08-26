package com.misfit.ta.ios.modelapi.homescreen;

import java.io.File;

import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ios.ViewUtils;
import com.misfit.ta.gui.Gui;
import com.misfit.ta.gui.HomeScreen;
import com.misfit.ta.gui.PrometheusHelper;
import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.modelAPI.ModelAPI;

public class SetAlarmAPI extends ModelAPI {
	public SetAlarmAPI(AutomationTest automation, File model, boolean efsm,
			PathGenerator generator, boolean weight) {
		super(automation, model, efsm, generator, weight);
	}

	private boolean isSetTime = false;
	/**
	 * This method implements the Edge 'e_goToAddSound'
	 * 
	 */
	public void e_goToAddSound() {
		HomeScreen.tapChangeSleepSound();
	}

	/**
	 * This method implements the Edge 'e_goToAddTime'
	 * 
	 */
	public void e_goToAddTime() {
		HomeScreen.tapSetAlarmTime();
	}

	/**
	 * This method implements the Edge 'e_goToSetAlarmScreen'
	 * 
	 */
	public void e_goToSetAlarmScreen() {
		Gui.touchAVIew("UIButton", ">");
	}

	/**
	 * This method implements the Edge 'e_goToSleepScreen'
	 * 
	 */
	public void e_goToSleepScreen() {
		// go to sleep view
		HomeScreen.tapSleepTimeline();
	}

	/**
	 * This method implements the Edge 'e_init'
	 * 
	 */
	public void e_init() {
		PrometheusHelper.signUpDefaultProfile();
		PrometheusHelper.handleUpdateFirmwarePopup();
	}

	/**
	 * This method implements the Edge 'e_setSound'
	 * 
	 */
	public void e_setSound() {
		HomeScreen.setSound(new String[]{"30 minutes", "Day Forest"});
		HomeScreen.tapSave();
	}

	/**
	 * This method implements the Edge 'e_setTime'
	 * 
	 */
	public void e_setTime() {
		PrometheusHelper.inputAlarmTime(new String[]{"6", "00", "AM"});
		HomeScreen.tapSave();
		isSetTime = true;
	}

	/**
	 * This method implements the Vertex 'v_AddAlarmScreen'
	 * 
	 */
	public void v_AddAlarmScreen() {
	}

	/**
	 * This method implements the Vertex 'v_AddSoundScreen'
	 * 
	 */
	public void v_AddSoundScreen() {
		Assert.assertTrue(HomeScreen.isSleepSoundScreen(), "Current View is Sleep Sound View");
	}

	/**
	 * This method implements the Vertex 'v_AlarmScreen'
	 * 
	 */
	public void v_AlarmScreen() {
		Assert.assertTrue(HomeScreen.isAlarmScreen(), "Current View is Alarm Screen");
	}

	/**
	 * This method implements the Vertex 'v_AlarmScreenUpdatedTime'
	 * 
	 */
	public void v_AlarmScreenUpdatedTime() {
		if(isSetTime){
			Assert.assertTrue(ViewUtils.isExistedView("UIButtonLabel", "6:00am"), "Time is true");
		}else{
			Assert.assertTrue(ViewUtils.isExistedView("UIButtonLabel", "Day Forest"), "Sound is true");
		}
	}

	/**
	 * This method implements the Vertex 'v_HomeScreen'
	 * 
	 */
	public void v_HomeScreen() {
		Assert.assertTrue(HomeScreen.isHomeScreen(),
				"Current View is HomeScreen");
	}

	/**
	 * This method implements the Vertex 'v_SleepHomeScreen'
	 * 
	 */
	public void v_SleepHomeScreen() {
		
	}

}
