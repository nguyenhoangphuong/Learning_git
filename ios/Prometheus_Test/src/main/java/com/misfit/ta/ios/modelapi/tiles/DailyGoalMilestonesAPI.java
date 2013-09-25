package com.misfit.ta.ios.modelapi.tiles;

import java.io.File;
import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ta.Gui;
import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.backend.aut.DefaultValues;
import com.misfit.ta.backend.data.goal.Goal;
import com.misfit.ta.gui.DefaultStrings;
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
	
	int hour = 6;

	public void e_init() {
		
		// sign up with goal = 1000 pts
		String email = PrometheusHelper.signUp();
		ShortcutsTyper.delayTime(5000);
		
		// api: create yesterday's goal
		long yesterday = System.currentTimeMillis() / 1000 - 3600 * 24;
		String token = MVPApi.signIn(email, "qwerty1").token;
		Goal goal = DefaultValues.CreateGoal(yesterday);
		MVPApi.createGoal(token, goal);
		
		// sign out and sign in to dismiss tutorial tiles
		PrometheusHelper.signOut();
		PrometheusHelper.signIn(email, "qwerty1");
		ShortcutsTyper.delayTime(5000);
	}
	
	public void e_inputHalfGoal() {
		
		// input 500 pts activity session
		String[] times = new String[] { String.valueOf(++hour), "00", "AM" };
		
		Timeline.dragDownTimeline();
		HomeScreen.tapOpenManualInput();
		PrometheusHelper.inputManualRecord(times, 50, 5000);
		HomeScreen.tapSave();
		ShortcutsTyper.delayTime(10000);
		Timeline.dragUpTimeline();
	}
	
	
	public void v_HomeScreen() {
		
		Assert.assertTrue(HomeScreen.isTodayDefault(), "Current screen is HomeScreen");
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
	
	public void checkActivityTile() {
		
		String startTime = hour + ":00am";
		String endTime = hour + ":50am";
		
		Timeline.openTile(startTime);
		Assert.assertTrue(Timeline.isActivityTileCorrect(startTime, endTime, 50, 500, DefaultStrings.WalkingLevel[2]),
				"Activity tile is displayed correctly");
		Timeline.closeCurrentTile();
		
	}
	
	public void checkGoalTile(int points, String[] messages) {
		
		String time = hour + ":50am";
		
		Timeline.openTile(time);
		Assert.assertTrue(Timeline.isDailyGoalMilestoneTileCorrect(time, points, messages),
				"Daily goal milestone tile is displayed correctly");
		Timeline.closeCurrentTile();
	}
	
}
