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

public class DayProgressAPI extends ModelAPI {
	public DayProgressAPI(AutomationTest automation, File model, boolean efsm,
			PathGenerator generator, boolean weight) {
		super(automation, model, efsm, generator, weight);
	}

	String lastStartTime = "";
	int lastDuration = 0;
	int lastSteps = 0;
	float lastPoints = 0f;
	float lastMiles = 0f;
	float lastCalories = 0f;
		
	int totalSteps = 0;
	int totalMinutes = 0;
	float totalPoints = 0f;
	float totalMiles = 0f;
	float totalCalories = 0f;
	
	int hour = 5;						// for ease of keeping tracks
	int height = 68;					// 68 inches is default height for men
	float weight = 0.453592f * 120f;	// 120 lbs is default weight for men
	
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
		// Birthyear: 1981
		
		// DEVICE SETTINGS:
		// Time: 12 hours
		
		PrometheusHelper.signUp(MVPApi.generateUniqueEmail(), "qwerty1", true, 16, 9, 1981, true, "5'", "8\\\"", "120", ".0", 1);
	}

	/**
	 * This method implements the Edge 'e_closeTray'
	 * 
	 */
	public void e_closeTray() {
		HomeScreen.tapSyncTray();
		ShortcutsTyper.delayTime(1000);
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
		this.lastDuration = PrometheusHelper.randInt(5, 11);
		this.lastSteps = this.lastDuration * PrometheusHelper.randInt(100, 180);
		HomeScreen.enterManualActivity(time, this.lastDuration, this.lastSteps);
		ShortcutsTyper.delayTime(1000);
		HomeScreen.tapSave();
		ShortcutsTyper.delayTime(6000);
		
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
	}

	/**
	 * This method implements the Edge 'e_openTray'
	 * 
	 */
	public void e_openTray() {
		HomeScreen.tapSyncTray();
		ShortcutsTyper.delayTime(1000);
	}

	/**
	 * This method implements the Vertex 'v_Today'
	 * 
	 */
	public void v_Today() {
		// check initial data: on progress circle, sync tray closed, today
		Assert.assertTrue(HomeScreen.isToday(), "Today is displayed by default");
		Assert.assertTrue(HomeScreen.syncTrayIsClosed(), "Sync tray is closed by default");
	}

	/**
	 * This method implements the Vertex 'v_TrayDisplayed'
	 * 
	 */
	public void v_TrayDisplayed() {
		// check sync tray is opened
		Assert.assertTrue(!HomeScreen.syncTrayIsClosed(), "Sync tray is displyed");
	}

	/**
	 * This method implements the Vertex 'v_UpdatedTimeline'
	 * 
	 */
	public void v_UpdatedToday() {
		// check activity record is saved
		Assert.assertTrue(ViewUtils.isExistedView("UILabel", this.lastStartTime), "Start time displayed correctly");
		//Assert.assertTrue(ViewUtils.isExistedView("UILabel", String.format("%d", this.lastDuration)), "Duration displayed correctly");	// there's something wrong with the framework
		Assert.assertTrue(ViewUtils.isExistedView("UILabel", String.format("%d", this.lastSteps)), "Steps displayed correctly");
		Assert.assertTrue(ViewUtils.isExistedView("UILabel", String.format("%d", (int)this.lastPoints)), "Points displayed correctly");
		Assert.assertTrue(ViewUtils.isExistedView("UILabel", String.format("%.1f", this.lastMiles).replace(".0", "")), "Miles displayed correctly");
		//Assert.assertTrue(ViewUtils.isExistedView("UILabel", String.format("%.1f", this.lastCalories).replace(".0", "")), "Calories displayed correctly");
		
		// check summary is updated
		Assert.assertTrue(ViewUtils.isExistedView("UILabel", String.format("%.1f", this.totalMinutes / 60f).replace(".0", "")), "Total hours displayed correctly");
		Assert.assertTrue(ViewUtils.isExistedView("UILabel", String.format("%d", this.totalSteps)), "Total steps displayed correctly");
		Assert.assertTrue(ViewUtils.isExistedView("UILabel", String.format("%d", (int)this.totalPoints)), "Total points displayed correctly");
		Assert.assertTrue(ViewUtils.isExistedView("UILabel", String.format("%.1f", this.totalMiles).replace(".0", "")), "Total miles displayed correctly");
		//Assert.assertTrue(ViewUtils.isExistedView("UILabel", String.format("%.1f", this.totalCalories).replace(".0", "")), "Total calories displayed correctly");
		
		// TODO: can improve summary check by specify the control for summary's value
		// (currently it just look for labels that have the corresponding text, 
		// but acctually we have to take the control, get its value and compare)
	}

}
