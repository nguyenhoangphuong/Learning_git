package com.misfit.ta.ios.modelapi.social.board;

import java.io.File;
import java.util.Calendar;

import org.apache.log4j.Logger;
import org.graphwalker.Util;
import org.graphwalker.generators.PathGenerator;
import org.testng.Assert;

import com.misfit.ta.backend.api.internalapi.MVPApi;
import com.misfit.ta.backend.data.DataGenerator;
import com.misfit.ta.backend.data.goal.Goal;
import com.misfit.ta.backend.data.profile.ProfileData;
import com.misfit.ta.backend.data.timeline.TimelineItem;
import com.misfit.ta.backend.data.timeline.timelineitemdata.TimelineItemDataBase;
import com.misfit.ta.gui.DefaultStrings;
import com.misfit.ta.gui.HomeScreen;
import com.misfit.ta.gui.LaunchScreen;
import com.misfit.ta.gui.PrometheusHelper;
import com.misfit.ta.gui.social.SocialProfileView;
import com.misfit.ta.gui.social.WorldFeedView;
import com.misfit.ta.ios.AutomationTest;
import com.misfit.ta.modelAPI.ModelAPI;
import com.misfit.ta.utils.ShortcutsTyper;
import com.misfit.ta.utils.TextTool;

public class WorldFeedAPI extends ModelAPI {
	
	protected static final Logger logger = Util.setupLogger(WorldFeedAPI.class);
	
	private String tokenA;
	private Goal goalA;
	private ProfileData profileA, profileB;
	
	public WorldFeedAPI(AutomationTest automation, File model, boolean efsm,
			PathGenerator generator, boolean weight) {
		super(automation, model, efsm, generator, weight);
	}
	

	
	public void e_init() {
		
		/*
		 * Scenerio:
		 * - Sign up 2 accounts A, B and join social
		 * - On account A: turn off world feed on get some records then turn on and get some more
		 * - On account B: otherwise
		 * - Expect:
		 *   + Privacy option is promted every time user signs up
		 *   + Turn off world feed will erase old records
		 *   + Turn on world feed will include records from that time on
		 */
	}
	
	public void e_signUpAccountA() {
	
		// sign up
		String emailA = PrometheusHelper.signUpDefaultProfile();
		
		// join social
		tokenA = MVPApi.signIn(emailA, "qwerty1").token;
		profileA = new ProfileData();
		profileA.setName(TextTool.getRandomString(5, 10));
		profileA.setHandle(TextTool.getRandomString(5, 10).toLowerCase() + System.nanoTime());
		profileA.setGender(1);
		MVPApi.updateProfile(tokenA, profileA);
		
		// update goal
		goalA = MVPApi.searchGoal(tokenA, 0, Integer.MAX_VALUE, 0).goals[0];
		goalA.setValue(100 * 2.5);
		MVPApi.updateGoal(tokenA, goalA);
		
		// pull to refresh
		HomeScreen.pullToRefresh();
		PrometheusHelper.waitForViewToDissappear("UILabel", DefaultStrings.LoadingLabel);
	}
	
	public void e_signUpAccountB() {

		// sign up
		String emailB = PrometheusHelper.signUpDefaultProfile();

		// join social
		String tokenB = MVPApi.signIn(emailB, "qwerty1").token;
		profileB = new ProfileData();
		profileB.setName(TextTool.getRandomString(5, 10));
		profileB.setHandle(TextTool.getRandomString(5, 10).toLowerCase() + System.nanoTime());
		profileB.setGender(0);
		MVPApi.updateProfile(tokenB, profileB);
		
		// update goal
		Goal goal = MVPApi.searchGoal(tokenB, 0, Integer.MAX_VALUE, 0).goals[0];
		goal.setValue(100 * 2.5);
		MVPApi.updateGoal(tokenB, goal);
		
		// pull to refresh
		HomeScreen.pullToRefresh();
		PrometheusHelper.waitForViewToDissappear("UILabel", DefaultStrings.LoadingLabel);
	}
	
	public void e_goToWorldFeed() {
		
		HomeScreen.tapWordView();
		WorldFeedView.pullToRefresh();
		WorldFeedView.waitForPullToRefreshToFinish();
	}
	
	public void e_choosePrivacyOff() {
		
		WorldFeedView.choosePrivacyOff();
		WorldFeedView.tapSave();
	}
	
	public void e_choosePrivacyOn() {
		
		WorldFeedView.choosePrivacyOn();
		WorldFeedView.tapSave();
	}
	
	public void e_input100() {
		
		HomeScreen.tapMyProgress();
		HomeScreen.tapOpenManualInput();
		PrometheusHelper.inputManualRecord(new String[] {"12", "00", "AM"}, 10, 1000);
		HomeScreen.tapSave();
		ShortcutsTyper.delayTime(3000);
		HomeScreen.tapWordView();
		
		WorldFeedView.pullToRefresh();
		WorldFeedView.waitForPullToRefreshToFinish();
	}
	
