package com.misfit.ta.android.modelapi.settings;

import java.io.File;
import java.util.Random;

import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ta.android.AutomationTest;
import com.misfit.ta.android.Gui;
import com.misfit.ta.android.aut.DefaultStrings;
import com.misfit.ta.android.gui.HomeScreen;
import com.misfit.ta.android.gui.PrometheusHelper;
import com.misfit.ta.android.gui.Settings;
import com.misfit.ta.modelAPI.ModelAPI;
import com.misfit.ta.utils.ShortcutsTyper;

public class SleepGoalSettingsAPI extends ModelAPI {
	public SleepGoalSettingsAPI(AutomationTest automation, File model,
			boolean efsm, PathGenerator generator, boolean weight) {
		super(automation, model, efsm, generator, weight);
	}

	private int fullScreenHeight = 0;
	private int fullScreenWidth = 0;
	private int goalHr;
	private int currentGoalHr = 8;
	private int goalMin;
	private int currentGoalMin = 0;

	
	/**
	 * This method implements the Edge 'e_BackToHomeScreen'
	 * 
	 */
	public void e_BackToHomeScreen() {
		Gui.touchAView("TextView", "mText", DefaultStrings.GoalsText);
	}

	/**
	 * This method implements the Edge 'e_CancelEdit'
	 * 
	 */
	public void e_CancelEdit() {
		PrometheusHelper.dismissPopup(fullScreenHeight, fullScreenWidth,
				DefaultStrings.CancelText);
	}

	/**
	 * This method implements the Edge 'e_ChangeSleepGoal'
	 * 
	 */
	public void e_ChangeSleepGoal() {
		Random r = new Random();
		int[] minuteOptions = {0, 15, 30, 45};
		goalHr = r.nextInt(16) + 1;
		if (goalHr == 16) {
			goalMin = 0;
		} else {
			goalMin = minuteOptions[r.nextInt(4)];
		}
		System.out.println("Random hour: " + goalHr);
		System.out.println("Random minute: " + goalMin);
		
		PrometheusHelper.editSleepGoal(goalHr, goalMin, fullScreenHeight, fullScreenWidth);
	}

	/**
	 * This method implements the Edge 'e_ConfirmNewGoal'
	 * 
	 */
	public void e_ConfirmNewGoal() {
		currentGoalHr = goalHr; 
		currentGoalMin = goalMin;
		PrometheusHelper.dismissPopup(fullScreenHeight, fullScreenWidth,
				DefaultStrings.SetText);
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
		PrometheusHelper.manualInputSleep();
		ShortcutsTyper.delayTime(10000);
		PrometheusHelper.pullToRefresh(fullScreenWidth, fullScreenHeight);
		ShortcutsTyper.delayTime(6000);
	}

	/**
	 * This method implements the Edge 'e_PullToRefresh'
	 * 
	 */
	public void e_PullToRefresh() {
		PrometheusHelper.pullToRefresh(fullScreenWidth, fullScreenHeight);
		ShortcutsTyper.delayTime(5000);
	}

	/**
	 * This method implements the Edge 'e_ToSleepGoalSettings'
	 * 
	 */
	public void e_ToSleepGoalSettings() {
		HomeScreen.openDashboardMenu(fullScreenHeight, fullScreenWidth);
		ShortcutsTyper.delayTime(2000);
		Gui.setInvalidView();
		ShortcutsTyper.delayTime(5000);
		Settings.tapGoalsOnDashboard();
		ShortcutsTyper.delayTime(1000);
		Settings.tapSetSleepGoal();
	}

	/**
	 * This method implements the Vertex 'v_GoalSettingsUpdated'
	 * 
	 */
	public void v_GoalSettingsUpdated() {
		String expectedNightlyGoalSummary = String.valueOf(currentGoalHr) + " hours";
		if (currentGoalMin != 0) {
			expectedNightlyGoalSummary += " " + String.valueOf(currentGoalMin) + " mins";
		}
		String nightlyGoalSummary = Settings.getNightlyGoalSummary();
		System.out.println("Expected nightly goal summary: " + expectedNightlyGoalSummary);
		System.out.println("Nightly goal summary: " + nightlyGoalSummary);
		Assert.assertTrue(expectedNightlyGoalSummary.equals(nightlyGoalSummary));
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
		ShortcutsTyper.delayTime(5000);
		String totalSleep = HomeScreen.getTotalSleep();
		String totalSleepHour = totalSleep.substring(0, totalSleep.indexOf(":"));
		String totalSleepMintue = totalSleep.substring(totalSleep.indexOf(":") + 1, totalSleep.length());
	}

	/**
	 * This method implements the Vertex 'v_SleepGoalSettings'
	 * 
	 */
	public void v_SleepGoalSettings() {
		// TODO:
	}

	/**
	 * This method implements the Vertex 'v_SleepGoalSettingsUpdated'
	 * 
	 */
	public void v_SleepGoalSettingsUpdated() {
		
	}

}
