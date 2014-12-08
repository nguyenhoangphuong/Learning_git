package com.misfit.ta.backend.aut.correctness.backendapi;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.google.resting.json.JSONObject;
import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.backend.aut.BackendAutomation;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.backend.data.goalprogress.GoalProgress;
import com.misfit.ta.backend.data.goalprogress.GoalSettings;
import com.misfit.ta.backend.data.goalprogress.sleep.SleepGoalProgress;
import com.misfit.ta.backend.data.goalprogress.sleep.SleepGoalSettings;
import com.misfit.ta.backend.data.goalprogress.weight.WeightGoalSettings;
import com.misfit.ta.common.MVPCommon;


public class BackendGoalSettingsTC extends BackendAutomation {
	
	private int sleepType = GoalProgress.GOAL_PROGRESS_TYPE_SLEEP;
	private int weightType = GoalProgress.GOAL_PROGRESS_TYPE_WEIGHT;
	

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "GoalSettings" })
	public void GetGoalSettings_WithInvalidType() {
		
		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), "qqqqqq").token;
		
		// without type param
		BaseResult result = MVPApi.getGoalSettings(token, null);
		Assert.assertEquals(result.statusCode, 400, "Status code");
		Assert.assertEquals(result.debugMessage, "Missing or invalid goal type parameter", "Debug message");
		
		// with invalid type param
		result = MVPApi.getGoalSettings(token, 999);
		Assert.assertEquals(result.statusCode, 400, "Status code");
		Assert.assertEquals(result.debugMessage, "Missing or invalid goal type parameter", "Debug message");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "GoalSettings" })
	public void GetGoalSettings_NoSettings() {
		
		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), "qqqqqq").token;
		
		BaseResult result1 = MVPApi.getGoalSettings(token, sleepType);
		BaseResult result2 = MVPApi.getGoalSettings(token, weightType);
		
		Assert.assertEquals(result1.statusCode, 200, "Status code 1");
		Assert.assertEquals(result2.statusCode, 200, "Status code 2");
		Assert.assertEquals(result1.getJsonResponseValue("goal_settings"), JSONObject.NULL, "Sleep goal settings");
		Assert.assertEquals(result2.getJsonResponseValue("goal_settings"), JSONObject.NULL, "Weight goal settings");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "GoalSettings" })
	public void GetGoalSettings_CurrentSettings() {
		
		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), "qqqqqq").token;
		
		// create goal settings
		SleepGoalSettings sleepSettings = SleepGoalSettings.getDefaultSleepGoalSettings(0);
		WeightGoalSettings weightSettings = WeightGoalSettings.getDefaultWeightGoalSettings();

		MVPApi.setGoalSettings(token, GoalSettings.GOAL_SETTINGS_TYPE_SLEEP, sleepSettings);
		MVPApi.setGoalSettings(token, GoalSettings.GOAL_SETTINGS_TYPE_WEIGHT, weightSettings);
		
		// set updated settings
		sleepSettings.setGoalValue(sleepSettings.getGoalValue() + 1);
		sleepSettings.setAppliedFrom(System.currentTimeMillis() / 1000);
		weightSettings.setGoalValue(weightSettings.getGoalValue() + 1);
		
		MVPApi.setGoalSettings(token, GoalSettings.GOAL_SETTINGS_TYPE_SLEEP, sleepSettings);
		MVPApi.setGoalSettings(token, GoalSettings.GOAL_SETTINGS_TYPE_WEIGHT, weightSettings);
		
		// get current settings
		BaseResult result1 = MVPApi.getGoalSettings(token, sleepType);
		BaseResult result2 = MVPApi.getGoalSettings(token, weightType);
		
		Assert.assertEquals(result1.statusCode, 200, "Status code 1");
		Assert.assertEquals(result2.statusCode, 200, "Status code 2");
		
		SleepGoalSettings sSleepSettings = SleepGoalSettings.fromResponse(result1.response);
		WeightGoalSettings sWeightSettings = WeightGoalSettings.fromResponse(result2.response);
		
		Assert.assertEquals(sSleepSettings.getGoalValue(), sleepSettings.getGoalValue(), "Sleep goal settings");
		Assert.assertEquals(sSleepSettings.getAppliedFrom(), sleepSettings.getAppliedFrom(), "Sleep goal applied from");
		Assert.assertEquals(sWeightSettings.getGoalValue(), weightSettings.getGoalValue(), "Weight goal settings");
		
		// set updated settings again with applied from < current applied from
		sleepSettings.setGoalValue(sleepSettings.getGoalValue() + 1);
		sleepSettings.setAppliedFrom(System.currentTimeMillis() / 1000 - 3600 * 24 * 10);
		
		MVPApi.setGoalSettings(token, GoalSettings.GOAL_SETTINGS_TYPE_SLEEP, sleepSettings);
		
		// get current settings
		result1 = MVPApi.getGoalSettings(token, sleepType);
		sSleepSettings = SleepGoalSettings.fromResponse(result1.response);
		
		Assert.assertEquals(result1.statusCode, 200, "Status code 1");
		Assert.assertEquals(sSleepSettings.getGoalValue(), sleepSettings.getGoalValue(), "Sleep goal settings");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "GoalSettings" })
	public void SetGoalSettings_WithInvalidType() {

		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), "qqqqqq").token;
		
		// without type param
		BaseResult result = MVPApi.setGoalSettings(token, null, SleepGoalSettings.getDefaultSleepGoalSettings(0));
		Assert.assertEquals(result.statusCode, 400, "Status code");
		Assert.assertEquals(result.debugMessage, "Missing or invalid goal type parameter", "Debug message");
		
		// with invalid type param
		result = MVPApi.setGoalSettings(token, 999, SleepGoalSettings.getDefaultSleepGoalSettings(0));
		Assert.assertEquals(result.statusCode, 400, "Status code");
		Assert.assertEquals(result.debugMessage, "Missing or invalid goal type parameter", "Debug message");
	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "GoalSettings" })
	public void SetGoalSettings_BeforeCreateProgress() {

		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), "qqqqqq").token;
		
		// create settings then progress
		SleepGoalSettings settings = SleepGoalSettings.getDefaultSleepGoalSettings(0);
		MVPApi.setGoalSettings(token, sleepType, settings);
		MVPApi.createGoalProgress(token, sleepType, SleepGoalProgress.getDefaultSleepGoalProgress());
		
		// search and check settings
		BaseResult result = MVPApi.searchGoalProgress(token, sleepType, null, null, null);
		SleepGoalProgress progress = SleepGoalProgress.getSleepGoalProgressesFromResponse(result.response).get(0);
		
		Assert.assertEquals(progress.getSettings().getGoalValue(), settings.getGoalValue(), "Goal settings value");
	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "GoalSettings" })
	public void SetGoalSettings_AfterCreateProgress() {

		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), "qqqqqq").token;
		
		// create progresses
		MVPApi.createGoalProgress(token, sleepType, SleepGoalProgress.getDefaultSleepGoalProgress());
		
		// check settings, should be null
		BaseResult result = MVPApi.searchGoalProgress(token, sleepType, null, null, null);
		SleepGoalProgress progress = SleepGoalProgress.getSleepGoalProgressesFromResponse(result.response).get(0);
		
		Assert.assertNull(progress.getSettings(), "Goal settings");
		
		// create settings
		SleepGoalSettings settings = SleepGoalSettings.getDefaultSleepGoalSettings(0);
		MVPApi.setGoalSettings(token, sleepType, settings);
		
		// search and check settings again, should be applied new settings
		result = MVPApi.searchGoalProgress(token, sleepType, null, null, null);
		progress = SleepGoalProgress.getSleepGoalProgressesFromResponse(result.response).get(0);
		
		Assert.assertEquals(progress.getSettings().getGoalValue(), settings.getGoalValue(), "Goal settings value");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "GoalSettings" })
	public void SetGoalSettings_ForFutureGoalProgresses() {

		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), "qqqqqq").token;
		
		// create default settings
		SleepGoalSettings settings = SleepGoalSettings.getDefaultSleepGoalSettings(0);
		MVPApi.setGoalSettings(token, sleepType, settings);
		
		// create 3 progresses use default settings
		long timestamp = System.currentTimeMillis() / 1000;
		MVPApi.createGoalProgress(token, sleepType, SleepGoalProgress.getDefaultSleepGoalProgress(timestamp - 3600 * 24 * 2));
		MVPApi.createGoalProgress(token, sleepType, SleepGoalProgress.getDefaultSleepGoalProgress(timestamp - 3600 * 24 * 1));
		MVPApi.createGoalProgress(token, sleepType, SleepGoalProgress.getDefaultSleepGoalProgress());
		
		// now create new settings applied from yesterday
		SleepGoalSettings newSettings = SleepGoalSettings.getDefaultSleepGoalSettings(MVPCommon.getDayStartEpoch(timestamp - 3600 * 24));
		newSettings.setGoalValue(settings.getGoalValue() + 60);
		MVPApi.setGoalSettings(token, sleepType, newSettings);
		
		// now yesterday and today goal should use new settings
		BaseResult result = MVPApi.searchGoalProgress(token, sleepType, null, null, null);
		List<SleepGoalProgress> progresses = SleepGoalProgress.getSleepGoalProgressesFromResponse(result.response);
		
		Assert.assertEquals(progresses.get(0).getSettings().getGoalValue(), settings.getGoalValue(), "Goal settings value of 2 days ago progress");
		Assert.assertEquals(progresses.get(1).getSettings().getGoalValue(), newSettings.getGoalValue(), "Goal settings value yesterday progress");
		Assert.assertEquals(progresses.get(2).getSettings().getGoalValue(), newSettings.getGoalValue(), "Goal settings value today progress");
	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "GoalSettings" })
	public void SetGoalSettings_UpdateAndCaching() {
		
		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), "qqqqqq").token;
		
		// create goal settings
		SleepGoalSettings sleepSettings = SleepGoalSettings.getDefaultSleepGoalSettings(0);
		WeightGoalSettings weightSettings = WeightGoalSettings.getDefaultWeightGoalSettings();

		MVPApi.setGoalSettings(token, GoalSettings.GOAL_SETTINGS_TYPE_SLEEP, sleepSettings);
		MVPApi.setGoalSettings(token, GoalSettings.GOAL_SETTINGS_TYPE_WEIGHT, weightSettings);
		
		// set updated settings
		for(int i = 0; i < MVPApi.CACHE_TRY_TIME; i++) {
			
			sleepSettings.setGoalValue(sleepSettings.getGoalValue() + 1);
			weightSettings.setGoalValue(weightSettings.getGoalValue() + 1);

			MVPApi.setGoalSettings(token, GoalSettings.GOAL_SETTINGS_TYPE_SLEEP, sleepSettings);
			MVPApi.setGoalSettings(token, GoalSettings.GOAL_SETTINGS_TYPE_WEIGHT, weightSettings);

			// get current settings
			BaseResult result1 = MVPApi.getGoalSettings(token, sleepType);
			BaseResult result2 = MVPApi.getGoalSettings(token, weightType);

			Assert.assertEquals(result1.statusCode, 200, "Status code 1");
			Assert.assertEquals(result2.statusCode, 200, "Status code 2");

			SleepGoalSettings sSleepSettings = SleepGoalSettings.fromResponse(result1.response);
			WeightGoalSettings sWeightSettings = WeightGoalSettings.fromResponse(result2.response);

			Assert.assertEquals(sSleepSettings.getGoalValue(), sleepSettings.getGoalValue(), "Sleep goal settings");
			Assert.assertEquals(sWeightSettings.getGoalValue(), weightSettings.getGoalValue(), "Weight goal settings");
		}
	}

}