	public void e_input150() {
		
		HomeScreen.tapMyProgress();
		HomeScreen.tapOpenManualInput();
		PrometheusHelper.inputManualRecord(new String[] {"12", "20", "AM"}, 5, 500);
		HomeScreen.tapSave();
		ShortcutsTyper.delayTime(3000);
		HomeScreen.tapWordView();
		
		WorldFeedView.pullToRefresh();
		WorldFeedView.waitForPullToRefreshToFinish();
	}
	
	public void e_input200ForAAndPullToRefresh() {
		
		// create 200% timeline item for A
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 50);
		cal.set(Calendar.SECOND, 0);
		
		TimelineItem milestone = DataGenerator.generateRandomMilestoneItem(cal.getTimeInMillis() / 1000, 
				TimelineItemDataBase.EVENT_TYPE_200_GOAL, null);
		
		MVPApi.createTimelineItem(tokenA, milestone);
		
		// pull to refresh world view
		WorldFeedView.pullToRefresh();
		PrometheusHelper.waitForViewToDissappear("UILabel", DefaultStrings.LoadingEtcLabel);
	}
	
	public void e_goToSocialProfileAndEdit() {
		
		HomeScreen.tapSocialProfile();
		SocialProfileView.tapEdit();
	}
	
	public void e_turnOffPrivacy() {

		SocialProfileView.switchPrivacyOff();
		SocialProfileView.tapDone();
		ShortcutsTyper.delayTime(2000);
		SocialProfileView.tapBack();
		HomeScreen.tapMyProgress();
	}
	
	public void e_turnOnPrivacy() {

		SocialProfileView.switchPrivacyOn();
		SocialProfileView.tapDone();
		ShortcutsTyper.delayTime(2000);
		SocialProfileView.tapBack();
		HomeScreen.tapMyProgress();
	}
	
	public void e_signOut() {

		PrometheusHelper.signOut();
	}
	
	
	
	public void v_LaunchScreen() {
		
		Assert.assertTrue(LaunchScreen.isAtLaunchScreen() || LaunchScreen.isAtInitialScreen(), "Current view is start up view");
	}
	
	public void v_HomeScreen() {
		
		Assert.assertTrue(HomeScreen.isToday(), "Current view is HomeScreen");
	}
	
	public void v_WorldFeedInitial() {
		
		Assert.assertTrue(WorldFeedView.isWorldViewDefault(), "Current view is world view");
		Assert.assertTrue(WorldFeedView.hasPrivacyOptionPanel(), "Privacy option panel is visible");
	}
	
	public void v_WorldFeed() {
		
		Assert.assertTrue(WorldFeedView.isWorldViewDefault(), "Current view is world view");
	}
	
	public void v_SocialProfileEditA() {

		Assert.assertTrue(SocialProfileView.isSocialProfileView() && SocialProfileView.isInEditMode(), 
				"Social profile view is in edit mode");
		
		Assert.assertEquals(SocialProfileView.getPrivacySwitchStatus(), false, "Privacy status of A");
	}

	public void v_SocialProfileEditB() {

		Assert.assertTrue(SocialProfileView.isSocialProfileView() && SocialProfileView.isInEditMode(), 
				"Social profile view is in edit mode");
		
		Assert.assertEquals(SocialProfileView.getPrivacySwitchStatus(), true, "Privacy status of B");
	}
	
	public void v_WorldFeedAWithRecords() {

		Assert.assertTrue(WorldFeedView.has150PercentRecord(profileA.getHandle(), 150, profileA.getGender().equals(0)),
				"There is 150% record of A on world view");
	}
	
	public void v_WorldFeedBWithRecords() {

		Assert.assertTrue(WorldFeedView.has100PercentRecord(profileB.getHandle(), 100, profileB.getGender().equals(0)),
				"No 100% record of B on world view");
	}
	
	public void v_WorldFeedAWithoutRecords() {

		Assert.assertTrue(!WorldFeedView.has100PercentRecord(profileA.getHandle(), 100, profileA.getGender().equals(0)),
				"No 100% record of A on world view");
	}

	public void v_WorldFeedBWithoutRecords() {

		Assert.assertTrue(!WorldFeedView.has100PercentRecord(profileB.getHandle(), 100, profileB.getGender().equals(0)) &&
				!WorldFeedView.has150PercentRecord(profileB.getHandle(), 150, profileB.getGender().equals(0)),
				"No 100% or 150% record of B on world view");
	}
		
	public void v_WorldFeedBRefreshed() {

		Assert.assertTrue(WorldFeedView.has200PercentRecord(profileA.getHandle(), 200, profileA.getGender().equals(0)),
				"There is 200% record of A on world view");
	}

}
