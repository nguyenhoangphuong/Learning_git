package com.misfit.ta.ios.modelapi.homescreen;

import java.io.File;
import java.util.ArrayList;
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
	private ArrayList<String> arrStartTimes = new ArrayList<String>();
	private String title;
	private String email;
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
	public void chooseActivityRandom() {
		number = PrometheusHelper.randInt(0, 5);
		int i = 5;
		if(number < i){
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
		email = PrometheusHelper.signUpDefaultProfile();
	}

	/**
	 * This method implements the Edge 'e_start'
	 * 
	 */
	public void e_start() {
		chooseActivityRandom();
		HomeScreen.tapStartButton();
		getCurrentTime();
		ShortcutsTyper.delayTime(7000);
	}

	public void getCurrentTime(){
		Calendar now = Calendar.getInstance();
		int hour = now.get(Calendar.HOUR_OF_DAY);
		int minute = now.get(Calendar.MINUTE);
		boolean isAM = true;
		if(hour > 12){
			hour -= 12;
			isAM = false;
		}
		arrStartTimes.add(String.valueOf(hour));
		arrStartTimes.add(String.valueOf(minute));
		arrStartTimes.add(isAM ? String.valueOf("AM") : String.valueOf("PM"));
		isAM = true;
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
		Timeline.dragUpTimelineAndHandleTutorial();
		title = arrStartTimes.get(0) + ":" + String.format("%02d", Integer.parseInt(arrStartTimes.get(1))) + arrStartTimes.get(2).toLowerCase();
		if(!ViewUtils.isExistedView("UILabel", title)){
			title = arrStartTimes.get(0) + ":" + String.format("%02d", Integer.parseInt(arrStartTimes.get(1)) - 1) + arrStartTimes.get(3).toLowerCase();
		}
		System.out.println("Title : " + title);
		checkTile();
	}
	
	public void checkTile(){
		Timeline.openTile(title);
		Timeline.closeCurrentTile();
		Timeline.dragDownTimeline();
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
	
	public void e_Done(){
		HomeScreen.tapDone();
	}
	
	public void v_RecordScreenResult(){
		
	}
	
	public void e_SyncLater(){
		HomeScreen.tapSyncLater();
	}
	
	public void e_trackNow(){
		HomeScreen.tapTrackNow();
	}
	
	public void v_HomeScreenAfterSignIn(){
		checkTile();
	}
	
	public void e_signOutAndSignInAgain(){
		PrometheusHelper.signOut();
		PrometheusHelper.signIn(email, "qwerty1");
	}
}
