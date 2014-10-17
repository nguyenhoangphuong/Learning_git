package com.misfit.ta.backend.aut.correctness.openapi.resourceapi;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

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
import com.misfit.ta.backend.data.openapi.resourceapi.OpenAPISleep;
import com.misfit.ta.backend.data.openapi.resourceapi.OpenAPISleepDetail;
import com.misfit.ta.backend.data.timeline.TimelineItem;
import com.misfit.ta.backend.data.timeline.timelineitemdata.SleepSessionItem;
import com.misfit.ta.common.MVPCommon;
import com.misfit.ta.common.Verify;
import com.misfit.ta.utils.TextTool;

public class OpenApiSleepsGetTC extends OpenAPIAutomationBase {

	private String accessToken;
	private List<List<TimelineItem>> allTimelineItems;
	private List<Goal> goals;
	
	private String fromDate = MVPCommon.getDateString(System.currentTimeMillis() / 1000 - 3600 * 24 * 2);
	private String toDate = MVPCommon.getDateString(System.currentTimeMillis() / 1000);
	
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		
		super.beforeClass();
		
		allTimelineItems = new ArrayList<List<TimelineItem>>();
		goals = new ArrayList<Goal>();
		List<TimelineItem> batchItems = new ArrayList<TimelineItem>();
		
		// 5 days
		for(int i = 0; i < 5; i++) {
		
			long timestamp = System.currentTimeMillis() / 1000 - i * 3600 * 24;
			List<TimelineItem> items = new ArrayList<TimelineItem>();
			
			// create goal
			Goal goal = Goal.getDefaultGoal(timestamp);
			goals.add(goal);
			MVPApi.createGoal(myToken, goal);
						
			// add sleep item
			items.add(DataGenerator.generateRandomSleepTimelineItem(timestamp + 600 * 1, null));
			
			// add some other timeline items (session, milestone, achievement)
			items.add(DataGenerator.generateRandomActivitySessionTimelineItem(timestamp + 600 * 1, null));
			items.add(DataGenerator.generateRandomFoodTimelineItem(timestamp + 600 * 2, null));
			items.add(DataGenerator.generateRandomLifetimeDistanceItem(timestamp + 600 * 3, null));
								
			allTimelineItems.add(items);
			batchItems.addAll(items);
		}
		
		// call api
		MVPApi.createTimelineItems(myToken, batchItems);
		MVPApi.createTimelineItems(yourToken, batchItems);
		MVPApi.createTimelineItems(strangerToken, batchItems);
		
