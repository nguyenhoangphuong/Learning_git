package com.misfit.ta.gui.social;

import com.misfit.ios.NuRemoteClient;
import com.misfit.ios.ViewUtils;
import com.misfit.ta.gui.DefaultStrings;
import com.misfit.ta.gui.Gui;

public class WorldFeedView {

	public static void tapUserWithHandle(String handle) {
		
		Gui.touchAVIew("UILabel", handle);
	}
	
	public static void tapToCloseCurrentUser() {
		
		String cmd = "((ViewUtils findViewWithViewName: @\"PTSocialDetailPopup\" andIndex:0) dismissPopup)";
		NuRemoteClient.sendToServer(cmd);
	}
	
	public static void tapDontHaveFacebook() {
		
		Gui.touchAVIew("UILabel", DefaultStrings.DontHaveFacebookButton);
	}
	
	public static void tapConnectFacebook() {
		
		Gui.touchAVIew("UILabel", DefaultStrings.ConnectWithFacebookButton);
	}
	
	public static void tapGetStarted() {
		
		Gui.touchAVIew("UILabel", DefaultStrings.GetStartedButton);
	}
	
	public static void pullToRefresh() {
		
		Gui.swipeDown(1000);
	}	
	
	public static void tapSave() {
		
		Gui.touchAVIew("UILabel", DefaultStrings.SaveButton);
	}
	
	
	
	public static void chooseIncludeOption() {
		
		Gui.touchAVIew("UIButton", 13);
	}
	
	public static void chooseDontIncludeOption() {
		
		Gui.touchAVIew("UIButton", 14);
	}
	
	
	
	public static boolean isWelcomeView() {
		
		return ViewUtils.isExistedView("UILabel", DefaultStrings.ShineWithFriendsTitle);
	}
	
	public static boolean isWorldViewDefault() {
		
		return !isWelcomeView();
	}
	
	public static boolean hasOptionPanel() {
		
		return ViewUtils.isExistedView("UIButton", DefaultStrings.SaveButton);
	}
	
}
