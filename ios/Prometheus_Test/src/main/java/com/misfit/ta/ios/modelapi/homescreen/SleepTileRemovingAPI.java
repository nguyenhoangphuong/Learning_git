package com.misfit.ta.ios.modelapi.homescreen;

import java.io.File;

import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ios.ViewUtils;
import com.misfit.ta.backend.api.internalapi.MVPApi;
import com.misfit.ta.backend.aut.BackendHelper;
import com.misfit.ta.gui.DefaultStrings;
import com.misfit.ta.gui.Gui;
import com.misfit.ta.gui.HomeScreen;
import com.misfit.ta.gui.PrometheusHelper;
import com.misfit.ta.gui.SleepViews;
import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.modelAPI.ModelAPI;
import com.misfit.ta.utils.ShortcutsTyper;

public class SleepTileRemovingAPI extends ModelAPI {
	public SleepTileRemovingAPI(AutomationTest automation, File model,
			boolean efsm, PathGenerator generator, boolean weight) {
		super(automation, model, efsm, generator, weight);
	}
	
	private String email;
	private String token;
	
	private static String OldestSleepTitle = "10:00am - 12:59pm";
	private static String MidSleepTitle = "1:00pm - 3:59am";
	private static String LatestSleepTitle = "5:00pm - 7:59am";
	

	public void e_init() {
		
		email = PrometheusHelper.signUpDefaultProfile();
		PrometheusHelper.handleUpdateFirmwarePopup();
		token = MVPApi.signIn(email, "qwerty1").token;
		
		// set personal best to 500pts
		BackendHelper.setPersonalBest(token, 500);
		
		// pull to refresh
		HomeScreen.pullToRefresh();
		
		// input 1 sleep and 2 naps
		HomeScreen.tapOpenManualInput();
		PrometheusHelper.manualInputTime(new String[] {"10", "00", "AM"});
		HomeScreen.tap180MinNap();
		PrometheusHelper.manualInputTime(new String[] {"1", "00", "PM"});
		HomeScreen.tap180MinNap();
		PrometheusHelper.manualInputTime(new String[] {"5", "00", "PM"});
		HomeScreen.tap180MinNap();
		HomeScreen.tapSave();
		
		// go to sleep view
		HomeScreen.tapSleepTimeline();
	}
	
	public void e_toOldestSleepOfToday() {
		
		HomeScreen.tapPreviousDayButton(2);
	}
	
	public void e_toLatestSleepOfToday() {
		
		HomeScreen.tapNextDayButton(1);
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
	
	public void v_SleepTimelineMidSleep() {

		Assert.assertTrue(ViewUtils.isExistedView("UILabel", MidSleepTitle),
				"Current sleep is second sleep of today");
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
