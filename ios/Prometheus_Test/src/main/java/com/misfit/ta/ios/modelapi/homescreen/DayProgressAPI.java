package com.misfit.ta.ios.modelapi.homescreen;

import java.io.File;
import java.util.Calendar;

import org.apache.commons.lang.StringUtils;
import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ios.ViewUtils;
import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.common.MVPCalculator;
import com.misfit.ta.common.MVPEnums;
import com.misfit.ta.gui.Gui;
import com.misfit.ta.gui.HomeScreen;
import com.misfit.ta.gui.PrometheusHelper;
import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.modelAPI.ModelAPI;
import com.misfit.ta.utils.ShortcutsTyper;

public class DayProgressAPI extends ModelAPI {
	private static final String MINUTELABEL = "MINUTES";
	private static final String POINTLABEL = "POINTS";

	public DayProgressAPI(AutomationTest automation, File model, boolean efsm,
			PathGenerator generator, boolean weight) {
		super(automation, model, efsm, generator, weight);
	}

	private String lastStartTime = "";
	private int lastDuration = 0;
	private int lastSteps = 0;
	private float lastPoints = 0f;
	private float lastMiles = 0f;

	private int totalSteps = 0;
	private float totalPoints = 0f;
	private float totalMiles = 0f;

	private int hour = 6; // for ease of keeping tracks
	private float height = 68f; // 68 inches is default height for men
	private float weight = 120f; // 120 lbs is default weight for men
	private boolean isMale = true;
	private int year = 1981;

