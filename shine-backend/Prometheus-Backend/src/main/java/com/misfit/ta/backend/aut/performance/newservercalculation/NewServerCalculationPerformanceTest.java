package com.misfit.ta.backend.aut.performance.newservercalculation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.graphwalker.Util;

import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.backend.aut.ResultLogger;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.backend.data.servercalculation.ServerCalculationCursor;
import com.misfit.ta.base.SeedThread;
import com.misfit.ta.base.SeedThreadParallelExecutor;
import com.misfit.ta.utils.ShortcutsTyper;

public class NewServerCalculationPerformanceTest {
	protected static Logger logger = Util.setupLogger(MVPApi.class);
	public static ResultLogger resultLogger = ResultLogger.getLogger("data_collector_" + System.nanoTime());
	protected static List<Long> seedRunningTime = Collections.synchronizedList(new ArrayList<Long>());
	public static void main(String[] args) {
		if (args.length == 0) {
			args = new String[] { "10", "10" };
		}
		
		int numberOfSeed = Integer.valueOf(args[0]);
		int numberOfThread = Integer.valueOf(args[1]);
		SeedThread seed = new NewServerCalculationSeed();
		SeedThreadParallelExecutor executor = new SeedThreadParallelExecutor(seed, numberOfSeed, numberOfThread);
		executor.execute();
		String email = MVPApi.generateUniqueEmail();
		NewServerCalculationScenario lastScenarioTest = new NewServerCalculationScenario();
		long startTime = lastScenarioTest.runNewServerCalculationIntgerationTest(email);
		long delayTime = 120000;
		
		String token = MVPApi.signIn(email, "qqqqqq").token;
		
		BaseResult result = MVPApi.getCursors(token);
		ServerCalculationCursor cursor =  ServerCalculationCursor.fromResponse(result.response);
		while (cursor != null && cursor.getUpdatedAt() == null) {
			logger.info("Waiting " + delayTime + " miliseconds");
			ShortcutsTyper.delayTime(delayTime);
			result = MVPApi.getCursors(token);
			if (result != null) {
				cursor = ServerCalculationCursor.fromResponse(result.response);
			}
		}
		long runningTime = -1;
		if (result.statusCode == 200) {
			long endTime = cursor.getUpdatedAt();
			logger.info("End time for email " + email + ": " + endTime);
			System.out.println("End time for email " + email + ": " + endTime);
			runningTime = endTime - startTime;
		}
		System.out.println(runningTime);
		
	}

	public static class NewServerCalculationSeed extends SeedThread {
		protected static Logger logger = Util.setupLogger(MVPApi.class);
		public void run() {
			NewServerCalculationScenario scenarioTest = new NewServerCalculationScenario();
			scenarioTest.runNewServerCalculationIntegrationTest();
		}

		public SeedThread duplicate() {
			return new NewServerCalculationSeed();
		}
	}
}
