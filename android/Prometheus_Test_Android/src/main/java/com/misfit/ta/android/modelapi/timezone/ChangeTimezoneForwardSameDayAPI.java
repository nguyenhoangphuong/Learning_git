package com.misfit.ta.android.modelapi.timezone;

import java.io.File;

import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ta.android.AutomationTest;
import com.misfit.ta.android.gui.PrometheusHelper;
import com.misfit.ta.android.gui.Timezone;
import com.misfit.ta.backend.data.goal.Goal;
import com.misfit.ta.backend.data.goal.GoalsResult;
import com.misfit.ta.modelAPI.ModelAPI;
import com.misfit.ta.utils.ShortcutsTyper;

public class ChangeTimezoneForwardSameDayAPI extends ModelAPI {
	private String email = "";
	private String password = "test12";
	private int currentTimezone = 7;
	private int previousTimezone = 7;
	private int delta = 1;
	private long beforeStartTime = 0;
	private long beforeEndTime = 0;
	private long beforeOffset = 0;
	private int fullScreenHeight;
	private int fullScreenWidth;

	public ChangeTimezoneForwardSameDayAPI(AutomationTest automation,
			File model, boolean efsm, PathGenerator generator, boolean weight) {
		super(automation, model, efsm, generator, weight);
	}

	// edge
	public void e_init() {
		email = PrometheusHelper.generateUniqueEmail();
		ShortcutsTyper.delayTime(2000);
		PrometheusHelper.signUp(email);
		ShortcutsTyper.delayTime(2000);
		PrometheusHelper.manualInputActivity("06", "05", 5, 580);
		ShortcutsTyper.delayTime(1000);
	}

	public void e_changeTimezone() {
		GoalsResult goalsResult = Timezone.searchGoal(email, password);
		Goal goal = goalsResult.goals[0];
		beforeEndTime = goal.getEndTime();
		beforeStartTime = goal.getStartTime();
		beforeOffset = goal.getTimeZoneOffsetInSeconds();

		this.previousTimezone = this.currentTimezone;
		this.currentTimezone = this.currentTimezone + delta;

		Timezone.changeTimezone(currentTimezone);
		System.out.println("Change timezone from " + this.previousTimezone
				+ " to " + this.currentTimezone);
		PrometheusHelper.pullToRefresh(fullScreenWidth, fullScreenHeight);
	}

	// vertex
	public void v_HomeScreen() {

	}

	public void v_HomeScreenUpdated() {
		// check goal start time and end time
		Timezone.assertGoal(email, password, beforeStartTime, beforeEndTime,
				delta, beforeOffset, true);
		Timezone.changeTimezone(7);
	}
}
