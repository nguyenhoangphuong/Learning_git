package com.misfit.ta.backend.aut.correctness.backendapi;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.misfit.ta.backend.api.metawatch.MVPApi;
import com.misfit.ta.backend.aut.BackendAutomation;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.backend.data.goalprogress.GoalProgress;
import com.misfit.ta.backend.data.goalprogress.sleep.SleepGoalProgress;
import com.misfit.ta.backend.data.goalprogress.weight.WeightGoalProgress;
import com.misfit.ta.common.MVPCommon;
import com.misfit.ta.utils.TextTool;


public class BackendGoalProgressUpdateTC extends BackendAutomation {
	
	private int sleepType = GoalProgress.GOAL_PROGRESS_TYPE_SLEEP;
	private int weightType = GoalProgress.GOAL_PROGRESS_TYPE_WEIGHT;
	

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "UpdateGoalProgress" })
	public void UpdateGoalsProgress_WithInvalidServerId() {
		
		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), "qqqqqq").token;
		
		// no server id
		GoalProgress progress = SleepGoalProgress.getDefaultSleepGoalProgress();
		BaseResult result = MVPApi.updateGoalProgress(token, progress);
		Assert.assertEquals(result.statusCode, 404, "Status code");
		
		
		// invalid serverid
		progress.setServerId(TextTool.getRandomString(10, 20));
		result = MVPApi.updateGoalProgress(token, progress);
		Assert.assertEquals(result.statusCode, 404, "Status code");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "UpdateGoalProgress" })
	public void UpdateGoalsProgress_WithValidServerId() {
		
		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), "qqqqqq").token;
		
		// create progress
		SleepGoalProgress sleepProgress = SleepGoalProgress.getDefaultSleepGoalProgress();
		WeightGoalProgress weightProgress = WeightGoalProgress.getDefaultWeightGoalProgress();
		
		BaseResult result1 = MVPApi.createGoalProgress(token, sleepType, sleepProgress);
		BaseResult result2 = MVPApi.createGoalProgress(token, weightType, weightProgress);
		
		sleepProgress.setServerId(SleepGoalProgress.getSleepGoalProgressFromResponse(result1.response).getServerId());
		weightProgress.setServerId(WeightGoalProgress.getWeightGoalProgressFromResponse(result2.response).getServerId());
		
		// update / cache checking
		for(int i = 0; i < MVPApi.CACHE_TRY_TIME; i++) {
			
			sleepProgress.setDuration(MVPCommon.randInt(10, 600));
			weightProgress.setProgressValue(MVPCommon.randInt(100, 200));
			
			MVPApi.updateGoalProgress(token, sleepProgress);
			MVPApi.updateGoalProgress(token, weightProgress);
			
			result1 = MVPApi.searchGoalProgress(token, sleepType, null, null, null);
			result2 = MVPApi.searchGoalProgress(token, weightType, null, null, null);
			
			SleepGoalProgress sSleepProgress = SleepGoalProgress.getSleepGoalProgressesFromResponse(result1.response).get(0);
			WeightGoalProgress sWeightProgress = WeightGoalProgress.getWeightGoalProgressesFromResponse(result2.response).get(0);
			
			Assert.assertEquals(sSleepProgress.getDuration(), sleepProgress.getDuration(), "Sleep duration");
			Assert.assertEquals(sWeightProgress.getProgressValue(), weightProgress.getProgressValue(), "Weight progress value");
		}
		
		
	}
	
}
