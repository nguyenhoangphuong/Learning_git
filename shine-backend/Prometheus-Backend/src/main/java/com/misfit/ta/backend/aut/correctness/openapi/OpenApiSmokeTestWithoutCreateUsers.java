package com.misfit.ta.backend.aut.correctness.openapi;

import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.google.resting.component.impl.ServiceResponse;
import com.misfit.ta.Settings;
import com.misfit.ta.backend.api.internalapi.MVPApi;
import com.misfit.ta.backend.api.openapi.OpenAPI;
import com.misfit.ta.backend.aut.BackendAutomation;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.backend.data.DataGenerator;
import com.misfit.ta.backend.data.goal.Goal;
import com.misfit.ta.backend.data.goal.GoalsResult;
import com.misfit.ta.backend.data.openapi.OpenAPIThirdPartyApp;
import com.misfit.ta.backend.data.pedometer.Pedometer;
import com.misfit.ta.backend.data.profile.ProfileData;
import com.misfit.ta.backend.data.profile.ProfileResult;
import com.misfit.ta.backend.data.timeline.TimelineItem;
import com.misfit.ta.backend.data.timeline.timelineitemdata.TimelineItemDataBase;
import com.misfit.ta.common.MVPCommon;
import com.misfit.ta.common.Verify;

public class OpenApiSmokeTestWithoutCreateUsers extends BackendAutomation {
	
