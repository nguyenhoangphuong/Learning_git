package com.misfit.ta.backend.aut.performance.social;

import org.apache.log4j.Logger;
import org.graphwalker.Util;

import com.misfit.ta.backend.api.internalapi.MVPApi;
import com.misfit.ta.backend.aut.ResultLogger;
import com.misfit.ta.backend.aut.correctness.servercalculation.BackendServerCalculationTest;
import com.misfit.ta.base.SeedThread;
import com.misfit.ta.base.ServerResultSummary;

public class ServerCalculationSeed extends SeedThread {

	protected static Logger logger = Util.setupLogger(MVPApi.class);
	
	// static fields
	public static ResultLogger resultLogger = ResultLogger.getLogger("user_generate_seed_" + System.nanoTime());
	public static ServerResultSummary summary = new ServerResultSummary();
	public static int numberOfUserFullyCreated = 0;
	public static int numberOfUserCreated = 0;
	
	public static long globalStartTime = 0;
	public static long globalEndTime = 0;


	// implements intefaces
	public void run() {
		
		BackendServerCalculationTest test = new BackendServerCalculationTest();
		test.runPerformance();
	}
	
	public SeedThread duplicate() {
		
		return new ServerCalculationSeed();
	}

}
