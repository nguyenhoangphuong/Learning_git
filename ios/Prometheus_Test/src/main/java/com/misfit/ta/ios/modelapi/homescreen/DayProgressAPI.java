package com.misfit.ta.ios.modelapi.homescreen;

import java.io.File;

import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ios.ViewUtils;
import com.misfit.ta.modelAPI.ModelAPI;
import com.misfit.ta.utils.ShortcutsTyper;
import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.gui.HomeScreen;
import com.misfit.ta.gui.PrometheusHelper;
import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.gui.Gui;

public class DayProgressAPI extends ModelAPI {
	public DayProgressAPI(AutomationTest automation, File model, boolean efsm,
			PathGenerator generator, boolean weight) {
		super(automation, model, efsm, generator, weight);
	}

	private String lastStartTime = "";
	private int lastDuration = 0;
	private int lastSteps = 0;
	private float lastPoints = 0f;
	private float lastMiles = 0f;
	private float lastCalories = 0f;
		
	private int totalSteps = 0;
	private int totalMinutes = 0;
	private float totalPoints = 0f;
	private float totalMiles = 0f;
	private float totalCalories = 0f;
	
	private int hour = 5;						// for ease of keeping tracks
	private int height = 68;					// 68 inches is default height for men
	private float weight = 120f;				// 120 lbs is default weight for men
	
	/**
	 * This method implements the Edge 'e_Init'
	 * 
	 */
	public void e_Init() {
		// THIS FUNCTION REQUIRED SIGN UP WITH DEFAULT PROFILES AND GOAL
		// THESE INFOMATION IS REQUIRED TO CALCULATE BMR AND TEE
		// Gender: Male
		// Height: 68" (or 5'8")
		// Weight: 120 lbs
		// Goal: 2500
		// Year of birth: 1981
		
		// DEVICE SETTINGS:
		// Time: 12 hours
		
		// APP SETTINGS:
		// Manual Input
		
		// Initalize
		PrometheusHelper.signUp(MVPApi.generateUniqueEmail(), "qwerty1", true, 16, 9, 1981, true, "5'", "8\\\"", "120", ".0", 1);
		ShortcutsTyper.delayTime(5000);
		//PrometheusHelper.setInputModeToManual();
		//ShortcutsTyper.delayTime(1000);
	}

	/**
	 * This method implements the Edge 'e_inputActivity'
	 * 
	 */
	public void e_inputActivity() {
		HomeScreen.tapOpenManualInput();
		ShortcutsTyper.delayTime(1000);
		
		// input record
		String[] time = { String.format("%d", hour > 12 ? 12 - hour : hour), "00", hour < 12 ? "AM" : "PM" };
		this.lastDuration = PrometheusHelper.randInt(5, 9);
		this.lastSteps = this.lastDuration * PrometheusHelper.randInt(100, 180);
		HomeScreen.enterManualActivity(time, this.lastDuration, this.lastSteps);
		ShortcutsTyper.delayTime(1000);
		HomeScreen.tapSave();
		ShortcutsTyper.delayTime(1000);
		
		// save last record info
		this.lastStartTime = String.format("%d", hour > 12 ? 12 - hour : hour) + ":00" + (hour < 12 ? " am": " pm");
		this.lastMiles = PrometheusHelper.calculateMiles(this.lastSteps, this.height);
		this.lastPoints = PrometheusHelper.calculatePoint(this.lastSteps, this.lastDuration);
		this.lastCalories = PrometheusHelper.calculateCalories(this.lastPoints, this.weight);
		
		// calculate total progress info
		this.totalSteps += this.lastSteps;
		this.totalMinutes += this.lastDuration;
		this.totalMiles += PrometheusHelper.calculateMiles(this.lastSteps, this.height);
		this.totalPoints += PrometheusHelper.calculatePoint(this.lastSteps, this.lastDuration);
		this.totalCalories += PrometheusHelper.calculateCalories(this.lastPoints, this.weight);
		
		hour++;
		PrometheusHelper.handleTutorial();
		ShortcutsTyper.delayTime(500);
	}

	/**
	 * This method implements the Edge 'e_checkEnd'
	 * 
	 */
	public void e_checkEnd() {
		// graph walker to handle
	}
	
	
	
	
	/**
	 * This method implements the Vertex 'v_Today'
	 * 
	 */
	public void v_Today() {
		// check initial data: on progress circle, sync tray closed, today
		Assert.assertTrue(HomeScreen.isToday(), "Today is displayed by default");
	}

	/**
	 * This method implements the Vertex 'v_UpdatedTimeline'
	 * 
	 */
	public void v_UpdatedToday() {
		// check summary is 
		Assert.assertTrue(ViewUtils.isExistedView("UILabel", String.format("%d", this.totalSteps)), "Total steps displayed correctly");
		Assert.assertTrue(ViewUtils.isExistedView("UILabel", String.format("%.1f", this.totalMiles).replace(".0", "")), "Total miles displayed correctly");
		// Total calories are still being discussed, therefore we should not verified now
		
		// check activity record is saved
		Assert.assertTrue(ViewUtils.isExistedView("UILabel", this.lastStartTime), "Start time displayed correctly");
		Assert.assertTrue(ViewUtils.isExistedView("UILabel", String.format("%.0f", Math.floor(this.lastPoints))), "Points displayed correctly");
	}	
	
	/**
	 * This method implements the Vertex 'v_EndInput'
	 * 
	 */
	public void v_EndInput() {
		// nothing to do here
	}

	/**
	* This method implements the Vertex 'v_TutorialShown'
	* 
	*/
	public void v_TutorialShown() {
		PrometheusHelper.handleTutorial();
	}
}
