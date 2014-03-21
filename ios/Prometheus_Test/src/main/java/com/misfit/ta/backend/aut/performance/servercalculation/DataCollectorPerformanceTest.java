package com.misfit.ta.backend.aut.performance.servercalculation;

import org.apache.log4j.Logger;
import org.graphwalker.Util;

import com.misfit.ta.backend.api.internalapi.MVPApi;
import com.misfit.ta.backend.aut.ResultLogger;
import com.misfit.ta.backend.aut.correctness.servercalculation.ServerCalculationTestHelpers;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.backend.data.sync.sdk.SDKSyncLog;
import com.misfit.ta.base.SeedThread;
import com.misfit.ta.base.SeedThreadParallelExecutor;

public class DataCollectorPerformanceTest {

	protected static Logger logger = Util.setupLogger(MVPApi.class);
	public static ResultLogger resultLogger = ResultLogger.getLogger("data_collector_" + System.nanoTime());
	
	protected static int[] resultCode = new int[600];
	protected static int numberOfRequest = 0;
	protected static int numberOfResponse = 0;

	private static synchronized void addRequest() {

		numberOfRequest++;
	}

	private static synchronized void addResult(BaseResult result) {

		numberOfResponse++;
		resultCode[result.statusCode]++;
	}
	
	public static void main(String[] args) {
		
		if (args.length == 0) {
			args = new String[] { "20", "200" };
		}
		
		int numberOfSeed = Integer.valueOf(args[0]);
		int numberOfThread = Integer.valueOf(args[1]);
		
		SeedThread seed = new DataCollectorPerformanceSeed();
		SeedThreadParallelExecutor executor = new SeedThreadParallelExecutor(seed, numberOfSeed, numberOfThread);
		executor.execute();

		StringBuffer sb = new StringBuffer();
		sb.append("\nTest statistics: ");
		sb.append(executor.getSummary());
		
		sb.append("\nHTTP statistics: ");
		sb.append("\n------------------------------------------------------------------");
		sb.append("\nTotal requests: " + numberOfRequest);
		sb.append("\nTotal response: " + numberOfResponse);
		sb.append("\nStatus code distribution:");
		
		for(int i = 0; i < resultCode.length; i++) {
			if(resultCode[i] != 0)
				sb.append("\n- " + i + ": " + resultCode[i]);
		}
		sb.append("\n------------------------------------------------------------------\n");
		
		logger.info(sb.toString());
		resultLogger.log(sb.toString());
	}
	
	public static class DataCollectorPerformanceSeed extends SeedThread {

		// implements intefaces
		public void run() {

			SDKSyncLog syncLog = ServerCalculationTestHelpers.createSDKSyncLogFromFilesInFolder(System.currentTimeMillis() / 1000,
					"dc_performance@qa.com", "ABCDEFGHIJ", "rawdata/test1/1392170920");
			
			addRequest();
			addResult(MVPApi.pushSDKSyncLog(syncLog));
		}

		public SeedThread duplicate() {

			return new DataCollectorPerformanceSeed();
		}
	}

}
