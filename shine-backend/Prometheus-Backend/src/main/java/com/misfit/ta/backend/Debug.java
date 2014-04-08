package com.misfit.ta.backend;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.graphwalker.Util;

import com.misfit.ta.backend.api.internalapi.MVPApi;
import com.misfit.ta.backend.aut.BackendHelper;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.backend.data.DataGenerator;
import com.misfit.ta.backend.data.beddit.BedditSleepSession;

public class Debug {

	protected static Logger logger = Util.setupLogger(Debug.class);
	
	public static void main(String[] args) throws FileNotFoundException {
	
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
		
		BackendHelper.link("nhhai16991@gmail.com", "qqqqqq", "HaiDangYeu");
		
//		Files.delete("rawdata");
//		Files.getFile("rawdata");
//		MVPApi.pushSDKSyncLog(ServerCalculationTestHelpers.createSDKSyncLogFromFilesInFolder(
//				System.currentTimeMillis() / 1000, 
//				"a@a.a", "adsasdasds", "rawdata/test1/1392170920"));
	}
}
