package com.misfit.ta.ios.modelapi.homescreen;

import java.io.File;

import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ta.gui.HomeScreen;
import com.misfit.ta.gui.PrometheusHelper;
import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.modelAPI.ModelAPI;
import com.misfit.ta.utils.ShortcutsTyper;

public class DayInPastAPI extends ModelAPI {
	public DayInPastAPI(AutomationTest automation, File model, boolean efsm,
			PathGenerator generator, boolean weight) {
		super(automation, model, efsm, generator, weight);
	}

	private int days = 0;

	
	public void e_Init() {

		PrometheusHelper.handleUpdateFirmwarePopup();
	}

	public void e_BackToADayInPast() {
		
		// we're using qa account which has many days in the past
		this.days = PrometheusHelper.randInt(2, 14);
		HomeScreen.tapPreviousDayButton(this.days);
	}

	public void e_BackToYesterday() {
		
		this.days = 1;
		HomeScreen.tapPreviousDayButton(1);
	}

	public void e_SwipeBackToday() {
		
		HomeScreen.tapNextDayButton(this.days);

		// restore days to 0
		this.days = 0;
	}

	public void e_TapToday() {
		
		HomeScreen.tapArrowButtonToday();
		ShortcutsTyper.delayOne();

		// restore days to 0
		this.days = 0;
	}

	
	public void v_DayInPast() {
		
		// check date display correctly
		Assert.assertTrue(HomeScreen.isADayBefore(this.days),
				"Current view is HomeScreen - Day Before " + this.days
						+ " days");
	}

	public void v_HomeScreen() {
		
		// check if this screen is home screen - today
		Assert.assertTrue(HomeScreen.isToday(),
				"Current view is HomeScreen - Today");
	}

	public void v_Yesterday() {
		
		// check if this screen is home screen - yesterday
		Assert.assertTrue(HomeScreen.isYesterday(),
				"Current view is HomeScreen - Yesterday");
	}

}
