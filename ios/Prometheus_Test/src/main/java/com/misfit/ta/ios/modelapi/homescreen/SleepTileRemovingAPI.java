package com.misfit.ta.ios.modelapi.homescreen;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ios.ViewUtils;
import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.backend.data.timeline.TimelineItem;
import com.misfit.ta.backend.data.timeline.timelineitemdata.TimelineItemDataBase;
import com.misfit.ta.gui.DefaultStrings;
import com.misfit.ta.gui.Gui;
import com.misfit.ta.gui.HomeScreen;
import com.misfit.ta.gui.PrometheusHelper;
import com.misfit.ta.gui.Timeline;
import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.modelAPI.ModelAPI;

public class SleepTileRemovingAPI extends ModelAPI {
	public SleepTileRemovingAPI(AutomationTest automation, File model,
			boolean efsm, PathGenerator generator, boolean weight) {
		super(automation, model, efsm, generator, weight);
	}
	
	private String email;
	private String currentTitle;
	private int sleepTileCount = 0;
	private List<String> allSleepTileTitles = new ArrayList<String>();
	

	public void e_init() {
		
		email = PrometheusHelper.signUpDefaultProfile();
	}
	
	public void e_inputSleep() {
		
		currentTitle = "5:59am ";
		sleepTileCount++;
		allSleepTileTitles.add(currentTitle);
		
		HomeScreen.tapOpenManualInput();
		HomeScreen.tap8HourSleep();
		HomeScreen.tapSave();
		Timeline.dragUpTimelineAndHandleTutorial();
	}
	
	public void e_inputNap() {
		
		currentTitle = "5:00pm ";
		sleepTileCount++;
		allSleepTileTitles.add(currentTitle);
		
		HomeScreen.tapOpenManualInput();
		PrometheusHelper.manualInputTime(new String[] {"2", "00", "PM"});
		HomeScreen.tap180MinNap();
		HomeScreen.tapSave();
		Timeline.dragUpTimelineAndHandleTutorial();
	}
	
	public void e_removeSleepTile() {
		
		Timeline.holdAndPressTile(currentTitle);
	}
	
	public void e_cancelRemove() {
		
		Gui.touchAVIew("UILabel", DefaultStrings.CancelButton);
	}
	
	public void e_confirmRemove() {
		
		Gui.touchAVIew("UILabel", DefaultStrings.RemoveSleepButton);
	}
	
	public void e_signOutAndSignInAgain() {
		
		PrometheusHelper.signOut();
		PrometheusHelper.signIn(email, "qwerty1");
		Timeline.dragUpTimelineAndHandleTutorial();
	}



	public void v_HomeScreen() {
		
		Assert.assertTrue(HomeScreen.isToday(), "Current view is HomeScreen - Today");
	}
	
	public void v_HomeScreenWithSleepTile() {
		
		Assert.assertTrue(ViewUtils.isExistedView("UILabel", currentTitle), "Current sleep tile is existed");
	}
	
	public void v_RemoveSleepConfirm() {
		
		Assert.assertTrue(ViewUtils.isExistedView("UILabel", DefaultStrings.RemoveSleepButton) &&
				ViewUtils.isExistedView("UILabel", DefaultStrings.CancelButton), 
				"Action sheet confirmation is shown");		
	}
	
	public void v_HomeScreenAfterRemoveSleep() {
		
		// make sure the no sleep tile is displayed on app
		Assert.assertTrue(!ViewUtils.isExistedView("UILabel", currentTitle), "There's no sleep tile");
	}
	
	public void v_HomeScreenAfterSignInAgain() {

		for(String title : allSleepTileTitles)
			Assert.assertTrue(!ViewUtils.isExistedView("UILabel", title), "There's no sleep tile");
		
		// make sure the tile is stored on server with state = deleted
		List<TimelineItem> items = MVPApi.getTimelineItems(MVPApi.signIn(email, "qwerty1").token, 0, Integer.MAX_VALUE, 0);
		int numberOfDeletedSleepTile = 0;
		for(TimelineItem item : items) {
			if(item.getItemType().equals(TimelineItemDataBase.TYPE_SLEEP) && item.getState() != null && item.getState() == 1)
				numberOfDeletedSleepTile++;
		}
		
		Assert.assertEquals(numberOfDeletedSleepTile, sleepTileCount, "Number of items with state = 1 (deleted) on server");
	}

}
