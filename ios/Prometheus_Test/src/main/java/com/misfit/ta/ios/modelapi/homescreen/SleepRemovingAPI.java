package com.misfit.ta.ios.modelapi.homescreen;

import java.io.File;
import java.util.Calendar;

import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ios.ViewUtils;
import com.misfit.ta.gui.DefaultStrings;
import com.misfit.ta.gui.Gui;
import com.misfit.ta.gui.HomeScreen;
import com.misfit.ta.gui.PrometheusHelper;
import com.misfit.ta.gui.SleepViews;
import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.modelAPI.ModelAPI;
import com.misfit.ta.utils.ShortcutsTyper;

public class SleepRemovingAPI extends ModelAPI {
	public SleepRemovingAPI(AutomationTest automation, File model,
			boolean efsm, PathGenerator generator, boolean weight) {
		super(automation, model, efsm, generator, weight);
	}
	
	private String email;
	
	private static String OldestSleepTitle = "10:00pm - 5:25am";
	private static String LatestSleepTitle = "5:00pm - 7:49pm";
	

	public void e_init() {
		
		email = PrometheusHelper.signUpDefaultProfile();
		PrometheusHelper.handleUpdateFirmwarePopup();
		
		// input 1 sleep and 1 nap
		HomeScreen.tapOpenManualInput();
		Gui.swipeUp(5000);
		HomeScreen.tap8HourSleep();
		ShortcutsTyper.delayTime(2000);
		Gui.swipeDown(5000);
		PrometheusHelper.manualInputTime(new String[] {"5", "00", "PM"});
		Gui.swipeUp(5000);
		HomeScreen.tap180MinNap();
		ShortcutsTyper.delayTime(2000);
		HomeScreen.tapSave();
		
		// go to sleep view
		HomeScreen.tapSleepTimeline();
	}
	
	public void e_toLatestSleepOfToday() {
		
		int hourOfDay = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    	if(hourOfDay > 6 && hourOfDay < 20) { 
	    	// current view is today latest sleep
    		// do nothing
    	}
    	else {
    		// current view is tonight, back a view
    		HomeScreen.tapPreviousDayButton(1);
    	}
	}
	
	public void e_editSleep() {
		
		SleepViews.tapEditSleep();
	}
	
	public void e_removeSleep() {
		
		SleepViews.tapDeleteSleep();
	}
	
	public void e_cancelRemove() {
		
		Gui.touchPopupButton(DefaultStrings.CancelButton);
	}
	
	public void e_confirmRemove() {
		
		Gui.touchPopupButton(DefaultStrings.RemoveButton);
		ShortcutsTyper.delayTime(2000);
	}
	
	public void e_forceDeleteSleep() {
		
		SleepViews.tapEditSleep();
		SleepViews.tapDeleteSleep();
		Gui.touchPopupButton(DefaultStrings.RemoveButton);
		ShortcutsTyper.delayTime(2000);
	}
	
	public void e_signOutAndSignInAgain() {
		
		PrometheusHelper.signOut();
		PrometheusHelper.signIn(email, "qwerty1");
		ShortcutsTyper.delayTime(2000);
		HomeScreen.tapSleepTimeline();
		PrometheusHelper.waitForViewToDissappear("UILabel", DefaultStrings.LoadingEtcLabel);
	}



	public void v_SleepTimelineNoCheck() {
		
		// entry vertex
	}
	
	public void v_SleepTimelineOldestSleep() {

		Assert.assertTrue(ViewUtils.isExistedView("UILabel", OldestSleepTitle), 
				"Current sleep is first sleep of today");
	}
	
	public void v_SleepTimelineLatestSleep() {

		Assert.assertTrue(ViewUtils.isExistedView("UILabel", LatestSleepTitle),
				"Current sleep is last sleep of today");
	}
	
	public void v_SleepTimelineNoData() {
		
		Assert.assertTrue(SleepViews.isNoSleepDataView(), 
				"Current view is no sleep data view");
	}
	
	public void v_EditSleep() {
		
		Assert.assertTrue(ViewUtils.isExistedView("UILabel", DefaultStrings.RemoveSleepButton), 
				"Current view is edit sleep popup");
	}
	
	public void v_RemoveSleepConfirm() {
		
		Assert.assertTrue(SleepViews.hasRemoveSleepConfirmationAlert(), 
				"Action sheet confirmation is shown");		
	}

}
