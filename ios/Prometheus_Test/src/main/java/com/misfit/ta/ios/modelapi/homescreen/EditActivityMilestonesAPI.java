package com.misfit.ta.ios.modelapi.homescreen;

import java.io.File;

import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ios.ViewUtils;
import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.backend.aut.BackendHelper;
import com.misfit.ta.common.MVPCalculator;
import com.misfit.ta.common.MVPEnums;
import com.misfit.ta.gui.DefaultStrings;
import com.misfit.ta.gui.EditTagScreen;
import com.misfit.ta.gui.Gui;
import com.misfit.ta.gui.HomeScreen;
import com.misfit.ta.gui.PrometheusHelper;
import com.misfit.ta.gui.Sync;
import com.misfit.ta.gui.Timeline;
import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.modelAPI.ModelAPI;

public class EditActivityMilestonesAPI extends ModelAPI {
	
	public EditActivityMilestonesAPI(AutomationTest automation, File model, boolean efsm,
			PathGenerator generator, boolean weight) {
		super(automation, model, efsm, generator, weight);
	}
	
	private int steps = 5000;
	private int mins = 50;
	
	public void e_init() {
		
		// sign up
		String email = PrometheusHelper.signUpDefaultProfile();
		String token = MVPApi.signIn(email, "qwerty1").token;
		
		// create 2 goals in the past
		BackendHelper.completeGoalInPast(token, 1);
		BackendHelper.completeGoalInPast(token, 2);
				
		// create a personal best record
		BackendHelper.setPersonalBest(token, 1000);
		
		// get new data from server
		HomeScreen.pullToRefresh();
		PrometheusHelper.waitForViewToDissappear("UILabel", DefaultStrings.LoadingLabel);
	}
	
	public void e_inputActivityAndFinishTutorial() {
		
		// input first activity
		HomeScreen.tapOpenManualInput();
		HomeScreen.enterManualActivity(new String[] {"1", "00", "AM"}, mins, steps);
		HomeScreen.tapSave();
		
		// handle tutorial	
		Timeline.dragUpTimelineAndHandleTutorial();	
	}
	
	public void e_holdToEditActivity() {
	
		Timeline.holdAndPressTile("1:00am");
	}
	
	public void e_changeToSwimming() {

		EditTagScreen.selectActivity(DefaultStrings.SwimmingLabel);
		EditTagScreen.tapSave();
	}
	
	public void e_confirmSyncAlert() {
		
		Sync.tapPopupSyncLater();
	}
	
	
	
	public void v_HomeScreen() {
		
		Assert.assertTrue(HomeScreen.isToday(), "Current view is HomeScreen - Today");
	}
	
	public void v_EditActivityTag() {
		
		Assert.assertTrue(EditTagScreen.isEditTagScreen(), "Current view is Edit Tag");
	}
	
	public void v_SyncRequireAlert() {
		
		Assert.assertTrue(Sync.hasShineOutOfSyncMessage(), "Sync require alert shows up");
	}

	public void v_HomeScreenUpdated() {

		int newPoint = (int) Math.floor(MVPCalculator.calculatePointForNewTag(steps, mins, MVPEnums.ACTIVITY_SWIMMING));

		checkDailyGoalMilestone(newPoint);
		checkStreakMilestone(newPoint);
		checkPersonalBestMilestone(newPoint);
		checkNewTileAndProgress(newPoint);
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
			Assert.assertTrue(ViewUtils.isExistedView("UILabel", "GOAL"), "Goal animation plays");
			Timeline.dragUpTimeline();
			
			// check 100% tile
			String hit100GoalTime = String.format("1:%02dam", (int)(1000/ppm));
			boolean pass = false;
			for(int i = 0; i < 2 && !pass; i++) {
				
				Timeline.openTile(hit100GoalTime);
				capture();
				pass = Timeline.isDailyGoalMilestoneTileCorrect(hit100GoalTime, 1000, Timeline.DailyGoalMessagesFor100Percent);
				Timeline.closeCurrentTile();
			}
			
			Assert.assertTrue(pass, "Goal 100% tile displays correctly");
		}
		
		if(newPoint >= 1500) {
			
			// check 150% tile
			int startMin = (int)(1500/ppm);
			boolean pass = false;
			
			for(int i = -1; i <= 1; i++) {
				
				String hit150GoalTime = String.format("1:%02dam", startMin + i);
				if(ViewUtils.isExistedView("UILabel", hit150GoalTime)) {
					
					Timeline.openTile(hit150GoalTime);
					capture();
					pass = Timeline.isDailyGoalMilestoneTileCorrect(hit150GoalTime, 1500, Timeline.DailyGoalMessagesFor150Percent);
					Timeline.closeCurrentTile();
					break;
				}
			}
			
			Assert.assertTrue(pass, "Goal 150% tile displays correctly");
		}
		
		if(newPoint >= 2000) {
			
			// check 200% tile
			int startMin = (int)(2000/ppm);
			boolean pass = false;
			
			for(int i = -1; i <= 1; i++) {
				
				String hit200GoalTime = String.format("1:%02dam", startMin + i);
				if(ViewUtils.isExistedView("UILabel", hit200GoalTime)) {
					
					Timeline.openTile(hit200GoalTime);
					capture();
					pass = Timeline.isDailyGoalMilestoneTileCorrect(hit200GoalTime, 2000, Timeline.DailyGoalMessagesFor200Percent);
					Timeline.closeCurrentTile();
					break;
				}
			}
			
			Assert.assertTrue(pass, "Goal 200% tile displays correctly");
		}
	}

	private void checkStreakMilestone(int newPoint) {
		
		if(newPoint < 1000)
			return;
		
		float ppm = newPoint * 1f / mins;
		String hit100GoalTime = String.format("1:%02dam", (int)(1000/ppm));
		
		// check streak tile
		Timeline.openTile("3");
		capture();
		Assert.assertTrue(Timeline.isStreakTileCorrect(hit100GoalTime, 3, Timeline.Streak3DaysMessages));
		Timeline.closeCurrentTile();
	}

	private void checkPersonalBestMilestone(int newPoint) {
		
		if(newPoint <= 1000)
			return;
		
		boolean pass = false;
		for(int i = 0; i < 2 && !pass; i++) {
			Timeline.openTile("1:00am");
			capture();
			pass = Timeline.isPersonalBestTileCorrect("1:00am", newPoint, 1000, Timeline.PersonalBestMessages);
			Timeline.closeCurrentTile();
		}
		
		Assert.assertTrue(pass, "Personal best milestone displays correctly");
	}
	
	private void checkNewTileAndProgress(int newPoint) {
		
		// check progress circle
		Timeline.dragDownTimeline();
		Assert.assertTrue(ViewUtils.isExistedView("UILabel", newPoint + ""), "Total points updated correctly");
		Timeline.dragUpTimeline();
	}

}
