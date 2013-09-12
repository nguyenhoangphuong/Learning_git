package com.misfit.ta.ios.modelapi.homescreen;

import java.io.File;
import java.util.Calendar;

import org.apache.log4j.Logger;
import org.graphwalker.Util;
import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ios.ViewUtils;
import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.common.MVPEnums;
import com.misfit.ta.gui.DefaultStrings;
import com.misfit.ta.gui.Gui;
import com.misfit.ta.gui.HomeScreen;
import com.misfit.ta.gui.PrometheusHelper;
import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.modelAPI.ModelAPI;
import com.misfit.ta.utils.ShortcutsTyper;

public class TaggingActivityAPI extends ModelAPI {

	private static final String MINUTELABEL = "MINUTES";
	private static final String POINTLABEL = "POINTS";
	
	private String lastStartTime = "";
	private int lastDuration = 0;
	private int lastSteps = 0;
	private float lastPoints = 0f;

	private int totalSteps = 0;
	private float totalPoints = 0f;

	// private int firstStepHour = 5;
	private int hour = 0; // for ease of keeping tracks
	private int lastHour = 0;
	private String minute = "00";
	private int lastMinute = -1;
	private boolean isMale = true;
	private int year = 1981;

	private int activityType = 100;

	private boolean hasNoActivity = true;
	private int countSwimmingPopup = 0;
	private int countCyclingPopup = 0;

	public TaggingActivityAPI(AutomationTest automation, File model, boolean efsm, PathGenerator generator, boolean weight) {
		super(automation, model, efsm, generator, weight);
	}

	public void e_Init() {
		PrometheusHelper.signUp(MVPApi.generateUniqueEmail(), "qwerty1", isMale, 16, 9, year, true, "5'", "8\\\"", "120", ".0", 1);
		ShortcutsTyper.delayTime(5000);
	}

	/**
	 * This method implements the Edge 'e_chooseCycling'
	 * 
	 */
	public void e_chooseCycling() {
		openMyShineView();
		HomeScreen.chooseCycling();
		ShortcutsTyper.delayTime(300);
		if (HomeScreen.hasSuggestWearingPositionForCyclingMessage()) {
			Gui.touchPopupButton(DefaultStrings.GotItButton);
			countCyclingPopup++;
		}
		
		ShortcutsTyper.delayTime(300);
		HomeScreen.tapSave();
		activityType = MVPEnums.ACTIVITY_CYCLING;
		ShortcutsTyper.delayTime(1000);
	}

	/**
	 * This method implements the Edge 'e_chooseSleep'
	 * 
	 */
	public void e_chooseSleep() {
		openMyShineView();
		HomeScreen.chooseSleep();
		ShortcutsTyper.delayTime(300);
		HomeScreen.tapSave();
		activityType = MVPEnums.ACTIVITY_SLEEPING;
		ShortcutsTyper.delayTime(1000);
	}

	/**
	 * This method implements the Edge 'e_chooseSwimming'
	 * 
	 */
	public void e_chooseSwimming() {
		openMyShineView();
		HomeScreen.chooseSwimming();
		ShortcutsTyper.delayTime(300);
		if (HomeScreen.hasSuggestWearingPositionForSwimmingMessage()) {
			Gui.touchPopupButton(DefaultStrings.GotItButton);
			countSwimmingPopup++;
		}
		
		ShortcutsTyper.delayTime(300);
		HomeScreen.tapSave();
		activityType = MVPEnums.ACTIVITY_SWIMMING;
		ShortcutsTyper.delayTime(1000);
	}

	/**
	 * This method implements the Edge 'e_inputActivity'
	 * 
	 */
	public void e_inputActivity() {
		ShortcutsTyper.delayTime(2000);
		HomeScreen.tapOpenManualInput();
		ShortcutsTyper.delayTime(500);
		// input record
		Calendar now = Calendar.getInstance();
		hour = now.get(Calendar.HOUR_OF_DAY);
		if (lastMinute == -1) {
			lastMinute = now.get(Calendar.MINUTE);
		} else if (lastMinute > 0 && lastMinute < 49) {
			lastMinute = lastMinute + 10;
		} else {
			now.add(Calendar.MINUTE, 15);
			lastMinute = now.get(Calendar.MINUTE);
			hour = now.get(Calendar.HOUR_OF_DAY);
		}
		minute = String.format("%02d", lastMinute + 1);
		String[] time = { String.format("%d", hour > 12 ? hour - 12 : hour == 0 ? 12 : hour), minute, hour < 12 ? "AM" : "PM" };
		lastDuration = PrometheusHelper.randInt(3, 5);
		lastSteps = lastDuration * PrometheusHelper.randInt(100, 180);
		HomeScreen.enterManualActivity(time, lastDuration, lastSteps);
		ShortcutsTyper.delayTime(1000);
		HomeScreen.tapSave();
		ShortcutsTyper.delayTime(500);
		PrometheusHelper.handleTutorial();
		ShortcutsTyper.delayTime(500);

		// save last record info
		lastStartTime = String.format("%d", hour > 12 ? hour - 12 : hour == 0 ? 12 : hour) + ":" + minute + (hour < 12 ? "am" : "pm");
		calculateTotalProgressInfo();
		lastHour = hour;
		hasNoActivity = false;
	}

