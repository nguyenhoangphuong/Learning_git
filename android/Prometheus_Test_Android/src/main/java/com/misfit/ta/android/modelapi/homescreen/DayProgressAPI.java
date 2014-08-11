package com.misfit.ta.android.modelapi.homescreen;

import java.io.File;
import java.util.Calendar;

import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ta.android.AutomationTest;
import com.misfit.ta.android.Gui;
import com.misfit.ta.android.ViewUtils;
import com.misfit.ta.android.aut.DefaultStrings;
import com.misfit.ta.android.gui.HomeScreen;
import com.misfit.ta.android.gui.PrometheusHelper;
import com.misfit.ta.android.hierarchyviewer.scene.ViewNode;
import com.misfit.ta.common.MVPCalculator;
import com.misfit.ta.common.MVPEnums;
import com.misfit.ta.modelAPI.ModelAPI;
import com.misfit.ta.report.TRS;
import com.misfit.ta.utils.ShortcutsTyper;

public class DayProgressAPI extends ModelAPI {
	public DayProgressAPI(AutomationTest automation, File model, boolean efsm,
			PathGenerator generator, boolean weight) {
		super(automation, model, efsm, generator, weight);
	}

	private int lastDuration = 0;
	private int lastSteps = 0;
	private int hour = 6;

	private float lastPoints = 0f;
	private float lastMiles = 0f;
	private int totalSteps = 0;
	private float totalPoints = 0f;
	private float totalMiles = 0f;
	private float height = 64f; // 5'4""
	private float weight = 140f; //140lbs
	private boolean isMale = true;
	private int year = 1982;
	private int fullScreenHeight;
	private int fullScreenWidth;
	private boolean isFirstTime = true;
	
	/**
	 * This method implements the Edge 'e_Init'
	 * 
	 */
	public void e_Init() {
		fullScreenHeight = Gui.getScreenHeight();
		fullScreenWidth = Gui.getScreenWidth();
		ShortcutsTyper.delayOne();
		// gender, height and weight have default values such as male, 5'4"", 140lbs
		PrometheusHelper.signUp();
		ShortcutsTyper.delayTime(2000);
	}

	/**
	 * This method implements the Edge 'e_SetActivity'
	 * 
	 */
	public void e_inputActivity() {
		HomeScreen.tapManual();
		// input record
		HomeScreen
				.inputManualTime(
						hour >= 10 ? String.valueOf(hour) : String.format(
								"%02d", hour), "00");

		this.lastDuration = PrometheusHelper.randInt(5, 9);
		this.lastSteps = this.lastDuration * PrometheusHelper.randInt(10, 180);

		HomeScreen.intputActivity(String.valueOf(this.lastDuration),
				String.valueOf(this.lastSteps));
		HomeScreen.saveManual();
		calculateTotalProgressInfo();
		this.hour += 1;
		
		PrometheusHelper.pullToRefresh(fullScreenWidth, fullScreenHeight);
		
	}

	/**
	 * This method implements the Vertex 'v_Today'
	 * 
	 */
	public void v_Today() {
//		Assert.assertTrue(isTodayView(), "This is Today view");
		if (isFirstTime) {
			Gui.touchAView("TextView", "mID", DefaultStrings.FirmwareUpdateDismissButtonId);
			isFirstTime = false;
		}
	}

	/**
	 * This method implements the Vertex 'v_UpdateToday'
	 * 
	 */
	public void v_UpdatedToday() {
		ShortcutsTyper.delayTime(5000);
		ViewNode view = ViewUtils.findView("TextView", "mID",
				DefaultStrings.PointsHomeScreenTextViewId, 0);
		System.out.println("Display: " + view.text);
		System.out.println("Total Points: " + totalPoints);
		System.out.println("Last Points: " + lastPoints);
		assertRemainWalkingTime();
		assertActivityInfo();
		assertCaloriesValue();
		Assert.assertTrue(
				view.text.equals(String.format("%d",
						(int) Math.floor(this.totalPoints))),
				"Total points value is correct");
	}

	private boolean isTodayView() {
		return ViewUtils.findView("TextView", "mText",
				DefaultStrings.WalkingLeftText, 0) != null;
	}

