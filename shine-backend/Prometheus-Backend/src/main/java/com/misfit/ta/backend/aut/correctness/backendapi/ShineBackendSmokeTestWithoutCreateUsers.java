package com.misfit.ta.backend.aut.correctness.backendapi;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.google.resting.component.impl.ServiceResponse;
import com.misfit.ta.backend.api.internalapi.MVPApi;
import com.misfit.ta.backend.aut.BackendAutomation;
import com.misfit.ta.backend.aut.DefaultValues;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.backend.data.DataGenerator;
import com.misfit.ta.backend.data.account.AccountResult;
import com.misfit.ta.backend.data.goal.Goal;
import com.misfit.ta.backend.data.goal.GoalsResult;
import com.misfit.ta.backend.data.graph.GraphItem;
import com.misfit.ta.backend.data.statistics.Statistics;
import com.misfit.ta.common.MVPCommon;
import com.misfit.ta.common.Verify;
import com.misfit.ta.utils.ShortcutsTyper;

public class ShineBackendSmokeTestWithoutCreateUsers extends BackendAutomation {

	private List<String> errors = new ArrayList<String>();
	private long DelayTime = 2000;
	
	
	// test methods
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "shineapi_light_smoke_test" })
	public void ShineBackendCommonAPIsSmokeTest() {
		
		errors.clear();
		
		String token = runRegistrationTest();
		runMigrationTest(token);
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
	
	// test helpers

	public void runMigrationTest(String token){
		//Create goal
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		
		Goal goal;
		long startTime = MVPCommon.getDayStartEpoch(calendar.getTimeInMillis() / 1000, TimeZone.getTimeZone("UTC"));
		long endTime = MVPCommon.getDayEndEpoch(calendar.getTimeInMillis() / 1000, TimeZone.getTimeZone("UTC")) - (-1) * 3600;
		
		goal = DataGenerator.generateRandomGoal(0, null);
		goal.setStartTime(startTime);
		goal.setEndTime(endTime);
		goal.setTimeZoneOffsetInSeconds(0 * 3600);

		GoalsResult goalResult = MVPApi.createGoal(token, goal);
		Assert.assertTrue(goalResult.isOK(), "Create goal failed!");
		
		//Create graph item
		List<GraphItem> listGraphItem = new ArrayList<GraphItem>();
		for(int i = 0; i < 55; i++){
			GraphItem graphItem = DefaultValues.RandomGraphItem(2020 * i);
			graphItem.setAverageValue(i * 1.0);
			graphItem.setTimestamp(calendar.getTimeInMillis()/1000);
			System.out.println("Timestamp : " +  graphItem.getTimestamp());
			calendar.set(Calendar.SECOND, calendar.get(Calendar.SECOND) + 2020);
			listGraphItem.add(graphItem);
		}
		
		ServiceResponse serviceResponse = MVPApi.createGraphItems(token, listGraphItem);
		Assert.assertEquals(serviceResponse.getStatusCode(), 200, "Can't not create multiple graph items for user");
	}
	
	public String runRegistrationTest() {
		
		// registration: sign_up / sign_in / sign_out
		// ----------------------------------------------
		String email = MVPApi.generateUniqueEmail();
		String password = "qqqqqq";
		System.out.println("Email : " + email);
		
		AccountResult accountResult = MVPApi.signUp(email, password);
		Assert.assertTrue(accountResult.isOK(), "[sign_up] OK");
		
		AccountResult ar = MVPApi.signIn(email, password);
		
		BaseResult r = MVPApi.signOut(ar.token);
		Assert.assertTrue(r.isOK(), "[sign_out] OK");
		ShortcutsTyper.delayTime(DelayTime);
		
		ar = MVPApi.signIn(email, password);
		Assert.assertTrue(ar.isOK(), "[sign_in] OK");
		ShortcutsTyper.delayTime(DelayTime);
		
		return ar.token;
	}
	
	public void runProfileTest(String token) {
		
		// profile: create / update / get
		// ----------------------------------------------
		
	    BaseResult r = MVPApi.getProfile(token);
		errors.add(Verify.verifyTrue(r.isOK(), "[profile] Get Profile OK"));
	}
	
	public void runPedometerTest(String token) {
		
	    BaseResult r = MVPApi.getDeviceLinkingStatusRaw(token, "haideptrai");
		errors.add(Verify.verifyTrue(r.isOK(), "[pedometer] Get linking status OK"));
		
		r = MVPApi.getPedometerRaw(token);
		errors.add(Verify.verifyTrue(r.isOK(), "[pedometer] Get Pedometer OK"));
		
//		// TODO:
//		// in the future, we may not allow modify serial number in update route
//		// we will provide an api to unlink device
//		// when that happen, change the test below
//		pedo.setSerialNumberString(TextTool.getRandomString(10, 10));
//		r = MVPApi.updatePedometer(token, pedo);
//		errors.add(Verify.verifyTrue(r.isOK() || r.isExisted(), "[pedometer] Update Pedometer OK"));
	}
	
	public void runGoalTest(String token) {
		
	    BaseResult r = MVPApi.searchGoal(token, 0l, new Long(Long.MAX_VALUE), null);
		errors.add(Verify.verifyTrue(r.isOK(), "[goal] Search goal (start - end) OK"));
		
	}
	
	public void runGoalProgressAndGoalSettingsTest(String token) {
		
//		// goal settings: get / set
//		// ----------------------------------------------
//		long timestamp = System.currentTimeMillis() / 1000;
//		int wType = GoalProgress.GOAL_PROGRESS_TYPE_WEIGHT;
//		int sType = GoalProgress.GOAL_PROGRESS_TYPE_SLEEP;
//		GoalSettings wGoalSettings = DataGenerator.geenrateRandomGoalSettings(timestamp, wType, null);
//		GoalSettings sGoalSettings = DataGenerator.geenrateRandomGoalSettings(timestamp, sType, null);
//		
//		BaseResult r = MVPApi.setGoalSettings(token, wType, wGoalSettings);
//		errors.add(Verify.verifyTrue(r.isOK(), "[goal_settings] Set weight goal settings OK"));
//		ShortcutsTyper.delayTime(DelayTime);
//		
//		r = MVPApi.setGoalSettings(token, sType, sGoalSettings);
//		errors.add(Verify.verifyTrue(r.isOK(), "[goal_settings] Set sleep goal settings OK"));
//		ShortcutsTyper.delayTime(DelayTime);
//		
//		r = MVPApi.getGoalSettings(token, wType);
//		errors.add(Verify.verifyTrue(r.isOK(), "[goal_settings] Get weight goal settings OK"));
//		
//		r = MVPApi.getGoalSettings(token, wType);
//		errors.add(Verify.verifyTrue(r.isOK(), "[goal_settings] Get sleep goal settings OK"));
//		
//		
//		// goal progress: create / update / search
//		// ----------------------------------------------
//		GoalProgress wGoalProgress = DataGenerator.generateRandomGoalProgress(timestamp, wType, null);
//		GoalProgress sGoalProgress = DataGenerator.generateRandomGoalProgress(timestamp, sType, null);
//		
//		r = MVPApi.createGoalProgress(token, wType, wGoalProgress);
//		errors.add(Verify.verifyTrue(r.isOK(), "[goal_progress] Create Weight goal progress OK"));
//		ShortcutsTyper.delayTime(DelayTime);
//		wGoalProgress.setServerId(WeightGoalProgress.getWeightGoalProgressFromResponse(r.response).getServerId());
//		
//		r = MVPApi.createGoalProgress(token, sType, sGoalProgress);
//		errors.add(Verify.verifyTrue(r.isOK(), "[goal_progress] Create Sleep goal progress OK"));
//		ShortcutsTyper.delayTime(DelayTime);
//		sGoalProgress.setServerId(SleepGoalProgress.getSleepGoalProgressFromResponse(r.response).getServerId());
//		
//		r = MVPApi.updateGoalProgress(token, wGoalProgress);
//		errors.add(Verify.verifyTrue(r.isOK() || r.isExisted(), "[goal_progress] Update Weight goal progress OK"));
//		ShortcutsTyper.delayTime(DelayTime);
//		
//		r = MVPApi.updateGoalProgress(token, sGoalProgress);
//		errors.add(Verify.verifyTrue(r.isOK() || r.isExisted(), "[goal_progress] Update Sleep goal progress OK"));
//		ShortcutsTyper.delayTime(DelayTime);
//		
//		r = MVPApi.searchGoalProgress(token, wType, timestamp - 100, timestamp + 100, null);
//		errors.add(Verify.verifyTrue(r.isOK(), "[goal_progress] Search Weight goal progress (start - end) OK"));
//		
//		r = MVPApi.searchGoalProgress(token, sType, timestamp - 100, timestamp + 100, null);
//		errors.add(Verify.verifyTrue(r.isOK(), "[goal_progress] Search Sleep goal progress (start - end) OK"));
//		
//		r = MVPApi.searchGoalProgress(token, wType, null, null, timestamp - 100);
//		errors.add(Verify.verifyTrue(r.isOK(), "[goal_progress] Search Weight goal progress (modified since) OK"));
//		
//		r = MVPApi.searchGoalProgress(token, sType, null, null, timestamp - 100);
//		errors.add(Verify.verifyTrue(r.isOK(), "[goal_progress] Search Sleep goal progress (modified since) OK"));
//		
//		r = MVPApi.searchGoalProgress(token, wType, null, null, null);
//		errors.add(Verify.verifyTrue(r.isOK(), "[goal_progress] Search Weight goal progress (all) OK"));
//		
//		r = MVPApi.searchGoalProgress(token, sType, null, null, null);
//		errors.add(Verify.verifyTrue(r.isOK(), "[goal_progress] Search Sleep goal progress (all) OK"));
	}
 
	public void runTimelineItemTest(String token) {
		
		// timeline items: create / batch_create / update / get / search
		// ----------------------------------------------
		
		BaseResult r = MVPApi.getTimelineItemsRaw(token, null, null, System.currentTimeMillis() - 1000, null);
		errors.add(Verify.verifyTrue(r.isOK(), "[timeline_items] Search timeline items (modified since) OK"));
		
		r = MVPApi.getTimelineItemsRaw(token, null, null, null, null);
		errors.add(Verify.verifyTrue(r.isOK(), "[timeline_items] Search timeline items (all) OK"));
		
		r = MVPApi.getTimelineItemsRaw(token, null, null, null, 2);
		errors.add(Verify.verifyTrue(r.isOK(), "[timeline_items] Search timeline items (all with specific type) OK"));
	}
	
	public void runGraphItemTest(String token) {
		
		long timestamp = System.currentTimeMillis();
		BaseResult r = MVPApi.getGraphItemsRaw(token, timestamp - 1000, timestamp + 1000, null);
		errors.add(Verify.verifyTrue(r.isOK(), "[graph_item] Search graph items (start, end) OK"));
		
		r = MVPApi.getGraphItemsRaw(token, null, null, timestamp - 1000);
		errors.add(Verify.verifyTrue(r.isOK(), "[graph_item] Search graph items (modified since) OK"));
		
		r = MVPApi.getGraphItemsRaw(token, null, null, null);
		errors.add(Verify.verifyTrue(r.isOK(), "[graph_item] Search graph items (all) OK"));
	}

	public void runBedditTest(String token) {

		long timestamp = System.currentTimeMillis();
		BaseResult r = MVPApi.searchBedditSleepSessions(token, timestamp - 3600, timestamp + 3600, null);
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
		
//		BaseResult r = MVPApi.createStatistics(token, statistics);
//		errors.add(Verify.verifyTrue(r.isOK(), "[statistics] Create statistics OK"));
//		ShortcutsTyper.delayTime(DelayTime);
//		
//		statistics.setBestStreak(MVPCommon.randInt(10, 20));
//		r = MVPApi.updateStatistics(token, statistics);
//		errors.add(Verify.verifyTrue(r.isOK() || r.isExisted(), "[statistics] Update statistics OK"));
//		ShortcutsTyper.delayTime(DelayTime);
		
		BaseResult r = MVPApi.getStatisticsRaw(token);
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
//		// ----------------------------------------------
//		SyncLog syncLog = DataGenerator.generateRandomSyncLog(
//				System.currentTimeMillis() / 1000, 2, 100, null);
//		
//		BaseResult r = MVPApi.pushSyncLog(token, syncLog);
//		errors.add(Verify.verifyTrue(r.isOK(), "[push_sync_log] Push iOS sync log OK")); 
//		
//		r = MVPApi.userInfo(token);
//		errors.add(Verify.verifyTrue(r.isOK(), "[user_info] Get user info OK")); 
	}

}
