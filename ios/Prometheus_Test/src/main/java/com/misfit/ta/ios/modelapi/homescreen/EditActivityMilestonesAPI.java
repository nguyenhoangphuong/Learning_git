package com.misfit.ta.ios.modelapi.homescreen;

import java.io.File;

import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ios.ViewUtils;
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
	private int point = (int)Math.floor(PrometheusHelper.calculatePoint(steps, mins, MVPEnums.ACTIVITY_WALKING));
	
	public void e_init() {
		
		// sign up
		String email = PrometheusHelper.signUp();
		
		// create 2 goals in the past
		BackendHelper.completeGoalInPast(email, "qwerty1", 1);
		BackendHelper.completeGoalInPast(email, "qwerty1", 2);
				
		// create a personal best record
		BackendHelper.setPersonalBest(email, "qwerty1", 1000);
		
		// get new data from server
		HomeScreen.pullToRefresh();
		PrometheusHelper.waitForViewToDissappear("UILabel", DefaultStrings.LoadingLabel);
	}
	
	public void e_inputFirstActivityAndFinishTutorial() {
		
		// input first activity
		HomeScreen.tapOpenManualInput();
		HomeScreen.enterManualActivity(new String[] {"1", "00", "AM"}, mins, steps);
		HomeScreen.tapSave();
		
		// handle tutorial	
		Timeline.dragUpTimelineAndHandleTutorial();		
	}
	
	public void e_holdToEditActivity() {
	
		Timeline.editTile("1:00am");
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
			String hit100GoalTime = String.format("1:%02dam", 1000/ppm);
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
			String hit150GoalTime = String.format("1:%02dam", 1500/ppm);
			Timeline.openTile(hit150GoalTime);
			capture();
			Assert.assertTrue(Timeline.isDailyGoalMilestoneTileCorrect(hit150GoalTime, 1500, Timeline.DailyGoalMessagesFor150Percent),
					"Goal 150% tile displays correctly");
			Timeline.closeCurrentTile();
		}
		
		if(newPoint >= 2000) {
			
			// check 200% tile
			String hit200GoalTime = String.format("1:%02dam", 2000/ppm);
			Timeline.openTile(hit200GoalTime);
			capture();
			Assert.assertTrue(Timeline.isDailyGoalMilestoneTileCorrect(hit200GoalTime, 2000, Timeline.DailyGoalMessagesFor200Percent),
					"Goal 200% tile displays correctly");
			Timeline.closeCurrentTile();
		}
	}

	private void checkStreakMilestone(int newPoint) {
		
		if(newPoint < 1000)
			return;
		
		float ppm = newPoint * 1f / mins;
		String hit100GoalTime = String.format("1:%02dam", 1000/ppm);
		
		// check streak tile
		boolean pass = false;
		for(int i = 0; i < 2 && !pass; i++) {

			Timeline.openTile(hit100GoalTime);
			capture();
			pass = Timeline.isStreakTileCorrect(hit100GoalTime, 3, Timeline.Streak3DaysMessages);
			Timeline.closeCurrentTile();
		}

		Assert.assertTrue(pass, "Streak Milestone tile displays correctly");
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
		
		// check tile updated and progress
		Timeline.openTile("1:00am - 1:50am");
		Timeline.openTile("1:00am");
		capture();
		Assert.assertTrue(Timeline.isActivityTileCorrect("1:00am", "1:50am", mins, newPoint, null),
				"Activity updated correctly");
		Timeline.closeCurrentTile();

		// check progress circle
		Timeline.dragDownTimeline();
		Assert.assertTrue(ViewUtils.isExistedView("UILabel", newPoint + ""), "Total points updated correctly");
		Timeline.dragUpTimeline();
	}

}
