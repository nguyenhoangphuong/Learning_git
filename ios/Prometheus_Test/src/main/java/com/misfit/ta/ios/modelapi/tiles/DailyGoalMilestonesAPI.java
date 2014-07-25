package com.misfit.ta.ios.modelapi.tiles;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ta.backend.api.internalapi.MVPApi;
import com.misfit.ta.common.Verify;
import com.misfit.ta.gui.DefaultStrings;
import com.misfit.ta.gui.Gui;
import com.misfit.ta.gui.HomeScreen;
import com.misfit.ta.gui.PrometheusHelper;
import com.misfit.ta.gui.Timeline;
import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.modelAPI.ModelAPI;
import com.misfit.ta.utils.ShortcutsTyper;

public class DailyGoalMilestonesAPI extends ModelAPI {

	public DailyGoalMilestonesAPI(AutomationTest automation, File model, boolean efsm,
			PathGenerator generator, boolean weight) {
		super(automation, model, efsm, generator, weight);
	}
	
	private int hour = 6;
	private String email = MVPApi.generateUniqueEmail();
	private String password = "qwerty1";
	private List<String> errors = new ArrayList<String>();
	
	
	public void e_init() {
		
		// sign up with goal = 1000 pts
		PrometheusHelper.signUpDefaultProfile(email, password);
	}
	
	public void e_inputHalfGoal() {
		
		// input 500 pts activity session
		String[] times = new String[] { String.valueOf(++hour), "00", "AM" };
		
		Timeline.dragDownTimeline();
		HomeScreen.tapOpenManualInput();
		PrometheusHelper.inputManualRecord(times, 50, 5000);
		HomeScreen.tapSave();
		Timeline.dragUpTimelineAndHandleTutorial();
		ShortcutsTyper.delayTime(5000);
	}
	
	public void e_signOutAndSignInAgain() {
		
		PrometheusHelper.signOut();
		PrometheusHelper.signIn(email, password);
		
		// wait for sync data
		ShortcutsTyper.delayTime(5000);
		Timeline.dragUpTimelineAndHandleTutorial();
	}
	
	
	public void v_HomeScreen() {
		
		Assert.assertTrue(HomeScreen.isToday(), "Current screen is HomeScreen");
	}
	
	public void v_HomeScreen50() {
		
		checkActivityTile();
	}
	
	public void v_HomeScreen100() {
		
		checkActivityTile();
		checkGoalTile(1000, Timeline.DailyGoalMessagesFor100Percent);
	}
	
	public void v_HomeScreen150() {
		
		checkActivityTile();
		checkGoalTile(1500, Timeline.DailyGoalMessagesFor150Percent);
	}
	
	public void v_HomeScreen200() {
		
		Gui.swipeUp(1000);
		checkActivityTile();
		checkGoalTile(2000, Timeline.DailyGoalMessagesFor200Percent);
	}
	
	public void v_HomeScreenAfterSignInAgain() {
		
		hour = 8;
		checkGoalTile(1000, Timeline.DailyGoalMessagesFor100Percent);
		
		hour = 9;
		checkGoalTile(1500, Timeline.DailyGoalMessagesFor150Percent);
		
		hour = 10;
		Gui.swipeUp(1000);
		Gui.swipeUp(1000);
		checkGoalTile(2000, Timeline.DailyGoalMessagesFor200Percent);
		
		if(!Verify.verifyAll(errors))
			Assert.fail("Not all assertions pass");
	}
	
	
	public void checkActivityTile() {
		
		String startTime = hour + ":00am";
		String endTime = hour + ":50am";
		
		Timeline.openTile(startTime);
		errors.add(Verify.verifyTrue(Timeline.isActivityTileCorrect(startTime, endTime, 50, 500, DefaultStrings.WalkingLevel[2]),
				String.format("Activity tile [%s - %s - 50mins - 500pts] is displayed correctly", startTime, endTime)));
		
		String title = startTime + " - " + endTime;
		Timeline.closeTile(title);
	}
	
	public void checkGoalTile(int points, String[] messages) {

		String time = hour + ":50am";
		Timeline.openNotableEventTile(time);
		errors.add(Verify.verifyTrue(Timeline.isDailyGoalMilestoneTileCorrect(time, points, messages),
				String.format("Daily goal milestone tile [%s - %d] is displayed correctly", time, points)));
		
		String title = time;
		Timeline.closeTile(title);
	}
	
}
