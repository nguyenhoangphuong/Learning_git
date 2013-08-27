package com.misfit.ta.ios.modelapi.timezone;

import java.io.File;
import org.apache.log4j.Logger;
import org.graphwalker.Util;
import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ios.ViewUtils;
import com.misfit.ta.modelAPI.ModelAPI;
import com.misfit.ta.utils.ShortcutsTyper;
import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.gui.DefaultStrings;
import com.misfit.ta.gui.Gui;
import com.misfit.ta.gui.HomeScreen;
import com.misfit.ta.gui.PrometheusHelper;
import com.misfit.ta.gui.Timezone;
import com.misfit.ta.ios.AutomationTest;

public class ChangeTimezoneForwardSameDayAPI extends ModelAPI {
	protected static final Logger logger = Util.setupLogger(ChangeTimezoneForwardSameDayAPI.class);
	private String email = MVPApi.generateUniqueEmail();
	private int currentTimezone = 7;
	private int previousTimezone = 7;
	private int delta = 1;

	public ChangeTimezoneForwardSameDayAPI(AutomationTest automation, File model, boolean efsm, PathGenerator generator, boolean weight) {
		super(automation, model, efsm, generator, weight);
	}

	// edge
	public void e_init() {
		// sign up new account
		PrometheusHelper.signUp(email, "qwerty1", true, 16, 9, 1991, true, "5'", "8\\\"", "120", ".0", 1);
		ShortcutsTyper.delayTime(1000);
	}

	public void e_changeTimezone() {
		this.previousTimezone = this.currentTimezone;
		this.currentTimezone = this.currentTimezone + delta;
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
		Gui.dragUpTimeline();
		ShortcutsTyper.delayOne();
		// check if there is a time travel tile
		String label = "UTC+" + String.valueOf(this.currentTimezone);
		Assert.assertTrue(ViewUtils.isExistedView("UILabel", label), "Time travel tile's title is visible");

		// tap on time travel tile and check content
		Gui.dragUpTimeline();
		Timezone.touchTimezoneWithLabel(label);
		String content = "UTC+" + String.valueOf(this.previousTimezone) + " to UTC+" + String.valueOf(this.currentTimezone);
		Assert.assertTrue(ViewUtils.isExistedView("UILabel", content), "Time travel detail title is valid");
		Assert.assertTrue(ViewUtils.isExistedView("UILabel", DefaultStrings.TimezoneForwardLabel), "Time travel message is valid");
		ShortcutsTyper.delayOne();
		// close the time travel tile
		Timezone.closeTimeTravelTile();
		ShortcutsTyper.delayOne();
		Gui.dragDownTimeline();
		ShortcutsTyper.delayOne();
	}

	public void v_End() {
	}
	
	public void e_stay() {
	}
}