	private void calculateTotalProgressInfo() {
		System.out.println("DEBUG: Activity Type " + this.activityType);
		System.out.println("DEBUG: Last steps " + this.lastSteps);
		System.out.println("DEBUG: Last duration " + this.lastDuration);
		this.lastPoints = PrometheusHelper.calculatePoint(this.lastSteps, this.lastDuration, activityType);
		if (this.lastPoints < 50f) {
			// not enough point to be a cycling/swimming session --> get it back
			// to walking session
			this.lastPoints = PrometheusHelper.calculatePoint(this.lastSteps, this.lastDuration, 100);
		}
		System.out.println("DEBUG: Last points " + this.lastPoints);

		// calculate total progress info
		System.out.println("DEBUG: calculate total steps and points ");
		this.totalSteps += this.lastSteps;
		this.totalPoints += this.lastPoints;
		System.out.println("DEBUG: calculate total points " + this.totalPoints);
		System.out.println("DEBUG: calculate total steps " + this.totalSteps);
	}

	/**
	 * This method implements the Edge 'e_stay'
	 * 
	 */
	public void e_stay() {
		ShortcutsTyper.delayOne();
	}

	/**
	 * This method implements the Vertex 'v_Today'
	 * 
	 */
	public void v_Today() {
		if (hasNoActivity) {
			// check initial data
			ShortcutsTyper.delayOne();
			Assert.assertTrue(HomeScreen.isTodayDefault(), "Progress circle display point earned by default");
		}
		Assert.assertTrue(HomeScreen.isToday(), "Today is displayed");
	}

	/**
	 * This method implements the Vertex 'v_UpdatedCyclingTimeline'
	 * 
	 */
	public void v_UpdatedCyclingTimeline() {
		checkUpdatedTimeline(DefaultStrings.CyclingLevel);
	}

	/**
	 * This method implements the Vertex 'v_UpdatedCyclingToday'
	 * 
	 */
	public void v_UpdatedCyclingToday() {
		Assert.assertTrue(countCyclingPopup > 0 && countCyclingPopup <= 2, "Wearing position for cycling isn't displayed correctly.");
	}

	/**
	 * This method implements the Vertex 'v_UpdatedNormalTimeline'
	 * 
	 */
	public void v_UpdatedNormalTimeline() {
		checkUpdatedTimeline(DefaultStrings.WalkingLevel);
	}

	/**
	 * This method implements the Vertex 'v_UpdatedNormalToday'
	 * 
	 */
	public void v_UpdatedNormalToday() {
		
	}

	/**
	 * This method implements the Vertex 'v_UpdatedSwimmingTimeline'
	 * 
	 */
	public void v_UpdatedSwimmingTimeline() {
		checkUpdatedTimeline(DefaultStrings.SwimmingLevel);
	}

	/**
	 * This method implements the Vertex 'v_UpdatedSwimmingToday'
	 * 
	 */
	public void v_UpdatedSwimmingToday() {
		Assert.assertTrue(countSwimmingPopup > 0 && countSwimmingPopup <= 2, "Wearing position for swimming isn't displayed correctly.");
	}

	private void openMyShineView() {
		HomeScreen.tapOpenSettingsTray();
		ShortcutsTyper.delayTime(500);
		HomeScreen.tapShinePreferences();
		ShortcutsTyper.delayTime(500);
	}

	private void checkUpdatedTimeline(String[] levelTitle) {
		// check summary through process circle
		for (int i = 0; i < 2; i++) {
			// progress circle display point earned => check total point
			if (HomeScreen.isPointEarnedProgessCircle()) {
				System.out.println("DEBUG: PROGRESS CIRCLE VIEW 1");
				System.out.println("DEBUG: Assert total points " + this.totalPoints);
				Assert.assertTrue(ViewUtils.isExistedView("UILabel", String.format("%d", (int) Math.floor(this.totalPoints))) || ViewUtils.isExistedView("UILabel", String.format("%d", (int) Math.round(this.totalPoints))), "Total points displayed correctly");

				// progress circle display summary => check steps / calories /
				// distance
			} else if (HomeScreen.isSummaryProgressCircle()) {
				System.out.println("DEBUG: PROGRESS CIRCLE VIEW 2");
				System.out.println("DEBUG: Total Steps: " + this.totalSteps);
				Assert.assertTrue(ViewUtils.isExistedView("PTRichTextLabel", String.format("_%d_ steps", this.totalSteps)), "Total steps displayed correctly");

			}
			HomeScreen.tapProgressCircle();
		}
		Gui.dragUpTimeline();
		if (this.lastPoints >= 50f) {
			// check activity record is saved
			Assert.assertTrue(ViewUtils.isExistedView("UILabel", this.lastStartTime), "Start time displayed correctly");
			// open overlay
			Gui.touchAVIew("PTTimelineItemSessionView", this.lastStartTime);
			System.out.println("DEBUG: Assert last duration " + this.lastDuration);
			Assert.assertTrue(ViewUtils.isExistedView("UILabel", String.valueOf(this.lastDuration)) && ViewUtils.isExistedView("UILabel", MINUTELABEL), "Duration on timeline item is displayed correctly");
			System.out.println("DEBUG: Assert last points " + this.lastPoints);
			Assert.assertTrue(ViewUtils.isExistedView("UILabel", String.format("%d", (int) Math.floor(this.lastPoints))) && ViewUtils.isExistedView("UILabel", POINTLABEL), "Points on timeline item are displayed correctly");

			boolean correctTitle = false;
			for (int i = 0; i < levelTitle.length; i++) {
				if (ViewUtils.isExistedView("UILabel", levelTitle[i])) {
					correctTitle = true;
					break;
				}
			}
			Assert.assertTrue(correctTitle, "Title for tile is not correct");
			// close overlay and drag down timeline view
			Gui.touchAVIew("UILabel", MINUTELABEL);
		} else {
			Assert.assertTrue(!ViewUtils.isExistedView("UILabel", this.lastStartTime), "Start time shouldn't be displayed because of low points");
		}
		Gui.dragDownTimeline();
	}
}
