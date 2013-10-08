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

public class EditActivityIntegrationAPI extends ModelAPI {
	
	public EditActivityIntegrationAPI(AutomationTest automation, File model, boolean efsm,
			PathGenerator generator, boolean weight) {
		super(automation, model, efsm, generator, weight);
	}
	
	
	public void e_init() {
		
		PrometheusHelper.signUp();
	}
	
	public void e_inputFirstActivity() {
		
		// input first activity
		HomeScreen.tapOpenManualInput();
		HomeScreen.enterManualActivity(new String[] {"1", "00", "AM"}, 50, 5000);
		HomeScreen.tapSave();
	}
	
	public void e_followTutorial() {
		
		// currently nothing
	}
	
	public void e_holdToEditActivity() {
	
		Timeline.editTile("1:00am");
	}
	
	public void e_cancelEdit() {
		
		EditTagScreen.tapBack();
	}
	
	public void e_changeToTaggedActivity() {
		// TODO
	}
	
	public void e_changeToDefault() {
		
		EditTagScreen.selectActivity(DefaultStrings.DefaultLabel);
		EditTagScreen.tapSave();
	}
	
	public void e_confirmSyncAlert() {
		
		Sync.tapPopupSyncLater();
	}
	
	public void e_confirmPointLostAlert() {
		
		EditTagScreen.tapPopupChangeTag();
	}
	
	public void e_cancelPointLostAlert() {
		
		EditTagScreen.tapPopupCancel();
	}
	
	
	
	public void v_HomeScreen() {
		
		Assert.assertTrue(HomeScreen.isToday(), "Current view is HomeScreen - Today");
	}
	
	public void v_ActivityTutorial() {
		// TODO
	}
	
	public void v_EditActivityTag() {
		
		Assert.assertTrue(EditTagScreen.isEditTagScreen(), "Current view is Edit Tag");
	}
	
	public void v_HomeScreenUpdated() {
		// TODO
	}
	
	public void v_SyncRequireAlert() {
		
		Assert.assertTrue(Sync.hasShineOutOfSyncMessage(), "Sync require alert shows up");
	}
	
	public void v_LostPointAlert() {
		
		Assert.assertTrue(EditTagScreen.hasPointLostAlert(), "Point lost alert shows up");
	}
	
}
