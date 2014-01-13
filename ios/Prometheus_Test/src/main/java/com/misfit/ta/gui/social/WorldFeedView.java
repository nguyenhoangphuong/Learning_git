package com.misfit.ta.gui.social;

import com.misfit.ios.NuRemoteClient;
import com.misfit.ios.ViewUtils;
import com.misfit.ta.gui.DefaultStrings;
import com.misfit.ta.gui.Gui;
import com.misfit.ta.gui.PrometheusHelper;

public class WorldFeedView {
	
	private static String[] Hit100PercentGoalMessage = new String[] {
		"[handle] just hit [padj] goal of [point] points. Sweetness!",
		"[handle] just achieved [padj] goal of [point] points. Solid.",
		"[handle] just achieved [padj] [point] point goal. Touchdown!",
		"[handle] gets to take a victory lap- [subject] just hit [padj] goal of [point] points.",
		"[handle] is making it look easy- [subject] just hit [padj] goal of [point] points.",
		"[handle] just hit [padj] [point] point goal. [Subject] shoots, [subject] scores! ",
		"[handle] nailed [padj] goal of [point] points. Mission accomplished.",
		"[handle] crushed [padj] goal of [point] points. #BringIt",
		"[handle] just hit [point] points. That's a goal!",
	};

	private static String[] Hit150PercentGoalMessage = new String[] {
		"[handle] is getting it done with [point] points- 150% of [padj] goal",
		"[handle] is crushing it with [point] points- that's 150% of [padj] goal.",
		"[handle] is picking up the pace with [point] points, 150% of [padj] goal!",
		"[handle] is stepping it up with [point] points and just hit 150% of [padj] goal.",
	};

	private static String[] Hit200PercentGoalMessage = new String[] {
		"[handle] doubled [padj] goal with [point] points. #winning",
		"[handle] is getting it done with [point] points- that's double [padj] goal!",
		"[handle] is crushing it with [point] points- that's 200% of [padj] goal.",
		"[handle] is stepping it up with [point] points, twice [padj] goal!",
	};
	

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
		
	public static void tapSave() {
		
		Gui.touchAVIew("UILabel", DefaultStrings.SaveButton);
	}
	
	
	
	public static void choosePrivacyOn() {
		
		Gui.touchAVIew("UIButton", 13);
	}
	
	public static void choosePrivacyOff() {
		
		Gui.touchAVIew("UIButton", 14);
	}
	
	
	
	public static void pullToRefresh() {
		
		Gui.swipeDown(1000);
	}
	
	public static void waitForPullToRefreshToFinish() {
		
		PrometheusHelper.waitForViewToDissappear("UILabel", DefaultStrings.LoadingEtcLabel);
	}
	
	
	
	public static boolean isWelcomeView() {
		
		return ViewUtils.isExistedView("UILabel", DefaultStrings.ShineWithFriendsTitle);
	}
	
	public static boolean isWorldViewDefault() {
		
		return !isWelcomeView();
	}
	
	public static boolean hasPrivacyOptionPanel() {
		
		return ViewUtils.isExistedView("UIButton", DefaultStrings.SaveButton);
	}
	
	private static boolean hasHitGoalRecord(String[] messages, String handle, int points, boolean isMale) {

		for(String message : messages) {

			String label = message.replace("[handle]", handle)
					.replace("[padj]", isMale ? "his" : "her")
					.replace("[point]", points + "")
					.replace("[subject]", isMale ? "he" : "she")
					.replace("[Subject]", isMale ? "He" : "She");
			if(ViewUtils.isExistedView("UILabel", label))
				return true;
		}

		return false;
	}
	
	public static boolean has100PercentRecord(String handle, int points, boolean isMale) {

		return hasHitGoalRecord(Hit100PercentGoalMessage, handle, points, isMale);
	}

	public static boolean has150PercentRecord(String handle, int points, boolean isMale) {

		return hasHitGoalRecord(Hit150PercentGoalMessage, handle, points, isMale);
	}

	public static boolean has200PercentRecord(String handle, int points, boolean isMale) {

		return hasHitGoalRecord(Hit200PercentGoalMessage, handle, points, isMale);
	}
	
}
