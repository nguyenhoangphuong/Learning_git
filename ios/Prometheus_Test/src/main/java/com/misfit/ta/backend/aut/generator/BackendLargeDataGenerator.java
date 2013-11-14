package com.misfit.ta.backend.aut.generator;

import com.misfit.ta.Settings;
import com.misfit.ta.backend.seed.GenerateUserSeed;
import com.misfit.ta.base.ParallelThreadExecutor;

public class BackendLargeDataGenerator {

	public static void main(String[] args) {
		
		int goalCount = Settings.getInt("GENERATE_USER_SEED_GOAL_COUNT");
		int tileMin = Settings.getInt("GENERATE_USER_SEED_TILE_MIN");
		int tileMax = Settings.getInt("GENERATE_USER_SEED_TILE_MAX");
		int logCount = Settings.getInt("GENERATE_USER_SEED_SYNCLOG_COUNT");
		
		GenerateUserSeed seed = new GenerateUserSeed(goalCount, tileMin, tileMax, logCount);
		int numberOfSeed = Settings.getInt("NUMBER_OF_GENERATE_USER_SEED");
		int numberOfThread = Settings.getInt("NUMBER_OF_GENERATE_USER_THREAD");
		
		ParallelThreadExecutor executor = new ParallelThreadExecutor(seed, numberOfSeed, numberOfThread);
		executor.execute();
		executor.printSummary();
	}
}
