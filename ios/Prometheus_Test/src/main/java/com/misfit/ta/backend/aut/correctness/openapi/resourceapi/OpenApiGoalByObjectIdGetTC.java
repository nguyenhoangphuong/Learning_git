package com.misfit.ta.backend.aut.correctness.openapi.resourceapi;

import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.misfit.ta.backend.api.internalapi.MVPApi;
import com.misfit.ta.backend.api.openapi.OpenAPI;
import com.misfit.ta.backend.aut.DefaultValues;
import com.misfit.ta.backend.aut.correctness.openapi.OpenAPIAutomationBase;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.backend.data.DataGenerator;
import com.misfit.ta.backend.data.goal.Goal;
import com.misfit.ta.backend.data.goal.GoalsResult;
import com.misfit.ta.backend.data.openapi.resourceapi.OpenAPIGoal;
import com.misfit.ta.utils.TextTool;

public class OpenApiGoalByObjectIdGetTC extends OpenAPIAutomationBase {

	private String accessToken;
	private List<Goal> goalsA;
	private List<Goal> goalsB;
	private List<Goal> goalsC;
	
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		
		super.beforeClass();
		
		goalsA = new ArrayList<Goal>();
		goalsB = new ArrayList<Goal>();
		goalsC = new ArrayList<Goal>();
		
		for(int i = 0; i < 1; i++) {
			
			long goalTimestamp = System.currentTimeMillis() / 1000 - 3600 * 24 * i;
			Goal goalA = DataGenerator.generateRandomGoal(goalTimestamp, null);
			Goal goalB = DataGenerator.generateRandomGoal(goalTimestamp, null);
			Goal goalC = DataGenerator.generateRandomGoal(goalTimestamp, null);
			
			GoalsResult resultA = MVPApi.createGoal(myToken, goalA);
			GoalsResult resultB = MVPApi.createGoal(yourToken, goalB);
			GoalsResult resultC = MVPApi.createGoal(strangerToken, goalC);
			
			goalA.setServerId(resultA.goals[0].getServerId());
			goalB.setServerId(resultB.goals[0].getServerId());
			goalC.setServerId(resultC.goals[0].getServerId());
			
			goalsA.add(goalA);
			goalsB.add(goalB);
			goalsC.add(goalC);
		}
		
		accessToken = OpenAPI.getAccessToken(myEmail, "qqqqqq", OpenAPI.RESOURCE_GOAL, ClientKey, "https://www.google.com.vn/");
	}
	
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "get_goals" })
	public void GetGoalByObjectIdUsingInvalidAccessToken() {
		
		// empty access token
		String nullString = null;
		BaseResult result = OpenAPI.getGoal(nullString, "me", goalsA.get(0).getServerId());
		Assert.assertEquals(result.statusCode, 401, "Status code");
		Assert.assertEquals(result.message, DefaultValues.InvalidAccessToken, "Error message");

		// invalid access token
		result = OpenAPI.getGoal(TextTool.getRandomString(10, 10), "me", goalsA.get(0).getServerId());
		Assert.assertEquals(result.statusCode, 401, "Status code");
		Assert.assertEquals(result.message, DefaultValues.InvalidAccessToken, "Error message");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "get_goals" })
	public void GetGoalByObjectIdWithValidAccessToken() {
		
		// use myUid route
		BaseResult result = OpenAPI.getGoal(accessToken, myUid, goalsA.get(0).getServerId());
		OpenAPIGoal rgoal = OpenAPIGoal.getGoalFromResponse(result.response);
		
		Assert.assertEquals(result.statusCode, 200, "Status code");
		Assert.assertEquals(rgoal.getDate(), getDateString(goalsA.get(0).getStartTime()), "goal date");
		Assert.assertEquals(rgoal.getPoint(), goalsA.get(0).getValue() / 2.5d, "goal value");
		
		
		// use "me" route
		result = OpenAPI.getGoal(accessToken, "me", goalsA.get(0).getServerId());
		rgoal = OpenAPIGoal.getGoalFromResponse(result.response);

		Assert.assertEquals(result.statusCode, 200, "Status code");
		Assert.assertEquals(rgoal.getDate(), getDateString(goalsA.get(0).getStartTime()), "goal date");
		Assert.assertEquals(rgoal.getPoint(), goalsA.get(0).getValue() / 2.5d, "goal value");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "get_goals", "Excluded" })
	public void GetGoalByObjectIdWithoutPermission() {
		
		String invalidScopeAccessToken = OpenAPI.getAccessToken(myEmail, "qqqqqq", OpenAPI.RESOURCE_PROFILE, ClientKey, "https://www.google.com.vn/");
		BaseResult result = OpenAPI.getGoal(invalidScopeAccessToken, "me", goalsA.get(0).getServerId());
		
		Assert.assertEquals(result.statusCode, 403, "Status code");
		Assert.assertEquals(result.message, DefaultValues.ResourceForbidden, "Error message");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "get_goals" })
	public void GetGoalByObjectIdOfOtherUser() {
				
		// "me" + objectId from other authorized user
		BaseResult result = OpenAPI.getGoal(accessToken, "me", goalsB.get(0).getServerId());
		Assert.assertEquals(result.statusCode, 400, "Status code");
		Assert.assertEquals(result.message, DefaultValues.ObjectNotFound, "Error message");
		
		// myUid + objectId from other authorized user
		result = OpenAPI.getGoal(accessToken, myUid, goalsC.get(0).getServerId());
		Assert.assertEquals(result.statusCode, 400, "Status code");
		Assert.assertEquals(result.message, DefaultValues.ObjectNotFound, "Error message");
		
		// from other authorized user
		result = OpenAPI.getGoal(accessToken, yourUid, goalsB.get(0).getServerId());
		Assert.assertEquals(result.statusCode, 403, "Status code");
		Assert.assertEquals(result.message, DefaultValues.ResourceForbidden, "Error message");

		// from unauthorized user
		result = OpenAPI.getGoal(accessToken, strangerUid, goalsC.get(0).getServerId());
		Assert.assertEquals(result.statusCode, 403, "Status code");
		Assert.assertEquals(result.message, DefaultValues.ResourceForbidden, "Error message");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "get_goals" })
	public void GetGoalByObjectIdInvalidObjectId() {
		
		BaseResult result = OpenAPI.getGoal(accessToken, myUid, TextTool.getRandomString(10, 10));
		
		Assert.assertEquals(result.statusCode, 400, "Status code");
		Assert.assertEquals(result.message, DefaultValues.InvalidParameters, "Error message");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "get_goals" })
	public void GetGoalByObjectIdUsingAppCredential() {
		
		// from authorized user
		BaseResult result = OpenAPI.getGoal(ClientApp, myUid, goalsA.get(0).getServerId());
		OpenAPIGoal rgoal = OpenAPIGoal.getGoalFromResponse(result.response);
		
		Assert.assertEquals(result.statusCode, 200, "Status code");
		Assert.assertEquals(rgoal.getDate(), getDateString(goalsA.get(0).getStartTime()), "goal date");
		Assert.assertEquals(rgoal.getPoint(), goalsA.get(0).getValue() / 2.5d, "goal value");
		
		
		// from unauthorized user
		result = OpenAPI.getGoal(ClientApp, strangerUid, goalsA.get(0).getServerId());

		Assert.assertEquals(result.statusCode, 403, "Status code");
		Assert.assertEquals(result.message, DefaultValues.ResourceForbidden, "Error message");
	}
	
	/*
	 * TODO:
	 * - Get a resource using expired token
	 * - Get a resource with only selected fields
	 */

}
