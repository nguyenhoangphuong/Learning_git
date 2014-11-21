package com.misfit.ta.backend.aut.correctness.backendapi;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.misfit.ta.backend.api.metawatch.MVPApi;
import com.misfit.ta.backend.aut.BackendAutomation;
import com.misfit.ta.backend.aut.DefaultValues;
import com.misfit.ta.backend.data.goal.Goal;
import com.misfit.ta.backend.data.goal.GoalsResult;

public class BackendGoalCreateTC extends BackendAutomation {

	String password = "qwerty1";
	Goal defaultGoal = Goal.getDefaultGoal();

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "goal" })
	public void CreateNewGoal() {
		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), password).token;
		GoalsResult r = MVPApi.createGoal(token, defaultGoal);
		r.printKeyPairsValue();

		Assert.assertTrue(r.isOK(), "Status code is OK");
		Assert.assertTrue(r.goals[0].getServerId() != null, "Server Id is not null");
		Assert.assertEquals(r.goals[0].getLocalId(), defaultGoal.getLocalId(), "Local id is not the same");
	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "goal" })
	public void CreateMultipleGoals() {
		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), password).token;

		for (int i = 0; i < 5; i++) {
			long timestamp = System.currentTimeMillis() / 1000 - i * 3600 * 24; 
			GoalsResult r = MVPApi.createGoal(token, Goal.getDefaultGoal(timestamp));
			r.printKeyPairsValue();

			Assert.assertTrue(r.isOK(), "Status code is OK");
			Assert.assertTrue(r.goals[0].getServerId() != null, "Server Id is not null");
		}
	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "goal" })
	public void CreateDupplicatedGoal() {
		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), password).token;
		GoalsResult r1 = MVPApi.createGoal(token, defaultGoal);
		r1.printKeyPairsValue();
		GoalsResult r2 = MVPApi.createGoal(token, defaultGoal);
		r2.printKeyPairsValue();

		Assert.assertTrue(r2.isExisted(), "Status code is 210");
		Assert.assertEquals(r1.goals[0].getServerId(), r2.goals[0].getServerId(), "Server Id is not same for 2 goals");
		Assert.assertEquals(r1.goals[0].getLocalId(), r2.goals[0].getLocalId(), "Local id is same for 2 goals");
	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "goal" })
	public void CreateGoalInvalidToken() {
		String token = DefaultValues.ArbitraryToken;
		GoalsResult r = MVPApi.createGoal(token, defaultGoal);
		r.printKeyPairsValue();

		Assert.assertTrue(r.isAuthInvalid(), "Status code is 401");
		Assert.assertTrue(r.goals == null, "Goal is null");
	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "goal" })
	public void CreateGoalExpiredToken() {
		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), password).token;
		MVPApi.signOut(token);
		GoalsResult r = MVPApi.createGoal(token, defaultGoal);
		r.printKeyPairsValue();

		Assert.assertTrue(r.isAuthInvalid(), "Status code is 401");
		Assert.assertTrue(r.goals == null, "Goal is null");
	}

}
