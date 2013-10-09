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

public class EditActivityFlowAPI extends ModelAPI {
	
	public EditActivityFlowAPI(AutomationTest automation, File model, boolean efsm,
			PathGenerator generator, boolean weight) {
		super(automation, model, efsm, generator, weight);
	}
	
	private int steps = 500;
	private int mins = 5;
	private int rawPoint = (int)Math.floor(PrometheusHelper.calculatePoint(steps, mins, MVPEnums.ACTIVITY_WALKING));
	private int point = rawPoint;
	private int currentActivity;
	private boolean checkedPersonalBest = false;
	
	public void e_init() {
		
		// sign up
		String email = PrometheusHelper.signUp();
		
		// create 2 goals in the past
		BackendHelper.completeGoalInPast(email, "qwerty1", 1);
		BackendHelper.completeGoalInPast(email, "qwerty1", 2);
				
		// create a personal best record
		BackendHelper.setPersonalBest(email, "qwerty1", 2500);
		
		// get new data from server
		HomeScreen.pullToRefresh();
		PrometheusHelper.waitForViewToDissappear("UILabel", DefaultStrings.LoadingLabel);
		
	}
	
	public void e_inputFirstActivity() {
		
		// input first activity
		HomeScreen.tapOpenManualInput();
		HomeScreen.enterManualActivity(new String[] {"1", "00", "AM"}, mins, steps);
		HomeScreen.tapSave();
	}
	
	public void e_dragUpTimeline() {
		
		Timeline.dragUpTimeline();
	}
	
	public void e_followTutorial() {
		
		PrometheusHelper.handleTagEditingTutorial();
	}
	
	public void e_holdToEditActivity() {
	
		Timeline.editTile("1:00am");
	}
	
	public void e_changeToSwimming() {

		currentActivity = MVPEnums.ACTIVITY_SWIMMING;
		EditTagScreen.selectActivity(DefaultStrings.SwimmingLabel);
		EditTagScreen.tapSave();
	}
	
	public void e_changeToCycling() {

		currentActivity = MVPEnums.ACTIVITY_CYCLING;
		EditTagScreen.selectActivity(DefaultStrings.CyclingLabel);
		EditTagScreen.tapSave();
	}
	
	public void e_changeToSoccer() {

		currentActivity = MVPEnums.ACTIVITY_SOCCER;
		EditTagScreen.selectActivity(DefaultStrings.SoccerLabel);
		EditTagScreen.tapSave();
	}
	
	public void e_changeToTennis() {

		currentActivity = MVPEnums.ACTIVITY_TENNIS;
		EditTagScreen.selectActivity(DefaultStrings.TennisLabel);
		EditTagScreen.tapSave();
	}
	
	public void e_changeToBasketBall() {

		currentActivity = MVPEnums.ACTIVITY_BASKETBALL;
		EditTagScreen.selectActivity(DefaultStrings.BasketballLabel);
		EditTagScreen.tapSave();
	}
	
	public void e_changeToDefault() {
		
		currentActivity = MVPEnums.ACTIVITY_WALKING;
		EditTagScreen.selectActivity(DefaultStrings.DefaultLabel);
		EditTagScreen.tapSave();
	}
	
	public void e_confirmSyncAlert() {
		
		Sync.tapPopupSyncLater();
	}
	
	public void e_confirmPointLostAlert() {
		
		EditTagScreen.tapPopupChangeTag();
	}
	
	
	
	public void v_HomeScreen() {
		
		Assert.assertTrue(HomeScreen.isToday(), "Current view is HomeScreen - Today");
	}
	
	public void v_ActivityTutorial() {
		
		Assert.assertTrue(HomeScreen.isEditTagTutorial(), "Current view is HomeScreen - Edit Tag tutorial");
	}
	
	public void v_EditActivityTag() {
		
		Assert.assertTrue(EditTagScreen.isEditTagScreen(), "Current view is Edit Tag");
	}
	
	public void v_SyncRequireAlert() {
		
		Assert.assertTrue(Sync.hasShineOutOfSyncMessage(), "Sync require alert shows up");
	}

	public void v_HomeScreenUpdated() {

		int newPoint = (int) Math.floor(MVPCalculator.calculatePointForNewTag(steps, mins, currentActivity));

		checkDailyGoalMilestone(newPoint);
	}
	
	public void v_LostPointAlert() {
		
		Assert.assertTrue(EditTagScreen.hasPointLostAlert(), "Point lost alert shows up");
	}
	
	
	
	private void capture() {
		
		Gui.captureScreen("EditActivityFlow-" + System.nanoTime());
	}
	
	private void checkDailyGoalMilestone(int newPoint) {
		
		float ppm = newPoint * 1f / mins;
		
		// check daily goal milestones
		if(newPoint >= 1000) {
			
			// check hit goal animation
			Assert.assertTrue(ViewUtils.isExistedView("UILabel", "GOAL"), "Goal animation plays");
			Timeline.dragUpTimeline();
			
			// check 100% tile
			String hit100GoalTime = String.format("1:%02dam", 1000/ppm);
			Timeline.openTile(hit100GoalTime);
			capture();
			Assert.assertTrue(Timeline.isDailyGoalMilestoneTileCorrect(hit100GoalTime, 1000, Timeline.DailyGoalMessagesFor100Percent),
					"Goal 100% tile displays correctly");
			Timeline.closeCurrentTile(); 
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
		
		if(newPoint <= point)
			return;
		
		float ppm = newPoint * 1f / mins;
		String hit100GoalTime = String.format("1:%02dam", 1000/ppm);
		
		for(int i = 0; i < 10; i++) {
			
			Timeline.openTile(hit100GoalTime);
			capture();
			if(Timeline.isStreakTileCorrect(hit100GoalTime, 3, Timeline.Streak3DaysMessages)) {
				Timeline.closeCurrentTile();
				return;
			}
			Timeline.closeCurrentTile();
		}

		Assert.fail("No streak milestone tile");
	}

	private void checkPersonalBestMilestone(int newPoint) {
		
		if(checkedPersonalBest || newPoint <= 1000)
			return;
		
		Timeline.openTile("1:00am");
		capture();
		Assert.assertTrue(Timeline.isPersonalBestTileCorrect("1:00am", newPoint, 1000, Timeline.PersonalBestMessages),
				"PersonalBest tile displays correctly");
		Timeline.closeCurrentTile();
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
