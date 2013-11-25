package com.misfit.ta.ios.modelapi.homescreen;

import java.io.File;

import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ios.ViewUtils;
import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.gui.DefaultStrings;
import com.misfit.ta.gui.Gui;
import com.misfit.ta.gui.HomeScreen;
import com.misfit.ta.gui.PrometheusHelper;
import com.misfit.ta.gui.Sync;
import com.misfit.ta.gui.Timeline;
import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.modelAPI.ModelAPI;
import com.misfit.ta.utils.ShortcutsTyper;

public class SleepTileRemovingAPI extends ModelAPI {
	public SleepTileRemovingAPI(AutomationTest automation, File model,
			boolean efsm, PathGenerator generator, boolean weight) {
		super(automation, model, efsm, generator, weight);
	}
	private String token;
	private String currentTitle;
	private boolean isRemoved;
	// dev team hard-codes these 2 value
	private String _8hrSleepStartTimeTitle = "10:01pm";
	private String _8hrSleepEndTimeTitle = "5:59am";
	
	private int hour = 10;
	/**
	 * This method implements the Edge 'e_CheckEnd'
	 * 
	 */
	public void e_CheckEnd() {
	}


	/**
	 * This method implements the Edge 'e_Init'
	 * 
	 */
	public void e_Init() {
		String email = "milestone@qa.com";
		String password = "test12";
		Sync.signIn(email, password);
		ShortcutsTyper.delayTime(2000);
		token = MVPApi.signIn(email, password).token;
	}

	/**
	 * This method implements the Edge 'e_InputActivity'
	 * 
	 */
	public void e_InputActivity() {
		HomeScreen.tapOpenManualInput();
		HomeScreen.enterManualActivity(new String[] {"1", "00", "AM"}, 8, 600);
		HomeScreen.tapSave();
	}

	/**
	 * This method implements the Edge 'e_RemoveSleepTile'
	 * 
	 */
	public void e_RemoveSleepTile() {
		Timeline.dragUpTimelineAndHandleTutorial();
		Timeline.holdAndPressTile(currentTitle);
		Gui.touchAVIew("UILabel", DefaultStrings.RemoveSleepButton);
		isRemoved = true;
		Timeline.dragDownTimeline();
	}
	
	public void e_CancelRemoveSleepTile() {
		Timeline.dragUpTimelineAndHandleTutorial();
		Timeline.holdAndPressTile(currentTitle);
		Gui.touchAVIew("UILabel", DefaultStrings.CancelButton);
		isRemoved = false;
		Timeline.dragDownTimeline();
	}

	/**
	 * This method implements the Edge 'e_SleepAfterMidnight'
	 * 
	 */
	public void e_SleepAfterMidnight() {
		HomeScreen.tapOpenManualInput();
		String[] time = { String.format("%d", hour > 12 ? hour - 12 : hour == 0 ? 12 : hour), "00", hour < 12 ? "AM" : "PM" };
		currentTitle = String.format("%d", hour > 12 ? hour - 12 : hour == 0 ? 12 : hour) + ":01" + (hour < 12 ? "am" : "pm");
		PrometheusHelper.manualInputTime(time);
		HomeScreen.tap180MinNap();
		HomeScreen.tapSave();
		hour++;
	}

	/**
	 * This method implements the Edge 'e_SleepBeforeMidnight'
	 * 
	 */
	public void e_SleepBeforeMidnight() {
		HomeScreen.tapOpenManualInput();
		HomeScreen.tap8HourSleep();
		HomeScreen.tapSave();
		currentTitle = _8hrSleepEndTimeTitle;
	}

	/**
	 * This method implements the Edge 'e_Stay'
	 * 
	 */
	public void e_Stay() {
		// TODO:
	}

	/**
	 * This method implements the Vertex 'v_EndInput'
	 * 
	 */
	public void v_EndInput() {
		// TODO:
	}

	/**
	 * This method implements the Vertex 'v_Timeline'
	 * 
	 */
	public void v_Timeline() {
		Timeline.dragUpTimelineAndHandleTutorial();
		Assert.assertTrue(ViewUtils.isExistedView("UILabel", currentTitle), "Expected tile is dislplayed with current title " + currentTitle);
		Timeline.dragDownTimeline();
	}

	/**
	 * This method implements the Vertex 'v_Today'
	 * 
	 */
	public void v_Today() {
		PrometheusHelper.handleUpdateFirmwarePopup();
	}

	/**
	 * This method implements the Vertex 'v_UpdatedTimeline'
	 * 
	 */
	public void v_UpdatedTimeline() {
		Timeline.dragUpTimelineAndHandleTutorial();
		Assert.assertTrue(!ViewUtils.isExistedView("UILabel", currentTitle), "Tile with current title " + currentTitle + " is removed");
		Timeline.dragDownTimeline();
	}

}
