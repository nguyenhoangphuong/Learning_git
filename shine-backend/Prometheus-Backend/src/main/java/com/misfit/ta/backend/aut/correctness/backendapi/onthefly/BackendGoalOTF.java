package com.misfit.ta.backend.aut.correctness.backendapi.onthefly;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.misfit.ta.backend.api.internalapi.MVPApi;
import com.misfit.ta.backend.aut.BackendAutomation;
import com.misfit.ta.backend.data.BaseParams;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.backend.data.goal.Goal;

public class BackendGoalOTF extends BackendAutomation {

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "OTF", "goals" })
	public void CreateGoalsInsufficientParams() {
		
		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), "qqqqqq").token;
		BaseParams params = new BaseParams();
		params.addHeader("auth_token", token);
		
		BaseResult r = MVPApi.customRequest("goals", MVPApi.HTTP_POST, params);
		Assert.assertEquals(r.statusCode, 400, "Status code");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "OTF", "goals" })
	public void UpdateGoalInsufficientParams() {
		
		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), "qqqqqq").token;
		Goal goal = MVPApi.createGoal(token, Goal.getDefaultGoal()).goals[0];
		BaseParams params = new BaseParams();
		params.addHeader("auth_token", token);
		
		BaseResult r = MVPApi.customRequest("goals/" + goal.getServerId(), MVPApi.HTTP_PUT, params);
		Assert.assertEquals(r.statusCode, 400, "Status code");

	}

}
