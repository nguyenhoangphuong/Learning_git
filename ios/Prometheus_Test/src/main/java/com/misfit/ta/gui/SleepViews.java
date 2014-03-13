package com.misfit.ta.gui;

import com.misfit.ios.NuRemoteClient;
import com.misfit.ios.ViewUtils;

public class SleepViews {

	public static boolean isEditSleepView() {
		
		return ViewUtils.isExistedView("UILabel", DefaultStrings.EditSleepTitleLabel);
	}

	public static boolean hasRemoveSleepConfirmationAlert() {
		
		return PrometheusHelper.hasAlert(DefaultStrings.RemoveSleepConfirmationAlertMessage, 
				DefaultStrings.RemoveSleepConfirmationAlertTitle);
	}
	
	public static void tapDeleteSleep() {
		
		String parentView = String.format("(ViewUtils findViewWithViewName: @\"%s\" andIndex: @\"%d\")", 
				"PTSleepSessionEditView", 0);
		String cmd = String.format("(Gui touchAView: (ViewUtils findViewWithViewName: @\"%s\" andIndex: @\"%d\" inView: %s))",
				"UIButton", 0, parentView);
		
		NuRemoteClient.sendToServer(cmd);
	}

}
