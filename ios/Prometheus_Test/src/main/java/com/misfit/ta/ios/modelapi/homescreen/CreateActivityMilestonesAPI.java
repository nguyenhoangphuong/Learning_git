package com.misfit.ta.ios.modelapi.homescreen;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ios.ViewUtils;
import com.misfit.ta.backend.api.internalapi.MVPApi;
import com.misfit.ta.backend.aut.BackendHelper;
import com.misfit.ta.common.MVPCalculator;
import com.misfit.ta.common.MVPEnums;
import com.misfit.ta.common.Verify;
import com.misfit.ta.gui.DefaultStrings;
import com.misfit.ta.gui.EditTagScreen;
import com.misfit.ta.gui.Gui;
import com.misfit.ta.gui.HomeScreen;
import com.misfit.ta.gui.HomeSettings;
import com.misfit.ta.gui.PrometheusHelper;
import com.misfit.ta.gui.Sync;
import com.misfit.ta.gui.Timeline;
import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.modelAPI.ModelAPI;
import com.misfit.ta.utils.ShortcutsTyper;

public class CreateActivityMilestonesAPI extends ModelAPI {
	
	public CreateActivityMilestonesAPI(AutomationTest automation, File model, boolean efsm,
			PathGenerator generator, boolean weight) {
		super(automation, model, efsm, generator, weight);
	}
	private String email = PrometheusHelper.signUpDefaultProfile();
	private String password = "qwerty1";
	private int steps = 5000;
	private int mins = 5;
	private List<String> errors = new ArrayList<String>();
	
	public void e_init() {
		
		// sign up
		String token = MVPApi.signIn(email, password).token;
		
		// create 2 goals in the past
		BackendHelper.completeGoalInPast(token, 1);
		BackendHelper.completeGoalInPast(token, 2);

		// create a personal best record
		long yesterdayTimestamp = (System.currentTimeMillis() - 24*3600*1000) / 1000;
		BackendHelper.setPersonalBest(token, 1000, yesterdayTimestamp);
		
		// get new data from server
		HomeScreen.pullToRefresh();
	}
	
	public void e_inputActivityAndFinishTutorial() {
		
		// input first activity
		HomeScreen.tapOpenManualInput();
		HomeScreen.enterManualActivity(new String[] {"1", "00", "AM"}, mins, steps);
		HomeScreen.tapSave();
		
		// handle tutorial	
		Timeline.dragUpTimelineAndHandleTutorial();	
	}
	
	public void v_HomeScreen() {
		
		Assert.assertTrue(HomeScreen.isToday(), "Current view is HomeScreen - Today");
	}
	
	public void v_HomeScreenUpdated() {

		int newPoint = (int) Math.floor(MVPCalculator.calculatePointForNewTag(steps, mins, MVPEnums.ACTIVITY_RUNNING)) + 2;

		checkDailyGoalMilestone(newPoint);
		checkStreakMilestone(newPoint);
		checkPersonalBestMilestone(newPoint);
		checkNewTileAndProgress(newPoint);
	}
	
	public void v_LaunchScreen() {
		
	}
	
	public void v_End() {
		
		if(!Verify.verifyAll(errors)) 
			Assert.fail("Not all assertions pass");
	}
	
	
	
	private void capture() {
		
		Gui.captureScreen("EditActivityFlow-" + System.nanoTime());
	}
	
