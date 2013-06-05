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
	private int goalPoints = 1000;				// total goal is 1000 points
	
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
		// Goal: 1000
		// Year of birth: 1981
		
		// DEVICE SETTINGS:
		// Time: 12 hours
		
		// Initalize
		PrometheusHelper.signUp(MVPApi.generateUniqueEmail(), "qwerty1", true, 16, 9, 1981, true, "5'", "8\\\"", "120", ".0", 1);
		ShortcutsTyper.delayTime(5000);
	}

	/**
	 * This method implements the Edge 'e_inputActivity'
	 * 
	 */
	public void e_inputActivity() 
	{
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
	}

	/**
	 * This method implements the Edge 'e_checkEnd'
	 * 
	 */
	public void e_checkEnd() {
		// graph walker to handle
	}
	
	
	
	
	/**
	 * This method implements the Vertex 'v_TodayDefault'
	 * 
	 */
	public void v_TodayDefault() 
	{
		// check initial data
		Assert.assertTrue(HomeScreen.isPointEarnedProgessCircle(), "Progress circle display point earned by default");
		Assert.assertTrue(ViewUtils.isExistedView("PTRichTextLabel", "0"), "Zero point is displayed by default");
		Assert.assertTrue(ViewUtils.isExistedView("PTRichTextLabel", String.format("of _%d_ points", this.goalPoints)), "Total goal displayed correctly");
	}
	
	/**
	 * This method implements the Vertex 'v_Today'
	 * 
	 */
	public void v_Today() 
	{
		Assert.assertTrue(HomeScreen.isToday(), "Today is displayed");
	}

	/**
	 * This method implements the Vertex 'v_UpdatedTimeline'
	 * 
	 */
	public void v_UpdatedToday() 
	{
		// check summary through process circle
		for(int i = 0; i < 3; i++)
		{		
			// progress circle display point earned => check total point
			if(HomeScreen.isPointEarnedProgessCircle())
				Assert.assertTrue(ViewUtils.isExistedView("UILabel", String.format("%d", (int)Math.floor(this.totalPoints))), "Total points displayed correctly");
			
			// progress circle display point remained => check remain point
			else if(HomeScreen.isPointEarnedProgessCircle())
			{
				String text = Gui.getProperty("PTRichTextLabel", 0, "text");
				int remainPoints = this.goalPoints - (int)Math.floor(this.totalPoints);
				Assert.assertTrue(text.contains(String.format("%d", remainPoints)), "Remain points displayed correctly");
			}
						
			// progress circle display summary => check steps / calories / distance
			else if(HomeScreen.isSummaryProgressCircle())
			{
				Assert.assertTrue(ViewUtils.isExistedView("PTRichTextLabel", String.format("_%d_ steps", this.totalSteps)), "Total steps displayed correctly");
				Assert.assertTrue(ViewUtils.isExistedView("PTRichTextLabel", String.format("_%.1f_ miles", this.totalMiles)), "Total miles displayed correctly");
			}
			
			HomeScreen.tapProgressCircle();
		}
		
		// check activity record is saved
		Assert.assertTrue(ViewUtils.isExistedView("UILabel", this.lastStartTime), "Start time displayed correctly");
		Assert.assertTrue(ViewUtils.isExistedView("UILabel", String.format("%d", this.lastDuration)), "Duration displayed correctly");
		
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
