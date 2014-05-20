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
import com.misfit.ta.backend.data.openapi.resourceapi.OpenAPISummary;
import com.misfit.ta.common.MVPCommon;
import com.misfit.ta.utils.TextTool;

public class OpenApiSummaryGetTC extends OpenAPIAutomationBase {

	private String accessToken;
	private List<Goal> goals;
	
	private String fromDate = MVPCommon.getDateString(System.currentTimeMillis() / 1000 - 3600 * 24 * 2);
	private String toDate = MVPCommon.getDateString(System.currentTimeMillis() / 1000);
	
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
		
		accessToken = OpenAPI.getAccessToken(myEmail, "qqqqqq", OpenAPI.RESOURCE_GOAL, ClientKey, "https://www.google.com.vn/");
	}
	
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "get_summary" })
	public void GetSummaryUsingInvalidAccessToken() {
		
		// empty access token
		String nullString = null;
		BaseResult result = OpenAPI.getSummary(nullString, "me", fromDate, toDate);
		Assert.assertEquals(result.statusCode, 401, "Status code");
		Assert.assertEquals(result.code, 401, "OpenAPI code");
		Assert.assertEquals(result.message, DefaultValues.InvalidAccessToken, "Error message");

		// invalid access token
		result = OpenAPI.getSummary(TextTool.getRandomString(10, 10), "me", fromDate, toDate);
		Assert.assertEquals(result.statusCode, 401, "Status code");
		Assert.assertEquals(result.code, 401, "OpenAPI code");
		Assert.assertEquals(result.message, DefaultValues.InvalidAccessToken, "Error message");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "get_summary" })
	public void GetSummaryWithValidAccessToken() {
		
		// use "me" route
		BaseResult result = OpenAPI.getSummary(accessToken, "me", toDate, toDate);
		OpenAPISummary summary = OpenAPISummary.getSummary(result.response);
		
		Assert.assertEquals(result.statusCode, 200, "Status code");
		Assert.assertEquals(summary.getCalories(), MVPCommon.round(goals.get(0).getProgressData().getCalorie(), 1), "Calories");
		Assert.assertEquals(summary.getDistance(), MVPCommon.round(goals.get(0).getProgressData().getDistanceMiles(), 1), "Distance");
		Assert.assertEquals(summary.getPoints(), goals.get(0).getProgressData().getPoints() / 2.5, "Points");
		Assert.assertEquals(summary.getSteps(), goals.get(0).getProgressData().getSteps(), "Steps");
		Assert.assertEquals(MVPCommon.round(summary.getActivityCalories(), 1), 
				MVPCommon.round(goals.get(0).getProgressData().getCalorie() 
				- goals.get(0).getProgressData().getFullBmrCalorie(), 1), "Activity calories");
		
				
		// use "myUid" route
		result = OpenAPI.getSummary(accessToken, myUid, toDate, toDate);
		summary = OpenAPISummary.getSummary(result.response);
		
		Assert.assertEquals(result.statusCode, 200, "Status code");
		Assert.assertEquals(summary.getCalories(), MVPCommon.round(goals.get(0).getProgressData().getCalorie(), 1), "Calories");
		Assert.assertEquals(summary.getDistance(), MVPCommon.round(goals.get(0).getProgressData().getDistanceMiles(), 1), "Distance");
		Assert.assertEquals(summary.getPoints(), goals.get(0).getProgressData().getPoints() / 2.5, "Points");
		Assert.assertEquals(summary.getSteps(), goals.get(0).getProgressData().getSteps(), "Steps");
		Assert.assertEquals(MVPCommon.round(summary.getActivityCalories(), 1), 
				MVPCommon.round(goals.get(0).getProgressData().getCalorie() 
				- goals.get(0).getProgressData().getFullBmrCalorie(), 1), "Activity calories");
		
		
		// summary of a specific range
		double calories = 0;
		double distance = 0;
		double points = 0;
		double activityCalories = 0;
		int steps = 0;
		
		for(int i = 0; i < 3; i++) {
			
			calories += goals.get(i).getProgressData().getCalorie();
			distance += goals.get(i).getProgressData().getDistanceMiles();
			points += goals.get(i).getProgressData().getPoints();
			steps += goals.get(i).getProgressData().getSteps();
			activityCalories += (goals.get(i).getProgressData().getCalorie()
					- goals.get(i).getProgressData().getFullBmrCalorie());
		}
		
		result = OpenAPI.getSummary(accessToken, myUid, fromDate, toDate);
		summary = OpenAPISummary.getSummary(result.response);
		
		Assert.assertEquals(result.statusCode, 200, "Status code");
		Assert.assertEquals(summary.getCalories(), MVPCommon.round(calories, 1), "Calories");
		Assert.assertEquals(summary.getDistance(), MVPCommon.round(distance, 1), "Distance");
		Assert.assertEquals(summary.getPoints(), points / 2.5, "Points");
		Assert.assertEquals((int)summary.getSteps(), steps, "Steps");
		Assert.assertEquals(MVPCommon.round(summary.getActivityCalories(), 1), 
				MVPCommon.round(activityCalories, 1), "Activity calories");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "get_summary" })
	public void GetSummaryDetailWithValidAccessToken() {
		
		// from date to date
		BaseResult result = OpenAPI.getSummary(accessToken, "me", fromDate, toDate, true);
		List<OpenAPISummary> summaries = OpenAPISummary.getSummaries(result.response);
		Collections.reverse(summaries);

		Assert.assertEquals(result.statusCode, 200, "Status code");
		Assert.assertEquals(summaries.size(), 3, "Number of summaries in response");
		for(int i = 0; i < summaries.size(); i++) {
		
			OpenAPISummary summary = summaries.get(i);
			Assert.assertEquals(summary.getCalories(), MVPCommon.round(goals.get(i).getProgressData().getCalorie(), 1), "Calories");
			Assert.assertEquals(summary.getDistance(), MVPCommon.round(goals.get(i).getProgressData().getDistanceMiles(), 1), "Distance");
			Assert.assertEquals(summary.getPoints(), goals.get(i).getProgressData().getPoints() / 2.5, "Points");
			Assert.assertEquals(summary.getSteps(), goals.get(i).getProgressData().getSteps(), "Steps");
		}
		
		
		// from date == to date
		result = OpenAPI.getSummary(accessToken, "me", toDate, toDate, true);
		summaries = OpenAPISummary.getSummaries(result.response);

		Assert.assertEquals(result.statusCode, 200, "Status code");
		Assert.assertEquals(summaries.size(), 1, "Number of summaries in response");
		for(int i = 0; i < summaries.size(); i++) {

			OpenAPISummary summary = summaries.get(i);
			Assert.assertEquals(summary.getCalories(), MVPCommon.round(goals.get(i).getProgressData().getCalorie(), 1), "Calories");
			Assert.assertEquals(summary.getDistance(), MVPCommon.round(goals.get(i).getProgressData().getDistanceMiles(), 1), "Distance");
			Assert.assertEquals(summary.getPoints(), goals.get(i).getProgressData().getPoints() / 2.5, "Points");
			Assert.assertEquals(summary.getSteps(), goals.get(i).getProgressData().getSteps(), "Steps");
		}
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "get_summary" })
	public void GetSummaryDetailDateOutOfRange() {
		
		// get detail with toDate in future
		long timestamp = System.currentTimeMillis() / 1000;
		BaseResult result = OpenAPI.getSummary(accessToken, "me", MVPCommon.getISO8601Time(timestamp - 3600 * 24 * 10), 
				MVPCommon.getISO8601Time(timestamp + 3600 * 24 * 10), true);
		List<OpenAPISummary> summaries = OpenAPISummary.getSummaries(result.response);
		Collections.reverse(summaries);

		Assert.assertEquals(result.statusCode, 200, "Status code");
		Assert.assertEquals(summaries.size(), 5, "Number of summaries in response");
		for(int i = 0; i < summaries.size(); i++) {
		
			OpenAPISummary summary = summaries.get(i);
			Assert.assertEquals(summary.getCalories(), MVPCommon.round(goals.get(i).getProgressData().getCalorie(), 1), "Calories");
			Assert.assertEquals(summary.getDistance(), MVPCommon.round(goals.get(i).getProgressData().getDistanceMiles(), 1), "Distance");
			Assert.assertEquals(summary.getPoints(), goals.get(i).getProgressData().getPoints() / 2.5, "Points");
			Assert.assertEquals(summary.getSteps(), goals.get(i).getProgressData().getSteps(), "Steps");
		}
		
		
		// get detail with toDate < fromDate
		result = OpenAPI.getSummary(accessToken, "me", toDate, fromDate, true);
		summaries = OpenAPISummary.getSummaries(result.response);

		Assert.assertEquals(result.statusCode, 200, "Status code");
		Assert.assertEquals(summaries.size(), 0, "Number of summaries in response");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "get_summary", "Excluded" })
	public void GetSummaryWithoutPermission() {
		
		String invalidScopeAccessToken = OpenAPI.getAccessToken(myEmail, "qqqqqq", OpenAPI.RESOURCE_PROFILE, ClientKey, "https://www.google.com.vn/");
		BaseResult result = OpenAPI.getSummary(invalidScopeAccessToken, "me", fromDate, toDate);
		
		Assert.assertEquals(result.statusCode, 403, "Status code");
		Assert.assertEquals(result.code, 403, "OpenAPI code");
		Assert.assertEquals(result.message, DefaultValues.ResourceForbidden, "Error message");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "get_summary" })
	public void GetSummaryOfOtherUser() {
		
		// from other authorized user
		BaseResult result = OpenAPI.getSummary(accessToken, yourUid, fromDate, toDate);
		Assert.assertEquals(result.statusCode, 403, "Status code");
		Assert.assertEquals(result.code, 403, "OpenAPI code");
		Assert.assertEquals(result.message, DefaultValues.ResourceForbidden, "Error message");

		// from unauthorized user
		result = OpenAPI.getSummary(accessToken, strangerUid, fromDate, toDate);
		Assert.assertEquals(result.statusCode, 403, "Status code");
		Assert.assertEquals(result.code, 403, "OpenAPI code");
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
		Assert.assertEquals(result1.code, 400, "OpenAPI code");
		Assert.assertEquals(result1.errorMessage, DefaultValues.MissingParameters, "Error message");
		Assert.assertEquals(result2.statusCode, 400, "Status code");
		Assert.assertEquals(result2.code, 400, "OpenAPI code");
		Assert.assertEquals(result2.errorMessage, DefaultValues.MissingParameters, "Error message");
		Assert.assertEquals(result3.statusCode, 400, "Status code");
		Assert.assertEquals(result3.code, 400, "OpenAPI code");
		Assert.assertEquals(result3.errorMessage, DefaultValues.InvalidParameters, "Error message");
		Assert.assertEquals(result4.statusCode, 400, "Status code");
		Assert.assertEquals(result4.code, 400, "OpenAPI code");
		Assert.assertEquals(result4.errorMessage, DefaultValues.InvalidParameters, "Error message");
		

		// valid in format but invalid in logic
		BaseResult result5 = OpenAPI.getSummary(accessToken, "me", toDate, fromDate);
		OpenAPISummary summary = OpenAPISummary.getSummary(result5.response);
		
		Assert.assertEquals(result5.statusCode, 200, "Status code");
		Assert.assertEquals(summary.getCalories(), 0.0, "Calories");
		Assert.assertEquals(summary.getDistance(), 0.0, "Distance");
		Assert.assertEquals(summary.getPoints(), 0.0, "Points");
		Assert.assertEquals((int)summary.getSteps(), 0, "Steps");
	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "get_summary" })
	public void GetSummaryUsingAppCredential() {
		
		// authorized user
		BaseResult result = OpenAPI.getSummary(ClientApp, myUid, toDate, toDate);
		OpenAPISummary summary = OpenAPISummary.getSummary(result.response);
		
		Assert.assertEquals(result.statusCode, 200, "Status code");
		Assert.assertEquals(summary.getCalories(), MVPCommon.round(goals.get(0).getProgressData().getCalorie(), 1), "Calories");
		Assert.assertEquals(summary.getDistance(), MVPCommon.round(goals.get(0).getProgressData().getDistanceMiles(), 1), "Distance");
		Assert.assertEquals(summary.getPoints(), goals.get(0).getProgressData().getPoints() / 2.5, "Points");
		Assert.assertEquals(summary.getSteps(), goals.get(0).getProgressData().getSteps(), "Steps");
		
				
		// unauthorized user
		result = OpenAPI.getSummary(ClientApp, strangerUid, toDate, toDate);
		
		Assert.assertEquals(result.statusCode, 403, "Status code");
		Assert.assertEquals(result.code, 403, "OpenAPI code");
		Assert.assertEquals(result.message, DefaultValues.UnauthorizedAccess, "Error message");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "get_summary" })
	public void GetSummaryWithNullProgressData() {
		
		// create a goal with progress data == null
		long timestamp = System.currentTimeMillis() / 1000 - 3600 * 24 * 10;
		Goal goal = DataGenerator.generateRandomGoal(timestamp, null);
		goal.setProgressData(null);
		MVPApi.createGoal(myToken, goal);
		
		
		// get summary detail = false
		String date = MVPCommon.getDateString(timestamp);
		BaseResult result = OpenAPI.getSummary(accessToken, "me", date, date);
		OpenAPISummary summary = OpenAPISummary.getSummary(result.response);
		
		Assert.assertEquals(result.statusCode, 200, "Status code");
		Assert.assertEquals(summary.getCalories(), 0d, "Calories");
		Assert.assertEquals(summary.getDistance(), 0d, "Distance");
		Assert.assertEquals(summary.getPoints(), 0d, "Points");
		Assert.assertEquals((int)summary.getSteps(), 0, "Steps");
		Assert.assertEquals(summary.getActivityCalories(), 0d, "Activity calories");
		
		
		// get summary detail = true
		result = OpenAPI.getSummary(accessToken, "me", date, date, true);
		summary = OpenAPISummary.getSummaries(result.response).get(0);

		Assert.assertEquals(result.statusCode, 200, "Status code");
		Assert.assertEquals(summary.getCalories(), 0d, "Calories");
		Assert.assertEquals(summary.getDistance(), 0d, "Distance");
		Assert.assertEquals(summary.getPoints(), 0d, "Points");
		Assert.assertEquals((int)summary.getSteps(), 0, "Steps");
		Assert.assertEquals(summary.getActivityCalories(), 0d, "Activity calories");
	}
	
	/*
	 * TODO:
	 * - Get a resource using expired token
	 * - Get a resource with only selected fields
	 */

}
