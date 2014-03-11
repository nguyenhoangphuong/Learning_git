package com.misfit.ta.ios.modelapi.social.averageuser;

import java.io.File;

import org.apache.log4j.Logger;
import org.graphwalker.Util;
import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ios.ViewUtils;
import com.misfit.ta.gui.DefaultStrings;
import com.misfit.ta.gui.HomeScreen;
import com.misfit.ta.gui.PrometheusHelper;
import com.misfit.ta.gui.social.LeaderboardView;
import com.misfit.ta.gui.social.SocialProfileView;
import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.modelAPI.ModelAPI;
import com.misfit.ta.utils.ShortcutsTyper;
import com.misfit.ta.utils.TextTool;

public class AverageUserAutoFriendAPI extends ModelAPI {
	
	protected static final Logger logger = Util.setupLogger(AverageUserAutoFriendAPI.class);
	
	
	public AverageUserAutoFriendAPI(AutomationTest automation, File model, boolean efsm,
			PathGenerator generator, boolean weight) {
		super(automation, model, efsm, generator, weight);
	}
	
	
	
	public void e_init() {
		
		PrometheusHelper.signUpDefaultProfile();
		
		HomeScreen.tapLeaderboard();
		LeaderboardView.tapDontHaveFacebook();
		PrometheusHelper.waitForView("UILabel", DefaultStrings.ProfilePreviewViewTitle);
		
		SocialProfileView.inputName(TextTool.getRandomString(5, 10));
		SocialProfileView.inputHandle(TextTool.getRandomString(5, 10).toLowerCase() + System.nanoTime());
		SocialProfileView.tapEditAvatarButton();
		ShortcutsTyper.delayOne();
		SocialProfileView.tapTakePhoto();
		ShortcutsTyper.delayTime(5000);
		SocialProfileView.tapCapturePhoto();
		ShortcutsTyper.delayTime(2000);
		SocialProfileView.tapUsePhoto();
		ShortcutsTyper.delayOne();
		SocialProfileView.tapDone();
		PrometheusHelper.waitForViewToDissappear("UILabel", DefaultStrings.ProfilePreviewViewTitle);
		HomeScreen.tapActivityTimeline();
	}
	
	public void e_goToLeaderboard() {
		
		HomeScreen.tapLeaderboard();
		LeaderboardView.tapGotIt();
		LeaderboardView.waitForNoFriendToDissapear();
	}
	
	public void e_tapAverageUser() {
		
		LeaderboardView.tapUserWithHandle("misterfit");
	}
	
	public void e_cancel() {
		
		LeaderboardView.tapToCloseCurrentUser();
	}
	
	public void e_goToFriendList() {
		
		HomeScreen.tapSocialProfile();
		SocialProfileView.waitForNoFriendToDissapear();
	}
	
	
	
	public void v_HomeScreen() {
		
		Assert.assertTrue(HomeScreen.isToday(), "Current view is home view");
	}
	
	public void v_Leaderboard() {
		
		Assert.assertTrue(LeaderboardView.isDefaultView(), "Current view is leaderboard view");
	}	
	
	public void v_AverageUserDetail() {
		
		Assert.assertTrue(ViewUtils.isExistedView("UILabel", "misterfit"), "Average user's handle is visible");
		Assert.assertTrue(ViewUtils.isExistedView("UILabel", "Mr. Average Misfit"), "Average user's name is visible");
		Assert.assertTrue(ViewUtils.isExistedView("UILabel", "Misfit Metrics"), "Misfit metrics is visible");
	}
	
	public void v_FriendList() {
		
		Assert.assertTrue(ViewUtils.isExistedView("UILabel", "misterfit"), "Average user's handle is visible");
		Assert.assertTrue(ViewUtils.isExistedView("UILabel", "Mr. Average Misfit"), "Average user's name is visible");
	}
	
}
