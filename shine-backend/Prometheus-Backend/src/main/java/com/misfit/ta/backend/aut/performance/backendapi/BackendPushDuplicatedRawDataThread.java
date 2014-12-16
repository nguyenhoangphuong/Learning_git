package com.misfit.ta.backend.aut.performance.backendapi;
import org.apache.log4j.Logger;
import org.graphwalker.Util;

import com.misfit.ta.backend.data.goal.Goal;
import com.misfit.ta.backend.data.pedometer.Pedometer;

public class BackendPushDuplicatedRawDataThread implements Runnable {
	
		Logger logger = Util.setupLogger(BackendPushDuplicatedRawDataThread.class);
		
		private String token;
		private String email;
		private Goal[] goals;
		private String userId;
		private long timestamp;
		private Pedometer pedometer;
		public BackendPushDuplicatedRawDataThread(String email, String token, Goal[] goals, String userId, long timestamp, Pedometer pedometer) {
			this.token = token;
			this.email = email;
			this.goals = goals;
			this.userId = userId;
			this.timestamp = timestamp;
			this.pedometer = pedometer;
		}
		
		
		@Override
		public void run() {
			logger.info("LOG [BackendStressTestThread.run]: Start push raw data for token " + token);
			BackendPushDuplicatedRawData pushDuplicatedRawData = new BackendPushDuplicatedRawData();
			pushDuplicatedRawData.generateAndPushRawData(email, token, goals, userId, timestamp, pedometer);
			logger.info("LOG [BackendStressTestThread.run]: ------------------------------------ DONE");
		}
	
}