	private List<String> errors = new ArrayList<String>();
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "openapi_light_smoke_test" })
	public void OpenAPIDevPortalAuthenticationResourceAPIsSmokeTest() {
		
		String email = "nhhai16991@gmail.com";
		String password = "qqqqqq";
		String returnUrl = "http://misfit.com";
		String allScopes = OpenAPI.allScopesAsString();
		
		BaseResult result = null;
		String cookie = null;
		
		
		// log in
		result = OpenAPI.logIn(email, password);
		errors.add(Verify.verifyTrue(result.statusCode < 400, "[Dev Portal]: Sign in flow is ok"));
		cookie = result.cookie;
				
		
		// register an app
		// NOTE: use created app to avoid creating on production
		String clientKey = Settings.getParameter("MVPOpenAPIClientID");
		String clientSecret = Settings.getParameter("MVPOpenAPIClientSecret");
		
		OpenAPIThirdPartyApp app = new OpenAPIThirdPartyApp();
		app.setClientKey(clientKey);
		app.setClientSecret(clientSecret);
				
				
		// log out that account
		result = OpenAPI.logOut(cookie);
		errors.add(Verify.verifyTrue(result.statusCode < 400, "[Dev Portal]: Sign out flow is ok"));
			

		// NOTE: use created user to avoid creating on production
		String userA = "aut_openapi_smokea@qa.com";
		String userB = "aut_openapi_smokeb@qa.com";
		String tokenA = MVPApi.signIn(userA, password).token;
		String tokenB = MVPApi.signIn(userB, password).token;
		
		
		// authorize users to app using token flow
		OpenAPI.logOut(cookie);
		result = OpenAPI.logIn(userA, password);
		cookie = result.cookie;
		
		result = OpenAPI.authorizationDialog(OpenAPI.RESPONSE_TYPE_TOKEN, clientKey, returnUrl, allScopes, null, cookie);
		
		System.out
                .println("LOG [OpenApiSmokeTestWithoutCreateUsers.OpenAPIDevPortalAuthenticationResourceAPIsSmokeTest]: --------------- 1"); 
		
		
		if (result.rawData.contains("Request for permission")) {
            result = OpenAPI.authorizationConfirm(OpenAPI.parseTransactionId(result), cookie);
            String location = OpenAPI.parseReturnUrl(result);
            String token = OpenAPI.parseAccessToken(result);
            logger.info(result.getHeaderValue("Location"));
            logger.info(location);
            logger.info(token);
            Assert.assertEquals(location, returnUrl, "Return url");
            Assert.assertNotNull(token, "Return token");
            
        } else {
            Assert.assertTrue(result.statusCode == 200 || result.statusCode == 304, "Error code is: " + result.statusCode);
        }
//		result = OpenAPI.authorizationConfirm(OpenAPI.parseTransactionId(result), cookie);
		String accessTokenA = OpenAPI.parseAccessToken(result);
		
		
		// authorize users to ap using code flow
		OpenAPI.logOut(cookie);
		result = OpenAPI.logIn(userB, password);
		cookie = result.cookie;
		
		result = OpenAPI.authorizationDialog(OpenAPI.RESPONSE_TYPE_CODE, clientKey, returnUrl, allScopes, null, cookie);
		
		if (result.rawData.contains("Request for permission")) {
            result = OpenAPI.authorizationConfirm(OpenAPI.parseTransactionId(result), cookie);
            String location = OpenAPI.parseReturnUrl(result);
            String token = OpenAPI.parseAccessToken(result);
            logger.info(result.getHeaderValue("Location"));
            logger.info(location);
            logger.info(token);
            Assert.assertEquals(location, returnUrl, "Return url");
            Assert.assertNotNull(token, "Return token");
            
        } else {
            Assert.assertTrue(result.statusCode == 200 || result.statusCode == 304, "Error code is: " + result.statusCode);
        }
//		result = OpenAPI.authorizationConfirm(OpenAPI.parseTransactionId(result), cookie);
		
		
		String code = OpenAPI.parseCode(result); 
		
		result = OpenAPI.exchangeCodeForToken(OpenAPI.GRANT_TYPE_AUTHORIZATION_CODE, code, clientKey, clientSecret, returnUrl, cookie);
		String accessTokenB = (String)result.getJsonResponseValue("access_token");
		
		logger.info("AccessTokenA: " + accessTokenA);
		logger.info("AccessTokenB: " + accessTokenB);
		
		Assert.assertNotNull(accessTokenA, "[Auth]: Authorize by token flow ok");
		Assert.assertNotNull(accessTokenB, "[Auth]: Authorize by code and exchange flow ok");
		
		
		// create some data for those users
		createRandomProfile(tokenA);
		createRandomProfile(tokenB);
		
		createRandomPedometer(tokenA);
		createRandomPedometer(tokenB);
		
		List<Goal> goalsA = new ArrayList<Goal>();
		List<Goal> goalsB = new ArrayList<Goal>();
		goalsA.add(createDefaultGoal(tokenA, 0));
		goalsA.add(createDefaultGoal(tokenA, 1));
		goalsB.add(createDefaultGoal(tokenB, 0));
		goalsB.add(createDefaultGoal(tokenB, 1));
		
		List<TimelineItem> itemsA = createAllTypesOfTimelineItem(tokenA);
		List<TimelineItem> itemsB = createAllTypesOfTimelineItem(tokenB);
		
		
		// query and verify data of user A using access token and "me" route
		long timestamp = System.currentTimeMillis() / 1000;
		String fromDate = MVPCommon.getDateString(timestamp - 3600 * 24 * 1);
		String toDate = MVPCommon.getDateString(timestamp);
		
		result = OpenAPI.getProfile(accessTokenA, "me");
		errors.add(Verify.verifyTrue(result.isOK(), "[Resource APIs]: /users/me/profile ok"));
		
		result = OpenAPI.getDevice(accessTokenA, "me");
		errors.add(Verify.verifyTrue(result.isOK(), "[Resource APIs]: /users/me/device ok"));
		
		result = OpenAPI.getSummary(accessTokenA, "me", fromDate, toDate);
		errors.add(Verify.verifyTrue(result.isOK(), "[Resource APIs]: /users/me/summary ok"));
		
		result = OpenAPI.getGoals(accessTokenA, "me", fromDate, toDate);
		errors.add(Verify.verifyTrue(result.isOK(), "[Resource APIs]: /users/me/activity/goals ok"));
		
		result = OpenAPI.getGoal(accessTokenA, "me", goalsA.get(0).getServerId());
		errors.add(Verify.verifyTrue(result.isOK(), "[Resource APIs]: /users/me/activity/goals/:id ok"));
		
		result = OpenAPI.getSessions(accessTokenA, "me", fromDate, toDate);
		errors.add(Verify.verifyTrue(result.isOK(), "[Resource APIs]: /users/me/activity/sessions ok"));
		
		result = OpenAPI.getSession(accessTokenA, "me", itemsA.get(0).getServerId());
		errors.add(Verify.verifyTrue(result.isOK(), "[Resource APIs]: /users/me/activity/sessions/:id ok"));
		
		result = OpenAPI.getSleeps(accessTokenA, "me", fromDate, toDate);
		errors.add(Verify.verifyTrue(result.isOK(), "[Resource APIs]: /users/me/activity/sleeps ok"));
		
		result = OpenAPI.getSleep(accessTokenA, "me", itemsA.get(1).getServerId());
		errors.add(Verify.verifyTrue(result.isOK(), "[Resource APIs]: /users/me/activity/sleeps/:id ok"));
		
		
		// query and verify data of user B using client id and secret and userid route
		String uidB = MVPApi.getUserId(tokenB);
		
		result = OpenAPI.getProfile(app, uidB);
		errors.add(Verify.verifyTrue(result.isOK(), "[Resource APIs]: /users/:userid/profile ok"));
		
		result = OpenAPI.getDevice(app, uidB);
		errors.add(Verify.verifyTrue(result.isOK(), "[Resource APIs]: /users/:userid/device ok"));
		
		result = OpenAPI.getSummary(app, uidB, fromDate, toDate);
		errors.add(Verify.verifyTrue(result.isOK(), "[Resource APIs]: /summary ok"));
		
		result = OpenAPI.getGoals(app, uidB, fromDate, toDate);
		errors.add(Verify.verifyTrue(result.isOK(), "[Resource APIs]: /users/:userid/activity/goals ok"));
		
		result = OpenAPI.getGoal(app, uidB, goalsB.get(0).getServerId());
		errors.add(Verify.verifyTrue(result.isOK(), "[Resource APIs]: /users/:userid/activity/goals/:id ok"));
		
		result = OpenAPI.getSessions(app, uidB, fromDate, toDate);
		errors.add(Verify.verifyTrue(result.isOK(), "[Resource APIs]: /users/:userid/activity/sessions ok"));
		
		result = OpenAPI.getSession(app, uidB, itemsB.get(0).getServerId());
		errors.add(Verify.verifyTrue(result.isOK(), "[Resource APIs]: /users/:userid/activity/sessions/:id ok"));
		
		result = OpenAPI.getSleeps(app, uidB, fromDate, toDate);
		errors.add(Verify.verifyTrue(result.isOK(), "[Resource APIs]: /users/:userid/activity/sleeps ok"));
		
		result = OpenAPI.getSleep(app, uidB, itemsB.get(1).getServerId());
		errors.add(Verify.verifyTrue(result.isOK(), "[Resource APIs]: /users/:userid/activity/sleeps/:id ok"));
		
		
		if(!Verify.verifyAll(errors))
			Assert.fail("Not all assertions pass");
	}

	
	private ProfileData createRandomProfile(String token) {

		long timestamp = System.currentTimeMillis() / 1000;
		ProfileData profile = DataGenerator.generateRandomProfile(timestamp, null);
		ProfileResult result = MVPApi.createProfile(token, profile);
		profile.setServerId(result.profile.getServerId());

		return profile;
	}

	private Pedometer createRandomPedometer(String token) {

		long timestamp = System.currentTimeMillis() / 1000;
		Pedometer pedometer = DataGenerator.generateRandomPedometer(timestamp, null);
		BaseResult result = MVPApi.createPedometer(token, pedometer);
		Pedometer resultPedometer = Pedometer.getPedometer(result.response);
		pedometer.setServerId(resultPedometer.getServerId());

		return pedometer;
	}

	private Goal createDefaultGoal(String token, int diffFromToday) {

		Goal goal = Goal.getDefaultGoal(System.currentTimeMillis() / 1000 - 3600 * 24 * diffFromToday);
		GoalsResult result = MVPApi.createGoal(token, goal);
		goal.setServerId(result.goals[0].getServerId());

		return goal;
	}

	private List<TimelineItem> createAllTypesOfTimelineItem(String token) {

		long timestamp = System.currentTimeMillis() / 1000;
		
		List<TimelineItem> items = new ArrayList<TimelineItem>();
		items.add(DataGenerator.generateRandomActivitySessionTimelineItem(timestamp, null));
		items.add(DataGenerator.generateRandomSleepTimelineItem(timestamp, null));
		items.add(DataGenerator.generateRandomMilestoneItem(timestamp, TimelineItemDataBase.EVENT_TYPE_100_GOAL, null));
		items.add(DataGenerator.generateRandomLifetimeDistanceItem(timestamp, null));
		items.add(DataGenerator.generateRandomTimezoneTimelineItem(timestamp, null));
		items.add(DataGenerator.generateRandomFoodTimelineItem(timestamp, null));
		items.add(DataGenerator.generateRandomActivitySessionTimelineItem(timestamp + 1, null));
		items.add(DataGenerator.generateRandomSleepTimelineItem(timestamp + 1, null));

		ServiceResponse response = MVPApi.createTimelineItems(token, items);
		List<TimelineItem> ritems = TimelineItem.getTimelineItems(response);

		for(TimelineItem item : items) {
			for(TimelineItem ritem : ritems) {
				if(ritem.getLocalId().equals(item.getLocalId()))
					item.setServerId(ritem.getServerId());
			}
		}

		return items;
	}
}
