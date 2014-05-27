package com.misfit.ta.backend.aut.correctness;

import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.misfit.ta.backend.api.internalapi.MVPApi;
import com.misfit.ta.backend.api.internalapi.social.SocialAPI;
import com.misfit.ta.backend.aut.BackendAutomation;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.backend.data.DataGenerator;
import com.misfit.ta.backend.data.account.AccountResult;
import com.misfit.ta.backend.data.beddit.BedditSleepSession;
import com.misfit.ta.backend.data.goal.Goal;
import com.misfit.ta.backend.data.goalprogress.GoalProgress;
import com.misfit.ta.backend.data.goalprogress.GoalSettings;
import com.misfit.ta.backend.data.goalprogress.sleep.SleepGoalProgress;
import com.misfit.ta.backend.data.goalprogress.weight.WeightGoalProgress;
import com.misfit.ta.backend.data.graph.GraphItem;
import com.misfit.ta.backend.data.pedometer.Pedometer;
import com.misfit.ta.backend.data.profile.ProfileData;
import com.misfit.ta.backend.data.statistics.Statistics;
import com.misfit.ta.backend.data.sync.SyncLog;
import com.misfit.ta.backend.data.timeline.TimelineItem;
import com.misfit.ta.backend.data.timeline.timelineitemdata.TimelineItemDataBase;
import com.misfit.ta.common.MVPCommon;
import com.misfit.ta.common.Verify;
import com.misfit.ta.utils.TextTool;

public class ShineBackendSmokeTestWithoutCreateUsers extends BackendAutomation {

