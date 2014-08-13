package com.misfit.ta.ios.modelapi.homescreen;

import java.io.File;
import java.util.Calendar;

import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ios.ViewUtils;
import com.misfit.ta.common.MVPEnums;
import com.misfit.ta.gui.DefaultStrings;
import com.misfit.ta.gui.Gui;
import com.misfit.ta.gui.HomeScreen;
import com.misfit.ta.gui.HomeSettings;
import com.misfit.ta.gui.PrometheusHelper;
import com.misfit.ta.gui.Timeline;
import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.modelAPI.ModelAPI;

public class TaggingActivityAPI extends ModelAPI {

	private static final String MINUTELABEL = "MINUTES";
	private static final String POINTLABEL = "POINTS";

	private int minuteOffset = 0;		// timestamp for activity in minutes
	private int activityType = 100;		// activity default to running
	private int tilesCount = 4;			// 4 tutorial tiles at the beginning
	
	private String lastStartTime = "";
	private int lastDuration = 0;
	private int lastSteps = 0;
	private float lastPoints = 0f;

	private int totalSteps = 0;
	private float totalPoints = 0f;
	
	private boolean isNoActivity = true;
	private boolean isAutoSleepTurnedOn = true;


	public TaggingActivityAPI(AutomationTest automation, File model, boolean efsm, PathGenerator generator, boolean weight) {
		super(automation, model, efsm, generator, weight);
	}

	public void e_init() {
		PrometheusHelper.signUpDefaultProfile();
	}

	public void e_chooseCycling() {

		activityType = MVPEnums.ACTIVITY_CYCLING;
		int popUpCount = changeTaggingSetting(activityType);
		Assert.assertEquals(popUpCount, 2, "Maximum number of cycling popup");
	}

	public void e_chooseSwimming() {

		activityType = MVPEnums.ACTIVITY_SWIMMING;
		int popUpCount = changeTaggingSetting(activityType);
		Assert.assertEquals(popUpCount, 2, "Maximum number of swimming popup");
	}

	public void e_chooseSleep() {

		activityType = MVPEnums.ACTIVITY_RUNNING;
		int popUpCount = changeTaggingSetting(activityType);
		Assert.assertEquals(popUpCount, 2, "Maximum number of sleep popup");
	}

	public void e_chooseTennis() {

		activityType = MVPEnums.ACTIVITY_TENNIS;
		changeTaggingSetting(activityType);
	}

	public void e_chooseSoccer() {

		activityType = MVPEnums.ACTIVITY_SOCCER;
		changeTaggingSetting(activityType);
	}

	public void e_chooseBasketball() {

		activityType = MVPEnums.ACTIVITY_BASKETBALL;
		changeTaggingSetting(activityType);
	}

	public void e_inputActivity() {

		if(isNoActivity) {
			
			Calendar cal = Calendar.getInstance();
			minuteOffset = (cal.get(Calendar.HOUR_OF_DAY) + 1) * 60;
			isNoActivity = false;
		}
		
		// prepare timestamp
		int hour = minuteOffset / 60;
		int minute = minuteOffset % 60;
		String noon = (hour < 12 ? "AM" : "PM");

		hour = (hour > 12 ? hour - 12 : hour == 0 ? 12 : hour);
		String[] time = new String[] { hour + "", String.format("%02d", minute), noon};

		// advance next timestamp by 10 minutes
		minuteOffset += 10;

		// input activity
		lastDuration = PrometheusHelper.randInt(5, 8);
		lastSteps = lastDuration * PrometheusHelper.randInt(100, 200);

		HomeScreen.tapOpenManualInput();
		HomeScreen.enterManualActivity(time, lastDuration, lastSteps);
		HomeScreen.tapSave();
		PrometheusHelper.handleTutorial();

		// save last record info
		lastStartTime = String.format("%d:%02d%s", hour, minute, noon.toLowerCase());
		calculateTotalProgressInfo();
	}

	
		
	public void v_HomeScreen() {
		
		Assert.assertTrue(HomeScreen.isToday(), "Current view is HomeScreen - Today");
	}
	
	public void v_UpdatedCyclingTimeline() {
		checkUpdatedTimeline(DefaultStrings.CyclingLevel);
	}
	
	public void v_UpdatedNormalTimeline() {
		checkUpdatedTimeline(DefaultStrings.WalkingLevel);
	}
	
	public void v_UpdatedSwimmingTimeline() {
		checkUpdatedTimeline(DefaultStrings.SwimmingLevel);
	}

	public void v_UpdatedTennisTimeline() {
		checkUpdatedTimeline(DefaultStrings.TennisLevel);
	}
	
	public void v_UpdatedBasketballTimeline() {
		checkUpdatedTimeline(DefaultStrings.BasketballLevel);
	}

	public void v_UpdatedSoccerTimeline() {
		checkUpdatedTimeline(DefaultStrings.SoccerLevel);
	}
	
		
	
