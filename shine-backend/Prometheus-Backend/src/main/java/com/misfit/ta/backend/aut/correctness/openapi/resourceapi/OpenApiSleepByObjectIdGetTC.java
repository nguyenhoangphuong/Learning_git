package com.misfit.ta.backend.aut.correctness.openapi.resourceapi;

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
import com.misfit.ta.backend.data.openapi.resourceapi.OpenAPISleep;
import com.misfit.ta.backend.data.openapi.resourceapi.OpenAPISleepDetail;
import com.misfit.ta.backend.data.timeline.TimelineItem;
import com.misfit.ta.backend.data.timeline.timelineitemdata.SleepSessionItem;
import com.misfit.ta.common.MVPCommon;
import com.misfit.ta.utils.TextTool;

public class OpenApiSleepByObjectIdGetTC extends OpenAPIAutomationBase {

	private String accessToken;
	private TimelineItem itemA;
	private TimelineItem itemB;
	private TimelineItem itemC;
	
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		
		super.beforeClass();
		
		long timestamp = System.currentTimeMillis() / 1000;
		itemA = DataGenerator.generateRandomSleepTimelineItem(timestamp, null);
		itemB = DataGenerator.generateRandomSleepTimelineItem(timestamp, null);
		itemC = DataGenerator.generateRandomSleepTimelineItem(timestamp, null);

		// call api
		BaseResult resultA = MVPApi.createTimelineItem(myToken, itemA);
		BaseResult resultB = MVPApi.createTimelineItem(yourToken, itemB);
		BaseResult resultC = MVPApi.createTimelineItem(strangerToken, itemC);
		
		itemA.setServerId(TimelineItem.getTimelineItem(resultA.response).getServerId());
		itemB.setServerId(TimelineItem.getTimelineItem(resultB.response).getServerId());
		itemC.setServerId(TimelineItem.getTimelineItem(resultC.response).getServerId());
		
