package com.misfit.ta.backend;

import java.io.FileNotFoundException;
import java.util.Calendar;

import org.apache.log4j.Logger;
import org.graphwalker.Util;

import com.misfit.ta.backend.api.internalapi.MVPApi;
import com.misfit.ta.backend.aut.BackendHelper;
import com.misfit.ta.backend.aut.correctness.servercalculation.ServerCalculationTestHelpers;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.backend.data.profile.ProfileData;
import com.misfit.ta.backend.data.servercalculation.ServerCalculationCursor;
import com.misfit.ta.common.MVPCommon;

public class Debug {

	protected static Logger logger = Util.setupLogger(Debug.class);
	
	public static void main(String[] args) throws FileNotFoundException {
	
		ProfileData data = new ProfileData();
		data.setHeight(-1d);
		MVPApi.updateProfile(MVPApi.signIn("nhhai16991@gmail.com", "qqqqqq").token, data);
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
