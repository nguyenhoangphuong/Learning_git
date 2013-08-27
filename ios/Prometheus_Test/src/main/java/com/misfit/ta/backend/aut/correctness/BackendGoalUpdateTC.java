package com.misfit.ta.backend.aut.correctness;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.backend.aut.DefaultValues;
import com.misfit.ta.backend.data.goal.Goal;
import com.misfit.ta.backend.data.goal.GoalsResult;
import com.misfit.ta.backend.data.goal.ProgressData;

public class BackendGoalUpdateTC {
	
	String email = MVPApi.generateUniqueEmail();
	String password = "qwerty1";
	Goal defaultGoal = DefaultValues.DefaultGoal();
	
	@BeforeClass(alwaysRun = true)
	public void setUp() {
		// sign up and create goal
		String token = MVPApi.signUp(email, password).token;
		Goal goal = MVPApi.createGoal(token, defaultGoal).goals[0];
		defaultGoal.setServerId(goal.getServerId());
	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "goal" })
	public void UpdateGoal() {
		String token = MVPApi.signIn(email, password).token;
		defaultGoal.setUpdatedAt(defaultGoal.getUpdatedAt() + 1);
		defaultGoal.setProgressData(new ProgressData(300, 5000, 2000, 500));
		
		GoalsResult r = MVPApi.updateGoal(token, defaultGoal);
		r.printKeyPairsValue();

		Assert.assertTrue(r.isExisted(), "Status code is 210");
		Assert.assertTrue(r.goals != null, "Goal is not null");
		Assert.assertEquals(r.goals[0].getProgressData().getFullBmrCalorie(), defaultGoal.getProgressData().getFullBmrCalorie(), "Full BRM is the same");
		Assert.assertEquals(r.goals[0].getProgressData().getPoints(), defaultGoal.getProgressData().getPoints(), "Points is the same");
		Assert.assertEquals(r.goals[0].getProgressData().getSeconds(), defaultGoal.getProgressData().getSeconds(), "Seconds is the same");
		Assert.assertEquals(r.goals[0].getProgressData().getSteps(), defaultGoal.getProgressData().getSteps(), "Steps is the same");
		
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "goal" })
	public void UpdateGoalInvalidToken() {
		String token = DefaultValues.ArbitraryToken;
		defaultGoal.setUpdatedAt(defaultGoal.getUpdatedAt() + 1);
		defaultGoal.setProgressData(new ProgressData(400, 6000, 3000, 600));
		
		GoalsResult r = MVPApi.updateGoal(token, defaultGoal);
		r.printKeyPairsValue();

		Assert.assertTrue(r.isAuthInvalid(), "Status code is 401");
		Assert.assertTrue(r.goals == null, "Goal is null");
		
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "goal" })
	public void UpdateGoalExpiredToken() {
		String token = MVPApi.signIn(email, password).token;
		MVPApi.signOut(token);
		defaultGoal.setUpdatedAt(defaultGoal.getUpdatedAt() + 1);
		defaultGoal.setProgressData(new ProgressData(500, 7000, 4000, 700));
		
		GoalsResult r = MVPApi.updateGoal(token, defaultGoal);
		r.printKeyPairsValue();

		Assert.assertTrue(r.isAuthInvalid(), "Status code is 401");
		Assert.assertTrue(r.goals == null, "Goal is null");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "goal" })
	public void UpdateGoalNoneExisted() {
		String token = MVPApi.signIn(email, password).token;
		Goal g = DefaultValues.DefaultGoal();
		g.setUpdatedAt(defaultGoal.getUpdatedAt() + 1);
		g.setProgressData(new ProgressData(600, 8000, 5000, 800));
		g.setLocalId("noneexistgoal");
		
		GoalsResult r = MVPApi.updateGoal(token, defaultGoal);
		r.printKeyPairsValue();

		Assert.assertTrue(r.isNotFound(), "Status code is 404");
		Assert.assertTrue(r.goals == null, "Goal is null");
	}
	
	
}
