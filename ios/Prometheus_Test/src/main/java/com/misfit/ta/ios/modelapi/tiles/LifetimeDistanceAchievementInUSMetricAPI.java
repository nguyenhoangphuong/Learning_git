package com.misfit.ta.ios.modelapi.tiles;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ta.aut.AutomationTest;
import com.misfit.ta.common.Verify;
import com.misfit.ta.gui.Gui;
import com.misfit.ta.gui.HomeScreen;
import com.misfit.ta.gui.PrometheusHelper;
import com.misfit.ta.gui.Timeline;
import com.misfit.ta.modelAPI.ModelAPI;
import com.misfit.ta.utils.ShortcutsTyper;

public class LifetimeDistanceAchievementInUSMetricAPI extends ModelAPI {

	public LifetimeDistanceAchievementInUSMetricAPI(AutomationTest automation,
			File model, boolean efsm, PathGenerator generator, boolean weight) {
		super(automation, model, efsm, generator, weight);
		// TODO Auto-generated constructor stub
	}

	private String email;
	private int hour = 0;
	private List<String> errors = new ArrayList<String>();

	/**
	 * This method implements the Edge 'e_init'
	 * 
	 */
	public void e_init() {
		// sign up with default unit = us
		email = PrometheusHelper.signUpDefaultProfile();
	}

	/**
	 * This method implements the Edge 'e_input'
	 * 
	 */
	public void e_input() {
		// input to hit achievement (108694 steps - 22 mins ~ 100.00 miles)
		String[] times = new String[] { String.valueOf(++hour), "00", "AM" };
		HomeScreen.tapOpenManualInput();
		PrometheusHelper.inputManualRecord(times, 22, 108694);
		HomeScreen.tapSave();
		ShortcutsTyper.delayTime(5000);
	}

	/**
	 * This method implements the Edge 'e_signOutAndSignInAgain'
	 * 
	 */
	public void e_signOutAndSignInAgain() {
//		MVPApi.updateGoal(token, blankGoal);
		HomeScreen.pullToRefresh();
		ShortcutsTyper.delayTime(2000);
		PrometheusHelper.signOut();
		PrometheusHelper.signIn(email, "qwerty1");
	}

	/**
	 * This method implements the Vertex 'v_12MarathonsInUSMetric'
	 * 
	 */
	public void v_12MarathonsInUSMetric() {
		Timeline.dragUpTimelineAndHandleTutorial();
		Gui.swipeUp(1000);
		checkBadgesTile(true, hour, 22, 12);
		Timeline.dragDownTimeline();
	}

	/**
	 * This method implements the Vertex 'v_2MarathonsInUSMetric'
	 * 
	 */
	public void v_2MarathonsInUSMetric() {
		Timeline.dragUpTimelineAndHandleTutorial();
		Gui.swipeUp(1000);
		checkBadgesTile(true, hour, 11, 2);
		Timeline.dragDownTimeline();
	}

	/**
	 * This method implements the Vertex 'v_6MarathonsInUSMetric'
	 * 
	 */
	public void v_6MarathonsInUSMetric() {
		Timeline.dragUpTimelineAndHandleTutorial();
		Gui.swipeUp(1000);
		checkBadgesTile(true, hour, 11, 6);
		Timeline.dragDownTimeline();
	}

	/**
	 * This method implements the Vertex 'v_HomeScreen'
	 * 
	 */
	public void v_HomeScreen() {
		PrometheusHelper.handleUpdateFirmwarePopup();
		Assert.assertTrue(HomeScreen.isToday(),
				"Current screen is HomeScreen - Today");
	}

	/**
	 * This method implements the Vertex 'v_HomeScreenAfterSignInAgain'
	 * 
	 */
	public void v_HomeScreenAfterSignInAgain() {
		ShortcutsTyper.delayTime(500);
		Timeline.dragUpTimelineAndHandleTutorial();
		checkBadgesTile(true, 1, 11, 2);

		Gui.swipeUp(100);
		checkBadgesTile(true, 2, 11, 6);
		checkBadgesTile(true, 3, 22, 12);

		// print all errors
		if (!Verify.verifyAll(errors))
			Assert.fail("Not all assertions pass");
	}

	private void checkBadgesTile(boolean usUnit, int hitAchievementHour,
			int hitAchievementMinute, int marathonsNumber) {

		int i = -1;
		for (i = -1; i <= 1; i++) {

			String title = String.format("%d:%02dam", hitAchievementHour,
					hitAchievementMinute + i);
			if (!Timeline.isTileExisted(title))
				continue;

			String[] messages = Timeline.LifetimeDistanceInUSUnitMessages;
			String message = messages[marathonsNumber == 2 ? 0
					: (marathonsNumber == 6 ? 1 : (marathonsNumber == 12 ? 2
							: 0))];

			Timeline.openTile(title);
			Gui.captureScreen("streaktile-" + System.nanoTime());
			errors.add(Verify.verifyTrue(Timeline
					.isLifetimeDistanceBadgeTileCorrect(title, marathonsNumber,
							message), String.format(
					"Life time distance tile [%s - %d] is existed", title,
					marathonsNumber)));
			Timeline.closeAchievementTile();
			return;
		}

		if (i > 1) {
			errors.add(String.format(
					"No achievement tile [%d:%02dam - %d - %s] is existed",
					hitAchievementHour, hitAchievementMinute, marathonsNumber,
					"us"));
		}
	}

}