	public void v_EndInput() {

	}

	private void calculateTotalProgressInfo() {
		this.lastPoints = PrometheusHelper.calculatePoint(this.lastSteps,
				this.lastDuration, MVPEnums.ACTIVITY_SLEEPING);
		this.lastMiles = PrometheusHelper.calculateMiles(lastSteps,
				lastDuration, height);

		System.out.println("DEBUG: Last steps " + this.lastSteps);
		System.out.println("DEBUG: Last duration " + this.lastDuration);
		System.out.println("DEBUG: Last points " + this.lastPoints);
		System.out.println("DEBUG: Last miles " + this.lastMiles);

		// calculate total progress info
		this.totalSteps += this.lastSteps;
		this.totalPoints += this.lastPoints;
		this.totalMiles += this.lastMiles;
	}

	private void assertRemainWalkingTime() {
		// we don't change goal after signing up, so it's always 1000
		int remainMins = MVPCalculator.calculateNearestTimeRemainInMinute(
				1000 - (int) this.totalPoints, MVPEnums.ACTIVITY_WALKING);
		ViewNode remainWalkingTimeView = ViewUtils.findView("TextView", "mID",
				DefaultStrings.RemainWalkingTimeHomeScreenTextViewId, 0);
		if (!remainWalkingTimeView.text.equals(DefaultStrings.HitGoalText)) {
			String expectedRemainWalkingTimeText = PrometheusHelper
					.convertNearestTimeInMinuteToStringNumber(remainMins);
			String realRemainWalkingTimeText = remainWalkingTimeView.text
					.substring(6, 6 + expectedRemainWalkingTimeText.length());
			System.out.println("Real display of remain walking time: "
					+ realRemainWalkingTimeText);
			System.out.println("Expected display of remain walking time: "
					+ expectedRemainWalkingTimeText);
			Assert.assertTrue(Float.valueOf(expectedRemainWalkingTimeText)
					.equals(Float.valueOf(realRemainWalkingTimeText)),
					"The remain walking time is correct!!!");
		} else {
			System.out.println("******* Assert hit goal points");
			ViewNode displayPoint = ViewUtils.findView("TextView", "mID",
					DefaultStrings.PointsHomeScreenTextViewId, 0);
			Assert.assertTrue(Integer.valueOf(displayPoint.text) >= 1000, "The hit goal points value is correct");
		}
	}

	private void assertActivityInfo() {
		ViewNode displaySteps = ViewUtils.findView("TextView", "mID", DefaultStrings.StepsHomeScreenTextViewId, 0);
		System.out.println("Display total steps: " + displaySteps.text);
		System.out.println("Total steps: " + this.totalSteps);
		Assert.assertTrue(Integer.valueOf(displaySteps.text).equals(this.totalSteps), "Total steps are correct");
		ViewNode displayMiles = ViewUtils.findView("TextView", "mID", DefaultStrings.MilesHomeScreenTextViewId, 0);
		System.out.println("Display total miles: " + displayMiles.text);
		System.out.println(String.format("%.1f", this.totalMiles));
		System.out.println("Total miles: " + this.totalMiles);
		Assert.assertTrue(displayMiles.text.equals(String.format("%.1f", this.totalMiles)));
	}
	
	private void assertCaloriesValue() {
		Calendar now = Calendar.getInstance();
		float fullBMR = PrometheusHelper.calculateFullBMR(weight, height, now.get(Calendar.YEAR) - year, isMale);	
		System.out.println("Full BMR: " + fullBMR);
		String caloriesText = ViewUtils.findView("TextView", "mID", DefaultStrings.CaloriesHomeScreenTextViewId, 0).text;
		System.out.println("Display calories: " + caloriesText);
		float result = PrometheusHelper.calculateCalories(this.totalPoints, weight, fullBMR, now.get(Calendar.HOUR_OF_DAY) * 60 + now.get(Calendar.MINUTE));
		System.out.println("Calculated calories: " + result);
		if (Math.abs(Float.valueOf(caloriesText) - result) > 50f) {
			Assert.fail("Calories calculation is not correct");
		}
	}

}
