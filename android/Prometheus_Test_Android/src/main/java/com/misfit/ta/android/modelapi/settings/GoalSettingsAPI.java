package com.misfit.ta.android.modelapi.settings;

import java.io.File;

import org.testng.Assert;

import org.graphwalker.generators.PathGenerator;

import com.misfit.ta.android.AutomationTest;
import com.misfit.ta.android.Gui;
import com.misfit.ta.android.ViewUtils;
import com.misfit.ta.android.aut.DefaultStrings;
import com.misfit.ta.android.gui.HomeScreen;
import com.misfit.ta.android.gui.PrometheusHelper;
import com.misfit.ta.android.gui.Settings;
import com.misfit.ta.android.hierarchyviewer.scene.ViewNode;
import com.misfit.ta.modelAPI.ModelAPI;
import com.misfit.ta.utils.ShortcutsTyper;

public class GoalSettingsAPI extends ModelAPI {
	public GoalSettingsAPI(AutomationTest automation, File model, boolean efsm,
			PathGenerator generator, boolean weight) {
		super(automation, model, efsm, generator, weight);
	}

	private int fullScreenHeight = 0;
	private int fullScreenWidth = 0;
	private int goal = 1000;
	private int currentGoal = -1;
	private boolean isEdited = false;
	private boolean isFirstTime = true;
	/**
	 * This method implements the Edge 'e_CancelEdit'
	 * 
	 */
	public void e_CancelEdit() {
		PrometheusHelper.dismissPopup(fullScreenHeight, fullScreenWidth,
				DefaultStrings.CancelText);
		isEdited = false;
	}

	/**
	 * This method implements the Edge 'e_ChangeGoal'
	 * 
	 */
	public void e_ChangeGoal() {
		goal = PrometheusHelper.randInt(1, 20) * 100;
		System.out.println("******* New goal value: " + goal);
		int goalInPicker = HomeScreen.getCurrentGoalInPicker();
		boolean swipeDown = true;
		int steps = 0;
		int delta = Math.abs(goalInPicker - goal);
		if ((goal == 100 || goal == 9900) && delta > 4900) {
			steps = (int) (9900 - delta) / 100;
			swipeDown = (goal == 9900);
		} else {
			steps = (int) (delta / 100);
			swipeDown = (goal < goalInPicker);
		}
		ViewNode goalPickerNode = ViewUtils.findView("ShineCustomEditText",
				"mID", DefaultStrings.ShineCustomEditTextInPickerId, 0);
		if (swipeDown) {
			Settings.swipeDownValuePicker(fullScreenHeight, fullScreenWidth,
					Gui.getHeight(), Gui.getWidth(), goalPickerNode, steps);
		} else {
			Settings.swipeUpValuePicker(fullScreenHeight, fullScreenWidth,
					Gui.getHeight(), Gui.getWidth(), goalPickerNode, steps);
		}
	}

	/**
	 * This method implements the Edge 'e_ConfirmNewGoal'
	 * 
	 */
	public void e_ConfirmNewGoal() {
		PrometheusHelper.dismissPopup(fullScreenHeight, fullScreenWidth,
				DefaultStrings.SetText);
		isEdited = currentGoal != goal;
		currentGoal = goal;
		ShortcutsTyper.delayTime(4000);
	}

	/**
	 * This method implements the Edge 'e_Init'
	 * 
	 */
	public void e_Init() {
		ShortcutsTyper.delayOne();
		PrometheusHelper.signUp();
		ShortcutsTyper.delayTime(2000);
		fullScreenHeight = Gui.getScreenHeight();
		fullScreenWidth = Gui.getScreenWidth();
		System.out.println(fullScreenHeight);
		System.out.println(fullScreenWidth);
		if (isFirstTime) {
			Gui.touchAView("TextView", "mID", DefaultStrings.FirmwareUpdateDismissButtonId);
			isFirstTime = false;
		}
		int duration = PrometheusHelper.randInt(5, 9);
		int steps = duration * PrometheusHelper.randInt(10, 180);
		PrometheusHelper.manualInputActivity("06", "05", duration, steps);
		ShortcutsTyper.delayTime(6000);
		PrometheusHelper.pullToRefresh(fullScreenWidth, fullScreenHeight);
	}