	private boolean hasNoActivity = true;

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
		PrometheusHelper.signUp(MVPApi.generateUniqueEmail(), "qwerty1",
				isMale, 16, 9, year, true, "5'", "8\\\"", "120", ".0", 1);
		ShortcutsTyper.delayTime(5000);
	}

	/**
	 * This method implements the Edge 'e_inputActivity'
	 * 
	 */
	public void e_inputActivity() {
		HomeScreen.tapOpenManualInput();
		ShortcutsTyper.delayTime(500);

		// input record
		String[] time = { 
				String.format("%d", hour > 12 ? hour - 12 : hour == 0 ? 12 : hour),
				"00", 
				hour < 12 ? "AM" : "PM" };
		
		this.lastDuration = PrometheusHelper.randInt(5, 9);
		this.lastSteps = this.lastDuration * PrometheusHelper.randInt(100, 180);
		
		HomeScreen.enterManualActivity(time, this.lastDuration, this.lastSteps);
		ShortcutsTyper.delayTime(1000);
		HomeScreen.tapSave();
		ShortcutsTyper.delayTime(500);
		PrometheusHelper.handleTutorial();
		ShortcutsTyper.delayTime(500);

		// save last record info
		this.lastStartTime = String.format("%d", hour > 12 ? hour - 12 : hour == 0 ? 12 : hour)
				+ ":00" + (hour < 12 ? "am" : "pm");
		calculateTotalProgressInfo();
		this.hour++;
		this.hasNoActivity = false;
	}

	private void calculateTotalProgressInfo() {

		this.lastPoints = PrometheusHelper.calculatePoint(this.lastSteps, this.lastDuration, 100);
		this.lastMiles = PrometheusHelper.calculateMiles(lastSteps, lastDuration, height);
		
		System.out.println("DEBUG: Last steps " + this.lastSteps);
		System.out.println("DEBUG: Last duration " + this.lastDuration);
		System.out.println("DEBUG: Last points " + this.lastPoints);
		System.out.println("DEBUG: Last miles " + this.lastMiles);

		// calculate total progress info
		this.totalSteps += this.lastSteps;
		this.totalPoints += this.lastPoints;
		this.totalMiles += this.lastMiles;
	}

	/**
	 * This method implements the Edge 'e_checkEnd'
	 * 
	 */
	public void e_checkEnd() {
	}

	/**
	 * This method implements the Vertex 'v_TodayDefault'
	 * 
	 */
	public void v_Today() {
		if (hasNoActivity) {
			// check initial data
			ShortcutsTyper.delayOne();
			Assert.assertTrue(HomeScreen.isTodayDefault(),
					"Progress circle display point earned by default");
		}
		Assert.assertTrue(HomeScreen.isToday(), "Today is displayed");
	}

	/**
	 * This method implements the Vertex 'v_UpdatedTimeline'
	 * 
	 */
	public void v_UpdatedToday() {
		// check summary through process circle
		for (int i = 0; i < 2; i++) {
			// progress circle display point earned => check total point
			if (HomeScreen.isPointEarnedProgessCircle()) {

				// check total point
				Assert.assertTrue(
						ViewUtils.isExistedView(
								"UILabel",
								String.format("%d",
										(int) Math.floor(this.totalPoints)))
								|| ViewUtils.isExistedView("UILabel", String
										.format("%d", (int) Math
												.round(this.totalPoints))),
						"Total points displayed correctly");

				// check remain walking time
				int remainMins = MVPCalculator.calculateNearestTimeRemainInMinute(1000 - (int)this.totalPoints, MVPEnums.ACTIVITY_WALKING);
				String remainTimeString = Gui.getProperty("PTRichTextLabel", 0, "text");
				remainTimeString = remainTimeString.substring(
						remainTimeString.indexOf('_') + 1,
						remainTimeString.lastIndexOf('_'));
				
				Assert.assertEquals(remainTimeString, MVPCalculator.convertNearestTimeInMinuteToString(remainMins),
						"Remain time displayed correctly");

			}
			// progress circle display summary => check steps / calories /
			// distance
			else if (HomeScreen.isSummaryProgressCircle()) {
				System.out.println("DEBUG: PROGRESS CIRCLE VIEW 2");
				
				// check total step
				Assert.assertTrue(
						ViewUtils.isExistedView("PTRichTextLabel",
								String.format("_%d_ steps", this.totalSteps)),
						"Total steps displayed correctly");
				
				// check total distance
				// TODO: uncomment this when distance calculation in app is fixed
//				Assert.assertTrue(
//						ViewUtils.isExistedView("PTRichTextLabel",
//								String.format("_%.1f_ miles", this.totalMiles)),
//						"Total miles displayed correctly");
				
				// check total burned calories
				Calendar now = Calendar.getInstance();
				System.out.println("NOW: " + now);
				float fullBMR = PrometheusHelper.calculateFullBMR(weight,
						height, now.get(Calendar.YEAR) - year, isMale);
				System.out.println("AGE: " + (now.get(Calendar.YEAR) - year));
				String caloriesText = Gui.getProperty("PTRichTextLabel", 1,
						"text");
				float calories = Float.valueOf(StringUtils.substring(
						caloriesText, 1, caloriesText.lastIndexOf("_")));
				System.out.println("MINUTE:" + now.get(Calendar.MINUTE));
				System.out.println("HOUR:" + now.get(Calendar.HOUR_OF_DAY));
				float minTotalCalories = PrometheusHelper.calculateCalories(
						this.totalPoints,
						weight,
						fullBMR,
						now.get(Calendar.HOUR_OF_DAY) * 60
								+ now.get(Calendar.MINUTE) - 10);
				float maxTotalCalories = PrometheusHelper.calculateCalories(
						this.totalPoints,
						weight,
						fullBMR,
						now.get(Calendar.HOUR_OF_DAY) * 60
								+ now.get(Calendar.MINUTE) + 10);
				float result = PrometheusHelper.calculateCalories(
						this.totalPoints,
						weight,
						fullBMR,
						now.get(Calendar.HOUR_OF_DAY) * 60
								+ now.get(Calendar.MINUTE));
				System.out.println("DEBUG: Calories text " + calories);
				System.out.println("DEBUG: Min calories " + minTotalCalories);
				System.out.println("DEBUG: Max calories " + maxTotalCalories);
				System.out.println("DEBUG: Calculated Calories " + result);
				if (calories < minTotalCalories || calories > maxTotalCalories) {
					Assert.assertTrue(false,
							"Calories calculation is not correct");
				}

			}
			HomeScreen.tapProgressCircle();
		}
		Gui.dragUpTimeline();
		if (this.lastPoints >= 50f) {
			// check activity record is saved
			Assert.assertTrue(
					ViewUtils.isExistedView("UILabel", this.lastStartTime),
					"Start time displayed correctly");
			// open overlay
			Gui.touchAVIew("PTTimelineItemSessionView", this.lastStartTime);
			System.out.println("DEBUG: Assert last duration "
					+ this.lastDuration);
			Assert.assertTrue(
					ViewUtils.isExistedView("UILabel",
							String.valueOf(this.lastDuration))
							&& ViewUtils.isExistedView("UILabel", MINUTELABEL),
					"Duration on timeline item is displayed correctly");
			System.out.println("DEBUG: Assert last points " + this.lastPoints);
			Assert.assertTrue(
					ViewUtils.isExistedView(
							"UILabel",
							String.format("%d",
									(int) Math.floor(this.lastPoints)))
							&& ViewUtils.isExistedView("UILabel", POINTLABEL),
					"Points on timeline item are displayed correctly");
			// close overlay and drag down timeline view
			Gui.touchAVIew("UILabel", MINUTELABEL);
		} else {
			Assert.assertTrue(
					!ViewUtils.isExistedView("UILabel", this.lastStartTime),
					"Start time shouldn't be displayed because of low points");
		}
		Gui.dragDownTimeline();
	}

	public void e_stay() {

	}
	
	public void v_EndInput() {

	}
}
