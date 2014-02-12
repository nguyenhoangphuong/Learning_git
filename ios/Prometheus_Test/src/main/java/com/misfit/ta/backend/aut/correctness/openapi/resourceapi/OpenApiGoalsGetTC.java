package com.misfit.ta.backend.aut.correctness.openapi.resourceapi;

import java.util.ArrayList;
import java.util.Collections;
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
import com.misfit.ta.backend.data.openapi.resourceapi.OpenAPIGoal;
import com.misfit.ta.utils.TextTool;

public class OpenApiGoalsGetTC extends OpenAPIAutomationBase {

	private String accessToken;
	private List<Goal> goals;
	
	private String fromDate = getDateString(System.currentTimeMillis() / 1000 - 3600 * 24 * 2);
	private String toDate = getDateString(System.currentTimeMillis() / 1000);
	
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		
		super.beforeClass();
		
		goals = new ArrayList<Goal>();
		for(int i = 0; i < 5; i++) {
			
			Goal goal = DataGenerator.generateRandomGoal(System.currentTimeMillis() / 1000 - 3600 * 24 * i, null);
			goals.add(goal);
			MVPApi.createGoal(myToken, goal);
			MVPApi.createGoal(yourToken, goal);
			MVPApi.createGoal(strangerToken, goal);
		}
		