	private List<String> errors = new ArrayList<String>();
	
	
	// test methods
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "shinebackend_light_smoke_test" })
	public void ShineBackendCommonAPIsSmokeTest() {
		
		errors.clear();
		
		String token = runRegistrationTest();
		runProfileTest(token);	
		runPedometerTest(token);
		runGoalTest(token);
		runGoalProgressAndGoalSettingsTest(token);
		runTimelineItemTest(token);
		runGraphItemTest(token);
		runBedditTest(token);
		runStatisticsTest(token);
		runSummaryTest(token);
		runMiscTest(token);

		if(!Verify.verifyAll(errors))
			Assert.fail("Smoke test fails, some routes don't work as expected");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "shinebackend_light_smoke_test" })
	public void ShineBackendSocialAPIsSmokeTest() {
		
		errors.clear();
		
		// clean up
		String myToken = MVPApi.signIn("shine_backend_smoke_test_me@qa.com", "qqqqqq").token;
		String friendToken = MVPApi.signIn("shine_backend_smoke_test_you@qa.com", "qqqqqq").token;
		String myUid = MVPApi.getUserId(myToken);
		String friendUid = MVPApi.getUserId(friendToken);
		
		SocialAPI.deleteFriend(myToken, friendUid);
		SocialAPI.cancelFriendRequest(myToken, friendUid);	
		
		// run test
		runFriendRequestTest(myToken, myUid, friendToken, friendUid);
		runDashboardTest(myToken);
		runSocialMiscTest(myToken);
		
		if(!Verify.verifyAll(errors))
			Assert.fail("Smoke test fails, some routes don't work as expected");
	}

	
	// test helpers
	public String runRegistrationTest() {
		
		// registration: sign_up / sign_in / sign_out
		// ----------------------------------------------
		String email = MVPApi.generateUniqueEmail();
		String password = "qqqqqq";
		
		AccountResult ar = MVPApi.signUp(email, password);
		Assert.assertTrue(ar.isOK(), "[sign_up] OK");
		
		BaseResult r = MVPApi.signOut(ar.token);
		Assert.assertTrue(r.isOK(), "[sign_out] OK");
		
		ar = MVPApi.signIn(email, password);
		Assert.assertTrue(ar.isOK(), "[sign_in] OK");
		
		return ar.token;
	}
	
	public void runProfileTest(String token) {
		
		// profile: create / update / get
		// ----------------------------------------------
		long timestamp = System.currentTimeMillis() / 1000;
		ProfileData profile = DataGenerator.generateRandomProfile(timestamp, null);
		
		BaseResult r = MVPApi.createProfile(token, profile);
		errors.add(Verify.verifyTrue(r.isOK(), "[profile] Create Profile OK"));
		
		profile.setWeight(profile.getWeight() + 1);
		r = MVPApi.updateProfile(token, profile);
		errors.add(Verify.verifyTrue(r.isOK() || r.isExisted(), "[profile] Update Profile OK"));
		
		r = MVPApi.getProfile(token);
		errors.add(Verify.verifyTrue(r.isOK(), "[profile] Get Profile OK"));
	}
	
	public void runPedometerTest(String token) {
		
		// pedometer: create / update / get / delete / linking_status
		// ----------------------------------------------
		long timestamp = System.currentTimeMillis() / 1000;
		Pedometer pedo = DataGenerator.generateRandomPedometer(timestamp, null);
		
		BaseResult r = MVPApi.createPedometer(token, pedo);
		errors.add(Verify.verifyTrue(r.isOK(), "[pedometer] Create Pedometer (link) OK"));
		
		r = MVPApi.getDeviceLinkingStatusRaw(token, pedo.getSerialNumberString());
		errors.add(Verify.verifyTrue(r.isOK(), "[pedometer] Get linking status OK"));
		
		r = MVPApi.unlinkDeviceRaw(token);
		errors.add(Verify.verifyTrue(r.isOK(), "[pedometer] Delete Pedometer (unlink) OK"));
		
		// TODO:
		// in the future, we may not allow modify serial number in update route
		// we will provide an api to unlink device
		// when that happen, change the test below
		pedo.setSerialNumberString(TextTool.getRandomString(10, 10));
		r = MVPApi.updatePedometer(token, pedo);
		errors.add(Verify.verifyTrue(r.isOK() || r.isExisted(), "[pedometer] Update Pedometer OK"));

	}
	
	public void runGoalTest(String token) {

		// goal: create / update / get / search
		// ----------------------------------------------
		long timestamp = System.currentTimeMillis() / 1000;
		Goal goal = Goal.getDefaultGoal();
		
		BaseResult r = MVPApi.createGoal(token, goal);
		errors.add(Verify.verifyTrue(r.isOK(), "[goal] Create Goal OK"));
		String goalId = Goal.getGoal(r.response).getServerId();
		goal.setServerId(goalId);
		
		goal.getProgressData().setCalorie(MVPCommon.randInt(1000, 2000) * 1d);
		r = MVPApi.updateGoal(token, goal);
		errors.add(Verify.verifyTrue(r.isOK() || r.isExisted(), "[goal] Update Goal OK"));
		
		r = MVPApi.getGoal(token, goalId);
		errors.add(Verify.verifyTrue(r.isOK(), "[goal] Get Goal OK"));
		
		r = MVPApi.searchGoal(token, goal.getStartTime() - 1, goal.getStartTime() + 1, null);
		errors.add(Verify.verifyTrue(r.isOK(), "[goal] Search goal (start - end) OK"));
		
		r = MVPApi.searchGoal(token, null, null, timestamp - 100);
		errors.add(Verify.verifyTrue(r.isOK(), "[goal] Search goal (modified since) OK"));
		
		r = MVPApi.searchGoal(token, null, null, null);
		errors.add(Verify.verifyTrue(r.isOK(), "[goal] Search goal (all) OK"));
	}
	
	public void runGoalProgressAndGoalSettingsTest(String token) {
		
		// goal settings: get / set
		// ----------------------------------------------
		long timestamp = System.currentTimeMillis() / 1000;
		int wType = GoalProgress.GOAL_PROGRESS_TYPE_WEIGHT;
		int sType = GoalProgress.GOAL_PROGRESS_TYPE_SLEEP;
		GoalSettings wGoalSettings = DataGenerator.geenrateRandomGoalSettings(timestamp, wType, null);
		GoalSettings sGoalSettings = DataGenerator.geenrateRandomGoalSettings(timestamp, sType, null);
		
		BaseResult r = MVPApi.setGoalSettings(token, wType, wGoalSettings);
		errors.add(Verify.verifyTrue(r.isOK(), "[goal_settings] Set weight goal settings OK"));
		
		r = MVPApi.setGoalSettings(token, sType, sGoalSettings);
		errors.add(Verify.verifyTrue(r.isOK(), "[goal_settings] Set sleep goal settings OK"));
		
		r = MVPApi.getGoalSettings(token, wType);
		errors.add(Verify.verifyTrue(r.isOK(), "[goal_settings] Get weight goal settings OK"));
		
		r = MVPApi.getGoalSettings(token, wType);
		errors.add(Verify.verifyTrue(r.isOK(), "[goal_settings] Get sleep goal settings OK"));
		
		
		// goal progress: create / update / search
		// ----------------------------------------------
		GoalProgress wGoalProgress = DataGenerator.generateRandomGoalProgress(timestamp, wType, null);
		GoalProgress sGoalProgress = DataGenerator.generateRandomGoalProgress(timestamp, sType, null);
		
		r = MVPApi.createGoalProgress(token, wType, wGoalProgress);
		errors.add(Verify.verifyTrue(r.isOK(), "[goal_progress] Create Weight goal progress OK"));
		wGoalProgress.setServerId(WeightGoalProgress.getWeightGoalProgressFromResponse(r.response).getServerId());
		
		r = MVPApi.createGoalProgress(token, sType, sGoalProgress);
		errors.add(Verify.verifyTrue(r.isOK(), "[goal_progress] Create Sleep goal progress OK"));
		sGoalProgress.setServerId(SleepGoalProgress.getSleepGoalProgressFromResponse(r.response).getServerId());
		
		r = MVPApi.updateGoalProgress(token, wGoalProgress);
		errors.add(Verify.verifyTrue(r.isOK() || r.isExisted(), "[goal_progress] Update Weight goal progress OK"));
		
		r = MVPApi.updateGoalProgress(token, sGoalProgress);
		errors.add(Verify.verifyTrue(r.isOK() || r.isExisted(), "[goal_progress] Update Sleep goal progress OK"));
		
		r = MVPApi.searchGoalProgress(token, wType, timestamp - 100, timestamp + 100, null);
		errors.add(Verify.verifyTrue(r.isOK(), "[goal_progress] Search Weight goal progress (start - end) OK"));
		
		r = MVPApi.searchGoalProgress(token, sType, timestamp - 100, timestamp + 100, null);
		errors.add(Verify.verifyTrue(r.isOK(), "[goal_progress] Search Sleep goal progress (start - end) OK"));
		
		r = MVPApi.searchGoalProgress(token, wType, null, null, timestamp - 100);
		errors.add(Verify.verifyTrue(r.isOK(), "[goal_progress] Search Weight goal progress (modified since) OK"));
		
		r = MVPApi.searchGoalProgress(token, sType, null, null, timestamp - 100);
		errors.add(Verify.verifyTrue(r.isOK(), "[goal_progress] Search Sleep goal progress (modified since) OK"));
		
		r = MVPApi.searchGoalProgress(token, wType, null, null, null);
		errors.add(Verify.verifyTrue(r.isOK(), "[goal_progress] Search Weight goal progress (all) OK"));
		
		r = MVPApi.searchGoalProgress(token, sType, null, null, null);
		errors.add(Verify.verifyTrue(r.isOK(), "[goal_progress] Search Sleep goal progress (all) OK"));

	}
 
	public void runTimelineItemTest(String token) {
		
		// timeline items: create / batch_create / update / get / search
		// ----------------------------------------------
		long timestamp = System.currentTimeMillis() / 1000;
		List<TimelineItem> timelineItems = new ArrayList<TimelineItem>();
		timelineItems.add(DataGenerator.generateRandomActivitySessionTimelineItem(timestamp, null));
		timelineItems.add(DataGenerator.generateRandomLifetimeDistanceItem(timestamp, null));
		timelineItems.add(DataGenerator.generateRandomMilestoneItem(timestamp, TimelineItemDataBase.EVENT_TYPE_100_GOAL, null));
		timelineItems.add(DataGenerator.generateRandomSleepTimelineItem(timestamp, null));
		timelineItems.add(DataGenerator.generateRandomTimezoneTimelineItem(timestamp, null));
		
		BaseResult r = MVPApi.createTimelineItem(token, DataGenerator.generateRandomFoodTimelineItem(timestamp, null));
		errors.add(Verify.verifyTrue(r.isOK(), "[timeline_items] Create food item OK"));
		
		r = MVPApi.createTimelineItem(token, timelineItems.get(0));
		errors.add(Verify.verifyTrue(r.isOK(), "[timeline_items] Create normal timeline item OK"));
		timelineItems.get(0).setServerId(TimelineItem.getTimelineItem(r.response).getServerId());
		
		r = MVPApi.createTimelineItemsRaw(token, timelineItems);
		errors.add(Verify.verifyTrue(r.isOK() || r.isExisted(), "[timeline_items] Create timeline items (batch) OK"));
		
		timelineItems.get(0).setState(1);
		r = MVPApi.updateTimelineItem(token, timelineItems.get(0));
		errors.add(Verify.verifyTrue(r.isOK() || r.isExisted(), "[timeline_items] Update timeline item OK"));
		
		r = MVPApi.getTimelineItemRaw(token, timelineItems.get(0).getServerId());
		errors.add(Verify.verifyTrue(r.isOK(), "[timeline_items] Get timeline item OK"));
		
		r = MVPApi.getTimelineItemsRaw(token, timestamp - 3600, timestamp + 3600, null, null);
		errors.add(Verify.verifyTrue(r.isOK(), "[timeline_items] Search timeline items (start, end) OK"));
		
		r = MVPApi.getTimelineItemsRaw(token, null, null, timestamp - 1000, null);
		errors.add(Verify.verifyTrue(r.isOK(), "[timeline_items] Search timeline items (modified since) OK"));
		
		r = MVPApi.getTimelineItemsRaw(token, null, null, null, null);
		errors.add(Verify.verifyTrue(r.isOK(), "[timeline_items] Search timeline items (all) OK"));

		r = MVPApi.getTimelineItemsRaw(token, null, null, null, 2);
		errors.add(Verify.verifyTrue(r.isOK(), "[timeline_items] Search timeline items (all with specific type) OK"));
	}
	
	public void runGraphItemTest(String token) {
		
		// graph items: create / batch_create / get / search
		// ----------------------------------------------
		long timestamp = System.currentTimeMillis() / 1000;
		List<GraphItem> graphItems = new ArrayList<GraphItem>();
		for(int i = 0; i < 5; i++)
			graphItems.add(DataGenerator.generateRandomGraphItem(timestamp + i, null));
		
		BaseResult r = MVPApi.createGraphItem(token, graphItems.get(0));
		errors.add(Verify.verifyTrue(r.isOK(), "[graph_item] Create graph item OK"));
		graphItems.get(0).setServerId(GraphItem.getGraphItem(r.response).getServerId());
		
		r = MVPApi.createGraphItemsRaw(token, graphItems);
		errors.add(Verify.verifyTrue(r.isOK() || r.isExisted(), "[graph_item] Create graph items (batch) OK"));
		
		r = MVPApi.getGraphItemRaw(token, graphItems.get(0).getServerId());
		errors.add(Verify.verifyTrue(r.isOK(), "[graph_item] Get graph item OK"));
		
		r = MVPApi.getGraphItemsRaw(token, timestamp - 1000, timestamp + 1000, null);
		errors.add(Verify.verifyTrue(r.isOK(), "[graph_item] Search graph items (start, end) OK"));
		
		r = MVPApi.getGraphItemsRaw(token, null, null, timestamp - 1000);
		errors.add(Verify.verifyTrue(r.isOK(), "[graph_item] Search graph items (modified since) OK"));
		
		r = MVPApi.getGraphItemsRaw(token, null, null, null);
		errors.add(Verify.verifyTrue(r.isOK(), "[graph_item] Search graph items (all) OK"));
	}

	public void runBedditTest(String token) {

		// beddit: create / batch_create / update / get
		// ----------------------------------------------
		long timestamp = System.currentTimeMillis() / 1000;
		List<BedditSleepSession> bedditSleeps = new ArrayList<BedditSleepSession>();
		for(int i = 5; i > 0; i--)
			bedditSleeps.add(DataGenerator.generateRandomBedditSleepSession(timestamp - 3600 * 24 * i, null));
				
		BaseResult r = MVPApi.createBedditSleepSession(token, bedditSleeps.get(0));
		errors.add(Verify.verifyTrue(r.isOK(), "[beddit_sleep] Create beddit sleep OK"));
		bedditSleeps.get(0).setServerId(BedditSleepSession.getBedditSleepSessionFromResponse(r.response).getServerId());
		
		r = MVPApi.createBedditSleepSessions(token, bedditSleeps);
		errors.add(Verify.verifyTrue(r.isOK() || r.isExisted(), "[beddit_sleep] Create beddit sleeps (batch) OK"));
		
		bedditSleeps.get(0).getProperties().setNormalizedSleepQuality(MVPCommon.randInt(60, 90));
		r = MVPApi.updateBedditSleepSession(token, bedditSleeps.get(0));
		errors.add(Verify.verifyTrue(r.isOK() || r.isExisted(), "[beddit_sleep] Update beddit sleep OK"));
		
		r = MVPApi.searchBedditSleepSessions(token, timestamp - 3600, timestamp + 3600, null);
		errors.add(Verify.verifyTrue(r.isOK(), "[beddit_sleep] Search beddit sleeps (start, end) OK"));
		
		r = MVPApi.searchBedditSleepSessions(token, null, null, timestamp - 1000);
		errors.add(Verify.verifyTrue(r.isOK(), "[beddit_sleep] Search beddit sleeps (modified since) OK"));
		
		r = MVPApi.searchBedditSleepSessions(token, null, null, null);
		errors.add(Verify.verifyTrue(r.isOK(), "[beddit_sleep] Search beddit sleeps (all) OK"));
	}

	public void runStatisticsTest(String token) {

		// statistics: create / update / get
		// ----------------------------------------------
		long timestamp = System.currentTimeMillis() / 1000;
		Statistics statistics = DataGenerator.generateRandomStatistics(timestamp, null);
		
		BaseResult r = MVPApi.createStatistics(token, statistics);
		errors.add(Verify.verifyTrue(r.isOK(), "[statistics] Create statistics OK"));
		
		statistics.setBestStreak(MVPCommon.randInt(10, 20));
		r = MVPApi.updateStatistics(token, statistics);
		errors.add(Verify.verifyTrue(r.isOK() || r.isExisted(), "[statistics] Update statistics OK"));
		
		r = MVPApi.getStatisticsRaw(token);
		errors.add(Verify.verifyTrue(r.isOK(), "[statistics] Get statistics OK"));
	}

	public void runSummaryTest(String token) {

		// summary: month / week
		// ----------------------------------------------
		long timestamp = System.currentTimeMillis() / 1000;
		
		BaseResult r = MVPApi.getSummaryByWeek(token, MVPCommon.getDateString(timestamp));
		errors.add(Verify.verifyTrue(r.isOK(), "[summary] Get weekly summary OK"));
		
		r = MVPApi.getSummaryByMonth(token, MVPCommon.getDateString(timestamp));
		errors.add(Verify.verifyTrue(r.isOK(), "[summary] Get monthly summary OK"));
	}

	public void runMiscTest(String token) {

		// misc: push_sync_log / user_info
		// ----------------------------------------------
		SyncLog syncLog = DataGenerator.generateRandomSyncLog(
				System.currentTimeMillis() / 1000, 2, 100, null);
		
		BaseResult r = MVPApi.pushSyncLog(token, syncLog);
		errors.add(Verify.verifyTrue(r.isOK(), "[push_sync_log] Push iOS sync log OK")); 
		
		r = MVPApi.userInfo(token);
		errors.add(Verify.verifyTrue(r.isOK(), "[user_info] Get user info OK")); 
	}

	
	public void runFriendRequestTest(String myToken, String myUid, String friendToken, String friendUid) {
		
		List<String> friendUids = new ArrayList<String>();
		friendUids.add(friendUid);
		friendUids.add(friendUid);
		
		// friend request: send (multiple) / accept / ignore / delete / cancel / list
		//                 get request from / to
		// ----------------------------------------------
		BaseResult r = SocialAPI.sendFriendRequest(myToken, friendUid);
		errors.add(Verify.verifyTrue(r.isOK(), "[friend_request] Send friend request OK"));
		
		r = SocialAPI.acceptFriendRequest(friendToken, myUid);
		errors.add(Verify.verifyTrue(r.isOK(), "[friend_request] Accept friend request OK"));
		
		r = SocialAPI.getFriends(myToken);
		errors.add(Verify.verifyTrue(r.isOK(), "[friend_request] Get friends list OK"));
		
		r = SocialAPI.getFriendsWithAverageUsers(myToken);
		errors.add(Verify.verifyTrue(r.isOK(), "[friend_request] Get friends list with average users OK"));
		
		r = SocialAPI.deleteFriend(myToken, friendUid);
		errors.add(Verify.verifyTrue(r.isOK(), "[friend_request] Delete friend OK"));
		
		
		r = SocialAPI.sendFriendRequests(myToken, friendUids);
		errors.add(Verify.verifyTrue(r.isOK(), "[friend_request] Send friend requests (multiple) OK"));
		
		r = SocialAPI.getFriendRequestsFromMe(myToken);
		errors.add(Verify.verifyTrue(r.isOK(), "[friend_request] Get friend requests from me OK"));
		
		r = SocialAPI.getFriendRequestsToMe(friendToken);
		errors.add(Verify.verifyTrue(r.isOK(), "[friend_request] Get friend requests to me OK"));
	
		r = SocialAPI.ignoreFriendRequest(friendToken, myUid);
		errors.add(Verify.verifyTrue(r.isOK(), "[friend_request] Ignore friend request OK"));
		
		r = SocialAPI.cancelFriendRequest(myToken, friendUid);
		errors.add(Verify.verifyTrue(r.isOK(), "[friend_request] Cancel friend request OK"));
	}
	
	public void runDashboardTest(String myToken) {
		
		// dashboard: leaderboard / world_feed
		// ----------------------------------------------
		BaseResult r = SocialAPI.getLeaderboardInfo(myToken);
		errors.add(Verify.verifyTrue(r.isOK(), "[dashboard] Get leaderboard OK"));
		
		r = SocialAPI.getLeaderboardInfoWithAverageUsers(myToken);
		errors.add(Verify.verifyTrue(r.isOK(), "[dashboard] Get leaderboard with average users OK"));
		
		r = SocialAPI.getWorldInfo(myToken);
		errors.add(Verify.verifyTrue(r.isOK(), "[dashboard] Get world feed OK"));
	}
	
	public void runSocialMiscTest(String myToken) {

		List<String> emails = new ArrayList<String>();
		emails.add("shine_backend_smoke_test_me@qa.com");
		emails.add("shine_backend_smoke_test_you@qa.com");
		
		
		// misc: match contacts / search
		// ----------------------------------------------
		BaseResult r = SocialAPI.searchSocialUsers(myToken, "shine_backend");
		errors.add(Verify.verifyTrue(r.isOK(), "[social_misc] Search social users OK"));
		
		r = SocialAPI.matchContacts(myToken, emails);
		errors.add(Verify.verifyTrue(r.isOK(), "[social_misc] Match contacts OK"));
	}

}
