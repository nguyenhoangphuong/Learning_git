package com.misfit.ta.ios.modelapi.social.friend;

import java.io.File;

import org.apache.log4j.Logger;
import org.graphwalker.Util;
import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ios.ViewUtils;
import com.misfit.ta.backend.api.internalapi.MVPApi;
import com.misfit.ta.backend.api.internalapi.social.SocialAPI;
import com.misfit.ta.backend.data.DataGenerator;
import com.misfit.ta.backend.data.goal.Goal;
import com.misfit.ta.backend.data.pedometer.Pedometer;
import com.misfit.ta.backend.data.profile.ProfileData;
import com.misfit.ta.gui.DefaultStrings;
import com.misfit.ta.gui.HomeScreen;
import com.misfit.ta.gui.LaunchScreen;
import com.misfit.ta.gui.PrometheusHelper;
import com.misfit.ta.gui.social.SocialProfileView;
import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.modelAPI.ModelAPI;
import com.misfit.ta.utils.ShortcutsTyper;
import com.misfit.ta.utils.TextTool;

public class DeleteFriendsAPI extends ModelAPI {
	
	protected static final Logger logger = Util.setupLogger(DeleteFriendsAPI.class);
	
	long timestamp = System.currentTimeMillis() / 1000;
	private String emailA = MVPApi.generateUniqueEmail();
	private String emailB = MVPApi.generateUniqueEmail();
	
	ProfileData profileA = DataGenerator.generateRandomProfile(timestamp, null);
	ProfileData profileB = DataGenerator.generateRandomProfile(timestamp, null);
	
	public DeleteFriendsAPI(AutomationTest automation, File model, boolean efsm,
			PathGenerator generator, boolean weight) {
		super(automation, model, efsm, generator, weight);
	}
	

	
	public void e_init() {
		
		/*
		 * Scenerio:
		 * - Sign up 2 accounts and make friends
		 * - Sign in account A and delete B
		 * - Sign in account to make sure there's no A in B's list
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
//		MVPApi.unlinkDevice(tokenA);
		
		String tokenB = MVPApi.signUp(emailB, "qwerty1").token;
		pedo.setSerialNumberString(TextTool.getRandomString(10, 10));
		MVPApi.createProfile(tokenB, profileB);
		MVPApi.createGoal(tokenB, goal);
		MVPApi.createPedometer(tokenB, pedo);
//		MVPApi.unlinkDevice(tokenB);
		
		// make friends
		SocialAPI.sendFriendRequest(tokenA, MVPApi.getUserId(tokenB));
		SocialAPI.acceptFriendRequest(tokenB, MVPApi.getUserId(tokenA));
	}
	
	public void e_signInAccountA() {
	
		PrometheusHelper.signIn(emailA, "qwerty1");
	}
	
	public void e_signInAccountB() {
		
		PrometheusHelper.signIn(emailB, "qwerty1");
	}
	
	public void e_goToSocialProfile() {
		
		HomeScreen.tapWordView();
		HomeScreen.tapSocialProfile();
		SocialProfileView.waitForNoFriendToDissapear();
	}
	
	public void e_deleteB() {
		
		SocialProfileView.tapEdit();
		ShortcutsTyper.delayOne();
		SocialProfileView.tapDeleteFriend(profileB.getHandle());
		ShortcutsTyper.delayOne();
		SocialProfileView.tapRemoveFriend();
		SocialProfileView.tapDone();
	}
	
	public void e_refreshAFriendList() {
		
		SocialProfileView.tapBack();
		HomeScreen.tapSocialProfile();
		ShortcutsTyper.delayTime(5000);
	}
	
	public void e_signOut() {
		
		SocialProfileView.tapBack();
		PrometheusHelper.signOut();
	}
	
	
	
	public void v_LaunchScreen() {
		
		Assert.assertTrue(LaunchScreen.isAtLaunchScreen() || LaunchScreen.isAtInitialScreen(), "Current view is start up view");
	}
	
	public void v_HomeScreen() {
		
		Assert.assertTrue(HomeScreen.isToday(), "Current view is HomeScreen");
	}
	
	public void v_FriendListA() {

		Assert.assertTrue(ViewUtils.isExistedView("UILabel", profileB.getHandle()), "Handle of B is existed on A's friend list");
		Assert.assertTrue(!ViewUtils.isExistedView("UILabel", DefaultStrings.AcceptButton), "Accept button is not existed");
		Assert.assertTrue(!ViewUtils.isExistedView("UILabel", DefaultStrings.IgnoreButton), "Ignore button is not existed");
	}
	
	public void v_FriendListAUpdated() {
		
		Assert.assertTrue(!ViewUtils.isExistedView("UILabel", profileB.getHandle()), "Handle of B is not on A's friendlist anymore");
	}
	
	public void v_SocialAccountNoFriends() {
		
		Assert.assertTrue(SocialProfileView.isSocialProfileViewNoFriend(), "Is no friends view");
	}

}
