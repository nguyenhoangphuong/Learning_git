package com.misfit.ta.backend.aut.correctness.backendapi;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.misfit.ta.backend.api.internalapi.MVPApi;
import com.misfit.ta.backend.aut.BackendAutomation;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.backend.data.DataGenerator;
import com.misfit.ta.backend.data.goal.Goal;
import com.misfit.ta.backend.data.pedometer.Pedometer;
import com.misfit.ta.backend.data.profile.ProfileData;
import com.misfit.ta.backend.data.statistics.Statistics;
import com.misfit.ta.common.MVPCommon;
import com.misfit.ta.utils.TextTool;

public class BackendUserInfoTC extends BackendAutomation {

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "userinfo" })
	public void GetUserInfo() {

		// set up an account with random profile, pedometer, statistics, goal
		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), "qqqqqq").token;
		
		long timestamp = System.currentTimeMillis() / 1000;
		ProfileData profile = DataGenerator.generateRandomProfile(timestamp, null);
		Pedometer pedometer = DataGenerator.generateRandomPedometer(timestamp, null);
		Statistics statistics = DataGenerator.generateRandomStatistics(timestamp, null);
		Goal goal = DataGenerator.generateRandomGoal(timestamp, null);
		
		MVPApi.createGoal(token, goal);
		MVPApi.createProfile(token, profile);
		MVPApi.createPedometer(token, pedometer);
		MVPApi.createStatistics(token, statistics);
		
		
		// get user info and check result
		BaseResult result = MVPApi.userInfo(token);
		ProfileData sprofile = ProfileData.fromResponse(result.response);
		Pedometer spedometer = Pedometer.getPedometer(result.response);
		Statistics sstatistics = Statistics.fromResponse(result.response);
		Goal sgoal = Goal.getGoal(result.response);
		goal.setServerId(sgoal.getServerId());
		
		Assert.assertEquals(profile.getHeight(), sprofile.getHeight(), "Profile height");
		Assert.assertEquals(pedometer.getSerialNumberString(), spedometer.getSerialNumberString(), "Pedometer serial number");
		Assert.assertEquals(statistics.getLifetimeDistance(), sstatistics.getLifetimeDistance(), "Statistics lifetime distance");
		Assert.assertEquals(goal.getProgressData().getPoints(), sgoal.getProgressData().getPoints(), "Goal today point");
		
		
		// update profile, pedometer, statistics and goal
		for(int i = 0; i < 3; i++) {
			
			profile.setName(TextTool.getRandomString(7, 10) + "-" + System.nanoTime());
			pedometer.setBatteryLevel(MVPCommon.randInt(10, 90));
			statistics.setLifetimeDistance(MVPCommon.randInt(10, 500) * 0.1);
			goal.getProgressData().setPoints(MVPCommon.randInt(100, 5000) * 1d);
			
			MVPApi.updateGoal(token, goal);
			MVPApi.updateProfile(token, profile);
			MVPApi.updatePedometer(token, pedometer);
			MVPApi.updateStatistics(token, statistics);
			
			result = MVPApi.userInfo(token);
			sprofile = ProfileData.fromResponse(result.response);
			spedometer = Pedometer.getPedometer(result.response);
			sstatistics = Statistics.fromResponse(result.response);
			sgoal = Goal.getGoal(result.response);
			
			Assert.assertEquals(profile.getHeight(), sprofile.getHeight(), "Profile height - " + i);
			Assert.assertEquals(pedometer.getBatteryLevel(), spedometer.getBatteryLevel(), "Pedometer battery level - " + i);
			Assert.assertEquals(statistics.getLifetimeDistance(), sstatistics.getLifetimeDistance(), "Statistics lifetime distance - " + i);
			Assert.assertEquals(goal.getProgressData().getPoints(), sgoal.getProgressData().getPoints(), "Goal today point - " + i);
		}
	}

}
