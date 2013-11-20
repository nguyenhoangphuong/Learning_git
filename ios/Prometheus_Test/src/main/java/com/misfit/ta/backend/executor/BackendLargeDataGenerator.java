package com.misfit.ta.backend.executor;

import com.misfit.ta.Settings;
import com.misfit.ta.backend.seed.GenerateUserSeed;
import com.misfit.ta.base.ParallelThreadExecutor;

public class BackendLargeDataGenerator {

	public static void main(String[] args) {
		
		int goalCount = Settings.getInt("GENERATE_USER_SEED_GOAL_COUNT");
		int tileMin = Settings.getInt("GENERATE_USER_SEED_TILE_MIN");
		int tileMax = Settings.getInt("GENERATE_USER_SEED_TILE_MAX");
		int logCount = Settings.getInt("GENERATE_USER_SEED_SYNCLOG_COUNT");
		boolean includeBinary = Settings.getInt("GENERATE_USER_SEED_SYNCLOG_BINARY") == 1;
		int graphItemInterval = Settings.getInt("GENERATE_USER_SEED_GRAPHITEM_INTERVAL");
		
		GenerateUserSeed seed = new GenerateUserSeed(goalCount, tileMin, tileMax, graphItemInterval, logCount, includeBinary);
		int numberOfSeed = Settings.getInt("NUMBER_OF_GENERATE_USER_SEED");
		int numberOfThread = Settings.getInt("NUMBER_OF_GENERATE_USER_THREAD");
		
		ParallelThreadExecutor executor = new ParallelThreadExecutor(seed, numberOfSeed, numberOfThread);
		executor.execute();
		executor.printSummary();
		GenerateUserSeed.printSummary();
	}
}
