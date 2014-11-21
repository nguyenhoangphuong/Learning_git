package com.misfit.ta.backend.aut.correctness.openapi.resourceapi;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.misfit.ta.backend.api.metawatch.MVPApi;
import com.misfit.ta.backend.api.openapi.OpenAPI;
import com.misfit.ta.backend.aut.DefaultValues;
import com.misfit.ta.backend.aut.correctness.openapi.OpenAPIAutomationBase;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.backend.data.DataGenerator;
import com.misfit.ta.backend.data.goal.Goal;
import com.misfit.ta.backend.data.openapi.resourceapi.OpenAPISession;
import com.misfit.ta.backend.data.timeline.TimelineItem;
import com.misfit.ta.backend.data.timeline.timelineitemdata.ActivitySessionItem;
import com.misfit.ta.common.MVPCommon;
import com.misfit.ta.common.Verify;
import com.misfit.ta.utils.TextTool;

public class OpenApiSessionsGetTC extends OpenAPIAutomationBase {

	private String accessToken;
	private List<List<TimelineItem>> allSessions;
	private List<Goal> goals;
	
	private String fromDate = MVPCommon.getDateString(System.currentTimeMillis() / 1000 - 3600 * 24 * 2);
	private String toDate = MVPCommon.getDateString(System.currentTimeMillis() / 1000);
	
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		
		super.beforeClass();
		
		allSessions = new ArrayList<List<TimelineItem>>();
		goals = new ArrayList<Goal>();
		List<TimelineItem> batchItems = new ArrayList<TimelineItem>();
		
