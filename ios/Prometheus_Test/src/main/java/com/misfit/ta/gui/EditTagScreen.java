package com.misfit.ta.gui;

import com.misfit.ios.ViewUtils;

public class EditTagScreen {
	
	public static void tapBack() {
		
		Gui.touchAVIew("UIButtonLabel", DefaultStrings.BackButton);
	}
	
	public static void tapSave() {
		
		Gui.touchAVIew("UIButtonLabel", DefaultStrings.NextButton);
	}
	
	public static void selectActivity(String activityName) {
	
		Gui.touchAVIew("UILabel", activityName);
	}
	
	public static void tapPopupChangeTag() {
		
		Gui.touchPopupButton(1);
	}
	
	public static void tapPopupCancel() {
		
		Gui.touchPopupButton(0);
	}
	
	public static boolean isEditTagScreen() {
		
		return ViewUtils.isExistedView("UILabel", DefaultStrings.EditTagTitle);
	}
	
	public static boolean hasPointLostAlert() {
		
		return Gui.getPopupContent().equals(DefaultStrings.PointLostPopupMessage);
	}

}
