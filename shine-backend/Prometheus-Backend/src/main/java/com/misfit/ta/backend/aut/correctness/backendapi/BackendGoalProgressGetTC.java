package com.misfit.ta.backend.aut.correctness.backendapi;

import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.misfit.ta.backend.api.metawatch.MVPApi;
import com.misfit.ta.backend.aut.BackendAutomation;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.backend.data.goalprogress.GoalProgress;
import com.misfit.ta.backend.data.goalprogress.GoalSettings;
import com.misfit.ta.backend.data.goalprogress.sleep.SleepGoalProgress;
import com.misfit.ta.backend.data.goalprogress.sleep.SleepGoalSettings;
import com.misfit.ta.backend.data.goalprogress.weight.WeightGoalProgress;
import com.misfit.ta.backend.data.goalprogress.weight.WeightGoalSettings;
import com.misfit.ta.common.MVPCommon;
import com.misfit.ta.utils.ShortcutsTyper;

public class BackendGoalProgressGetTC extends BackendAutomation {
	
	private String token;
	private int sleepType = GoalProgress.GOAL_PROGRESS_TYPE_SLEEP;
	private int weightType = GoalProgress.GOAL_PROGRESS_TYPE_WEIGHT;
	private List<SleepGoalProgress> sleepProgresses = new ArrayList<SleepGoalProgress>();
	private List<WeightGoalProgress> weightProgresses = new ArrayList<WeightGoalProgress>();
	
