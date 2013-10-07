package com.misfit.ta.backend.aut;

import com.google.resting.json.JSONException;
import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.backend.data.goal.Goal;

public class BackendHelper {

	public static void unlink(String email, String password) {

		String token = MVPApi.signIn(email, password).token;
		MVPApi.unlinkDevice(token);
	}

	public static void link(String email, String password, String serialNumber) {

		long now = System.currentTimeMillis() / 1000;
		String token = MVPApi.signIn(email, password).token;
		MVPApi.createPedometer(token, serialNumber, "0.0.36r", now, null, now, "pedometer-" + System.nanoTime(), null, now);
	}
	
	public static void clearLatestGoal(String email, String password) {
		
		String token = MVPApi.signIn(email, password).token;
		Goal[] goals = MVPApi.searchGoal(token, 0, Integer.MAX_VALUE, 0).goals;
		if(goals == null || goals.length == 0)
			return;
		
		Goal blank = goals[0];
		blank.getProgressData().setDistanceMiles(0d);
		blank.getProgressData().setPoints(0);
		blank.getProgressData().setFullBmrCalorie(0);
		blank.getProgressData().setSteps(0);
		blank.getProgressData().setSeconds(0);
		
		MVPApi.updateGoal(token, blank);
	}
	
	public static void main(String[] args) throws JSONException {
		
		clearLatestGoal("qa089@a.a", "qqqqqq");
	}

}
