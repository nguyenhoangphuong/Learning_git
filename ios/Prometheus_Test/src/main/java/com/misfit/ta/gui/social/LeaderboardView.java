package com.misfit.ta.gui.social;


import java.util.concurrent.Callable;

import com.misfit.ios.NuRemoteClient;
import com.misfit.ios.ViewUtils;
import com.misfit.ta.gui.DefaultStrings;
import com.misfit.ta.gui.Gui;
import com.misfit.ta.gui.HomeScreen;
import com.misfit.ta.gui.PrometheusHelper;

public class LeaderboardView {

	
	public static void tapGotIt() {
		
		Gui.touchAVIew("UILabel", DefaultStrings.OKGotItButton);
	}
	
	public static void tapUserWithHandle(String handle) {
		
		Gui.touchAVIew("UILabel", handle);
	}
	
	public static void tapToCloseCurrentUser() {
		
		HomeScreen.tapMenuSocial();
	}
	
	public static void tapToday() {
		
		Gui.touchAVIew("UILabel", DefaultStrings.LeaderBoardTodayButtonLabel);
	}
	
	public static void tapYesterday() {
		
		PrometheusHelper.handleCalloutMessagePopup();
		Gui.touchAVIew("UILabel", DefaultStrings.LeaderBoardYesterdayButtonLabel);
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
	
	public static void waitForNoFriendToDissapear() {
				
		Callable<Boolean> condition = new Callable<Boolean>() {

			public Boolean call() {
				return !LeaderboardView.isNoFriendView();
			}
		};
		
		PrometheusHelper.waitForCondition(condition);
	}
	
	
	public static boolean isWelcomeView() {
		
		return ViewUtils.isExistedView("UILabel", DefaultStrings.ShineWithFriendsTitle);
	}
	
	public static boolean isNoFriendView() {
		
		return ViewUtils.isExistedView("UILabel", DefaultStrings.LeaderboardNoFriendMessage);
	}
	
	public static boolean isDefaultView() {
		
		return ViewUtils.isExistedView("UILabel", DefaultStrings.LeaderBoardYesterdayButtonLabel);
	}
	
	
	public static String getRankOfUser(String handle) {
		
		String parentView = String.format("((ViewUtils findViewWithViewName: @\"%s\" andTitle: @\"%s\") superview)", 
				"UILabel", handle);
		String view = String.format("(ViewUtils findViewWithViewName: @\"%s\" andIndex: @\"%d\" inView: %s)",
				"UILabel", 0, parentView);
		String cmd = String.format("(Gui getValueWithPropertyName: @\"%s\" inView: %s)",
				"text", view);

		NuRemoteClient.sendToServer(cmd);
		String lastMsg = NuRemoteClient.getLastMessage();
		return lastMsg;
	}
		
	public static String getPointOfUser(String handle) {
	
		String parentView = String.format("((ViewUtils findViewWithViewName: @\"%s\" andTitle: @\"%s\") superview)", 
				"UILabel", handle);
		String view = String.format("(ViewUtils findViewWithViewName: @\"%s\" andIndex: @\"%d\" inView: %s)",
				"UILabel", 1, parentView);
		String cmd = String.format("(Gui getValueWithPropertyName: @\"%s\" inView: %s)",
				"text", view);

		NuRemoteClient.sendToServer(cmd);
		String lastMsg = NuRemoteClient.getLastMessage();
		return lastMsg;
	}
	
}