		accessToken = OpenAPI.getAccessToken(myEmail, "qqqqqq", OpenAPI.RESOURCE_SESSION, ClientKey, "https://www.google.com.vn/");
	}
	
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "get_sleeps" })
	public void GetSleepByObjectIdUsingInvalidAccessToken() {
		
		// empty access token
		String nullString = null;
		BaseResult result = OpenAPI.getSleep(nullString, "me", itemA.getServerId());
		Assert.assertEquals(result.statusCode, 401, "Status code");
		Assert.assertEquals(result.code, 401, "OpenAPI code");
		Assert.assertEquals(result.message, DefaultValues.InvalidAccessToken, "Error message");

		// invalid access token
		result = OpenAPI.getSleep(TextTool.getRandomString(10, 10), "me", itemA.getServerId());
		Assert.assertEquals(result.statusCode, 401, "Status code");
		Assert.assertEquals(result.code, 401, "OpenAPI code");
		Assert.assertEquals(result.message, DefaultValues.InvalidAccessToken, "Error message");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "get_sleeps" })
	public void GetSleepByObjectIdWithValidAccessToken() {
		
		// use myUid route
		BaseResult result = OpenAPI.getSleep(accessToken, myUid, itemA.getServerId());
		OpenAPISleep ritem = OpenAPISleep.getSleepFromResponse(result.response);
		SleepSessionItem sessionA = (SleepSessionItem)itemA.getData();
		
		Assert.assertEquals(result.statusCode, 200, "Status code");
		Assert.assertEquals(ritem.getStartTime(), MVPCommon.getISOTime(sessionA.getRealStartTime()), "Sleep start time");
		Assert.assertEquals(ritem.getAutoDetected(), sessionA.getIsAutoDetected(), "Sleep auto detected");
		Assert.assertEquals((int)ritem.getDuration(), sessionA.getRealEndTime() - sessionA.getRealStartTime(), "Sleep duration");

		List<OpenAPISleepDetail> rdetails = ritem.getSleepDetails();
		List<Integer[]> cdetails = sessionA.getSleepStateChanges();
		
		for(int k = 0; k < cdetails.size(); k++) {
			
			long stateChangeTime = sessionA.getRealStartTime() + cdetails.get(k)[0] * 60;
			Assert.assertEquals(rdetails.get(k).getDatetime(), MVPCommon.getISOTime(stateChangeTime), "Sleep state change timestamp");
			Assert.assertEquals(rdetails.get(k).getValue(), cdetails.get(k)[1], "Sleep new state");
		}
		
		
		// use "me" route
		result = OpenAPI.getSleep(accessToken, "me", itemA.getServerId());
		ritem = OpenAPISleep.getSleepFromResponse(result.response);
		sessionA = (SleepSessionItem)itemA.getData();

		Assert.assertEquals(result.statusCode, 200, "Status code");
		Assert.assertEquals(ritem.getStartTime(), MVPCommon.getISOTime(sessionA.getRealStartTime()), "Sleep start time");
		Assert.assertEquals(ritem.getAutoDetected(), sessionA.getIsAutoDetected(), "Sleep auto detected");
		Assert.assertEquals((int)ritem.getDuration(), sessionA.getRealEndTime() - sessionA.getRealStartTime(), "Sleep duration");

		rdetails = ritem.getSleepDetails();
		cdetails = sessionA.getSleepStateChanges();

		for(int k = 0; k < cdetails.size(); k++) {

			long stateChangeTime = sessionA.getRealStartTime() + cdetails.get(k)[0] * 60;
			Assert.assertEquals(rdetails.get(k).getDatetime(), MVPCommon.getISOTime(stateChangeTime), "Sleep state change timestamp");
			Assert.assertEquals(rdetails.get(k).getValue(), cdetails.get(k)[1], "Sleep new state");
		}
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "get_sleeps", "Excluded" })
	public void GetSleepByObjectIdWithoutPermission() {
		
		String invalidScopeAccessToken = OpenAPI.getAccessToken(myEmail, "qqqqqq", OpenAPI.RESOURCE_PROFILE, ClientKey, "https://www.google.com.vn/");
		BaseResult result = OpenAPI.getSleep(invalidScopeAccessToken, "me", itemA.getServerId());
		
		Assert.assertEquals(result.statusCode, 403, "Status code");
		Assert.assertEquals(result.code, 403, "OpenAPI code");
		Assert.assertEquals(result.message, DefaultValues.ResourceForbidden, "Error message");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "get_sleeps" })
	public void GetSleepByObjectIdOfOtherUser() {
				
		// myUid + objectId from other authorized user
		BaseResult result = OpenAPI.getSleep(accessToken, myUid, itemB.getServerId());
		Assert.assertEquals(result.statusCode, 400, "Status code");
		Assert.assertEquals(result.code, 400, "OpenAPI code");
		Assert.assertEquals(result.message, DefaultValues.ObjectNotFound, "Error message");
		
		// "me" + objectId from other authorized user
		result = OpenAPI.getSleep(accessToken, "me", itemC.getServerId());
		Assert.assertEquals(result.statusCode, 400, "Status code");
		Assert.assertEquals(result.code, 400, "OpenAPI code");
		Assert.assertEquals(result.message, DefaultValues.ObjectNotFound, "Error message");

		// from other authorized user
		result = OpenAPI.getSleep(accessToken, yourUid, itemB.getServerId());
		Assert.assertEquals(result.statusCode, 403, "Status code");
		Assert.assertEquals(result.code, 403, "OpenAPI code");
		Assert.assertEquals(result.message, DefaultValues.ResourceForbidden, "Error message");

		// from unauthorized user
		result = OpenAPI.getSleep(accessToken, strangerUid, itemC.getServerId());
		Assert.assertEquals(result.statusCode, 403, "Status code");
		Assert.assertEquals(result.code, 403, "OpenAPI code");
		Assert.assertEquals(result.message, DefaultValues.ResourceForbidden, "Error message");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "get_sleeps" })
	public void GetSleepByObjectIdInvalidObjectId() {
		
		BaseResult result = OpenAPI.getSleep(accessToken, myUid, TextTool.getRandomString(10, 10));
		
		Assert.assertEquals(result.statusCode, 400, "Status code");
		Assert.assertEquals(result.code, 400, "OpenAPI code");
		Assert.assertEquals(result.message, DefaultValues.InvalidParameters, "Error message");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "get_sleeps" })
	public void GetSleepByObjectIdUsingAppCredential() {
		
		// authorized user
		BaseResult result = OpenAPI.getSleep(ClientApp, myUid, itemA.getServerId());
		OpenAPISleep ritem = OpenAPISleep.getSleepFromResponse(result.response);
		SleepSessionItem sessionA = (SleepSessionItem)itemA.getData();
		
		Assert.assertEquals(result.statusCode, 200, "Status code");
		Assert.assertEquals(ritem.getStartTime(), MVPCommon.getISOTime(sessionA.getRealStartTime()), "Sleep start time");
		Assert.assertEquals(ritem.getAutoDetected(), sessionA.getIsAutoDetected(), "Sleep auto detected");
		Assert.assertEquals((int)ritem.getDuration(), sessionA.getRealEndTime() - sessionA.getRealStartTime(), "Sleep duration");

		List<OpenAPISleepDetail> rdetails = ritem.getSleepDetails();
		List<Integer[]> cdetails = sessionA.getSleepStateChanges();
		
		for(int k = 0; k < cdetails.size(); k++) {
			
			long stateChangeTime = sessionA.getRealStartTime() + cdetails.get(k)[0] * 60;
			Assert.assertEquals(rdetails.get(k).getDatetime(), MVPCommon.getISOTime(stateChangeTime), "Sleep state change timestamp");
			Assert.assertEquals(rdetails.get(k).getValue(), cdetails.get(k)[1], "Sleep new state");
		}
		
		
		// unauthorized user
		result = OpenAPI.getSleep(ClientApp, strangerUid, itemA.getServerId());
		
		Assert.assertEquals(result.statusCode, 403, "Status code");
		Assert.assertEquals(result.code, 403, "OpenAPI code");
		Assert.assertEquals(result.message, DefaultValues.UnauthorizedAccess, "Error message");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "get_sleeps" })
	public void GetSleepByObjectIdWithStateEqualsTo1() {
		
		// create a new timeline item
		long timestamp = System.currentTimeMillis() / 1000 - 3600 * 24 * 5;
		TimelineItem deletedSleep = DataGenerator.generateRandomSleepTimelineItem(timestamp, null);
		BaseResult result = MVPApi.createTimelineItem(myToken, deletedSleep);
		deletedSleep.setServerId(TimelineItem.getTimelineItem(result.response).getServerId());
		long startTime = ((SleepSessionItem)deletedSleep.getData()).getRealStartTime();
		
		
		// get sleep
		result = OpenAPI.getSleep(accessToken, "me", deletedSleep.getServerId());
		OpenAPISleep rsleep = OpenAPISleep.getSleepFromResponse(result.response);
		
		Assert.assertEquals(result.statusCode, 200, "Status code");
		Assert.assertEquals(rsleep.getStartTime(), MVPCommon.getISOTime(startTime), "Sleep start time");
		
		
		// now update state of that sleep to 1
		deletedSleep.setState(1);
		MVPApi.updateTimelineItem(myToken, deletedSleep);
		
		
		// now get sleep again
		result = OpenAPI.getSleep(accessToken, "me", deletedSleep.getServerId());

		Assert.assertEquals(result.statusCode, 400, "Status code");
		Assert.assertEquals(result.code, 400, "OpenAPI code");
		Assert.assertEquals(result.message, DefaultValues.ObjectNotFound, "Error message");
	}
	
	/*
	 * TODO:
	 * - Get a resource using expired token
	 * - Get a resource with only selected fields
	 */

}
