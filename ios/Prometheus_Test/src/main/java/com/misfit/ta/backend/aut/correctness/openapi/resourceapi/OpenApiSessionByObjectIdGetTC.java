package com.misfit.ta.backend.aut.correctness.openapi.resourceapi;

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

public class OpenApiSessionByObjectIdGetTC extends OpenAPIAutomationBase {

	private String accessToken;
	private TimelineItem itemA;
	private TimelineItem itemB;
	private TimelineItem itemC;
	
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		
		super.beforeClass();
		
		long timestamp = System.currentTimeMillis() / 1000;
		itemA = DataGenerator.generateRandomActivitySessionTimelineItem(timestamp, null);
		itemB = DataGenerator.generateRandomActivitySessionTimelineItem(timestamp, null);
		itemC = DataGenerator.generateRandomActivitySessionTimelineItem(timestamp, null);

		// call api
		BaseResult resultA = MVPApi.createTimelineItem(myToken, itemA);
		BaseResult resultB = MVPApi.createTimelineItem(yourToken, itemB);
		BaseResult resultC = MVPApi.createTimelineItem(strangerToken, itemC);
		
		itemA.setServerId(TimelineItem.getTimelineItem(resultA.response).getServerId());
		itemB.setServerId(TimelineItem.getTimelineItem(resultB.response).getServerId());
		itemC.setServerId(TimelineItem.getTimelineItem(resultC.response).getServerId());
		