	private int changeTaggingSetting(int activity) {

		int popUpCount = 0;

		HomeScreen.tapOpenSettingsTray();
		HomeScreen.tapDevices();
		HomeScreen.tapActiveDevice();
		Gui.swipeUp(500);
		HomeSettings.tapMisfitLabs();
		
		if(isAutoSleepTurnedOn) {
			
			isAutoSleepTurnedOn = false;
			HomeScreen.switchAutoSleepTrackingOff();
			HomeSettings.tapSave();
			PrometheusHelper.waitForView("UILabel", DefaultStrings.ShineTitle);
			HomeSettings.tapMisfitLabs();
		}

		switch (activity) {
		case MVPEnums.ACTIVITY_BASKETBALL:
			HomeScreen.chooseBasketball();
			break;

		case MVPEnums.ACTIVITY_SOCCER:
			HomeScreen.chooseSoccer();
			break;

		case MVPEnums.ACTIVITY_TENNIS:
			HomeScreen.chooseTennis();
			break;

		case MVPEnums.ACTIVITY_SWIMMING:
			for (int i = 0; i < 3; i++) {
				HomeScreen.chooseSwimming();
				if (HomeScreen.hasSuggestWearingPositionForSwimmingMessage()) {
					Gui.touchPopupButton(DefaultStrings.GotItButton);
					popUpCount++;
				}
			}
			break;

		case MVPEnums.ACTIVITY_CYCLING:
			for (int i = 0; i < 3; i++) {
				HomeScreen.chooseCycling();
				if (HomeScreen.hasSuggestWearingPositionForCyclingMessage()) {
					Gui.touchPopupButton(DefaultStrings.GotItButton);
					popUpCount++;
				}
			}
			break;

		default:
			for (int i = 0; i < 3; i++) {
				HomeScreen.chooseSleep();
				if (HomeScreen.hasSuggestWearingPositionForSleepMessage()) {
					Gui.touchPopupButton(DefaultStrings.GotItButton);
					popUpCount++;
				}
			}
		}

		HomeSettings.tapSave();
		PrometheusHelper.waitForView("UILabel", DefaultStrings.ShineLabel);
		HomeSettings.tapSave();
		HomeSettings.tapBack();

		return popUpCount;
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

	private void checkProgressCircle() {
		
		for (int i = 0; i < 2; i++) {

			// progress circle display point earned => check total point
			if (HomeScreen.isPointEarnedProgessCircle()) {
				System.out.println("DEBUG: PROGRESS CIRCLE VIEW 1");
				System.out.println("DEBUG: Assert total points " + this.totalPoints);
				Assert.assertTrue(ViewUtils.isExistedView("UILabel", (int) Math.floor(this.totalPoints) + "") ||
						ViewUtils.isExistedView("UILabel", (int) Math.round(this.totalPoints) + ""), 
						"Total points displayed correctly");
			} 

			// progress circle display summary => check steps
			if (HomeScreen.isSummaryProgressCircle()) {
				System.out.println("DEBUG: PROGRESS CIRCLE VIEW 2");
				System.out.println("DEBUG: Total Steps: " + this.totalSteps);
				Assert.assertTrue(ViewUtils.isExistedView("PTRichTextLabel", String.format("_%d_ steps", this.totalSteps)), 
						"Total steps displayed correctly");

			}

			HomeScreen.tapProgressCircle();
		}
	}
	
	private void checkUpdatedTimeline(String[] levelTitle) {
		
		// check summary through process circle
		checkProgressCircle();
		
		
		// check timeline items
		Timeline.dragUpTimelineAndHandleTutorial();
		if (this.lastPoints >= 50f) {
			
			Gui.swipeUp(1000);
			tilesCount++;
			if (tilesCount > 9) {
				Gui.swipeUp( (int) (tilesCount / 9) * 1000 );
			}
			
			// check activity record is saved
			Assert.assertTrue(ViewUtils.isExistedView("UILabel", lastStartTime), "Start time displayed correctly");
			
			// open overlay
			Timeline.openTile(lastStartTime);
			
			Assert.assertTrue(ViewUtils.isExistedView("UILabel", lastDuration + "") && 
					ViewUtils.isExistedView("UILabel", MINUTELABEL), 
					"Duration on timeline item is displayed correctly");
			
			Assert.assertTrue(ViewUtils.isExistedView("UILabel", (int) Math.floor(this.lastPoints) + "") && 
					ViewUtils.isExistedView("UILabel", POINTLABEL), 
					"Points on timeline item are displayed correctly");

			boolean correctTitle = false;
			for (int i = 0; i < levelTitle.length; i++) {
				if (ViewUtils.isExistedView("UILabel", levelTitle[i])) {
					correctTitle = true;
					break;
				}
			}
			
			Assert.assertTrue(correctTitle, "Title for tile is correct");
			
			// close overlay and drag down timeline view
			Timeline.closeCurrentTile();
			
		} else {
			Assert.assertTrue(!Timeline.isTileExisted(lastStartTime), 
					"Start time shouldn't be displayed because of low points");
		}
		
		Timeline.dragDownTimeline();
	}

}
