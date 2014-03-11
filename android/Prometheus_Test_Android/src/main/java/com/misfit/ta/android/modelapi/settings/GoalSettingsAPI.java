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
	private int currentGoal = 1000;

	/**
	 * This method implements the Edge 'e_CancelEdit'
	 * 
	 */
	public void e_CancelEdit() {
		PrometheusHelper.dismissPopup(fullScreenHeight, fullScreenWidth,
				DefaultStrings.CancelText);
	}

	/**
	 * This method implements the Edge 'e_ChangeGoal'
	 * 
	 */
	public void e_ChangeGoal() {
		goal = PrometheusHelper.randInt(1, 99) * 100;
		System.out.println("******* New goal value: " + goal);
		currentGoal = HomeScreen.getCurrentGoalInPicker();
		boolean swipeDown = true;
		int steps = 0;
		int delta = Math.abs(currentGoal - goal);
		if ((goal == 100 || goal == 9900) && delta > 4900) {
			steps = (int) (9900 - delta) / 100;
			swipeDown = (goal == 9900);
		} else {
			steps = (int) (delta / 100);
			swipeDown = (goal < currentGoal);
		}
		ViewNode goalPickerNode = ViewUtils.findView("ShineCustomEditText",
				"mID", DefaultStrings.ShineCustomEditTextInPickerId, 0);
		if (swipeDown) {
			Settings.swipeDownGoalPicker(fullScreenHeight, fullScreenWidth,
					Gui.getHeight(), Gui.getWidth(), goalPickerNode, steps);
		} else {
			Settings.swipeUpGoalPicker(fullScreenHeight, fullScreenWidth,
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
		ShortcutsTyper.delayTime(4000);
		Assert.assertTrue(ViewUtils.findView("TextView", "mText",
				DefaultStrings.SyncNeededText, 0) != null
				&& ViewUtils.findView("TextView", "mText",
						DefaultStrings.SyncNeededStatementText, 0) != null, "This is not sync needed popup.");
		PrometheusHelper.dismissPopup(fullScreenHeight, fullScreenWidth,
				DefaultStrings.SyncLaterText);
		ShortcutsTyper.delayOne();
	}

	/**
	 * This method implements the Edge 'e_Init'
	 * 
	 */
	public void e_Init() {
		ShortcutsTyper.delayOne();
		PrometheusHelper.signUp();
		ShortcutsTyper.delayTime(2000);
	}

	/**
	 * This method implements the Edge 'e_ToGoalSettings'
	 * 
	 */
	public void e_ToGoalSettings() {
		fullScreenHeight = Gui.getScreenHeight();
		fullScreenWidth = Gui.getScreenWidth();
		System.out.println(fullScreenHeight);
		System.out.println(fullScreenWidth);
		HomeScreen.tapSettingsMenu();
		Settings.tapAdjustGoal();
	}
	
	/**
	 * This method implements the Edge 'e_PullToRefresh'
	 * 
	 */
	public void e_PullToRefresh() {
		Gui.swipeLeft(1);
		ShortcutsTyper.delayOne();
	}

	/**
	 * This method implements the Vertex 'v_GoalSettings'
	 * 
	 */
	public void v_GoalSettings() {
		ShortcutsTyper.delayOne();
		Assert.assertTrue(ViewUtils.findView("TextView", "mText",
				DefaultStrings.AdjustGoalText, 0) != null, "This is not adjust goal popup.");
	}

	/**
	 * This method implements the Vertex 'v_GoalUpdated'
	 * 
	 */
	public void v_GoalUpdated() {
//		Gui.getCurrentViews();
//		ShortcutsTyper.delayOne();
//		currentGoal = HomeScreen.getCurrentGoalInPicker();
//		System.out.println("******* Updated goal in picker: " + currentGoal);
//		System.out.println("******* Expected goal: " + goal);
//		Assert.assertTrue(currentGoal == goal, "Number picker is not working.");
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
		ViewNode homescreenGoal = ViewUtils.findView("TextView", "mID", DefaultStrings.GoalHomeScreenTextViewId, 0);
		String homescreenText = homescreenGoal.text;
		String goalText = homescreenText.substring(3, homescreenText.indexOf(" ", 3));
		System.out.println("Goal in homescreen: " + goalText);
		Assert.assertTrue(Integer.valueOf(goalText).equals(goal), "Goal in homescreen should be updated with the new value: " + goal);
	}

}