	@BeforeClass(alwaysRun = true)
	public void setUp() {
		
		token = MVPApi.signUp(MVPApi.generateUniqueEmail(), "qqqqqq").token;
		
		// create goal settings
		SleepGoalSettings sleepSettings = SleepGoalSettings.getDefaultSleepGoalSettings(0);
		WeightGoalSettings weightSettings = WeightGoalSettings.getDefaultWeightGoalSettings();
		
		MVPApi.setGoalSettings(token, GoalSettings.GOAL_SETTINGS_TYPE_SLEEP, sleepSettings);
		MVPApi.setGoalSettings(token, GoalSettings.GOAL_SETTINGS_TYPE_WEIGHT, weightSettings);
		
		for(int i = 4; i >= 0; i--) {
			
			long timestamp = System.currentTimeMillis() / 1000 - i * 3600 * 24;
			SleepGoalProgress sleepProgress = SleepGoalProgress.getDefaultSleepGoalProgress(timestamp);
			WeightGoalProgress weightProgress = WeightGoalProgress.getDefaultWeightGoalProgress(timestamp);
			
			BaseResult r1 = MVPApi.createGoalProgress(token, GoalProgress.GOAL_PROGRESS_TYPE_SLEEP, sleepProgress);
			BaseResult r2 = MVPApi.createGoalProgress(token, GoalProgress.GOAL_PROGRESS_TYPE_WEIGHT, weightProgress);
			
			sleepProgress.setServerId(SleepGoalProgress.getSleepGoalProgressFromResponse(r1.response).getServerId());
			weightProgress.setServerId(WeightGoalProgress.getWeightGoalProgressFromResponse(r2.response).getServerId());
			
			sleepProgresses.add(sleepProgress);
			weightProgresses.add(weightProgress);
		}
	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "GetGoalProgress" })
	public void SearchGoalsProgress_WithInvalidType() {
		
		// without type param
		BaseResult result = MVPApi.searchGoalProgress(token, null, null, null, null);
		Assert.assertEquals(result.statusCode, 400, "Status code");
		Assert.assertEquals(result.debugMessage, "Missing or invalid goal type parameter", "Debug message");
		
		// with invalid type param
		result = MVPApi.searchGoalProgress(token, 999, null, null, null);
		Assert.assertEquals(result.statusCode, 400, "Status code");
		Assert.assertEquals(result.debugMessage, "Missing or invalid goal type parameter", "Debug message");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "GetGoalProgress" })
	public void SearchGoalsProgress_WithTimeRange() {
		
		// from the beginning
		BaseResult result1 = MVPApi.searchGoalProgress(token, sleepType, 0l, null, null);
		BaseResult result2 = MVPApi.searchGoalProgress(token, weightType, 0l, null, null);
		List<SleepGoalProgress> sleepProgresses = SleepGoalProgress.getSleepGoalProgressesFromResponse(result1.response);
		List<WeightGoalProgress> weightProgresses = WeightGoalProgress.getWeightGoalProgressesFromResponse(result2.response);
		
		Assert.assertEquals(sleepProgresses.size(), 5, "Number of sleep progresses");
		Assert.assertEquals(weightProgresses.size(), 5, "Number of weight progresses");
		
		
		// in a speific range
		long startTime = MVPCommon.getDayStartEpoch(System.currentTimeMillis() / 1000 - 3600 * 24 * 4);
		long endTime = MVPCommon.getDayStartEpoch(System.currentTimeMillis() / 1000 - 3600 * 24 * 2);
		result1 = MVPApi.searchGoalProgress(token, sleepType, startTime, endTime, null);
		result2 = MVPApi.searchGoalProgress(token, weightType, startTime, endTime, null);
		sleepProgresses = SleepGoalProgress.getSleepGoalProgressesFromResponse(result1.response);
		weightProgresses = WeightGoalProgress.getWeightGoalProgressesFromResponse(result2.response);
		
		Assert.assertEquals(sleepProgresses.size(), 3, "Number of sleep progresses");
		Assert.assertEquals(weightProgresses.size(), 3, "Number of weight progresses");
		
		
		// start time == end time
		startTime = MVPCommon.getDayStartEpoch();
		endTime = MVPCommon.getDayStartEpoch();
		result1 = MVPApi.searchGoalProgress(token, sleepType, startTime, endTime, null);
		result2 = MVPApi.searchGoalProgress(token, weightType, startTime, endTime, null);
		sleepProgresses = SleepGoalProgress.getSleepGoalProgressesFromResponse(result1.response);
		weightProgresses = WeightGoalProgress.getWeightGoalProgressesFromResponse(result2.response);
		
		Assert.assertEquals(sleepProgresses.size(), 1, "Number of sleep progresses");
		Assert.assertEquals(weightProgresses.size(), 1, "Number of weight progresses");
		
		
		// no goal in that range
		startTime = MVPCommon.getDayStartEpoch(System.currentTimeMillis() / 1000 - 3600 * 24 * 15);
		endTime = MVPCommon.getDayStartEpoch(System.currentTimeMillis() / 1000 - 3600 * 24 * 13);
		result1 = MVPApi.searchGoalProgress(token, sleepType, startTime, endTime, null);
		result2 = MVPApi.searchGoalProgress(token, weightType, startTime, endTime, null);
		sleepProgresses = SleepGoalProgress.getSleepGoalProgressesFromResponse(result1.response);
		weightProgresses = WeightGoalProgress.getWeightGoalProgressesFromResponse(result2.response);
		
		Assert.assertEquals(sleepProgresses.size(), 0, "Number of sleep progresses");
		Assert.assertEquals(weightProgresses.size(), 0, "Number of weight progresses");
		

		// with end time only
		endTime = MVPCommon.getDayStartEpoch(System.currentTimeMillis() / 1000 - 3600 * 24 * 3);
		result1 = MVPApi.searchGoalProgress(token, sleepType, 0l, endTime, null);
		result2 = MVPApi.searchGoalProgress(token, weightType, 0l, endTime, null);
		sleepProgresses = SleepGoalProgress.getSleepGoalProgressesFromResponse(result1.response);
		weightProgresses = WeightGoalProgress.getWeightGoalProgressesFromResponse(result2.response);
		
		Assert.assertEquals(sleepProgresses.size(), 2, "Number of sleep progresses");
		Assert.assertEquals(weightProgresses.size(), 2, "Number of weight progresses");
	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "GetGoalProgress" })
	public void SearchGoalsProgress_WithModifiedSince() {
		
		// wait 5 seconds
		ShortcutsTyper.delayTime(5000);

		
		// search all goals which were modified since 3 seconds ago => no result
		BaseResult result1 = MVPApi.searchGoalProgress(token, sleepType, null, null, System.currentTimeMillis() / 1000 - 3);
		BaseResult result2 = MVPApi.searchGoalProgress(token, weightType, null, null, System.currentTimeMillis() / 1000 - 3);
		List<SleepGoalProgress> ssleepProgresses = SleepGoalProgress.getSleepGoalProgressesFromResponse(result1.response);
		List<WeightGoalProgress> sweightProgresses = WeightGoalProgress.getWeightGoalProgressesFromResponse(result2.response);
		
		Assert.assertEquals(ssleepProgresses.size(), 0, "Number of sleep progresses");
		Assert.assertEquals(sweightProgresses.size(), 0, "Number of weight progresses");
		
		
		// update a progress
		result1 = MVPApi.searchGoalProgress(token, sleepType, null, null, null);
		result2 = MVPApi.searchGoalProgress(token, weightType, null, null, null);
		sleepProgresses.get(0).setDuration(MVPCommon.randInt(100, 600));
		weightProgresses.get(0).setProgressValue(MVPCommon.randInt(100, 200));
		
		MVPApi.updateGoalProgress(token, sleepProgresses.get(0));
		MVPApi.updateGoalProgress(token, weightProgresses.get(0));
		
		
		// search all goals which were modified since 3 seconds ago => 1 result
		result1 = MVPApi.searchGoalProgress(token, sleepType, null, null, System.currentTimeMillis() / 1000 - 3);
		result2 = MVPApi.searchGoalProgress(token, weightType, null, null, System.currentTimeMillis() / 1000 - 3);
		ssleepProgresses = SleepGoalProgress.getSleepGoalProgressesFromResponse(result1.response);
		sweightProgresses = WeightGoalProgress.getWeightGoalProgressesFromResponse(result2.response);

		Assert.assertEquals(ssleepProgresses.size(), 1, "Number of sleep progresses");
		Assert.assertEquals(sweightProgresses.size(), 1, "Number of weight progresses");
		Assert.assertEquals(ssleepProgresses.get(0).getDuration(), sleepProgresses.get(0).getDuration(),
				"Sleep duration updated");
		Assert.assertEquals(sweightProgresses.get(0).getProgressValue(), weightProgresses.get(0).getProgressValue(),
				"Weight progress value updated");
	}

}
