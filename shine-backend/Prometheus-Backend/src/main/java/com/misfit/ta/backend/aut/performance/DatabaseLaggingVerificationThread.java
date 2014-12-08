package com.misfit.ta.backend.aut.performance;

import java.util.Date;
import java.util.Random;

import org.apache.log4j.Logger;
import org.graphwalker.Util;

import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.backend.data.DataGenerator;
import com.misfit.ta.backend.data.sync.SyncLog;

public class DatabaseLaggingVerificationThread implements Runnable {

    private String token;
//    private long end = System.currentTimeMillis()/1000 - 345600;
//    private long start = end - 2592000;
    
    // end = 30 days ago to now
    long endMax = System.currentTimeMillis()/1000;
    long endMin = endMax - 86400 * 4;
    long startMin = 1;
    long startMax = endMin - 86400;
    
    Random startRand = new Random(System.nanoTime());
    Random endRand = new Random(System.nanoTime());
    
    long end = endMin+ ((long)(endRand.nextDouble()*(endMax-endMin)));
    long start = startMin + ((long)(startRand.nextDouble()*(startMax-startMin)));
    
    

	Logger logger = Util.setupLogger(DatabaseLaggingVerificationThread.class);


	public DatabaseLaggingVerificationThread(UserToken user) {
		this.token = user.token;
	}

	public void run() {

	    
	    Date startDate = new Date(start*1000);
	    Date endDate = new Date(end* 1000);
	    System.out.println("LOG " + start + " - " + end);
	    System.out.println("LOG " + startDate + " - " + endDate);
	    
		doProfileOperation();
		doGoalOperation();
		doTimelineOperation();
		doGraphItemOperation();
		System.out.println("LOG [BackendStressTestThread.run]: ------------------------------------ DONE");
	}

	public void doProfileOperation() {
		
		// get Profile
		MVPApi.getProfile(token);
	}

	public void doGoalOperation() {
		// search goal
		MVPApi.searchGoal(token, start, end, start);
	}

	public void doTimelineOperation() {
	    MVPApi.getTimelineItems(token, start, end, start);
	}
	
	public void doGraphItemOperation() {
        MVPApi.getGraphItems(token, start, end, start);
    }

	public void doSyncOperation() {
		SyncLog log = DataGenerator.generateRandomSyncLog(System.currentTimeMillis() / 1000, 1, 60, null);
		MVPApi.pushSyncLog(token, log);
	}

}
