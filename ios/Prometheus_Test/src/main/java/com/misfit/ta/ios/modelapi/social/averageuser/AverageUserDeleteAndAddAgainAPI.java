package com.misfit.ta.ios.modelapi.social.averageuser;

import java.io.File;

import org.apache.log4j.Logger;
import org.graphwalker.Util;
import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ios.ViewUtils;
import com.misfit.ta.gui.DefaultStrings;
import com.misfit.ta.gui.Gui;
import com.misfit.ta.gui.HomeScreen;
import com.misfit.ta.gui.PrometheusHelper;
import com.misfit.ta.gui.social.LeaderboardView;
import com.misfit.ta.gui.social.SearchFriendView;
import com.misfit.ta.gui.social.SocialProfileView;
import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.modelAPI.ModelAPI;
import com.misfit.ta.utils.ShortcutsTyper;
import com.misfit.ta.utils.TextTool;

public class AverageUserDeleteAndAddAgainAPI extends ModelAPI {
	
	protected static final Logger logger = Util.setupLogger(AverageUserDeleteAndAddAgainAPI.class);
	
	
	public AverageUserDeleteAndAddAgainAPI(AutomationTest automation, File model, boolean efsm,
			PathGenerator generator, boolean weight) {
		super(automation, model, efsm, generator, weight);
	}
	
	
	
	public void e_init() {
		
		PrometheusHelper.signUpDefaultProfile();
		
//		HomeScreen.goToTodayViewOfLeaderboard();
		HomeScreen.tapMenuSocial();
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
		SocialProfileView.tapNext();
		ShortcutsTyper.delayOne();
		HomeScreen.tapActivityTimeline();
	}
	
	public void e_goToLeaderboard() {
		
		PrometheusHelper.handleUpdateFirmwarePopup();
		HomeScreen.goToTodayViewOfLeaderboard();
		LeaderboardView.tapGotIt();
		LeaderboardView.waitForNoFriendToDissapear();
	}
	
	public void e_deleteAverageUserAndGoToLeaderboard() {
		
		HomeScreen.tapSocialProfile();
		SocialProfileView.waitForNoFriendToDissapear();
		SocialProfileView.tapEdit();
		SocialProfileView.tapDeleteFriend("misterfit");
		SocialProfileView.tapRemoveFriend();
		SocialProfileView.tapDone();
		Gui.captureScreen("e_deleteAverageUserAndGoToLeaderboard" + System.nanoTime());
		SocialProfileView.tapBack();
		
		PrometheusHelper.handleUpdateFirmwarePopup();
		HomeScreen.goToTodayViewOfLeaderboard();
		LeaderboardView.pullToRefresh();
	}
	
	public void e_addAverageUserAndGoToLeaderboard() {
		
		HomeScreen.tapSocialProfile();
		SocialProfileView.tapSearchFriend();
		PrometheusHelper.waitForThrobberToDissappear();
		SearchFriendView.tapSearchField();
		SearchFriendView.inputKeyWordAndSearch("misterfit");
		SearchFriendView.waitUntilSearchFinish();
		
		SearchFriendView.tapAdd("misterfit");
		PrometheusHelper.waitForView("UILabel", DefaultStrings.PendingButton);
		Gui.captureScreen("e_addAverageUserAndGoToLeaderboard" + System.nanoTime());
		SearchFriendView.tapBack();
		SocialProfileView.tapBack();
		
		HomeScreen.goToTodayViewOfLeaderboard();
		LeaderboardView.pullToRefresh();
	}
	
	
	
	public void v_HomeScreen() {
		
		Assert.assertTrue(HomeScreen.isToday(), "Current view is home view");
	}
	
	public void v_Leaderboard() {
		
		Assert.assertTrue(LeaderboardView.isDefaultView(), "Current view is leaderboard view");
		Assert.assertTrue(ViewUtils.isExistedView("UILabel", "misterfit"), "Average user's handle is visible");
	}
	
	public void v_LeaderboardNoCheck() {
		
	}
	
	public void v_LeaderboardDeleted() {
		
		Assert.assertTrue(LeaderboardView.isDefaultView(), "Current view is leaderboard view");
		Assert.assertTrue(!ViewUtils.isExistedView("UILabel", "misterfit"), "Average user's handle is not visible");
	}
	
}