		accessToken = OpenAPI.getAccessToken(myEmail, "qqqqqq", OpenAPI.RESOURCE_SESSION, ClientKey, "/");
	}
	
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "get_sessions" })
	public void GetSessionByObjectIdUsingInvalidAccessToken() {
		
		// empty access token
		String nullString = null;
		BaseResult result = OpenAPI.getSession(nullString, "me", itemA.getServerId());
		Assert.assertEquals(result.statusCode, 401, "Status code");
		Assert.assertEquals(result.message, DefaultValues.InvalidAccessToken, "Error message");

		// invalid access token
		result = OpenAPI.getSession(TextTool.getRandomString(10, 10), "me", itemA.getServerId());
		Assert.assertEquals(result.statusCode, 401, "Status code");
		Assert.assertEquals(result.message, DefaultValues.InvalidAccessToken, "Error message");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "get_sessions" })
	public void GetSessionByObjectIdWithValidAccessToken() {
		
		// use myUid route
		BaseResult result = OpenAPI.getSession(accessToken, myUid, itemA.getServerId());
		OpenAPISession ritem = OpenAPISession.getSessionFromResponse(result.response);
		ActivitySessionItem sessionA = (ActivitySessionItem)itemA.getData();
		
		Assert.assertEquals(result.statusCode, 200, "Status code");
		Assert.assertEquals(ritem.getStartTime(), getISOTime(itemA.getTimestamp()) , "Activity start time");
		Assert.assertEquals(ritem.getActivityType(), MVPCommon.getActivityName(sessionA.getActivityType()) , "Activity type");
		Assert.assertEquals(ritem.getCalories(), MVPCommon.round(sessionA.getCalories(), 1), "Activity calories");
		Assert.assertEquals(ritem.getDistance(), MVPCommon.round(sessionA.getDistance(), 1), "Activity distance");
		Assert.assertEquals(ritem.getDuration(), sessionA.getDuration(), "Activity duration");
		Assert.assertEquals(ritem.getPoints(), sessionA.getPoint() / 2.5, "Activity points");
		Assert.assertEquals(ritem.getSteps(), sessionA.getSteps(), "Activity steps");
		
		// use "me" route
		result = OpenAPI.getSession(accessToken, "me", itemA.getServerId());
		ritem = OpenAPISession.getSessionFromResponse(result.response);
		sessionA = (ActivitySessionItem)itemA.getData();

		Assert.assertEquals(result.statusCode, 200, "Status code");
		Assert.assertEquals(ritem.getStartTime(), getISOTime(itemA.getTimestamp()) , "Activity start time");
		Assert.assertEquals(ritem.getActivityType(), MVPCommon.getActivityName(sessionA.getActivityType()) , "Activity type");
		Assert.assertEquals(ritem.getCalories(), MVPCommon.round(sessionA.getCalories(), 1), "Activity calories");
		Assert.assertEquals(ritem.getDistance(), MVPCommon.round(sessionA.getDistance(), 1), "Activity distance");
		Assert.assertEquals(ritem.getDuration(), sessionA.getDuration(), "Activity duration");
		Assert.assertEquals(ritem.getPoints(), sessionA.getPoint() / 2.5, "Activity points");
		Assert.assertEquals(ritem.getSteps(), sessionA.getSteps(), "Activity steps");	
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "get_sessions", "Excluded" })
	public void GetSessionByObjectIdWithoutPermission() {
		
		String invalidScopeAccessToken = OpenAPI.getAccessToken(myEmail, "qqqqqq", OpenAPI.RESOURCE_PROFILE, ClientKey, "/");
		BaseResult result = OpenAPI.getSession(invalidScopeAccessToken, "me", itemA.getServerId());
		
		Assert.assertEquals(result.statusCode, 403, "Status code");
		Assert.assertEquals(result.message, DefaultValues.ResourceForbidden, "Error message");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "get_sessions" })
	public void GetSessionByObjectIdOfOtherUser() {
				
		// myUid + objectId from other authorized user
		BaseResult result = OpenAPI.getSession(accessToken, myUid, itemB.getServerId());
		Assert.assertEquals(result.statusCode, 400, "Status code");
		Assert.assertEquals(result.message, DefaultValues.ObjectNotFound, "Error message");

		// "me" + objectId from other authorized user
		result = OpenAPI.getSession(accessToken, "me", itemC.getServerId());
		Assert.assertEquals(result.statusCode, 400, "Status code");
		Assert.assertEquals(result.message, DefaultValues.ObjectNotFound, "Error message");

		// from other authorized user
		result = OpenAPI.getSession(accessToken, yourUid, itemB.getServerId());
		Assert.assertEquals(result.statusCode, 403, "Status code");
		Assert.assertEquals(result.message, DefaultValues.ResourceForbidden, "Error message");

		// from unauthorized user
		result = OpenAPI.getSession(accessToken, strangerUid, itemC.getServerId());
		Assert.assertEquals(result.statusCode, 403, "Status code");
		Assert.assertEquals(result.message, DefaultValues.ResourceForbidden, "Error message");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "get_sessions" })
	public void GetSessionByObjectIdInvalidObjectId() {
		
		BaseResult result = OpenAPI.getSession(accessToken, myUid, TextTool.getRandomString(10, 10));
		
		Assert.assertEquals(result.statusCode, 400, "Status code");
		Assert.assertEquals(result.message, DefaultValues.InvalidParameters, "Error message");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "get_sessions" })
	public void GetSessionByObjectIdUsingAppCredential() {

		// authorized user
		BaseResult result = OpenAPI.getSession(ClientApp, myUid, itemA.getServerId());
		OpenAPISession ritem = OpenAPISession.getSessionFromResponse(result.response);
		ActivitySessionItem sessionA = (ActivitySessionItem)itemA.getData();
		
		Assert.assertEquals(result.statusCode, 200, "Status code");
		Assert.assertEquals(ritem.getStartTime(), getISOTime(itemA.getTimestamp()) , "Activity start time");
		Assert.assertEquals(ritem.getActivityType(), MVPCommon.getActivityName(sessionA.getActivityType()) , "Activity type");
		Assert.assertEquals(ritem.getCalories(), MVPCommon.round(sessionA.getCalories(), 1), "Activity calories");
		Assert.assertEquals(ritem.getDistance(), MVPCommon.round(sessionA.getDistance(), 1), "Activity distance");
		Assert.assertEquals(ritem.getDuration(), sessionA.getDuration(), "Activity duration");
		Assert.assertEquals(ritem.getPoints(), sessionA.getPoint() / 2.5, "Activity points");
		Assert.assertEquals(ritem.getSteps(), sessionA.getSteps(), "Activity steps");
		
		// unauthorized user
		result = OpenAPI.getSession(ClientApp, strangerUid, itemA.getServerId());
		
		Assert.assertEquals(result.statusCode, 403, "Status code");
		Assert.assertEquals(result.message, DefaultValues.ResourceForbidden, "Error message");
	}
	
	/*
	 * TODO:
	 * - Get a resource using expired token
	 * - Get a resource with only selected fields
	 */

}