	/**
	 * This method implements the Edge 'e_ToGoalSettings'
	 * 
	 */
	public void e_ToGoalSettings() {
		HomeScreen.openDashboardMenu(fullScreenHeight, fullScreenWidth);
		ShortcutsTyper.delayTime(2000);
		Gui.setInvalidView();
		ShortcutsTyper.delayTime(5000);
		Settings.tapGoalsOnDashboard();
		ShortcutsTyper.delayTime(500);
		Settings.tapSetActivityGoal();
	}

	/**
	 * This method implements the Edge 'e_PullToRefresh'
	 * 
	 */
	public void e_PullToRefresh() {
		PrometheusHelper.pullToRefresh(fullScreenWidth, fullScreenHeight);
		ShortcutsTyper.delayOne();
		isEdited = false;
	}

	/**
	 * This method implements the Vertex 'v_ActivityGoalSettings'
	 * 
	 */
	public void v_ActivityGoalSettings() {
		ShortcutsTyper.delayTime(3000);
		Assert.assertTrue(ViewUtils.findView("TextView", "mText",
				DefaultStrings.AdjustGoalText, 0) != null,
				"This is not adjust goal popup.");
	}

	/**
	 * This method implements the Vertex 'v_ActivityGoalSettingUpdated'
	 * 
	 */
	public void v_ActivityGoalSettingsUpdated() {
	}

	/**
	 * This method implements the Vertex 'v_GoalSettingUpdated'
	 * 
	 */
	public void v_GoalSettingsUpdated() {
	}

	/**
	 * This method implements the Vertex 'v_HomeScreen'
	 * 
	 */
	public void v_HomeScreen() {
		// TODO:
	}

	/**
	 * This method implements the Vertex 'v_HomeScreenUpdated'
	 * 
	 */
	public void v_HomeScreenUpdated() {
		if (isEdited) {
			ShortcutsTyper.delayTime(5000);
			Assert.assertTrue(
					ViewUtils.findView("TextView", "mText",
							DefaultStrings.SyncNeededText, 0) != null
							&& ViewUtils.findView("TextView", "mText",
									DefaultStrings.SyncNeededStatementText, 0) != null,
					"This is not sync needed popup.");
			PrometheusHelper.dismissPopup(fullScreenHeight, fullScreenWidth,
					DefaultStrings.SyncLaterText);
			ShortcutsTyper.delayOne();
		}
		
		if (currentGoal != -1) {
			ViewNode homescreenGoal = ViewUtils.findView("TextView", "mID",
					DefaultStrings.GoalHomeScreenTextViewId, 0);
			String homescreenText = homescreenGoal.text;
			String goalText = homescreenText.substring(3,
					homescreenText.indexOf(" ", 3));
			System.out.println("Goal in homescreen: " + goalText);
			Assert.assertTrue(Integer.valueOf(goalText).equals(currentGoal),
					"Goal in homescreen should be updated with the new value: "
							+ goal);
			verifyActivityProgress();
		}
		
	}

	public void e_BackToHomeScreen() {
		Gui.touchAView("TextView", "mText", DefaultStrings.GoalsText);
	}
	
	private void verifyActivityProgress() {
		ShortcutsTyper.delayOne();
		HomeScreen.tapDebug();
		String[] values = HomeScreen.getDebugValues();
		String activityProgress = values[1];
		System.out.println("******* Calculated activity progress " + activityProgress + "%");
		ViewNode currentPointNode = ViewUtils.findView("TextView", "mID",
				DefaultStrings.PointsHomeScreenTextViewId, 0);
		int currentPoint = Integer.valueOf(currentPointNode.text);
		float progress = (1f * currentPoint) / currentGoal;
		System.out.println("******* Expected activity progress (before flooring): " + (progress * 100f) + "%");
		HomeScreen.tapDebug();
		Integer floorProgress = (int) (Math.floor(progress * 100f));
		Assert.assertTrue(Integer.valueOf(activityProgress).equals(floorProgress), "Progress is calculated properly");
	}

}
