package com.misfit.ta.ios.modelapi.tiles;

import java.io.File;

import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.backend.aut.DefaultValues;
import com.misfit.ta.backend.data.goal.Goal;
import com.misfit.ta.backend.data.statistics.Statistics;
import com.misfit.ta.gui.DefaultStrings;
import com.misfit.ta.gui.Gui;
import com.misfit.ta.gui.HomeScreen;
import com.misfit.ta.gui.HomeSettings;
import com.misfit.ta.gui.PrometheusHelper;
import com.misfit.ta.gui.Timeline;
import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.modelAPI.ModelAPI;
import com.misfit.ta.utils.ShortcutsTyper;

public class LifetimeDistanceAchievementAPI extends ModelAPI {

	public LifetimeDistanceAchievementAPI(AutomationTest automation, File model, boolean efsm, PathGenerator generator, boolean weight) {
		super(automation, model, efsm, generator, weight);
	}

	private int hour = 0;
	private String token = "";
	private Goal blankGoal;
	private Statistics statistics;

	public void e_init() {

		// sign up with default unit = us
		String email = PrometheusHelper.signUpDefaultProfile();

		// store the token and newly created goal for later use
		token = MVPApi.signIn(email, "qwerty1").token;
		blankGoal = MVPApi.searchGoal(token, MVPApi.getDayStartEpoch(), Integer.MAX_VALUE, 0).goals[0];
		
		// set lifetime distance to 45 miles
		statistics = DefaultValues.RandomStatistic();
		MVPApi.createStatistics(token, statistics);
		setLifetimeDistance(45d);
		
		// pull to refresh
		HomeScreen.pullToRefresh();
		PrometheusHelper.waitForViewToDissappear("UILabel", DefaultStrings.LoadingLabel);
	}

	public void e_changeUnitToSI() {

		changeDistanceSettings(false);
	}

	public void e_input() {

		// input to hit achievement (5625 steps - 25 mins ~ 5.00 miles)
		String[] times = new String[] { String.valueOf(++hour), "00", "AM" };
		HomeScreen.tapOpenManualInput();
		PrometheusHelper.inputManualRecord(times, 25, 5625);
		HomeScreen.tapSave();
		ShortcutsTyper.delayTime(5000);
	}

	public void e_reset() {

		// api: reset current goal
		MVPApi.updateGoal(token, blankGoal);

		// api: reset lifetimeDistance to 45 miles
		setLifetimeDistance(45d);

		// pull to refresh
		HomeScreen.pullToRefresh();
		PrometheusHelper.waitForViewToDissappear("UILabel", DefaultStrings.LoadingLabel);
	}

	public void v_HomeScreen() {

		PrometheusHelper.handleUpdateFirmwarePopup();
		Assert.assertTrue(HomeScreen.isToday(), "Current screen is HomeScreen - Today");
	}

	public void v_2MarathonsInUSMetric() {

		checkBadgesTile(true, 2);

		// set lifetime distance to 145
		setLifetimeDistance(145d);
		HomeScreen.pullToRefresh();
		PrometheusHelper.waitForViewToDissappear("UILabel", DefaultStrings.LoadingLabel);
	}
	
	public void v_6MarathonsInUSMetric() {

		checkBadgesTile(true, 6);
		
		// set lifetime distance to 295
		setLifetimeDistance(295d);
		HomeScreen.pullToRefresh();
		PrometheusHelper.waitForViewToDissappear("UILabel", DefaultStrings.LoadingLabel);
	}
	
	public void v_12MarathonsInUSMetric() {

		checkBadgesTile(true, 12);
	}

	public void v_2MarathonsInSIMetric() {

		checkBadgesTile(false, 2);
		
		// set lifetime distance to 145
		setLifetimeDistance(145d);
		HomeScreen.pullToRefresh();
		PrometheusHelper.waitForViewToDissappear("UILabel", DefaultStrings.LoadingLabel);
	}
	
	public void v_6MarathonsInSIMetric() {

		checkBadgesTile(false, 6);
		setLifetimeDistance(295d);
		
		// set lifetime distance to 295
		setLifetimeDistance(295d);
		HomeScreen.pullToRefresh();
		PrometheusHelper.waitForViewToDissappear("UILabel", DefaultStrings.LoadingLabel);
	}
	
	public void v_12MarathonsInSIMetric() {

		checkBadgesTile(false, 12);
	}

	private void checkBadgesTile(boolean usUnit, int marathonsNumber) {

		Timeline.dragUpTimelineAndHandleTutorial();
		Gui.swipeUp(1000);
		Gui.swipeUp(1000);
		Gui.swipeUp(1000);

		String title = hour + ":25am";
		String[] messages = usUnit ? Timeline.LifetimeDistanceInUSUnitMessages : Timeline.LifetimeDistanceInSIUnitMessages;
		String message = messages[marathonsNumber == 2 ? 0 : (marathonsNumber == 6 ? 1 : (marathonsNumber == 12? 2 : 0))];

		Timeline.openTile(title);
		Gui.captureScreen("streaktile-" + System.nanoTime());
		Assert.assertTrue(Timeline.isLifetimeDistanceBadgeTileCorrect(title, 2, message));
		Timeline.closeCurrentTile();
		Timeline.dragDownTimeline();
	}

	private void changeDistanceSettings(boolean usUnit) {

		HomeScreen.tapOpenSettingsTray();
		HomeScreen.tapSettings();
		if (usUnit)
			HomeSettings.tapMile();
		else
			HomeSettings.tapKm();
		HomeSettings.tapBack();
		ShortcutsTyper.delayTime(200);
	}
	
	private void setLifetimeDistance(double miles) {
			
		statistics.setLifetimeDistance(miles);
		MVPApi.updateStatistics(token, statistics);
	}

}
