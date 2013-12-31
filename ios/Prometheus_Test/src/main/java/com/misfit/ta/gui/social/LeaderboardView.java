package com.misfit.ta.gui.social;


import com.misfit.ios.NuRemoteClient;
import com.misfit.ios.ViewUtils;
import com.misfit.ta.gui.DefaultStrings;
import com.misfit.ta.gui.Gui;

public class LeaderboardView {

	
	public static void tapGotIt() {
		
		Gui.touchAVIew("UILabel", DefaultStrings.OKGotItButton);
	}
	
	public static void tapUserWithHandle(String handle) {
		
		Gui.touchAVIew("UILabel", handle);
	}
	
	public static void tapToCloseCurrentUser() {
		
		String cmd = "((ViewUtils findViewWithViewName: @\"PTSocialProfileDetailPopup\" andIndex:0) dismissPopup)";
		NuRemoteClient.sendToServer(cmd);
	}
	
	public static void tapToday() {
		
		Gui.touchAVIew("UILabel", DefaultStrings.TodayTitle);
	}
	
	public static void tapYesterday() {
		
		Gui.touchAVIew("UILabel", DefaultStrings.YesterdayTitle);
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
	
	
	public static boolean isWelcomeView() {
		
		return ViewUtils.isExistedView("UILabel", DefaultStrings.ShineWithFriendsTitle);
	}
	
	public static boolean isNoFriendView() {
		
		return ViewUtils.isExistedView("UILabel", DefaultStrings.LeaderboardNoFriendMessage);
	}
	
	public static boolean isDefaultView() {
		
		return ViewUtils.isExistedView("UILabel", DefaultStrings.YesterdayTitle);
	}
	
}