		accessToken = OpenAPI.getAccessToken(myEmail, "qqqqqq", OpenAPI.RESOURCE_GOAL, ClientKey, "/");
	}
	
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "get_goals" })
	public void GetGoalsUsingInvalidAccessToken() {
		
		// empty access token
		String nullString = null;
		BaseResult result = OpenAPI.getGoals(nullString, "me", fromDate, toDate);
		Assert.assertEquals(result.statusCode, 401, "Status code");
		Assert.assertEquals(result.message, DefaultValues.InvalidAccessToken, "Error message");

		// invalid access token
		result = OpenAPI.getGoals(TextTool.getRandomString(10, 10), "me", fromDate, toDate);
		Assert.assertEquals(result.statusCode, 401, "Status code");
		Assert.assertEquals(result.message, DefaultValues.InvalidAccessToken, "Error message");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "get_goals" })
	public void GetGoalsWithValidAccessToken() {
		
		// use "me" route
		BaseResult result = OpenAPI.getGoals(accessToken, "me", fromDate, toDate);
		List<OpenAPIGoal> rgoals = OpenAPIGoal.getGoalsFromResponse(result.response);
		Collections.reverse(rgoals);
		
		Assert.assertEquals(result.statusCode, 200, "Status code");
		Assert.assertEquals(rgoals.size(), 3, "Number of goals in response");
		
		Assert.assertEquals(rgoals.get(0).getDate(), getDateString(goals.get(0).getStartTime()), "goals[0] date");
		Assert.assertEquals(rgoals.get(1).getDate(), getDateString(goals.get(1).getStartTime()), "goals[1] date");
		Assert.assertEquals(rgoals.get(2).getDate(), getDateString(goals.get(2).getStartTime()), "goals[2] date");
		
		Assert.assertEquals(rgoals.get(0).getPoint(), goals.get(0).getValue() / 2.5d, "goals[0] value");
		Assert.assertEquals(rgoals.get(1).getPoint(), goals.get(1).getValue() / 2.5d, "goals[1] value");
		Assert.assertEquals(rgoals.get(2).getPoint(), goals.get(2).getValue() / 2.5d, "goals[2] value");
		
				
		// use "myUid" route
		result = OpenAPI.getGoals(accessToken, myUid, fromDate, toDate);
		rgoals = OpenAPIGoal.getGoalsFromResponse(result.response);
		Collections.reverse(rgoals);
		
		Assert.assertEquals(result.statusCode, 200, "Status code");
		Assert.assertEquals(rgoals.size(), 3, "Number of goals in response");
		
		Assert.assertEquals(rgoals.get(0).getDate(), getDateString(goals.get(0).getStartTime()), "goals[0] date");
		Assert.assertEquals(rgoals.get(1).getDate(), getDateString(goals.get(1).getStartTime()), "goals[1] date");
		Assert.assertEquals(rgoals.get(2).getDate(), getDateString(goals.get(2).getStartTime()), "goals[2] date");
		
		Assert.assertEquals(rgoals.get(0).getPoint(), goals.get(0).getValue() / 2.5d, "goals[0] value");
		Assert.assertEquals(rgoals.get(1).getPoint(), goals.get(1).getValue() / 2.5d, "goals[1] value");
		Assert.assertEquals(rgoals.get(2).getPoint(), goals.get(2).getValue() / 2.5d, "goals[2] value");
		
		
		// fromDate == toDate
		result = OpenAPI.getGoals(accessToken, myUid, toDate, toDate);
		rgoals = OpenAPIGoal.getGoalsFromResponse(result.response);
		
		Assert.assertEquals(result.statusCode, 200, "Status code");
		Assert.assertEquals(rgoals.size(), 1, "Number of goals in response");
		
		Assert.assertEquals(rgoals.get(0).getDate(), getDateString(goals.get(0).getStartTime()), "goals[0] date");
		Assert.assertEquals(rgoals.get(0).getPoint(), goals.get(0).getValue() / 2.5d, "goals[0] value");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "get_goals", "Excluded" })
	public void GetGoalsWithoutPermission() {
		
		String invalidScopeAccessToken = OpenAPI.getAccessToken(myEmail, "qqqqqq", OpenAPI.RESOURCE_PROFILE, ClientKey, "/");
		BaseResult result = OpenAPI.getGoals(invalidScopeAccessToken, "me", fromDate, toDate);
		
		Assert.assertEquals(result.statusCode, 403, "Status code");
		Assert.assertEquals(result.message, DefaultValues.ResourceForbidden, "Error message");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "get_goals" })
	public void GetGoalsOfOtherUser() {
		
		// from other authorized user
		BaseResult result = OpenAPI.getGoals(accessToken, yourUid, fromDate, toDate);
		Assert.assertEquals(result.statusCode, 403, "Status code");
		Assert.assertEquals(result.message, DefaultValues.ResourceForbidden, "Error message");

		// from unauthorized user
		result = OpenAPI.getGoals(accessToken, strangerUid, fromDate, toDate);
		Assert.assertEquals(result.statusCode, 403, "Status code");
		Assert.assertEquals(result.message, DefaultValues.ResourceForbidden, "Error message");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "get_goals" })
	public void GetGoalsInvalidParameters() {
		
		// invalid and missing parameters
		BaseResult result1 = OpenAPI.getGoals(accessToken, "me", null, toDate);
		BaseResult result2 = OpenAPI.getGoals(accessToken, "me", fromDate, null);
		BaseResult result3 = OpenAPI.getGoals(accessToken, "me", "abc", toDate);
		BaseResult result4 = OpenAPI.getGoals(accessToken, "me", fromDate, "def");
		
		Assert.assertEquals(result1.statusCode, 400, "Status code");
		Assert.assertEquals(result1.errorMessage, DefaultValues.MissingParameters, "Error message");
		Assert.assertEquals(result2.statusCode, 400, "Status code");
		Assert.assertEquals(result2.errorMessage, DefaultValues.MissingParameters, "Error message");
		Assert.assertEquals(result3.statusCode, 400, "Status code");
		Assert.assertEquals(result3.errorMessage, DefaultValues.InvalidParameters, "Error message");
		Assert.assertEquals(result4.statusCode, 400, "Status code");
		Assert.assertEquals(result4.errorMessage, DefaultValues.InvalidParameters, "Error message");
		

		// valid in format but invalid in logic
		BaseResult result5 = OpenAPI.getGoals(accessToken, "me", toDate, fromDate);
		List<OpenAPIGoal> goals = OpenAPIGoal.getGoalsFromResponse(result5.response);
		
		Assert.assertEquals(result5.statusCode, 200, "Status code");
		Assert.assertEquals(goals.size(), 0, "Number of goals");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "get_goals" })
	public void GetGoalsUsingAppCredential() {
		
		// authorized user
		BaseResult result = OpenAPI.getGoals(ClientApp, myUid, fromDate, toDate);
		List<OpenAPIGoal> rgoals = OpenAPIGoal.getGoalsFromResponse(result.response);
		Collections.reverse(rgoals);
		
		Assert.assertEquals(result.statusCode, 200, "Status code");
		Assert.assertEquals(rgoals.size(), 3, "Number of goals in response");
		
		Assert.assertEquals(rgoals.get(0).getDate(), getDateString(goals.get(0).getStartTime()), "goals[0] date");
		Assert.assertEquals(rgoals.get(1).getDate(), getDateString(goals.get(1).getStartTime()), "goals[1] date");
		Assert.assertEquals(rgoals.get(2).getDate(), getDateString(goals.get(2).getStartTime()), "goals[2] date");
		
		Assert.assertEquals(rgoals.get(0).getPoint(), goals.get(0).getValue() / 2.5d, "goals[0] value");
		Assert.assertEquals(rgoals.get(1).getPoint(), goals.get(1).getValue() / 2.5d, "goals[1] value");
		Assert.assertEquals(rgoals.get(2).getPoint(), goals.get(2).getValue() / 2.5d, "goals[2] value");
		
				
		// unauthorized user
		result = OpenAPI.getGoals(ClientApp, strangerUid, fromDate, toDate);
		
		Assert.assertEquals(result.statusCode, 403, "Status code");
		Assert.assertEquals(result.message, DefaultValues.ResourceForbidden, "Error message");
	}
	
	/*
	 * TODO:
	 * - Get a resource using expired token
	 * - Get a resource with only selected fields
	 */

}
