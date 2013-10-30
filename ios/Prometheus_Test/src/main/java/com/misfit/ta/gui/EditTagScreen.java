package com.misfit.ta.gui;

import com.misfit.ios.ViewUtils;

public class EditTagScreen {
	
	public static void tapBack() {
		
		Gui.touchAVIew("UIButtonLabel", DefaultStrings.BackButton);
	}
	
	public static void tapSave() {
		
		Gui.touchAVIew("UIButtonLabel", DefaultStrings.SaveButton);
	}
	
	public static void selectActivity(String activityName) {
	
		Gui.touchAVIew("UILabel", activityName);
	}
	
	public static void tapPopupChangeTag() {
		
		Gui.touchPopupButton(DefaultStrings.ChangeTagButton);
	}
	
	public static void tapPopupCancel() {
		
		Gui.touchPopupButton(DefaultStrings.CancelButton);
	}
	
	public static boolean isEditTagScreen() {
		
		return ViewUtils.isExistedView("UILabel", DefaultStrings.EditTagTitle);
	}
	
	public static boolean hasPointLostAlert() {
		
		return Gui.getPopupContent().equals(DefaultStrings.PointLostPopupMessage);
	}

}
