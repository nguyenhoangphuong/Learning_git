package com.misfit.ta.ios.modelapi.homescreen;

import java.io.File;

import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ios.ViewUtils;
import com.misfit.ta.common.MVPCalculator;
import com.misfit.ta.common.MVPEnums;
import com.misfit.ta.gui.EditTagScreen;
import com.misfit.ta.gui.Gui;
import com.misfit.ta.gui.HomeScreen;
import com.misfit.ta.gui.HomeSettings;
import com.misfit.ta.gui.PrometheusHelper;
import com.misfit.ta.gui.Timeline;
import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.modelAPI.ModelAPI;
import com.misfit.ta.utils.ShortcutsTyper;

public class EditActivityFlowByChangeActivityTypeAPI extends ModelAPI {

	public static final String[] arrActivities = new String[] { "Basketball",
		"Tennis", "Swimming", "Soccer", "Cycling", "Running", "Yoga",
		"Dancing" };
	
	public EditActivityFlowByChangeActivityTypeAPI(AutomationTest automation, File model,
			boolean efsm, PathGenerator generator, boolean weight) {
		super(automation, model, efsm, generator, weight);
	}

	private int steps = 500;
	private int mins = 5;
	private int currentActivity;

	public void e_init() {
		PrometheusHelper.signUpDefaultProfile();
	}

	public void e_inputFirstActivity() {
		// input first activity
		HomeScreen.tapOpenManualInput();
		HomeScreen.enterManualActivity(new String[] { "1", "00", "AM" }, mins,
				steps);
		HomeScreen.tapSave();
	}

	public void e_dragUpTimeline() {
		Timeline.dragUpTimeline();
	}

	public void e_holdToEditActivity() {
		Timeline.openTile("1:00am");
		Gui.touchATaggedView("UIButton", 1000);
		ShortcutsTyper.delayTime(2000);
		HomeSettings.tapOKAtNewGoalPopup();
	}

	public void e_changeToSwimming() {
		currentActivity = MVPEnums.ACTIVITY_SWIMMING;
		changeActivity("Swimming");
		EditTagScreen.tapSave();
	}

	public void changeActivity(String activity){
		for(int i = 0; i < 6; i++){
			HomeScreen.tapBackwardActivity();
		}
		
		while(!HomeScreen.isTrueActivity(activity)){
			HomeScreen.tapForwardActivity();
		}
	}
	
	public void e_changeToCycling() {
		currentActivity = MVPEnums.ACTIVITY_CYCLING;
		changeActivity("Cycling");
		EditTagScreen.tapSave();
	}

	public void e_changeToSoccer() {
		currentActivity = MVPEnums.ACTIVITY_SOCCER;
		changeActivity("Soccer");
		EditTagScreen.tapSave();
	}

	public void e_changeToTennis() {
		currentActivity = MVPEnums.ACTIVITY_TENNIS;
		changeActivity("Tennis");
		EditTagScreen.tapSave();
	}

	public void e_changeToBasketBall() {
		currentActivity = MVPEnums.ACTIVITY_BASKETBALL;
		changeActivity("Basketball");
		EditTagScreen.tapSave();
	}

	public void e_changeToDefault() {
		currentActivity = MVPEnums.ACTIVITY_WALKING;
		changeActivity("Default");
		EditTagScreen.tapSave();
	}

	public void v_HomeScreen() {
		Assert.assertTrue(HomeScreen.isToday(),
				"Current view is HomeScreen - Today");
	}

	public void v_EditActivityTag() {
		Assert.assertTrue(EditTagScreen.isEditTagScreen(),
				"Current view is Edit Tag");
	}

	public void v_HomeScreenUpdated() {
		int newPoint = (int) Math.floor(MVPCalculator.calculatePointForNewTag(
				steps, mins, currentActivity));
		System.out.println("New point : " + newPoint);
		checkNewTileAndProgress(newPoint);
	}

	private void capture() {
		Gui.captureScreen("EditActivityFlow-" + System.nanoTime());
	}

	private void checkNewTileAndProgress(int newPoint) {

		// check tile updated and progress
		Timeline.openTile("1:00am - 1:05am");
		Timeline.openTile("1:00am");
		capture();
		Assert.assertTrue(Timeline.isActivityTileCorrect("1:00am", "1:05am",
				mins, newPoint, null), "Activity updated correctly");
		Timeline.closeCurrentTile();

		// check progress circle
		Timeline.dragDownTimeline();
		Assert.assertTrue(ViewUtils.isExistedView("UILabel", newPoint + ""),
				"Total points updated correctly");
		Timeline.dragUpTimeline();
	}

}
