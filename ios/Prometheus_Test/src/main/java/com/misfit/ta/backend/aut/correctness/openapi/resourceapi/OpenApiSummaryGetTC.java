package com.misfit.ta.backend.aut.correctness.openapi.resourceapi;

import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.misfit.ta.backend.api.internalapi.MVPApi;
import com.misfit.ta.backend.api.openapi.OpenAPI;
import com.misfit.ta.backend.aut.DefaultValues;
import com.misfit.ta.backend.aut.OpenAPIAutomationBase;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.backend.data.DataGenerator;
import com.misfit.ta.backend.data.goal.Goal;
import com.misfit.ta.backend.data.openapi.resourceapi.OpenAPISummary;
import com.misfit.ta.common.MVPCommon;
import com.misfit.ta.utils.TextTool;

public class OpenApiSummaryGetTC extends OpenAPIAutomationBase {

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
	
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "get_summary" })
	public void GetSummaryUsingInvalidAccessToken() {
		
		// empty access token
		BaseResult result = OpenAPI.getSummary(null, "me", fromDate, toDate);
		Assert.assertEquals(result.statusCode, 401, "Status code");
		Assert.assertEquals(result.message, DefaultValues.InvalidAccessToken, "Error message");

		// invalid access token
		result = OpenAPI.getSummary(TextTool.getRandomString(10, 10), "me", fromDate, toDate);
		Assert.assertEquals(result.statusCode, 401, "Status code");
		Assert.assertEquals(result.message, DefaultValues.InvalidAccessToken, "Error message");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "get_summary" })
	public void GetSummaryWithValidAccessToken() {
		
		// use "me" route
		BaseResult result = OpenAPI.getSummary(accessToken, "me", toDate, toDate);
		OpenAPISummary summary = OpenAPISummary.fromResponse(result.response);
		
		Assert.assertEquals(result.statusCode, 200, "Status code");
		Assert.assertEquals(summary.getCalories(), MVPCommon.round(goals.get(0).getProgressData().getCalorie(), 1), "Calories");
		Assert.assertEquals(summary.getDistance(), MVPCommon.round(goals.get(0).getProgressData().getDistanceMiles(), 1), "Distance");
		Assert.assertEquals(summary.getPoints(), goals.get(0).getProgressData().getPoints() / 2.5, "Points");
		Assert.assertEquals(summary.getSteps(), goals.get(0).getProgressData().getSteps(), "Steps");
		
				
		// use "myUid" route
		result = OpenAPI.getSummary(accessToken, myUid, toDate, toDate);
		summary = OpenAPISummary.fromResponse(result.response);
		
		Assert.assertEquals(result.statusCode, 200, "Status code");
		Assert.assertEquals(summary.getCalories(), MVPCommon.round(goals.get(0).getProgressData().getCalorie(), 1), "Calories");
		Assert.assertEquals(summary.getDistance(), MVPCommon.round(goals.get(0).getProgressData().getDistanceMiles(), 1), "Distance");
		Assert.assertEquals(summary.getPoints(), goals.get(0).getProgressData().getPoints() / 2.5, "Points");
		Assert.assertEquals(summary.getSteps(), goals.get(0).getProgressData().getSteps(), "Steps");
		
		
		// summary of a specific range
		double calories = 0;
		double distance = 0;
		double points = 0;
		int steps = 0;
		
		for(int i = 0; i < 3; i++) {
			
			calories += goals.get(i).getProgressData().getCalorie();
			distance += goals.get(i).getProgressData().getDistanceMiles();
			points += goals.get(i).getProgressData().getPoints();
			steps += goals.get(i).getProgressData().getSteps();
		}
		
		result = OpenAPI.getSummary(accessToken, myUid, fromDate, toDate);
		summary = OpenAPISummary.fromResponse(result.response);
		
		Assert.assertEquals(result.statusCode, 200, "Status code");
		Assert.assertEquals(summary.getCalories(), MVPCommon.round(calories, 1), "Calories");
		Assert.assertEquals(summary.getDistance(), MVPCommon.round(distance, 1), "Distance");
		Assert.assertEquals(summary.getPoints(), points / 2.5, "Points");
		Assert.assertEquals((int)summary.getSteps(), steps, "Steps");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "get_summary", "Excluded" })
	public void GetSummaryWithoutPermission() {
		
		String invalidScopeAccessToken = OpenAPI.getAccessToken(myEmail, "qqqqqq", OpenAPI.RESOURCE_PROFILE, ClientKey, "/");
		BaseResult result = OpenAPI.getSummary(invalidScopeAccessToken, "me", fromDate, toDate);
		
		Assert.assertEquals(result.statusCode, 403, "Status code");
		Assert.assertEquals(result.message, DefaultValues.ResourceForbidden, "Error message");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "get_summary" })
	public void GetSummaryOfOtherUser() {
		
		// from other authorized user
		BaseResult result = OpenAPI.getSummary(accessToken, yourUid, fromDate, toDate);
		Assert.assertEquals(result.statusCode, 403, "Status code");
		Assert.assertEquals(result.message, DefaultValues.ResourceForbidden, "Error message");

		// from unauthorized user
		result = OpenAPI.getSummary(accessToken, strangerUid, fromDate, toDate);
		Assert.assertEquals(result.statusCode, 403, "Status code");
		Assert.assertEquals(result.message, DefaultValues.ResourceForbidden, "Error message");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "get_summary" })
	public void GetSummaryInvalidParameters() {
		
		// invalid and missing parameters
		BaseResult result1 = OpenAPI.getSummary(accessToken, "me", null, toDate);
		BaseResult result2 = OpenAPI.getSummary(accessToken, "me", fromDate, null);
		BaseResult result3 = OpenAPI.getSummary(accessToken, "me", "abc", toDate);
		BaseResult result4 = OpenAPI.getSummary(accessToken, "me", fromDate, "def");
		
		Assert.assertEquals(result1.statusCode, 400, "Status code");
		Assert.assertEquals(result1.errorMessage, DefaultValues.MissingParameters, "Error message");
		Assert.assertEquals(result2.statusCode, 400, "Status code");
		Assert.assertEquals(result2.errorMessage, DefaultValues.MissingParameters, "Error message");
		Assert.assertEquals(result3.statusCode, 400, "Status code");
		Assert.assertEquals(result3.errorMessage, DefaultValues.InvalidParameters, "Error message");
		Assert.assertEquals(result4.statusCode, 400, "Status code");
		Assert.assertEquals(result4.errorMessage, DefaultValues.InvalidParameters, "Error message");
		

		// valid in format but invalid in logic
		BaseResult result5 = OpenAPI.getSummary(accessToken, "me", toDate, fromDate);
		OpenAPISummary summary = OpenAPISummary.fromResponse(result5.response);
		
		Assert.assertEquals(result5.statusCode, 200, "Status code");
		Assert.assertEquals(summary.getCalories(), 0.0, "Calories");
		Assert.assertEquals(summary.getDistance(), 0.0, "Distance");
		Assert.assertEquals(summary.getPoints(), 0.0, "Points");
		Assert.assertEquals((int)summary.getSteps(), 0, "Steps");
	}
	
	
	/*
	 * TODO:
	 * - Get a resource using expired token
	 * - Get a resource with only selected fields
	 * - Get a resource from authorized and unauthorized user using app id and app secret
	 */

}
