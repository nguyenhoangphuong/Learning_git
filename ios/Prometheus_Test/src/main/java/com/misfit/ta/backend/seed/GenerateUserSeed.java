package com.misfit.ta.backend.seed;

import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.backend.data.DataGenerator;
import com.misfit.ta.base.SeedThread;

public class GenerateUserSeed extends SeedThread {

	protected int numberOfGoal;
	protected int minimumActivityTile;
	protected int maximumActivityTile;
	protected int numberOfSyncLog;
	protected boolean includeSyncBinary;
	
	public GenerateUserSeed(int numberOfGoal, int minimumActivityTile, int maximumActivityTile, int numberOfSyncLog, 
			boolean includeSyncBinary) {
		
		this.numberOfGoal = numberOfGoal;
		this.minimumActivityTile = minimumActivityTile;
		this.maximumActivityTile = maximumActivityTile;
		this.numberOfSyncLog = numberOfSyncLog;
		this.includeSyncBinary = includeSyncBinary;
	}
	
	public void run() {
		
		long start = System.currentTimeMillis() / 1000;
		DataGenerator.createUserWithRandomData(MVPApi.generateUniqueEmail(), "qwerty1", 
				numberOfGoal, minimumActivityTile, maximumActivityTile, numberOfSyncLog, includeSyncBinary);
		long end = System.currentTimeMillis() / 1000;
		
		System.out.println("Running time: " + (end - start));
	}
	
	public SeedThread duplicate() {
		
		return new GenerateUserSeed(numberOfGoal, minimumActivityTile, maximumActivityTile, numberOfSyncLog, includeSyncBinary);
	}
}
