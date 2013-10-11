package com.misfit.ta.ios.modelapi.homescreen;

import java.io.File;

import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ios.ViewUtils;
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
	private int currentActivity;
	
	public void e_init() {
		
		PrometheusHelper.signUp();
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
	
		Timeline.holdAndPressTile("1:00am");
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

		checkNewTileAndProgress(newPoint);
	}
	
	public void v_LostPointAlert() {
		
		Assert.assertTrue(EditTagScreen.hasPointLostAlert(), "Point lost alert shows up");
	}
	
	
	
	private void capture() {
		
		Gui.captureScreen("EditActivityFlow-" + System.nanoTime());
	}
	
	private void checkNewTileAndProgress(int newPoint) {
		
		// check tile updated and progress
		Timeline.openTile("1:00am - 1:05am");
		Timeline.openTile("1:00am");
		capture();
		Assert.assertTrue(Timeline.isActivityTileCorrect("1:00am", "1:05am", mins, newPoint, null),
				"Activity updated correctly");
		Timeline.closeCurrentTile();

		// check progress circle
		Timeline.dragDownTimeline();
		Assert.assertTrue(ViewUtils.isExistedView("UILabel", newPoint + ""), "Total points updated correctly");
		Timeline.dragUpTimeline();
	}

}
