package com.misfit.ta.backend.aut.performance.servercalculation;

import com.misfit.ta.base.SeedThreadParallelExecutor;

public class ServerCalculationExecutor {

	public static void main(String[] args) {
				
		ServerCalculationSeed seed = new ServerCalculationSeed();
		int numberOfSeed = 1000;
		int numberOfThread = 200;
		
		SeedThreadParallelExecutor executor = new SeedThreadParallelExecutor(seed, numberOfSeed, numberOfThread);
		executor.execute();
		executor.printSummary();
	}
}
