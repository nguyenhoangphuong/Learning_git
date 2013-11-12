package com.misfit.ta.ios.modelapi.tiles;

import java.io.File;

import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.backend.aut.BackendHelper;
import com.misfit.ta.backend.data.goal.Goal;
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
	private boolean usUnit = true;

	
	
	public void e_init() {

		// sign up with default unit = us
		String email = PrometheusHelper.signUpDefaultProfile();

		// store the token and newly created goal for later use
		token = MVPApi.signIn(email, "qwerty1").token;
		blankGoal = MVPApi.searchGoal(token, MVPApi.getDayStartEpoch(), Integer.MAX_VALUE, 0).goals[0];
	}

	public void e_changeUnitToSI() {

		changeDistanceSettings(false);
		usUnit = false;
	}

	public void e_input() {

		if(usUnit) {
			
			// input to hit achievement (108694 steps - 22 mins ~ 100.00 miles)
			String[] times = new String[] { String.valueOf(++hour), "00", "AM" };
			HomeScreen.tapOpenManualInput();
			PrometheusHelper.inputManualRecord(times, 22, 108694);
			HomeScreen.tapSave();
			ShortcutsTyper.delayTime(5000);
		}
		else {
			
			// input to hit achievement (135079 steps - 27 mins ~ 200.00 km)
			String[] times = new String[] { String.valueOf(++hour), "00", "AM" };
			HomeScreen.tapOpenManualInput();
			PrometheusHelper.inputManualRecord(times, 27, 135079);
			HomeScreen.tapSave();
			ShortcutsTyper.delayTime(5000);
		}
	}

	public void e_reset() {

		// api: reset current goal
		MVPApi.updateGoal(token, blankGoal);
		
		// api: reset lifetime distance
		BackendHelper.setLifetimeDistance(token, 0d);
		
		// refresh
		HomeScreen.pullToRefresh();
		PrometheusHelper.waitForViewToDissappear("UILabel", DefaultStrings.LoadingLabel);
	}

	
	
	public void v_HomeScreen() {
		Assert.assertTrue(HomeScreen.isToday(), "Current screen is HomeScreen - Today");
	}

	public void v_2MarathonsInUSMetric() {

		checkBadgesTile(true, 11, 2);
	}
	
	public void v_6MarathonsInUSMetric() {

		checkBadgesTile(true, 11, 6);
	}
	
	public void v_12MarathonsInUSMetric() {

		checkBadgesTile(true, 22, 12);
	}

	public void v_2MarathonsInSIMetric() {

		checkBadgesTile(false, 11, 2);
	}
	
	public void v_6MarathonsInSIMetric() {

		checkBadgesTile(false, 7, 6);
	}
	
	public void v_12MarathonsInSIMetric() {

		checkBadgesTile(false, 14, 12);
	}

	
	
	private void checkBadgesTile(boolean usUnit, int hitAchievementMinute, int marathonsNumber) {

		Timeline.dragUpTimelineAndHandleTutorial();
		Gui.swipeUp(1000);
		Gui.swipeUp(1000);
		Gui.swipeUp(1000);
		
		int i = -1;
		for(i = -1; i <= 1; i++) {

			String title = String.format("%d:%02dam", hour, hitAchievementMinute + i);
			if(!Timeline.isTileExisted(title))
				continue;

			String[] messages = usUnit ? Timeline.LifetimeDistanceInUSUnitMessages : Timeline.LifetimeDistanceInSIUnitMessages;
			String message = messages[marathonsNumber == 2 ? 0 : (marathonsNumber == 6 ? 1 : (marathonsNumber == 12? 2 : 0))];

			Timeline.openTile(title);
			Gui.captureScreen("streaktile-" + System.nanoTime());
			Assert.assertTrue(Timeline.isLifetimeDistanceBadgeTileCorrect(title, marathonsNumber, message));
			Timeline.closeCurrentTile();
			Timeline.dragDownTimeline();
			return;
		}
		
		if(i > 1)
			Assert.fail("No achievement tile");
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

}
