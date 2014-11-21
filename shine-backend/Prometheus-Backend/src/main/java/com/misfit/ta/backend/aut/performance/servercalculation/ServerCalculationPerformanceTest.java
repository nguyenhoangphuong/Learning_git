package com.misfit.ta.backend.aut.performance.servercalculation;

import org.apache.log4j.Logger;
import org.graphwalker.Util;

import com.misfit.ta.backend.api.metawatch.MVPApi;
import com.misfit.ta.backend.aut.ResultLogger;
import com.misfit.ta.backend.aut.correctness.servercalculation.BackendServerCalculationIntegration;
import com.misfit.ta.base.SeedThread;
import com.misfit.ta.base.SeedThreadParallelExecutor;

public class ServerCalculationPerformanceTest {

	public static void main(String[] args) {
				
		ServerCalculationSeed seed = new ServerCalculationSeed();
		int numberOfSeed = 1000;
		int numberOfThread = 200;
		
		SeedThreadParallelExecutor executor = new SeedThreadParallelExecutor(seed, numberOfSeed, numberOfThread);
		executor.execute();
		executor.printSummary();
	}
	
	public static class ServerCalculationSeed extends SeedThread {

		protected static Logger logger = Util.setupLogger(MVPApi.class);
		
		// static fields
		public static ResultLogger resultLogger = ResultLogger.getLogger("server_calculation_" + System.nanoTime());
		public static int numberOfUserFullyCreated = 0;
		public static int numberOfUserCreated = 0;
		
		public static long globalStartTime = 0;
		public static long globalEndTime = 0;


		// implements intefaces
		public void run() {
			
			BackendServerCalculationIntegration test = new BackendServerCalculationIntegration();
			test.runPerformance();
		}
		
		public SeedThread duplicate() {
			
			return new ServerCalculationSeed();
		}

	}

}
