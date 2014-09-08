package com.misfit.ta.ios.modelapi.homescreen;

import java.io.File;
import java.util.Calendar;

import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ios.ViewUtils;
import com.misfit.ta.aut.AutomationTest;
import com.misfit.ta.gui.HomeScreen;
import com.misfit.ta.gui.HomeSettings;
import com.misfit.ta.gui.PrometheusHelper;
import com.misfit.ta.gui.Timeline;
import com.misfit.ta.modelAPI.ModelAPI;
import com.misfit.ta.utils.ShortcutsTyper;

public class TrackTaggingActivityAPI extends ModelAPI {

	public TrackTaggingActivityAPI(AutomationTest automation, File model,
			boolean efsm, PathGenerator generator, boolean weight) {
		super(automation, model, efsm, generator, weight);
	}

	private int number = 0;
	private String times = "";
	/**
	 * This method implements the Edge 'e_back'
	 * 
	 */
	public void e_back() {
		HomeSettings.tapBack();
	}

	/**
	 * This method implements the Edge 'e_cancel'
	 * 
	 */
	public void e_cancel() {
		HomeSettings.tapCancel();
	}

	/**
	 * This method implements the Edge 'e_chooseActivityRandom'
	 * 
	 */
	public void e_chooseActivityRandom() {
		number = PrometheusHelper.randInt(0, 5);
		int i = 5;
		if(number < 5){
			do{
				HomeScreen.tapBackwardActivity();
				i--;
			}while(number == i);
		}
	}

	/**
	 * This method implements the Edge 'e_goToActivityScreen'
	 * 
	 */
	public void e_goToActivityScreen() {
		HomeScreen.tapTrackNow();
	}

	/**
	 * This method implements the Edge 'e_goToTaggingActivityScreen'
	 * 
	 */
	public void e_goToTaggingActivityScreen() {
		HomeScreen.tapAddTrackingActivity();
	}

	/**
	 * This method implements the Edge 'e_init'
	 * 
	 */
	public void e_init() {
		PrometheusHelper.signUpDefaultProfile();
	}

	/**
	 * This method implements the Edge 'e_start'
	 * 
	 */
	public void e_start() {
		HomeScreen.tapStartButton();
		Calendar now = Calendar.getInstance();
		
	}

	/**
	 * This method implements the Edge 'e_stop'
	 * 
	 */
	public void e_stop() {
		HomeScreen.tapDone();
	}

	/**
	 * This method implements the Vertex 'v_ActivityScreen'
	 * 
	 */
	public void v_ActivityScreen() {
		Assert.assertTrue(HomeScreen.isActivityDialog(),
				"Current View is Activity Dialog");
	}

	/**
	 * This method implements the Vertex 'v_ActivityScreenCheck'
	 * 
	 */
	public void v_ActivityScreenCheck() {
		Assert.assertTrue(ViewUtils.isExistedView("UILabel", LogActivityAPI.arrActivities[number]), "It's not correct activity!");
	}

	/**
	 * This method implements the Vertex 'v_HomeScreen'
	 * 
	 */
	public void v_HomeScreen() {
		Assert.assertTrue(HomeScreen.isHomeScreen(),
				"Current View is HomeScreen");
	}

	/**
	 * This method implements the Vertex 'v_HomeScreenUpdated'
	 * 
	 */
	public void v_HomeScreenUpdated() {
		ShortcutsTyper.delayTime(2000);
		Timeline.dragUpTimelineAndHandleTutorial();
		
	}

	/**
	 * This method implements the Vertex 'v_RecordResultScreen'
	 * 
	 */
	public void v_RecordResultScreen() {
	}

	/**
	 * This method implements the Vertex 'v_RecordScreen'
	 * 
	 */
	public void v_RecordScreen() {
	}

	/**
	 * This method implements the Vertex 'v_TaggingActivityScreen'
	 * 
	 */
	public void v_TaggingActivityScreen() {
		Assert.assertTrue(HomeScreen.isDialogTracking(),
				"Current View is Tagging Activity Screen");
	}
	
	public void e_SyncLater(){
		HomeScreen.tapSyncLater();
	}

}
