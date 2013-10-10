package com.misfit.ta.ios.modelapi.homescreen;

import java.io.File;


import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ta.gui.DefaultStrings;
import com.misfit.ta.gui.EditTagScreen;
import com.misfit.ta.gui.HomeScreen;
import com.misfit.ta.gui.PrometheusHelper;
import com.misfit.ta.gui.Sync;
import com.misfit.ta.gui.Timeline;
import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.modelAPI.ModelAPI;

public class EditActivityRandomAPI extends ModelAPI {
	
	public EditActivityRandomAPI(AutomationTest automation, File model, boolean efsm,
			PathGenerator generator, boolean weight) {
		super(automation, model, efsm, generator, weight);
	}
	
	private int currentIndex = 0;
	private String[] taggedActivitiesName = new String[] {
		DefaultStrings.SwimmingLabel,
		DefaultStrings.CyclingLabel,
		DefaultStrings.BasketballLabel,
		DefaultStrings.SoccerLabel,
		DefaultStrings.TennisLabel,
	};
	
	
	public void e_init() {
		
		PrometheusHelper.signUp();
	}
	
	public void e_inputFirstActivityAndFinishTutorial() {
		
		// input first activity
		HomeScreen.tapOpenManualInput();
		HomeScreen.enterManualActivity(new String[] {"1", "00", "AM"}, 50, 5000);
		HomeScreen.tapSave();
		
		// follow tutorial
		// currently no implementation
	}
	
	public void e_holdToEditActivity() {
		
		Timeline.holdAndPressTile("1:00am");
	}
	
	public void e_changeToTaggedActivity() {
		
		int act;
		do {
			act = PrometheusHelper.randInt(0, taggedActivitiesName.length);
		}
		while(currentIndex == act);
		currentIndex = act;
		
		EditTagScreen.selectActivity(taggedActivitiesName[currentIndex]);
		EditTagScreen.tapSave();
	}

	public void e_confirmSyncAlert() {
		
		Sync.tapPopupSyncLater();
	}
	
	public void e_confirmPointLostAlert() {
		
		EditTagScreen.tapPopupChangeTag();
	}
	
	
	
	public void v_HomeScreen() {
		
		Assert.assertTrue(HomeScreen.isToday(), "Current view is HomeScreen - Today");
	}

	public void v_EditActivityTag() {
		
		Assert.assertTrue(EditTagScreen.isEditTagScreen(), "Current view is Edit Tag");
	}
	
	public void v_HomeScreenUpdated() {
		
		// TODO: lots of things to do here
	}
	
	public void v_SyncRequireAlert() {
		
		Assert.assertTrue(Sync.hasShineOutOfSyncMessage(), "Sync require alert shows up");
	}
	
	public void v_LostPointAlert() {
		
		Assert.assertTrue(EditTagScreen.hasPointLostAlert(), "Point lost alert shows up");
	}
	
}
