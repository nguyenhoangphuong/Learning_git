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
import com.misfit.ta.backend.data.openapi.resourceapi.OpenAPISession;
import com.misfit.ta.backend.data.timeline.TimelineItem;
import com.misfit.ta.backend.data.timeline.timelineitemdata.ActivitySessionItem;
import com.misfit.ta.common.MVPCommon;
import com.misfit.ta.utils.TextTool;

public class OpenApiSessionsGetTC extends OpenAPIAutomationBase {

	private String accessToken;
	private List<List<TimelineItem>> allSessions;
	
	private String fromDate = getDateString(System.currentTimeMillis() / 1000 - 3600 * 24 * 2);
	private String toDate = getDateString(System.currentTimeMillis() / 1000);
	
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		
		super.beforeClass();
		
		allSessions = new ArrayList<List<TimelineItem>>();
		List<TimelineItem> batchItems = new ArrayList<TimelineItem>();
		
		// 5 days
		for(int i = 0; i < 5; i++) {
		
			long timestamp = System.currentTimeMillis() / 1000 - i * 3600 * 24;
			List<TimelineItem> sessions = new ArrayList<TimelineItem>();
			
			// add some session items
			int j = 0;
			for(; j < 3; j++) {
				
				long itemTimestamp = timestamp + j * 600;
				TimelineItem session = DataGenerator.generateRandomActivitySessionTimelineItem(itemTimestamp, null);
				sessions.add(session);
			}
			
			// add some other timeline items
			sessions.add(DataGenerator.generateRandomFoodTimelineItem(timestamp + 600 * j++, null));
			sessions.add(DataGenerator.generateRandomLifetimeDistanceItem(timestamp + 600 * j++, null));
								
			allSessions.add(sessions);
			batchItems.addAll(sessions);
		}
		
		// call api
		MVPApi.createTimelineItems(myToken, batchItems);
		MVPApi.createTimelineItems(yourToken, batchItems);
		MVPApi.createTimelineItems(strangerToken, batchItems);
		
