package com.misfit.ta.backend;

import java.io.FileNotFoundException;
import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.apache.http.client.methods.HttpPost;
import org.apache.log4j.Logger;
import org.graphwalker.Util;

public class Debug {

	protected static Logger logger = Util.setupLogger(Debug.class);
	
	public static void main(String[] args) throws FileNotFoundException {
	
		URI uri = UriBuilder.fromUri("https://www.test.com:3000/v3").port(2000).build();
		HttpPost httpPost = new HttpPost(uri);
		logger.info(httpPost.getURI().getHost());
		logger.info(httpPost.getURI().getPort());
		
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
		
//		BackendHelper.link("nhhai16991@gmail.com", "qqqqqq", "HaiDangYeu");
		
		
//		Files.delete("rawdata");
//		Files.getFile("rawdata");
//		MVPApi.pushSDKSyncLog(ServerCalculationTestHelpers.createSDKSyncLogFromFilesInFolder(
//				System.currentTimeMillis() / 1000, 
//				"a@a.a", "adsasdasds", "rawdata/test1/1392170920"));
	}
}