		// 5 days
		for(int i = 0; i < 5; i++) {
		
			long timestamp = System.currentTimeMillis() / 1000 - i * 3600 * 24;
			List<TimelineItem> sessions = new ArrayList<TimelineItem>();
			
			// create goal
			Goal goal = Goal.getDefaultGoal(timestamp);
			goals.add(goal);
			MVPApi.createGoal(myToken, goal);
			
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
		
		accessToken = OpenAPI.getAccessToken(myEmail, "qqqqqq", OpenAPI.RESOURCE_SESSION, ClientKey, "http://misfit.com/");
	}
	
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "get_sessions" })
	public void GetSessionsUsingInvalidAccessToken() {
		
		// empty access token
		String nullString = null;
		BaseResult result = OpenAPI.getSessions(nullString, "me", fromDate, toDate);
		Assert.assertEquals(result.statusCode, 401, "Status code");
		Assert.assertEquals(result.message, DefaultValues.InvalidAccessToken, "Error message");

		// invalid access token
		result = OpenAPI.getSessions(TextTool.getRandomString(10, 10), "me", fromDate, toDate);
		Assert.assertEquals(result.statusCode, 401, "Status code");
		Assert.assertEquals(result.message, DefaultValues.InvalidAccessToken, "Error message");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "get_sessions" })
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

			String date = MVPCommon.getDateString(System.currentTimeMillis() / 1000 - 3600 * 24 * i);
			result = OpenAPI.getSessions(accessToken, myUid, date, date);
			rsessions = OpenAPISession.getSessionsFromResponse(result.response);

			Assert.assertEquals(result.statusCode, 200, "Status code");
			Assert.assertEquals(rsessions.size(), 3, "Number of sessions in response");
			
			for(int j = 0; j < 3; j++) {
				
				ActivitySessionItem csession = (ActivitySessionItem)allSessions.get(i).get(j).getData();
				OpenAPISession rsession = rsessions.get(j);
				
				Assert.assertEquals(rsession.getStartTime(), MVPCommon.getISO8601Time(allSessions.get(i).get(j).getTimestamp(), goals.get(i).getTimeZoneOffsetInSeconds()) , "Activity start time");
				Assert.assertEquals(rsession.getActivityType(), MVPCommon.getActivityName(csession.getActivityType()) , "Activity type");
				Assert.assertEquals(rsession.getCalories(), MVPCommon.round(csession.getCalories(), 1), "Activity calories");
				Assert.assertEquals(rsession.getDistance(), MVPCommon.round(csession.getDistance(), 1), "Activity distance");
				Assert.assertEquals(rsession.getDuration(), csession.getDuration(), "Activity duration");
				Assert.assertEquals(rsession.getPoints(), csession.getPoint() / 2.5, "Activity points");
				Assert.assertEquals(rsession.getSteps(), csession.getSteps(), "Activity steps");
			}
		}
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "get_sessions", "Excluded" })
	public void GetSessionsWithoutPermission() {
		
		String invalidScopeAccessToken = OpenAPI.getAccessToken(myEmail, "qqqqqq", OpenAPI.RESOURCE_PROFILE, ClientKey, "http://misfit.com/");
		BaseResult result = OpenAPI.getSessions(invalidScopeAccessToken, "me", fromDate, toDate);
		
		Assert.assertEquals(result.statusCode, 403, "Status code");
		Assert.assertEquals(result.message, DefaultValues.ResourceForbidden, "Error message");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "get_sessions" })
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
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "get_sessions" })
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
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "get_sessions" })
	public void GetSessionsUsingAppCredential() {
		
		// authorized user
		BaseResult result = OpenAPI.getSessions(ClientApp, myUid, fromDate, toDate);
		List<OpenAPISession> rsessions = OpenAPISession.getSessionsFromResponse(result.response);
		
		Assert.assertEquals(result.statusCode, 200, "Status code");
		Assert.assertEquals(rsessions.size(), 9, "Number of sessions in response");
				
		
		// unauthorized user
		result = OpenAPI.getSessions(ClientApp, strangerUid, fromDate, toDate);
		
		Assert.assertEquals(result.statusCode, 403, "Status code");
		Assert.assertEquals(result.code, 403, "OpenAPI code");
		Assert.assertEquals(result.message, DefaultValues.UnauthorizedAccess, "Error message");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "get_sessions" })
	public void GetSessionsDifferentTimezones() {
		
		// create account
		String email = MVPApi.generateUniqueEmail();
		String token = MVPApi.signUp(email, "qqqqqq").token;
		String uid = MVPApi.getUserId(token);
		
		// create some goals with different timezone
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		cal.setTimeInMillis(System.currentTimeMillis() - 3600 * 24 * 5 * 1000);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		
		int[] goalTimezoneOffsets = new int[] {0, +7, -5, +7, +10};
		Goal[] goals = new Goal[goalTimezoneOffsets.length];
		String[] dates = new String[goalTimezoneOffsets.length];
		
		for(int i = 0; i < goalTimezoneOffsets.length; i++) {

			// create goal
			long startTime = MVPCommon.getDayStartEpoch(cal.getTimeInMillis() / 1000, TimeZone.getTimeZone("UTC")) - 
					(i == 0 ? 0 : goalTimezoneOffsets[i - 1] * 3600);
			long endTime = MVPCommon.getDayEndEpoch(cal.getTimeInMillis() / 1000, TimeZone.getTimeZone("UTC")) - 
					goalTimezoneOffsets[i] * 3600;
			goals[i] = Goal.getDefaultGoal();
			goals[i].setStartTime(startTime);
			goals[i].setEndTime(endTime);
			goals[i].setTimeZoneOffsetInSeconds(i == 0 ? 0 : goalTimezoneOffsets[i - 1] * 3600);
			
			MVPApi.createGoal(token, goals[i]);
			
			// save date string
			dates[i] = MVPCommon.getUTCDateString(cal.getTimeInMillis() / 1000);
			cal.setTimeInMillis(cal.getTimeInMillis() + 86400 * 1000);
		}
		
		// create some sleeps		
		List<TimelineItem> items = new ArrayList<TimelineItem>();
		for(int i = 0; i < goalTimezoneOffsets.length; i++) {
			
			// at the beginning of the day
			items.add(DataGenerator.generateRandomActivitySessionTimelineItem(
					goals[i].getStartTime(), 
					null));
			
			// random
			items.add(DataGenerator.generateRandomActivitySessionTimelineItem(
					goals[i].getStartTime() + MVPCommon.randLong(100, 200), 
					null));
			
			items.add(DataGenerator.generateRandomActivitySessionTimelineItem(
					goals[i].getStartTime() + MVPCommon.randLong(200, 300), 
					null));

//			// in the middle of the day
//			items.add(DataGenerator.generateRandomActivitySessionTimelineItem(
//					MVPCommon.randLong(goals[i].getStartTime() + 1, goals[i].getEndTime() - 1),
//					null));
//			
//			// at the end of the day
//			items.add(DataGenerator.generateRandomActivitySessionTimelineItem(
//					goals[i].getEndTime(), 
//					null));
		}

		MVPApi.createTimelineItems(token, items);
		
		// query resource
		List<String> errors = new ArrayList<String>();
		String accessToken = OpenAPI.getAccessToken(email, "qqqqqq", OpenAPI.RESOURCE_SESSION, ClientKey, "http://misfit.com/");
		for(int i = 0; i < goalTimezoneOffsets.length; i++) {
			
			BaseResult result = OpenAPI.getSessions(accessToken, uid, dates[i], dates[i]);
			List<OpenAPISession> rsessions = OpenAPISession.getSessionsFromResponse(result.response);
			
			errors.add(Verify.verifyEquals(result.statusCode, 200, "Status code"));
			errors.add(Verify.verifyEquals(rsessions.size(), 3, 
					String.format("Number of sessions in response in goals[%d]", i)));
			
			for(int j = 0; j < rsessions.size(); j++) {
				
				ActivitySessionItem expect = (ActivitySessionItem)items.get(i * 3 + j).getData();
				OpenAPISession actual = rsessions.get(j);
				
				errors.add(Verify.verifyEquals(actual.getStartTime(), MVPCommon.getISO8601Time(items.get(i * 3 + j).getTimestamp(), goals[i].getTimeZoneOffsetInSeconds()) , "Activity start time"));
				errors.add(Verify.verifyEquals(actual.getActivityType(), MVPCommon.getActivityName(expect.getActivityType()) , "Activity type"));
				errors.add(Verify.verifyEquals(actual.getCalories(), MVPCommon.round(expect.getCalories(), 1), "Activity calories"));
				errors.add(Verify.verifyEquals(actual.getDistance(), MVPCommon.round(expect.getDistance(), 1), "Activity distance"));
				errors.add(Verify.verifyEquals(actual.getDuration(), expect.getDuration(), "Activity duration"));
				errors.add(Verify.verifyEquals(actual.getPoints(), expect.getPoint() / 2.5, "Activity points"));
				errors.add(Verify.verifyEquals(actual.getSteps(), expect.getSteps(), "Activity steps"));
			}
		}
		
		if(!Verify.verifyAll(errors))
			Assert.fail("Not all assertions pass");
	}

	
	/*
	 * TODO:
	 * - Get a resource using expired token
	 * - Get a resource with only selected fields
	 */

}
