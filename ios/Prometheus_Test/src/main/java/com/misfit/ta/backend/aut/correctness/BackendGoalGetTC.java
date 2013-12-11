package com.misfit.ta.backend.aut.correctness;

import java.util.Calendar;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.backend.aut.BackendAutomation;
import com.misfit.ta.backend.aut.DefaultValues;
import com.misfit.ta.backend.data.goal.Goal;
import com.misfit.ta.backend.data.goal.GoalsResult;

public class BackendGoalGetTC extends BackendAutomation {
	
	String email = MVPApi.generateUniqueEmail();
	String password = "qwerty1";
	int month = 9;
	int year = 2013;
		
	@BeforeClass(alwaysRun = true)
	public void setUp() {
		// sign up and create goals
		String token = MVPApi.signUp(email, password).token;
		for(int i = 1; i <= 5; i++) {
			Calendar calendar = Calendar.getInstance();
			calendar.set(year, month, i);
			long timestamp = calendar.getTimeInMillis() / 1000;
			MVPApi.createGoal(token, Goal.getDefaultGoal(timestamp));
		}
	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "goal" })
	public void SearchGoals() {
		String token = MVPApi.signIn(email, password).token;
		
		// search 1
		GoalsResult r = MVPApi.searchGoal(token, MVPApi.getDayStartEpoch(2, month, year), 
				MVPApi.getDayEndEpoch(4, month, year), 0);
		r.printKeyPairsValue();

		Assert.assertTrue(r.isOK(), "Status code is 200");
		Assert.assertEquals(r.goals.length, 3, "Found 3 goals");
		
		// search 2
		r = MVPApi.searchGoal(token, MVPApi.getDayStartEpoch(1, month, year), 
				MVPApi.getDayEndEpoch(10, month, year), 0);
		r.printKeyPairsValue();

		Assert.assertTrue(r.isOK(), "Status code is 200");
		Assert.assertEquals(r.goals.length, 5, "Found 5 goals");
		
		// search 3
		r = MVPApi.searchGoal(token, MVPApi.getDayStartEpoch(5, month, year), 
				MVPApi.getDayEndEpoch(1, month, year), 0);
		r.printKeyPairsValue();

		Assert.assertTrue(r.isOK(), "Status code is 200");
		Assert.assertEquals(r.goals.length, 0, "Found 0 goals");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "goal" })
	public void SearchGoalsInvalidToken() {
		String token = DefaultValues.ArbitraryToken;
		GoalsResult r = MVPApi.searchGoal(token, MVPApi.getDayStartEpoch(2, month, year), 
				MVPApi.getDayEndEpoch(4, month, year), 0);
		r.printKeyPairsValue();

		Assert.assertTrue(r.isAuthInvalid(), "Status code is 401");
		Assert.assertTrue(r.goals == null, "Goals is null");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "goal" })
	public void SearchGoalsExpiredToken() {
		String token = MVPApi.signIn(email, password).token;
		MVPApi.signOut(token);
		GoalsResult r = MVPApi.searchGoal(token, MVPApi.getDayStartEpoch(2, month, year), 
				MVPApi.getDayEndEpoch(4, month, year), 0);
		r.printKeyPairsValue();

		Assert.assertTrue(r.isAuthInvalid(), "Status code is 401");
		Assert.assertTrue(r.goals == null, "Goals is null");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "goal" })
	public void GetGoalUseServerId() {
		String token = MVPApi.signIn(email, password).token;
		Goal g1 = MVPApi.searchGoal(token, MVPApi.getDayStartEpoch(2, month, year), 
				MVPApi.getDayEndEpoch(4, month, year), 0).goals[0];
		GoalsResult r = MVPApi.getGoal(token, g1.getServerId()); 

		Assert.assertTrue(r.isOK(), "Status code is 200");
		Assert.assertTrue(r.goals != null, "Goals is not null");
		Assert.assertEquals(r.goals[0].getStartTime(), g1.getStartTime(), "Start time is the same");
		Assert.assertEquals(r.goals[0].getEndTime(), g1.getEndTime(), "Start time is the same");
	}
	
}
