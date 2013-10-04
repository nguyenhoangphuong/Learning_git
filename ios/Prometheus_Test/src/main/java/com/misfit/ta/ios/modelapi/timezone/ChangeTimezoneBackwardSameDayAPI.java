package com.misfit.ta.ios.modelapi.timezone;

import java.io.File;

import org.apache.log4j.Logger;
import org.graphwalker.Util;
import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.backend.data.goal.Goal;
import com.misfit.ta.backend.data.goal.GoalsResult;
import com.misfit.ta.gui.HomeScreen;
import com.misfit.ta.gui.PrometheusHelper;
import com.misfit.ta.gui.Timezone;
import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.modelAPI.ModelAPI;
import com.misfit.ta.utils.ShortcutsTyper;

public class ChangeTimezoneBackwardSameDayAPI extends ModelAPI {

	protected static final Logger logger = Util.setupLogger(ChangeTimezoneBackwardSameDayAPI.class);

	private String email = "";
	private String password = "qwerty1";
	private int currentTimezone = 4;
	private int previousTimezone = 4;
	private int delta = 1;
	private long beforeStartTime = 0;
	private long beforeEndTime = 0;
	private long beforeOffset = 0;

	public ChangeTimezoneBackwardSameDayAPI(AutomationTest automation, File model, boolean efsm, PathGenerator generator, boolean weight) {
		super(automation, model, efsm, generator, weight);
	}

	// edge
	public void e_init() {

		Timezone.changeTimezone(currentTimezone);
		PrometheusHelper.signUpDefaultProfile();
	}

	public void e_changeTimezone() {

		GoalsResult goalsResult = Timezone.searchGoal(email, password);
		Goal goal = goalsResult.goals[0];
		beforeEndTime = goal.getEndTime();
		beforeStartTime = goal.getStartTime();
		beforeOffset = goal.getTimeZoneOffsetInSeconds();
		this.previousTimezone = this.currentTimezone;
		this.currentTimezone = this.currentTimezone - delta;
		Timezone.changeTimezone(currentTimezone);
		logger.info("Change timezone from " + this.previousTimezone + " to " + this.currentTimezone);
	}

	public void e_checkEnd() {
	}

	// vertex
	public void v_HomeScreen() {
		
		Assert.assertTrue(HomeScreen.isToday(), "Current view is HomeScreen - Today");
	}

	public void v_HomeScreenUpdated() {
		
		// check tile
		Timezone.assertTimeZoneTile(this.currentTimezone, this.previousTimezone, this.delta, false);
		
		// check goal start time and end time
		Timezone.assertGoal(email, password, beforeStartTime, beforeEndTime, delta, beforeOffset, false);
	}

	public void v_End() {
	}

	public void e_stay() {
	}
}
