package com.misfit.ta.ios.modelapi.tiles;

import java.io.File;

import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ta.backend.api.MVPApi;
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

	public void e_init() {

		// sign up with default unit = us
		String email = PrometheusHelper.signUp();

		// store the token for later use
		token = MVPApi.signIn(email, "qwerty1").token;
		blankGoal = MVPApi.searchGoal(token, MVPApi.getDayStartEpoch(), Integer.MAX_VALUE, 0).goals[0];
	}

	public void e_changeUnitToUS() {

		changeDistanceSettings(true);
	}

	public void e_changeUnitToSI() {

		changeDistanceSettings(false);
	}

	public void e_input() {

		// input to hit goal
		String[] times = new String[] { String.valueOf(++hour), "00", "AM" };
		HomeScreen.tapOpenManualInput();
		PrometheusHelper.inputManualRecord(times, 1, 1000);
		HomeScreen.tapSave();

		// input to hit achievement
		times = new String[] { String.valueOf(++hour), "00", "AM" };
		HomeScreen.tapOpenManualInput();
		PrometheusHelper.inputManualRecord(times, 1, 9999999);
		HomeScreen.tapSave();
		ShortcutsTyper.delayTime(5000);
	}

	public void e_reset() {

		// api: reset current goal
		MVPApi.updateGoal(token, blankGoal);

		// api: reset lifetimeDistance
		Statistics stats = Statistics.fromResponse(MVPApi.userInfo(token).response);
		stats.setLifetimeDistance(0d);
		MVPApi.updateStatistics(token, stats);

		// pull to refresh
		HomeScreen.pullToRefresh();
		PrometheusHelper.waitForViewToDissappear("UILabel", DefaultStrings.LoadingLabel);
	}

	public void v_HomeScreen() {

		PrometheusHelper.handleUpdateFirmwarePopup();
		Assert.assertTrue(HomeScreen.isToday(), "Current screen is HomeScreen - Today");
	}

	public void v_BadgesInUSMetric() {

		checkBadgesTile(true);
	}

	public void v_BadgesInSIMetric() {

		checkBadgesTile(false);
	}

	private void checkBadgesTile(boolean usUnit) {

		Timeline.dragUpTimeline();
		Gui.swipeUp(1000);
		Gui.swipeUp(1000);
		Gui.swipeUp(1000);

		boolean[] passes = new boolean[] { false, false, false };
		String title = hour + ":00am";
		String[] messages = usUnit ? Timeline.LifetimeDistanceInUSUnitMessages : Timeline.LifetimeDistanceInSIUnitMessages;

		for (int i = 0; i < 3; i++) {
			Timeline.openTile(title);
			Gui.captureScreen("streaktile-" + System.nanoTime());

			if (!passes[0] && Timeline.isLifetimeDistanceBadgeTileCorrect(title, 2, messages[0])) {
				passes[0] = true;
				continue;
			}
			if (!passes[1] && Timeline.isLifetimeDistanceBadgeTileCorrect(title, 6, messages[1])) {
				passes[1] = true;
				continue;
			}
			if (!passes[2] && Timeline.isLifetimeDistanceBadgeTileCorrect(title, 12, messages[2])) {
				passes[2] = true;
				continue;
			}

			Timeline.closeCurrentTile();
		}

		Timeline.dragDownTimeline();

		Assert.assertTrue(passes[0] && passes[1] && passes[2], "All tiles have correct content");
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
