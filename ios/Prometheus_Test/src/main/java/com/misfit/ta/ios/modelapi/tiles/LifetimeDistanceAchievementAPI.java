package com.misfit.ta.ios.modelapi.tiles;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ta.backend.api.internalapi.MVPApi;
import com.misfit.ta.backend.aut.BackendHelper;
import com.misfit.ta.backend.data.goal.Goal;
import com.misfit.ta.backend.data.goal.ProgressData;
import com.misfit.ta.common.MVPCommon;
import com.misfit.ta.common.Verify;
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

	private String email;
	private int hour = 0;
	private String token = "";
	private Goal blankGoal;
	private boolean usUnit = true;
	private List<String> errors = new ArrayList<String>();

	
	
	public void e_init() {

		// sign up with default unit = us
		email = PrometheusHelper.signUpDefaultProfile();

		// store the token and newly created goal for later use
		token = MVPApi.signIn(email, "qwerty1").token;
		blankGoal = MVPApi.searchGoal(token, MVPCommon.getDayStartEpoch(), (long)Integer.MAX_VALUE, 0l).goals[0];
		blankGoal.setProgressData(ProgressData.getDefaultProgressData());
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
	}

	public void e_signOutAndSignInAgain() {
		
		PrometheusHelper.signOut();
		PrometheusHelper.signIn(email, "qwerty1");
	}
	
	
	
	public void v_HomeScreen() {
		PrometheusHelper.handleUpdateFirmwarePopup();
		Assert.assertTrue(HomeScreen.isToday(), "Current screen is HomeScreen - Today");
	}

	public void v_2MarathonsInUSMetric() {

		Timeline.dragUpTimelineAndHandleTutorial();
		Gui.swipeUp(1000);
		checkBadgesTile(true, hour, 11, 2);
		Timeline.dragDownTimeline();
	}
	
	public void v_6MarathonsInUSMetric() {

		Timeline.dragUpTimelineAndHandleTutorial();
		Gui.swipeUp(1000);
		checkBadgesTile(true, hour, 11, 6);
		Timeline.dragDownTimeline();
	}
	
	public void v_12MarathonsInUSMetric() {

		Timeline.dragUpTimelineAndHandleTutorial();
		Gui.swipeUp(1000);
		checkBadgesTile(true, hour, 22, 12);
		Timeline.dragDownTimeline();
	}

	public void v_2MarathonsInSIMetric() {

		Timeline.dragUpTimelineAndHandleTutorial();
		Gui.swipeUp(1000);
		checkBadgesTile(false, hour, 11, 2);
		Timeline.dragDownTimeline();
	}
	
	public void v_6MarathonsInSIMetric() {

		Timeline.dragUpTimelineAndHandleTutorial();
		Gui.swipeUp(1000);
		checkBadgesTile(false, hour, 7, 6);
		Timeline.dragDownTimeline();
	}
	
	public void v_12MarathonsInSIMetric() {

		Timeline.dragUpTimelineAndHandleTutorial();
		Gui.swipeUp(1000);
		checkBadgesTile(false, hour, 14, 12);
		Timeline.dragDownTimeline();
	}

	public void v_HomeScreenAfterSignInAgain() {
		
		Timeline.dragUpTimelineAndHandleTutorial();
		checkBadgesTile(true, 1, 11, 2);
		
		Gui.swipeUp(100);
		checkBadgesTile(true, 2, 11, 6);
		checkBadgesTile(true, 3, 22, 12);
		
		Gui.swipeUp(1000);
		checkBadgesTile(false, 4, 11, 2);
		checkBadgesTile(false, 5, 7, 6);
		checkBadgesTile(false, 6, 14, 12);
		Timeline.dragDownTimeline();
		
		// print all errors
		if(!Verify.verifyAll(errors))
			Assert.fail("Not all assertions pass");
	}
	
	
	
	private void checkBadgesTile(boolean usUnit, int hitAchievementHour, int hitAchievementMinute, int marathonsNumber) {
		
		int i = -1;
		for(i = -1; i <= 1; i++) {

			String title = String.format("%d:%02dam", hitAchievementHour, hitAchievementMinute + i);
			if(!Timeline.isTileExisted(title))
				continue;

			String[] messages = usUnit ? Timeline.LifetimeDistanceInUSUnitMessages : Timeline.LifetimeDistanceInSIUnitMessages;
			String message = messages[marathonsNumber == 2 ? 0 : (marathonsNumber == 6 ? 1 : (marathonsNumber == 12? 2 : 0))];

			Timeline.openTile(title);
			Gui.captureScreen("streaktile-" + System.nanoTime());
			errors.add(Verify.verifyTrue(Timeline.isLifetimeDistanceBadgeTileCorrect(title, marathonsNumber, message), 
					String.format("Life time distance tile [%s - %d] is existed", title, marathonsNumber)));
			Timeline.closeCurrentTile();
			return;
		}
		
		if(i > 1) {
			errors.add(String.format("No archievement tile [%d:%02dam - %d - %s] is existed", 
					hitAchievementHour, hitAchievementMinute, marathonsNumber, usUnit ? "us" : "si"));
		}
	}

	private void changeDistanceSettings(boolean usUnit) {

		HomeScreen.tapOpenSettingsTray();
		HomeScreen.tapSettings();
		if (usUnit)
			HomeSettings.tapMile();
		else
			HomeSettings.tapKm();
		HomeSettings.tapBack();
		
		// wait until new settings saved
		PrometheusHelper.waitForThrobberToDissappear();
	}

}
