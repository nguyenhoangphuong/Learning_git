package com.misfit.ta.ios.modelapi.tiles;

import java.io.File;
import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ta.backend.api.MVPApi;
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
	
	int hour = 6;
	String email = MVPApi.generateUniqueEmail();
	String password = "qwerty1";
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
		ShortcutsTyper.delayTime(500);
	}
	
	
	public void v_HomeScreen() {
		
		Assert.assertTrue(HomeScreen.isToday(), "Current screen is HomeScreen");
	}
	
	public void v_HomeScreen50() {
		
		checkActivityTile();
	}
	
	public void v_HomeScreen100() {
		ShortcutsTyper.delayTime(5000);
		checkActivityTile();
		checkGoalTile(1000, Timeline.DailyGoalMessagesFor100Percent);
	}
	
	public void v_HomeScreen150() {
		ShortcutsTyper.delayTime(5000);
		checkActivityTile();
		checkGoalTile(1500, Timeline.DailyGoalMessagesFor150Percent);
	}
	
	public void v_HomeScreen200() {
		ShortcutsTyper.delayTime(5000);
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
		System.out.println("DEBUG check goal tile Hour: " + hour);
		String time = hour + ":50am";
		System.out.println("DEBUG check goal tile Time: " + time);
		Timeline.openNotableEventTile(time);
		Assert.assertTrue(Timeline.isDailyGoalMilestoneTileCorrect(time, points, messages),
				"Daily goal milestone tile is displayed correctly");
		Timeline.closeCurrentTile();
	}
	
	public void e_SignOut() {
		PrometheusHelper.signOut();
	}
	 
	public void e_SignIn() {
		PrometheusHelper.signIn(email, password);
		ShortcutsTyper.delayTime(2000);
		Timeline.dragUpTimelineAndHandleTutorial();
	}
	
	public void v_LaunchScreen() {
		
	}
	
}