		accessToken = OpenAPI.getAccessToken(myEmail, "qqqqqq", OpenAPI.RESOURCE_GOAL, ClientKey, "/");
	}
	
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "get_profile" })
	public void GetSessionsUsingInvalidAccessToken() {
		
		// empty access token
		BaseResult result = OpenAPI.getSessions(null, "me", fromDate, toDate);
		Assert.assertEquals(result.statusCode, 401, "Status code");
		Assert.assertEquals(result.message, DefaultValues.InvalidAccessToken, "Error message");

		// invalid access token
		result = OpenAPI.getSessions(TextTool.getRandomString(10, 10), "me", fromDate, toDate);
		Assert.assertEquals(result.statusCode, 401, "Status code");
		Assert.assertEquals(result.message, DefaultValues.InvalidAccessToken, "Error message");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "get_profile" })
	public void GetSessionsWithValidAccessToken() {
		
		// use "me" route
		BaseResult result = OpenAPI.getSessions(accessToken, "me", fromDate, toDate);
		List<OpenAPISession> rsessions = OpenAPISession.getSessionsFromResponse(result.response);
		
		Assert.assertEquals(result.statusCode, 200, "Status code");
		Assert.assertEquals(rsessions.size(), 9, "Number of sessions in response");
				
		
		// use "myUid" route
		result = OpenAPI.getSessions(accessToken, myUid, fromDate, toDate);
		rsessions = OpenAPISession.getSessionsFromResponse(result.response);
		
		Assert.assertEquals(result.statusCode, 200, "Status code");
		Assert.assertEquals(rsessions.size(), 9, "Number of sessions in response");
		
		
		// fromDate == toDate
		for(int i = 0; i < 5; i++) {

			String date = getDateString(System.currentTimeMillis() / 1000 - 3600 * 24 * i);
			result = OpenAPI.getSessions(accessToken, myUid, date, date);
			rsessions = OpenAPISession.getSessionsFromResponse(result.response);

			Assert.assertEquals(result.statusCode, 200, "Status code");
			Assert.assertEquals(rsessions.size(), 3, "Number of sessions in response");
			
			for(int j = 0; j < 3; j++) {
				
				ActivitySessionItem csession = (ActivitySessionItem)allSessions.get(i).get(j).getData();
				OpenAPISession rsession = rsessions.get(j);
				
				Assert.assertEquals(rsession.getStartTime(), getISOTime(allSessions.get(i).get(j).getTimestamp()) , "Activity start time");
				Assert.assertEquals(rsession.getActivityType(), MVPCommon.getActivityName(csession.getActivityType()) , "Activity type");
				Assert.assertEquals(rsession.getCalories(), MVPCommon.round(csession.getCalories(), 1), "Activity calories");
				Assert.assertEquals(rsession.getDistance(), MVPCommon.round(csession.getDistance(), 1), "Activity distance");
				Assert.assertEquals(rsession.getDuration(), csession.getDuration(), "Activity duration");
				Assert.assertEquals(rsession.getPoints(), csession.getPoint() / 2.5, "Activity points");
				Assert.assertEquals(rsession.getSteps(), csession.getSteps(), "Activity steps");
			}
		}
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "get_profile", "Excluded" })
	public void GetSessionsWithoutPermission() {
		
		String invalidScopeAccessToken = OpenAPI.getAccessToken(myEmail, "qqqqqq", OpenAPI.RESOURCE_PROFILE, ClientKey, "/");
		BaseResult result = OpenAPI.getSessions(invalidScopeAccessToken, "me", fromDate, toDate);
		
		Assert.assertEquals(result.statusCode, 403, "Status code");
		Assert.assertEquals(result.message, DefaultValues.ResourceForbidden, "Error message");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "get_profile" })
	public void GetSessionsOfOtherUser() {
		
		// from other authorized user
		BaseResult result = OpenAPI.getSessions(accessToken, yourUid, fromDate, toDate);
		Assert.assertEquals(result.statusCode, 403, "Status code");
		Assert.assertEquals(result.message, DefaultValues.ResourceForbidden, "Error message");

		// from unauthorized user
		result = OpenAPI.getSessions(accessToken, strangerUid, fromDate, toDate);
		Assert.assertEquals(result.statusCode, 403, "Status code");
		Assert.assertEquals(result.message, DefaultValues.ResourceForbidden, "Error message");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "get_profile" })
	public void GetSessionsInvalidParameters() {
		
		// invalid and missing parameters
		BaseResult result1 = OpenAPI.getSessions(accessToken, "me", null, toDate);
		BaseResult result2 = OpenAPI.getSessions(accessToken, "me", fromDate, null);
		BaseResult result3 = OpenAPI.getSessions(accessToken, "me", "abc", toDate);
		BaseResult result4 = OpenAPI.getSessions(accessToken, "me", fromDate, "def");
		
		Assert.assertEquals(result1.statusCode, 400, "Status code");
		Assert.assertEquals(result1.errorMessage, DefaultValues.MissingParameters, "Error message");
		Assert.assertEquals(result2.statusCode, 400, "Status code");
		Assert.assertEquals(result2.errorMessage, DefaultValues.MissingParameters, "Error message");
		Assert.assertEquals(result3.statusCode, 400, "Status code");
		Assert.assertEquals(result3.errorMessage, DefaultValues.InvalidParameters, "Error message");
		Assert.assertEquals(result4.statusCode, 400, "Status code");
		Assert.assertEquals(result4.errorMessage, DefaultValues.InvalidParameters, "Error message");
		
		
		// valid in format but invalid in logic
		BaseResult result5 = OpenAPI.getSessions(accessToken, "me", toDate, fromDate);
		List<OpenAPISession> sessions = OpenAPISession.getSessionsFromResponse(result5.response);
		
		Assert.assertEquals(result5.statusCode, 200, "Status code");
		Assert.assertEquals(sessions.size(), 0, "Number of sessions");
	}
	
	
	/*
	 * TODO:
	 * - Get a resource using expired token
	 * - Get a resource with only selected fields
	 * - Get a resource from authorized and unauthorized user using app id and app secret
	 */

}