		accessToken = OpenAPI.getAccessToken(myEmail, "qqqqqq", OpenAPI.RESOURCE_SLEEP, ClientKey, "http://misfit.com/");
	}
	
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "get_sleeps" })
	public void GetSleepsUsingInvalidAccessToken() {
		
		// empty access token
		String nullString = null;
		BaseResult result = OpenAPI.getSleeps(nullString, "me", fromDate, toDate);
		Assert.assertEquals(result.statusCode, 401, "Status code");
		Assert.assertEquals(result.code, 401, "OpenAPI result code");
		Assert.assertEquals(result.message, DefaultValues.InvalidAccessToken, "Error message");

		// invalid access token
		result = OpenAPI.getSleeps(TextTool.getRandomString(10, 10), "me", fromDate, toDate);
		Assert.assertEquals(result.statusCode, 401, "Status code");
		Assert.assertEquals(result.code, 401, "OpenAPI result code");
		Assert.assertEquals(result.message, DefaultValues.InvalidAccessToken, "Error message");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "get_sleeps" })
	public void GetSleepsWithValidAccessToken() {
		
		// use "me" route
		BaseResult result = OpenAPI.getSleeps(accessToken, "me", fromDate, toDate);
		List<OpenAPISleep> rsleeps = OpenAPISleep.getSleepsFromResponse(result.response);
		
		Assert.assertEquals(result.statusCode, 200, "Status code");
		Assert.assertEquals(rsleeps.size(), 3, "Number of sleeps in response");
				
		
		// use "myUid" route
		result = OpenAPI.getSleeps(accessToken, myUid, fromDate, toDate);
		rsleeps = OpenAPISleep.getSleepsFromResponse(result.response);
		
		Assert.assertEquals(result.statusCode, 200, "Status code");
		Assert.assertEquals(rsleeps.size(), 3, "Number of sleeps in response");
		
		
		// fromDate == toDate
		for(int i = 0; i < 5; i++) {

			String date = MVPCommon.getDateString(System.currentTimeMillis() / 1000 - 3600 * 24 * i);
			result = OpenAPI.getSleeps(accessToken, myUid, date, date);
			rsleeps = OpenAPISleep.getSleepsFromResponse(result.response);

			Assert.assertEquals(result.statusCode, 200, "Status code");
			Assert.assertEquals(rsleeps.size(), 1, "Number of sleeps in response");
			
			SleepSessionItem csession = (SleepSessionItem)allTimelineItems.get(i).get(0).getData();
			OpenAPISleep rsession = rsleeps.get(0);

			Assert.assertEquals(rsession.getStartTime(), MVPCommon.getISO8601Time(csession.getRealStartTime(), goals.get(i).getTimeZoneOffsetInSeconds()), "Sleep start time");
			Assert.assertEquals(rsession.getAutoDetected(), csession.getIsAutoDetected(), "Sleep auto detected");
			Assert.assertEquals((int)rsession.getDuration(), csession.getRealEndTime() - csession.getRealStartTime(), "Sleep duration");

			List<OpenAPISleepDetail> rdetails = rsession.getSleepDetails();
			List<Integer[]> cdetails = csession.getSleepStateChanges();
			
			for(int k = 0; k < cdetails.size(); k++) {
				
				long stateChangeTime = csession.getRealStartTime() + cdetails.get(k)[0] * 60;
				Assert.assertEquals(rdetails.get(k).getDatetime(), MVPCommon.getISO8601Time(stateChangeTime, goals.get(i).getTimeZoneOffsetInSeconds()), "Sleep state change timestamp");
				Assert.assertEquals(rdetails.get(k).getValue(), cdetails.get(k)[1], "Sleep new state");
			}
		}
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "get_sleeps", "Excluded" })
	public void GetSleepsWithoutPermission() {
		
		String invalidScopeAccessToken = OpenAPI.getAccessToken(myEmail, "qqqqqq", OpenAPI.RESOURCE_PROFILE, ClientKey, "http://misfit.com/");
		BaseResult result = OpenAPI.getSleeps(invalidScopeAccessToken, "me", fromDate, toDate);
		
		Assert.assertEquals(result.statusCode, 403, "Status code");
		Assert.assertEquals(result.code, 403, "OpenAPI code");
		Assert.assertEquals(result.message, DefaultValues.ResourceForbidden, "Error message");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "get_sleeps" })
	public void GetSleepsOfOtherUser() {
		
		// from other authorized user
		BaseResult result = OpenAPI.getSleeps(accessToken, yourUid, fromDate, toDate);
		Assert.assertEquals(result.statusCode, 403, "Status code");
		Assert.assertEquals(result.code, 403, "OpenAPI code");
		Assert.assertEquals(result.message, DefaultValues.ResourceForbidden, "Error message");

		// from unauthorized user
		result = OpenAPI.getSleeps(accessToken, strangerUid, fromDate, toDate);
		Assert.assertEquals(result.statusCode, 403, "Status code");
		Assert.assertEquals(result.code, 403, "OpenAPI code");
		Assert.assertEquals(result.message, DefaultValues.ResourceForbidden, "Error message");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "get_sleeps", "KnownIssue" })
	public void GetSleepsInvalidParameters() {
		
		// invalid and missing parameters
		BaseResult result1 = OpenAPI.getSleeps(accessToken, "me", null, toDate);
		BaseResult result2 = OpenAPI.getSleeps(accessToken, "me", fromDate, null);
		BaseResult result3 = OpenAPI.getSleeps(accessToken, "me", "abc", toDate);
		BaseResult result4 = OpenAPI.getSleeps(accessToken, "me", fromDate, "def");
		
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
		BaseResult result5 = OpenAPI.getSleeps(accessToken, "me", toDate, fromDate);
		List<OpenAPISleep> sessions = OpenAPISleep.getSleepsFromResponse(result5.response);
		
		Assert.assertEquals(result5.statusCode, 200, "Status code");
		Assert.assertEquals(sessions.size(), 0, "Number of sessions");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "get_sleeps" })
	public void GetSleepsUsingAppCredential() {
		
		// authorized user
		BaseResult result = OpenAPI.getSleeps(ClientApp, myUid, fromDate, toDate);
		List<OpenAPISleep> rsleeps = OpenAPISleep.getSleepsFromResponse(result.response);
		
		Assert.assertEquals(result.statusCode, 200, "Status code");
		Assert.assertEquals(rsleeps.size(), 3, "Number of sleeps in response");
				
		
		// unauthorized user
		result = OpenAPI.getSleeps(ClientApp, strangerUid, fromDate, toDate);
		
		Assert.assertEquals(result.statusCode, 403, "Status code");
		Assert.assertEquals(result.code, 403, "OpenAPI code");
		Assert.assertEquals(result.message, DefaultValues.UnauthorizedAccess, "Error message");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "get_sleeps" })
	public void GetSleepsWithStateEqualsTo1() {
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		
		// create a new timeline item
		long timestamp = cal.getTimeInMillis() / 1000 - 3600 * 24 * 5;
		MVPApi.createGoal(myToken, Goal.getDefaultGoal(timestamp));
		TimelineItem deletedSleep = DataGenerator.generateRandomSleepTimelineItem(timestamp, null);
		BaseResult result = MVPApi.createTimelineItem(myToken, deletedSleep);
		deletedSleep.setServerId(TimelineItem.getTimelineItem(result.response).getServerId());

		
		// get sleep
		String date = MVPCommon.getDateString(timestamp);
		result = OpenAPI.getSleeps(accessToken, "me", date, date);
		List<OpenAPISleep> rsleeps = OpenAPISleep.getSleepsFromResponse(result.response);
		
		Assert.assertEquals(result.statusCode, 200, "Status code");
		Assert.assertEquals(rsleeps.size(), 1, "Number of sleeps in response");
		
		
		// now update state of that sleep to 1
		deletedSleep.setState(1);
		MVPApi.updateTimelineItem(myToken, deletedSleep);
		
		
		// now get sleep again
		result = OpenAPI.getSleeps(accessToken, "me", date, date);
		rsleeps = OpenAPISleep.getSleepsFromResponse(result.response);

		Assert.assertEquals(result.statusCode, 200, "Status code");
		Assert.assertEquals(rsleeps.size(), 0, "Number of sleeps in response");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "get_sleeps" })
	public void GetSleepsDifferentTimezones() {
		
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
		
		int[] goalTimezoneOffsets = new int[] {0, +7, -5, +7, +10, 0};
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
		List<TimelineItem> itemsNextDay = new ArrayList<TimelineItem>();
		List<TimelineItem> itemsToday = new ArrayList<TimelineItem>();
		for(int i = 0; i < goalTimezoneOffsets.length - 1; i++) {
			
			// 17pm today -> 3am tomorrow
			itemsNextDay.add(DataGenerator.generateRandomSleepTimelineItem(
					goals[i].getEndTime() - MVPCommon.randLong(3600 * 5, 3600 * 7), 
					goals[i].getEndTime() + MVPCommon.randLong(3600, 3600 * 3), 
					null));
			
			// 21pm today -> 7am tomorrow
			itemsNextDay.add(DataGenerator.generateRandomSleepTimelineItem(
					goals[i].getEndTime() - MVPCommon.randLong(3600 * 1, 3600 * 3), 
					goals[i].getEndTime() + MVPCommon.randLong(3600 * 5, 3600 * 7), 
					null));
			
			// 1am -> 10am today
			itemsToday.add(DataGenerator.generateRandomSleepTimelineItem(
					goals[i].getStartTime() + MVPCommon.randLong(3600 * 1, 3600 * 2), 
					goals[i].getStartTime() + MVPCommon.randLong(3600 * 6, 3600 * 10), 
					null));

			// 2am -> 11am today
			itemsToday.add(DataGenerator.generateRandomSleepTimelineItem(
					goals[i].getStartTime() + MVPCommon.randLong(3600 * 2, 3600 * 3), 
					goals[i].getStartTime() + MVPCommon.randLong(3600 * 6, 3600 * 11), 
					null));
			
//			// 17pm -> 22pm today
//			itemsToday.add(DataGenerator.generateRandomSleepTimelineItem(
//					goals[i].getEndTime() - MVPCommon.randLong(3600 * 5, 3600 * 7), 
//					goals[i].getEndTime() - MVPCommon.randLong(3600 * 1, 3600 * 2), 
//					null));
		}

		MVPApi.createTimelineItems(token, itemsNextDay);
		MVPApi.createTimelineItems(token, itemsToday);
		
		
		// query resource
		List<String> errors = new ArrayList<String>();
		String accessToken = OpenAPI.getAccessToken(email, "qqqqqq", OpenAPI.RESOURCE_SLEEP, ClientKey, "http://misfit.com/");
		for(int i = 0; i < goalTimezoneOffsets.length - 1; i++) {
			
			BaseResult result = OpenAPI.getSleeps(accessToken, uid, dates[i], dates[i]);
			List<OpenAPISleep> rsleeps = OpenAPISleep.getSleepsFromResponse(result.response);
			
			errors.add(Verify.verifyEquals(result.statusCode, 200, "Status code"));
			errors.add(Verify.verifyEquals(rsleeps.size(), 
					i == 0 || i == goalTimezoneOffsets.length ? 2 : 4, 
					String.format("Number of sleeps in response in goals[%d]", i)));
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
