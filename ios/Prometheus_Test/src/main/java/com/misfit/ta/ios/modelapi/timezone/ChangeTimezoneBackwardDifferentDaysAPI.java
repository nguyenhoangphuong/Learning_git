package com.misfit.ta.ios.modelapi.timezone;

import java.io.File;
import java.util.Calendar;

import org.apache.log4j.Logger;
import org.graphwalker.Util;
import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.backend.data.goal.Goal;
import com.misfit.ta.backend.data.goal.GoalsResult;
import com.misfit.ta.gui.DefaultStrings;
import com.misfit.ta.gui.HomeScreen;
import com.misfit.ta.gui.PrometheusHelper;
import com.misfit.ta.gui.Timezone;
import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.modelAPI.ModelAPI;
import com.misfit.ta.utils.ShortcutsTyper;

public class ChangeTimezoneBackwardDifferentDaysAPI extends ModelAPI {
	protected static final Logger logger = Util
			.setupLogger(ChangeTimezoneBackwardDifferentDaysAPI.class);
	private String email = MVPApi.generateUniqueEmail();
	private int currentTimezone = 7;
	private int previousTimezone = 7;
	private int delta = 14;
	private String password = "abcdef";
	private long beforeStartTime = 0;
	private long beforeEndTime = 0;
	private long beforeOffset = 0;

	public ChangeTimezoneBackwardDifferentDaysAPI(AutomationTest automation,
			File model, boolean efsm, PathGenerator generator, boolean weight) {
		super(automation, model, efsm, generator, weight);
	}

	// edge
	public void e_init() {
		// sign up new account
		PrometheusHelper.signUp(email, password, true, 16, 9, 1991, true, "5'",
				"8\\\"", "120", ".0", 1);
		ShortcutsTyper.delayTime(1000);
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
		logger.info("Change timezone from " + this.previousTimezone + " to "
				+ this.currentTimezone);
	}

	public void e_checkEnd() {
	}

	// vertex
	public void v_HomeScreen() {
		Assert.assertTrue(HomeScreen.isToday(),
				"Current view is HomeScreen - Today");
	}

	public void v_HomeScreenUpdated() {
		Timezone.assertTimeZoneTile(DefaultStrings.TimezoneBackwardLabel,
				this.currentTimezone, this.previousTimezone, this.delta);
		// check goal start time and end time
		Timezone.assertGoal(email, password, beforeStartTime, beforeEndTime,
				delta, beforeOffset, false);
	}

	public void v_End() {
	}

	public void e_stay() {
	}
}
