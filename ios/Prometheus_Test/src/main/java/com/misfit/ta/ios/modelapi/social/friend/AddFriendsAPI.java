package com.misfit.ta.ios.modelapi.social.friend;

import java.io.File;

import org.apache.log4j.Logger;
import org.graphwalker.Util;
import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ios.ViewUtils;
import com.misfit.ta.backend.api.internalapi.MVPApi;
import com.misfit.ta.backend.data.DataGenerator;
import com.misfit.ta.backend.data.goal.Goal;
import com.misfit.ta.backend.data.pedometer.Pedometer;
import com.misfit.ta.backend.data.profile.ProfileData;
import com.misfit.ta.gui.DefaultStrings;
import com.misfit.ta.gui.HomeScreen;
import com.misfit.ta.gui.LaunchScreen;
import com.misfit.ta.gui.PrometheusHelper;
import com.misfit.ta.gui.SignUp;
import com.misfit.ta.gui.social.LeaderboardView;
import com.misfit.ta.gui.social.SearchFriendView;
import com.misfit.ta.gui.social.SocialProfileView;
import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.modelAPI.ModelAPI;
import com.misfit.ta.utils.ShortcutsTyper;
import com.misfit.ta.utils.TextTool;

public class AddFriendsAPI extends ModelAPI {
	
	protected static final Logger logger = Util.setupLogger(AddFriendsAPI.class);
	
	long timestamp = System.currentTimeMillis() / 1000;
	private String emailA = MVPApi.generateUniqueEmail();
	private String emailB = MVPApi.generateUniqueEmail();
	
	ProfileData profileA = DataGenerator.generateRandomProfile(timestamp, null);
	ProfileData profileB = DataGenerator.generateRandomProfile(timestamp, null);
	
	public AddFriendsAPI(AutomationTest automation, File model, boolean efsm,
			PathGenerator generator, boolean weight) {
		super(automation, model, efsm, generator, weight);
	}
	

	
	public void e_init() {
		
		/*
		 * Scenerio:
		 * - Sign in A and add B, check status before and after add
		 * - Sign in B and accept A, check status before and after accept
		 * - Sign in A and check friend list
		 * - Sign in B and check friend list
		 */
		
		
		Goal goal = Goal.getDefaultGoal();
		Pedometer pedo = DataGenerator.generateRandomPedometer(timestamp, null);
		
		
		profileA.setHandle(TextTool.getRandomString(6, 10).toLowerCase());
		profileB.setHandle(TextTool.getRandomString(6, 10).toLowerCase());
		
		// create 2 accounts using api
		String tokenA = MVPApi.signUp(emailA, "qwerty1").token;
		MVPApi.createProfile(tokenA, profileA);
		MVPApi.createGoal(tokenA, goal);
		MVPApi.createPedometer(tokenA, pedo);
		
		String tokenB = MVPApi.signUp(emailB, "qwerty1").token;
		pedo.setSerialNumberString(TextTool.getRandomString(10, 10));
		MVPApi.createProfile(tokenB, profileB);
		MVPApi.createGoal(tokenB, goal);
		MVPApi.createPedometer(tokenB, pedo);
	}
	
	public void e_signInAccountA() {
	
		PrometheusHelper.signIn(emailA, "qwerty1");
	}
	
	public void e_signInAccountB() {
		
		PrometheusHelper.signIn(emailB, "qwerty1");
	}
	
	public void e_goToSocialAccount() {
		
		HomeScreen.tapWordView();
		HomeScreen.tapSocialProfile();
		SocialProfileView.waitForNoFriendToDissapear();
	}
	
	public void e_goToSearchFriendView() {
		
		HomeScreen.tapWordView();
		HomeScreen.tapSocialProfile();
		SocialProfileView.tapSearchFriend();
		PrometheusHelper.waitForThrobberToDissappear();
		PrometheusHelper.waitForViewToDissappear("UILabel", DefaultStrings.SyncingLabel);
	}
	
	public void e_sendRequestsToB() {
		
		ShortcutsTyper.delayOne();
		SearchFriendView.tapSearchField();
		SearchFriendView.inputKeyWordAndSearch(profileB.getHandle());
		SearchFriendView.waitUntilSearchFinish();
		SearchFriendView.tapAdd(profileB.getHandle());
	}
	
	public void e_acceptA() {
		
		SocialProfileView.tapAccept(profileA.getHandle());
	}
	
	public void e_goToLeaderboard() {
		
		SocialProfileView.tapBack();
		HomeScreen.goToTodayViewOfLeaderboard();
		LeaderboardView.waitForNoFriendToDissapear();
	}
	
	public void e_signOut() {
		
		SearchFriendView.tapBack();
		ShortcutsTyper.delayOne();
		SocialProfileView.tapBack();
		PrometheusHelper.signOut();
	}
	
	
	
	public void v_LaunchScreen() {
		
		Assert.assertTrue(LaunchScreen.isAtInitialScreen(), "Current view is start up view");
	}
	
	public void v_HomeScreen() {
		
		PrometheusHelper.waitForView("UILabel", DefaultStrings.TodayTitle, 10);
		Assert.assertTrue(HomeScreen.isToday(), "Current view is HomeScreen");
	}
	
	public void v_SearchFriend() {

		Assert.assertTrue(SearchFriendView.isSearchFriendView(), "Current view is search friend view");
	}

	public void v_SearchFriendUpdated() {

		Assert.assertTrue(!ViewUtils.isExistedView("UIButton", DefaultStrings.AddButton), "No Add button");
		Assert.assertTrue(ViewUtils.isExistedView("UIButton", DefaultStrings.PendingButton), "Only Pending button");
	}
	
	public void v_SocialAccount() {
		
		Assert.assertTrue(ViewUtils.isExistedView("UILabel", profileA.getHandle()), "Handle of A is existed");
		Assert.assertTrue(ViewUtils.isExistedView("UILabel", DefaultStrings.AcceptButton), "Accept button is existed");
		Assert.assertTrue(ViewUtils.isExistedView("UILabel", DefaultStrings.IgnoreButton), "Ignore button is existed");
	}
	
	public void v_FriendListA() {

		Assert.assertTrue(ViewUtils.isExistedView("UILabel", profileB.getHandle()), "Handle of B is existed on A's friend list");
		Assert.assertTrue(!ViewUtils.isExistedView("UILabel", DefaultStrings.AcceptButton), "Accept button is not existed");
		Assert.assertTrue(!ViewUtils.isExistedView("UILabel", DefaultStrings.IgnoreButton), "Ignore button is not existed");
	}

	public void v_FriendListB() {

		Assert.assertTrue(ViewUtils.isExistedView("UILabel", profileA.getHandle()), "Handle of A is existed on B's friend list");
		Assert.assertTrue(!ViewUtils.isExistedView("UILabel", DefaultStrings.AcceptButton), "Accept button is not existed");
		Assert.assertTrue(!ViewUtils.isExistedView("UILabel", DefaultStrings.IgnoreButton), "Ignore button is not existed");
	}

	public void v_LeaderboardB() {
		
		LeaderboardView.tapGotIt();
		Assert.assertTrue(ViewUtils.isExistedView("UILabel", profileA.getHandle()), "Handle of A is existed on B's leaderboard");
	}
	
}