	private void checkDailyGoalMilestone(int newPoint) {
		
		float ppm = newPoint * 1f / mins;
		
		// check daily goal milestones
		if(newPoint >= 1000) {
			
			// check hit goal animation
			capture();
			errors.add(Verify.verifyTrue(ViewUtils.isExistedView("UILabel", "GOAL"), "Goal animation plays"));
			Timeline.dragUpTimeline();
			
			// check 100% tile
			String hit100GoalTime = String.format("1:%02dam", (int)(1000/ppm));
			boolean pass = false;
			for(int i = 0; i < 2 && !pass; i++) {
				
				Timeline.openTile("100");
				capture();
				pass = Timeline.isDailyGoalMilestoneTileCorrect(hit100GoalTime, 1000, Timeline.DailyGoalMessagesFor100Percent);
				Timeline.closeMilestoneTile();
			}
			
			errors.add(Verify.verifyTrue(pass, String.format("Goal 100%% tile [%s - 1000pts] displays correctly", hit100GoalTime)));
		}
		
		if(newPoint >= 1500) {
			
			// check 150% tile
			int startMin = (int)(1500/ppm);
			boolean pass = false;
			
			for(int i = -1; i <= 1; i++) {
				
				String hit150GoalTime = String.format("1:%02dam", startMin + i);
				if(ViewUtils.isExistedView("UILabel", hit150GoalTime)) {
					
					Timeline.openTile("150");
					capture();
					pass = Timeline.isDailyGoalMilestoneTileCorrect(hit150GoalTime, 1500, Timeline.DailyGoalMessagesFor150Percent);
					Timeline.closeMilestoneTile();
					break;
				}
			}
			
			errors.add(Verify.verifyTrue(pass, String.format("Goal 150%% tile [1:%02dam - 1500pts] displays correctly", startMin)));
		}
		
		if(newPoint >= 2000) {
			
			// check 200% tile
			int startMin = (int)(2000/ppm);
			boolean pass = false;
			
			for(int i = -1; i <= 1; i++) {
				
				String hit200GoalTime = String.format("1:%02dam", startMin + i);
				if(ViewUtils.isExistedView("UILabel", hit200GoalTime)) {
					
					Timeline.openTile("200");
					capture();
					pass = Timeline.isDailyGoalMilestoneTileCorrect(hit200GoalTime, 2000, Timeline.DailyGoalMessagesFor200Percent);
					Timeline.closeMilestoneTile();
					break;
				}
			}
			
			errors.add(Verify.verifyTrue(pass, String.format("Goal 200%% tile [1:%02dam - 2000pts] displays correctly", startMin)));
		}
	}

	private void checkStreakMilestone(int newPoint) {
		
		if(newPoint < 1000)
			return;
		
		float ppm = newPoint * 1f / mins;
		int minutes = (int)(1000/ppm);
		String hit100GoalTime = String.format("1:%02dam", minutes);
		String hit100GoalTime2 = String.format("1:%02dam", minutes-1);
		String hit100GoalTime3 = String.format("1:%02dam", minutes+1);
		
		// check streak tile
		Timeline.openTile("3");
		capture();
		errors.add(
				Verify.verifyTrue(
						Timeline.isStreakTileCorrect(hit100GoalTime, 3, Timeline.Streak3DaysMessages)
						|| Timeline.isStreakTileCorrect(hit100GoalTime2, 3, Timeline.Streak3DaysMessages)
						|| Timeline.isStreakTileCorrect(hit100GoalTime3, 3, Timeline.Streak3DaysMessages),
						String.format("Streak tile [%s - 3 days] displays correctly", hit100GoalTime)
				)
		);
		Timeline.closeStreakTile();
	}

	private void checkPersonalBestMilestone(int newPoint) {
		
		if(newPoint <= 1000)
			return;
		
		boolean pass = false;
		for(int i = 0; i <= 5 && !pass; i++) {
			String time = "1:0"+String.valueOf(i)+"am";
			Timeline.openTile(time);
			capture();
			pass = Timeline.isPersonalBestTileCorrect(time, newPoint, 1000, Timeline.PersonalBestMessages);
			Timeline.closeTile(Timeline.LabelPersonalBest);
		}
		
		errors.add(Verify.verifyTrue(pass, String.format("Personal best milestone [1:00am - %dpts new - 1000pts old] displays correctly", newPoint)));
	}
	
	private void checkNewTileAndProgress(int newPoint) {
		
		// check progress circle
		Timeline.dragDownTimeline();
		errors.add(Verify.verifyTrue(ViewUtils.isExistedView("UILabel", newPoint + ""), 
				String.format("Total points [%d] updated correctly", newPoint)));
		Timeline.dragUpTimeline();
	}

}
