package com.misfit.ta.backend;

import java.io.FileNotFoundException;

import org.apache.log4j.Logger;
import org.graphwalker.Util;

import com.misfit.ta.backend.api.internalapi.MVPApi;
import com.misfit.ta.backend.data.goal.Goal;

public class Debug {

	protected static Logger logger = Util.setupLogger(Debug.class);
	
	public static void main(String[] args) throws FileNotFoundException {
	
//		
		String token = MVPApi.signIn("nhhai16991@gmail.com", "qqqqqq").token;
//		BackendHelper.unlink(token);
//		BackendHelper.link(token, "HaiDangYeu");
		Goal goal = Goal.getDefaultGoal(631126800l);
		MVPApi.createGoal(token, goal);
		
//		List<String> emails = new  ArrayList<String>();
//		emails.add("a");
//		emails.add("b");
//		SocialAPI.matchContacts(token, emails);
		
//		BackendHelper.link("haidangyeu@qa.com", "qqqqqq", "HaiDangYeu");
//		ServerCalculationTestHelpers.createTest("tests/test0", "haidangyeu@qa.com", 17, 4, 2014, 19, 4, 2014);
		
		
//		String token = MVPApi.signIn("nhhai16991@gmail.com", "qqqqqq").token;
//		BaseResult result = MVPApi.searchGoalProgress(token, 3, MVPCommon.getDayStartEpoch() - 3600 * 24 * 14, null, null);
//		List<WeightGoalProgress> weights = WeightGoalProgress.getWeighGoalProgressesFromResponse(result.response);
//		for(WeightGoalProgress weight : weights) {
//			logger.info(weight.toJson().toString());
//		}
		
//		List<BedditSleepSession> sleeps = new ArrayList<BedditSleepSession>();
//		sleeps.add(DataGenerator.generateRandomBedditSleepSession(System.currentTimeMillis() / 1000 - 3600 * 24 * 4, null));
//		sleeps.add(DataGenerator.generateRandomBedditSleepSession(System.currentTimeMillis() / 1000 - 3600 * 24 * 3, null));
//		sleeps.add(DataGenerator.generateRandomBedditSleepSession(System.currentTimeMillis() / 1000 - 3600 * 24 * 2, null));
//		sleeps.add(DataGenerator.generateRandomBedditSleepSession(System.currentTimeMillis() / 1000 - 3600 * 24 * 1, null));
//		
//		String token = MVPApi.signIn("testbeddit001@a.a", "qqqqqq").token;
//		BaseResult result = null;
////		result = MVPApi.createBedditSleepSession(token, sleeps.get(0));
////		MVPApi.createBedditSleepSessions(token, sleeps);
//		result = MVPApi.searchBedditSleepSessions(token, System.currentTimeMillis() / 1000 - 3600 * 24 * 15, null, null);
//		List<BedditSleepSession> sleeps_result = BedditSleepSession.getBedditSleepSessionsFromResponse(result.response);
//		sleeps_result.get(0).getProperties().setNormalizedSleepQuality(-1);
//		MVPApi.updateBedditSleepSession(token, sleeps_result.get(0));
	
	}
}
