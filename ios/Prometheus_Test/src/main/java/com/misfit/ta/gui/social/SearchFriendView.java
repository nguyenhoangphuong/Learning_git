package com.misfit.ta.gui.social;

import org.apache.commons.lang.StringUtils;

import com.misfit.ios.NuRemoteClient;
import com.misfit.ios.ViewUtils;
import com.misfit.ta.gui.DefaultStrings;
import com.misfit.ta.gui.Gui;
import com.misfit.ta.gui.PrometheusHelper;

public class SearchFriendView {

	static public void tapBack() {
		
		Gui.touchAVIew("UILabel", DefaultStrings.BackButton);
	}
	
	static public void tapCancel() {
		
		Gui.touchAVIew("UILabel", DefaultStrings.CancelButton);
	}
	
	static public void tapSearchField() {
		
		Gui.touchAVIew("UITextField", 0);
	}
	
	static public void tapAdd(String friendHandle) {
		
		String parentView = String.format("((ViewUtils findViewWithViewName: @\"%s\" andTitle: @\"%s\") superview)", 
				"UILabel", friendHandle);
		String cmd = String.format("(Gui touchAView: (ViewUtils findViewWithViewName: @\"%s\" andTitle: @\"%s\" inView: %s))",
				"UIButton", DefaultStrings.AddButton, parentView);
		
		NuRemoteClient.sendToServer(cmd);
	}
	
	static public void tapCancelAlert() {
		
		Gui.touchPopupButton(0);
	}
	
	
	
	static public void inputKeyWordAndSearch(String keyword) {

		Gui.setText("UITextField", 0, keyword);
		Gui.type("\\n");
	}
	
	static public void waitUntilSearchFinish() {
		
		PrometheusHelper.waitForViewToDissappear("UILabel", DefaultStrings.SearchingLabel);
	}
	
	
	
	static public boolean isSearchFriendView() {
		
		return ViewUtils.isExistedView("UILabel", DefaultStrings.SearchFriendTitle);
	}

	static public boolean isNoResult() {
		
		return ViewUtils.isExistedView("UILabel", DefaultStrings.NoMatchesFoundLabel);
	}
	
	static public boolean hasInvalidParamAlert() {
		
		return PrometheusHelper.hasAlert(DefaultStrings.InvalidParametersMessage, DefaultStrings.Title);
	}
	
	static public boolean hasPendingStatus(String friendHandle) {

		String parentView = String.format("((ViewUtils findViewWithViewName: @\"%s\" andTitle: @\"%s\") superview)", 
				"UILabel", friendHandle);
		String cmd = String.format("(ViewUtils findViewWithViewName: @\"%s\" andTitle: @\"%s\" inView: %s)",
				"UIButton", DefaultStrings.PendingButton, parentView);

		NuRemoteClient.sendToServer(cmd);
		String lastMsg = NuRemoteClient.getLastMessage();
		return !StringUtils.isEmpty(lastMsg);
	}

	static public boolean hasAddButton(String friendHandle) {

		String parentView = String.format("((ViewUtils findViewWithViewName: @\"%s\" andTitle: @\"%s\") superview)", 
				"UILabel", friendHandle);
		String cmd = String.format("(ViewUtils findViewWithViewName: @\"%s\" andTitle: @\"%s\" inView: %s)",
				"UIButton", DefaultStrings.AddButton, parentView);

		NuRemoteClient.sendToServer(cmd);
		String lastMsg = NuRemoteClient.getLastMessage();
		return !StringUtils.isEmpty(lastMsg);
	}
	
}
