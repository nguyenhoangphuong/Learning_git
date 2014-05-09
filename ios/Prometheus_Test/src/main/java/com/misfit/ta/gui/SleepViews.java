package com.misfit.ta.gui;

import com.misfit.ios.ViewUtils;

public class SleepViews {

	public static boolean isEditSleepView() {
		
		return ViewUtils.isExistedView("UILabel", DefaultStrings.EditSleepTitleLabel);
	}
	
	public static boolean isNoSleepDataView() {
		
		return ViewUtils.isExistedView("UILabel", DefaultStrings.NoSleepDataLabel);
	}
	
	public static boolean isSyncToSeeSleepView() {
		
		return ViewUtils.isExistedView("UILabel", DefaultStrings.SyncToSeeSleepLabel);
	}

	public static boolean isTonightUtilitiesView() {
		
		return ViewUtils.isExistedView("UILabel", DefaultStrings.TonightLabel);
	}
	
	
	public static boolean hasRemoveSleepConfirmationAlert() {
		
		return PrometheusHelper.hasAlert(DefaultStrings.RemoveSleepConfirmationAlertMessage, 
				DefaultStrings.RemoveSleepConfirmationAlertTitle);
	}
	
	public static boolean hasEditSleepButton() {
		
		return ViewUtils.isExistedView("UIButton", DefaultStrings.EditSleepButtonTag);
	}
	
	
	public static void tapEditSleep() {
		
		Gui.touchAVIew("UIButton", DefaultStrings.EditSleepButtonTag);
	}
	
	public static void tapDeleteSleep() {
		
		Gui.touchAVIew("UILabel", DefaultStrings.RemoveSleepButton);
	}

}
