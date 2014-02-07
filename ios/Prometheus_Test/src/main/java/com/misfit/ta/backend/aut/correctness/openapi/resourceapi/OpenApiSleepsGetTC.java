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
import com.misfit.ta.backend.data.openapi.resourceapi.OpenAPISleep;
import com.misfit.ta.backend.data.openapi.resourceapi.OpenAPISleepDetail;
import com.misfit.ta.backend.data.timeline.TimelineItem;
import com.misfit.ta.backend.data.timeline.timelineitemdata.SleepSessionItem;
import com.misfit.ta.utils.TextTool;

public class OpenApiSleepsGetTC extends OpenAPIAutomationBase {

	private String accessToken;
	private List<List<TimelineItem>> allTimelineItems;
	
	private String fromDate = getDateString(System.currentTimeMillis() / 1000 - 3600 * 24 * 2);
	private String toDate = getDateString(System.currentTimeMillis() / 1000);
	
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		
		super.beforeClass();
		
		allTimelineItems = new ArrayList<List<TimelineItem>>();
		List<TimelineItem> batchItems = new ArrayList<TimelineItem>();
		
		// 5 days
		for(int i = 0; i < 5; i++) {
		
			long timestamp = System.currentTimeMillis() / 1000 - i * 3600 * 24;
			List<TimelineItem> items = new ArrayList<TimelineItem>();
			
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
		
		accessToken = OpenAPI.getAccessToken(myEmail, "qqqqqq", OpenAPI.RESOURCE_GOAL, ClientKey, "/");
	}
	
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "get_sleeps" })
	public void GetSleepsUsingInvalidAccessToken() {
		
		// empty access token
		BaseResult result = OpenAPI.getSleeps(null, "me", fromDate, toDate);
		Assert.assertEquals(result.statusCode, 401, "Status code");
		Assert.assertEquals(result.message, DefaultValues.InvalidAccessToken, "Error message");

		// invalid access token
		result = OpenAPI.getSleeps(TextTool.getRandomString(10, 10), "me", fromDate, toDate);
		Assert.assertEquals(result.statusCode, 401, "Status code");
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

			String date = getDateString(System.currentTimeMillis() / 1000 - 3600 * 24 * i);
			result = OpenAPI.getSleeps(accessToken, myUid, date, date);
			rsleeps = OpenAPISleep.getSleepsFromResponse(result.response);

			Assert.assertEquals(result.statusCode, 200, "Status code");
			Assert.assertEquals(rsleeps.size(), 1, "Number of sleeps in response");
			
			SleepSessionItem csession = (SleepSessionItem)allTimelineItems.get(i).get(0).getData();
			OpenAPISleep rsession = rsleeps.get(0);

			Assert.assertEquals(rsession.getStartTime(), getISOTime(csession.getRealStartTime()), "Sleep start time");
			Assert.assertEquals(rsession.getAutoDetected(), csession.getIsAutoDetected(), "Sleep auto detected");
			Assert.assertEquals((int)rsession.getDuration(), csession.getRealEndTime() - csession.getRealStartTime(), "Sleep duration");

			List<OpenAPISleepDetail> rdetails = rsession.getSleepDetails();
			List<Integer[]> cdetails = csession.getSleepStateChanges();
			
			for(int k = 0; k < cdetails.size(); k++) {
				
				long stateChangeTime = csession.getRealStartTime() + cdetails.get(k)[0] * 60;
				Assert.assertEquals(rdetails.get(k).getDatetime(), getISOTime(stateChangeTime), "Sleep state change timestamp");
				Assert.assertEquals(rdetails.get(k).getValue(), cdetails.get(k)[1], "Sleep new state");
			}
		}
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "get_sleeps", "Excluded" })
	public void GetSleepsWithoutPermission() {
		
		String invalidScopeAccessToken = OpenAPI.getAccessToken(myEmail, "qqqqqq", OpenAPI.RESOURCE_PROFILE, ClientKey, "/");
		BaseResult result = OpenAPI.getSleeps(invalidScopeAccessToken, "me", fromDate, toDate);
		
		Assert.assertEquals(result.statusCode, 403, "Status code");
		Assert.assertEquals(result.message, DefaultValues.ResourceForbidden, "Error message");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "get_sleeps" })
	public void GetSleepsOfOtherUser() {
		
		// from other authorized user
		BaseResult result = OpenAPI.getSleeps(accessToken, yourUid, fromDate, toDate);
		Assert.assertEquals(result.statusCode, 403, "Status code");
		Assert.assertEquals(result.message, DefaultValues.ResourceForbidden, "Error message");

		// from unauthorized user
		result = OpenAPI.getSleeps(accessToken, strangerUid, fromDate, toDate);
		Assert.assertEquals(result.statusCode, 403, "Status code");
		Assert.assertEquals(result.message, DefaultValues.ResourceForbidden, "Error message");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "get_sleeps" })
	public void GetSleepsInvalidParameters() {
		
		// invalid and missing parameters
		BaseResult result1 = OpenAPI.getSleeps(accessToken, "me", null, toDate);
		BaseResult result2 = OpenAPI.getSleeps(accessToken, "me", fromDate, null);
		BaseResult result3 = OpenAPI.getSleeps(accessToken, "me", "abc", toDate);
		BaseResult result4 = OpenAPI.getSleeps(accessToken, "me", fromDate, "def");
		
		Assert.assertEquals(result1.statusCode, 400, "Status code");
		Assert.assertEquals(result1.errorMessage, DefaultValues.MissingParameters, "Error message");
		Assert.assertEquals(result2.statusCode, 400, "Status code");
		Assert.assertEquals(result2.errorMessage, DefaultValues.MissingParameters, "Error message");
		Assert.assertEquals(result3.statusCode, 400, "Status code");
		Assert.assertEquals(result3.errorMessage, DefaultValues.InvalidParameters, "Error message");
		Assert.assertEquals(result4.statusCode, 400, "Status code");
		Assert.assertEquals(result4.errorMessage, DefaultValues.InvalidParameters, "Error message");
		
		
		// valid in format but invalid in logic
		BaseResult result5 = OpenAPI.getSleeps(accessToken, "me", toDate, fromDate);
		List<OpenAPISleep> sessions = OpenAPISleep.getSleepsFromResponse(result5.response);
		
		Assert.assertEquals(result5.statusCode, 200, "Status code");
		Assert.assertEquals(sessions.size(), 0, "Number of sessions");
	}
	
	
	/*
	 * TODO:
	 * - Get a deleted sleep tile (state == 1)
	 * - Get a resource using expired token
	 * - Get a resource with only selected fields
	 * - Get a resource from authorized and unauthorized user using app id and app secret
	 */

}
