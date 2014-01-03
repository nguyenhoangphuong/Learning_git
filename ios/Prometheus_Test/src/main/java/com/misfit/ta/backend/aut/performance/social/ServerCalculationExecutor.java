package com.misfit.ta.backend.aut.performance.social;

import com.misfit.ta.base.ParallelThreadExecutor;

public class ServerCalculationExecutor {

	public static void main(String[] args) {
				
		ServerCalculationSeed seed = new ServerCalculationSeed();
		int numberOfSeed = 1000;
		int numberOfThread = 200;
		
		ParallelThreadExecutor executor = new ParallelThreadExecutor(seed, numberOfSeed, numberOfThread);
		executor.execute();
		executor.printSummary();
	}
}
