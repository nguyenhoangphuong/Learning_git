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
	/**
	 * This method implements the Edge 'e_BackToADayInPast'
	 * 
	 */
	public void e_BackToADayInPast() {
		// we're using qa account which has many days in the past
		this.days = PrometheusHelper.randInt(2, 31);
		HomeScreen.goToPreviousDays(this.days);
		ShortcutsTyper.delayTime(5000);
		
	}

	/**
	 * This method implements the Edge 'e_BackToYesterday'
	 * 
	 */
	public void e_BackToYesterday() {
		this.days = 1;
		HomeScreen.goToPreviousDays(1);
		ShortcutsTyper.delayTime(1000);
	}

	/**
	 * This method implements the Edge 'e_Init'
	 * 
	 */
	public void e_Init() {
		ShortcutsTyper.delayTime(100000);
	}

	/**
	 * This method implements the Edge 'e_SwipeBack'
	 * 
	 */
	public void e_SwipeBackToday() {
		HomeScreen.goToNextDays(this.days);
		ShortcutsTyper.delayTime(5000);
	}

	/**
	 * This method implements the Edge 'e_TapToday'
	 * 
	 */
	public void e_TapToday() {
		HomeScreen.tapToday();
		ShortcutsTyper.delayTime(1000);
	}

	/**
	 * This method implements the Vertex 'v_DayInPast'
	 * 
	 */
	public void v_DayInPast() {
		Assert.assertTrue(HomeScreen.isADayBefore(this.days), "Current view is HomeScreen - Day Before " + this.days + " days");
	}

	/**
	 * This method implements the Vertex 'v_HomeScreen'
	 * 
	 */
	public void v_HomeScreen() {
		 // check if this screen is home screen
    	Assert.assertTrue(HomeScreen.isToday(), "Current view is HomeScreen - Today");
	}

	/**
	 * This method implements the Vertex 'v_Yesterday'
	 * 
	 */
	public void v_Yesterday() {
		Assert.assertTrue(HomeScreen.isYesterday(), "Current view is HomeScreen - Yesterday");
	}

}
