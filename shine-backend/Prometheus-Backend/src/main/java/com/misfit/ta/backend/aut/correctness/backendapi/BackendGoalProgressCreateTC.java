package com.misfit.ta.backend.aut.correctness.backendapi;

import java.util.List;

import org.testng.Assert;
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


public class BackendGoalProgressCreateTC extends BackendAutomation {
	
	private int sleepType = GoalProgress.GOAL_PROGRESS_TYPE_SLEEP;
	private int weightType = GoalProgress.GOAL_PROGRESS_TYPE_WEIGHT;
	

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "CreateGoalProgress" })
	public void CreateGoalsProgress_WithInvalidType() {
		
		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), "qqqqqq").token;
		
		// without type param
		BaseResult result = MVPApi.createGoalProgress(token, null, SleepGoalProgress.getDefaultSleepGoalProgress());
		Assert.assertEquals(result.statusCode, 400, "Status code");
		Assert.assertEquals(result.debugMessage, "Missing or invalid goal type parameter", "Debug message");
		
		// with invalid type param
		result = MVPApi.createGoalProgress(token, 999, SleepGoalProgress.getDefaultSleepGoalProgress());
		Assert.assertEquals(result.statusCode, 400, "Status code");
		Assert.assertEquals(result.debugMessage, "Missing or invalid goal type parameter", "Debug message");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "CreateGoalProgress" })
	public void CreateGoalsProgress_WithoutSettings() {
		
		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), "qqqqqq").token;
		
		// create progress
		BaseResult result1 = MVPApi.createGoalProgress(token, sleepType, SleepGoalProgress.getDefaultSleepGoalProgress());
		BaseResult result2 = MVPApi.createGoalProgress(token, weightType, WeightGoalProgress.getDefaultWeightGoalProgress());
		
		Assert.assertEquals(result1.statusCode, 200, "Status code 1");
		Assert.assertEquals(result2.statusCode, 200, "Status code 2");
		
		result1 = MVPApi.searchGoalProgress(token, sleepType, null, null, null);
		result2 = MVPApi.searchGoalProgress(token, weightType, null, null, null);
		SleepGoalProgress sleepProgress = SleepGoalProgress.getSleepGoalProgressesFromResponse(result1.response).get(0);
		WeightGoalProgress weightProgress = WeightGoalProgress.getWeightGoalProgressesFromResponse(result2.response).get(0);
		
		Assert.assertNull(sleepProgress.getSettings(), "Sleep progress's settings");
		Assert.assertNull(weightProgress.getSettings(), "Weight progress's settings");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "CreateGoalProgress" })
	public void CreateGoalsProgress_MultipleProgresses() {
		
		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), "qqqqqq").token;
		
		// without type param
		for(int i = 5; i >= 0; i--) {
			
			long timestamp = System.currentTimeMillis() / 1000 - 3600 * 24 * i;
			SleepGoalProgress sleepProgress = SleepGoalProgress.getDefaultSleepGoalProgress(timestamp);
			WeightGoalProgress weightProgress = WeightGoalProgress.getDefaultWeightGoalProgress(timestamp);
			
			BaseResult result1 = MVPApi.createGoalProgress(token, sleepType, sleepProgress);
			BaseResult result2 = MVPApi.createGoalProgress(token, weightType, weightProgress);

			Assert.assertEquals(result1.statusCode, 200, "Status code 1");
			Assert.assertEquals(result2.statusCode, 200, "Status code 2");
		}
	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "CreateGoalProgress" })
	public void CreateGoalsProgress_Duplicated() {
		
		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), "qqqqqq").token;
		
		// create a progress
		long timestamp = System.currentTimeMillis() / 1000;
		SleepGoalProgress sleepProgress = SleepGoalProgress.getDefaultSleepGoalProgress(timestamp);
		WeightGoalProgress weightProgress = WeightGoalProgress.getDefaultWeightGoalProgress(timestamp);
		
		MVPApi.createGoalProgress(token, sleepType, sleepProgress);
		MVPApi.createGoalProgress(token, weightType, weightProgress);
		
		// by localid: not allow
		sleepProgress.setTimestamp(timestamp - 3600 * 24);
		weightProgress.setTimestamp(timestamp - 3600 * 24);

		BaseResult result1 = MVPApi.createGoalProgress(token, sleepType, sleepProgress);
		BaseResult result2 = MVPApi.createGoalProgress(token, weightType, weightProgress);

		Assert.assertEquals(result1.statusCode, 210, "Status code");
		Assert.assertEquals(result2.statusCode, 210, "Status code");

		// by timestamp: allow because there may be more than 1 sleep progress in 1 day
		sleepProgress.setTimestamp(timestamp);
		sleepProgress.setLocalId(sleepProgress.getLocalId() + "-edited");
		weightProgress.setTimestamp(timestamp);
		weightProgress.setLocalId(weightProgress.getLocalId() + "-edited");

		result1 = MVPApi.createGoalProgress(token, sleepType, sleepProgress);
		result2 = MVPApi.createGoalProgress(token, weightType, weightProgress);

		Assert.assertEquals(result1.statusCode, 200, "Status code");
		Assert.assertEquals(result2.statusCode, 200, "Status code");
	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "CreateGoalProgress" })
	public void CreateGoalProgress_WithValidSettings() {
		
		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), "qqqqqq").token;
		
		// create settings
		SleepGoalSettings sleepSettings = SleepGoalSettings.getDefaultSleepGoalSettings(0);
		WeightGoalSettings weightSettings = WeightGoalSettings.getDefaultWeightGoalSettings();
		
		MVPApi.setGoalSettings(token, GoalSettings.GOAL_SETTINGS_TYPE_SLEEP, sleepSettings);
		MVPApi.setGoalSettings(token, GoalSettings.GOAL_SETTINGS_TYPE_WEIGHT, weightSettings);
		
		
		// create goal progress
		BaseResult result1 = MVPApi.createGoalProgress(token, sleepType, SleepGoalProgress.getDefaultSleepGoalProgress());
		BaseResult result2 = MVPApi.createGoalProgress(token, weightType, WeightGoalProgress.getDefaultWeightGoalProgress());
		
		Assert.assertEquals(result1.statusCode, 200, "Status code 1");
		Assert.assertEquals(result2.statusCode, 200, "Status code 2");
		
		result1 = MVPApi.searchGoalProgress(token, sleepType, null, null, null);
		result2 = MVPApi.searchGoalProgress(token, weightType, null, null, null);
		SleepGoalProgress sleepProgress = SleepGoalProgress.getSleepGoalProgressesFromResponse(result1.response).get(0);
		WeightGoalProgress weightProgress = WeightGoalProgress.getWeightGoalProgressesFromResponse(result2.response).get(0);
		
		Assert.assertEquals(sleepProgress.getSettings().getGoalValue(), sleepSettings.getGoalValue(), "Sleep goal settings value");
		Assert.assertEquals(weightProgress.getSettings().getGoalValue(), weightSettings.getGoalValue(), "Weight goal settings value");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "CreateGoalProgress" })
	public void CreateGoalProgress_WithMultipleSettings() {
		
		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), "qqqqqq").token;
		
		// create settings
		long timestamp = System.currentTimeMillis() / 1000;
		SleepGoalSettings sleepSettings1 = SleepGoalSettings.getDefaultSleepGoalSettings(0);
		SleepGoalSettings sleepSettings2 = SleepGoalSettings.getDefaultSleepGoalSettings(timestamp - 3600 * 24 * 15);
		SleepGoalSettings sleepSettings3 = SleepGoalSettings.getDefaultSleepGoalSettings(timestamp - 3600 * 24 * 5);
		
		sleepSettings2.setGoalValue(9 * 60);
		sleepSettings3.setGoalValue(10 * 60);
		MVPApi.setGoalSettings(token, GoalSettings.GOAL_SETTINGS_TYPE_SLEEP, sleepSettings1);
		MVPApi.setGoalSettings(token, GoalSettings.GOAL_SETTINGS_TYPE_SLEEP, sleepSettings2);
		MVPApi.setGoalSettings(token, GoalSettings.GOAL_SETTINGS_TYPE_SLEEP, sleepSettings3);

		
		// create goal progress
		MVPApi.createGoalProgress(token, sleepType, SleepGoalProgress.getDefaultSleepGoalProgress(timestamp - 3600 * 24 * 16));
		MVPApi.createGoalProgress(token, sleepType, SleepGoalProgress.getDefaultSleepGoalProgress(timestamp - 3600 * 24 * 15));
		MVPApi.createGoalProgress(token, sleepType, SleepGoalProgress.getDefaultSleepGoalProgress(timestamp - 3600 * 24 * 11));
		MVPApi.createGoalProgress(token, sleepType, SleepGoalProgress.getDefaultSleepGoalProgress(timestamp - 3600 * 24 * 10));
		MVPApi.createGoalProgress(token, sleepType, SleepGoalProgress.getDefaultSleepGoalProgress(timestamp));
		
		BaseResult result = MVPApi.searchGoalProgress(token, sleepType, null, null, null);
		List<SleepGoalProgress> sleepProgresses = SleepGoalProgress.getSleepGoalProgressesFromResponse(result.response);
		
		Assert.assertEquals(sleepProgresses.get(0).getSettings().getGoalValue(), sleepSettings1.getGoalValue(), "Sleep goal settings value");
		Assert.assertEquals(sleepProgresses.get(1).getSettings().getGoalValue(), sleepSettings1.getGoalValue(), "Sleep goal settings value");
		Assert.assertEquals(sleepProgresses.get(2).getSettings().getGoalValue(), sleepSettings2.getGoalValue(), "Sleep goal settings value");
		Assert.assertEquals(sleepProgresses.get(3).getSettings().getGoalValue(), sleepSettings2.getGoalValue(), "Sleep goal settings value");
		Assert.assertEquals(sleepProgresses.get(4).getSettings().getGoalValue(), sleepSettings3.getGoalValue(), "Sleep goal settings value");
	}
	
}
