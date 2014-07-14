package com.misfit.ta.backend.aut.performance;

import org.apache.log4j.Logger;
import org.graphwalker.Util;

import com.misfit.ta.backend.api.internalapi.MVPApi;
import com.misfit.ta.backend.data.DataGenerator;
import com.misfit.ta.backend.data.sync.SyncLog;

public class DatabaseLaggingVerificationThread implements Runnable {

    private String token;

	Logger logger = Util.setupLogger(DatabaseLaggingVerificationThread.class);


	public DatabaseLaggingVerificationThread(UserToken user) {
		this.token = user.token;
	}

	public void run() {

//			doProfileOperation();
			doGoalOperation();
			doTimelineOperation();
			doGraphItemOperation();
//            doSyncOperation();
		System.out.println("LOG [BackendStressTestThread.run]: ------------------------------------ DONE");
	}

	public void doProfileOperation() {
		
		// get Profile
		MVPApi.getProfile(token);
	}

	public void doGoalOperation() {
		// search goal
		MVPApi.searchGoal(token, 0l, (long)Integer.MAX_VALUE, 0l);
	}

	public void doTimelineOperation() {
	    MVPApi.getTimelineItems(token, 0l, (long)Integer.MAX_VALUE, 0l);
	}
	
	public void doGraphItemOperation() {
        MVPApi.getGraphItems(token, 0l, (long)Integer.MAX_VALUE, 0l);
    }

	public void doSyncOperation() {
		SyncLog log = DataGenerator.generateRandomSyncLog(System.currentTimeMillis() / 1000, 1, 60, null);
		MVPApi.pushSyncLog(token, log);
	}

}
