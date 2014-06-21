package com.misfit.ta.backend.aut.correctness.openapi.notificationserver;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.resting.component.impl.ServiceResponse;
import com.google.resting.json.JSONArray;
import com.google.resting.json.JSONException;
import com.misfit.ta.Settings;
import com.misfit.ta.backend.api.internalapi.MVPApi;
import com.misfit.ta.backend.api.openapi.OpenAPI;
import com.misfit.ta.backend.aut.BackendAutomation;
import com.misfit.ta.backend.aut.ResultLogger;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.backend.data.DataGenerator;
import com.misfit.ta.backend.data.goal.Goal;
import com.misfit.ta.backend.data.goal.GoalsResult;
import com.misfit.ta.backend.data.openapi.notification.NotificationMessage;
import com.misfit.ta.backend.data.pedometer.Pedometer;
import com.misfit.ta.backend.data.profile.ProfileData;
import com.misfit.ta.backend.data.profile.ProfileResult;
import com.misfit.ta.backend.data.timeline.TimelineItem;
import com.misfit.ta.backend.data.timeline.timelineitemdata.TimelineItemDataBase;
import com.misfit.ta.backend.server.ServerHelper;
import com.misfit.ta.backend.server.notificationendpoint.NotificationEndpointServer;
import com.misfit.ta.base.BasicEvent;
import com.misfit.ta.utils.Files;
import com.misfit.ta.utils.ShortcutsTyper;
import com.misfit.ta.utils.TextTool;

/*
 * This test cover:
 * profile:
 * - PUT /profile
 * - POST /profile
 * 
 * device:
 * - POST /pedometer
 * - PUT /pedometer
 * - POST /unlink_device
 * 
 * goal
 * - POST /goal
 * - PUT /goal/:id
 * 
 * sessions + sleeps:
 * - POST /timeline_items
 * - POST /timeline_items/batch_insert
 * - PUT /timeline_itmes/:id
 */

public class OpenAPINotificationServerLight extends BackendAutomation {

	private static String ClientKey = Settings.getParameter("MVPOpenAPIClientID");
	private static String ClientSecret = Settings.getParameter("MVPOpenAPIClientSecret");
	private static String EPA = "http://115.77.26.115:8998/";
	private static String LocalhostA = "http://0.0.0.0:8998/";
	
	private static String scope = OpenAPI.allScopesAsString();
	private static String returnUrl = "https://www.google.com.vn/";
	
	private static ResultLogger resultLogger;
	
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		
		// set up interface to call back when notification is received
		NotificationEndpointServer.onNotificationReceived = new BasicEvent<Void>() {

			@Override
			public Void call(Object sender, Object arguments) {

				// get host and message string
				NotificationEndpointServer server = (NotificationEndpointServer)sender;
				String messageString = (String) arguments;
				
				// convert from json string to object
				List<NotificationMessage> messages = new ArrayList<NotificationMessage>();
				try {
					
					JSONArray jarr = new JSONArray(messageString);
					for (int i = 0; i < jarr.length(); i++) {

						NotificationMessage mess = new NotificationMessage();
						mess.fromJson(jarr.getJSONObject(i));
						messages.add(mess);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
				resultLogger.log("*** Notification received");
				resultLogger.log("Endpoint: " + server.context.getRequest().getBaseUri());
				resultLogger.log("Notification: " + messageString);
				resultLogger.log("Messages: " + messages.size());
				for(NotificationMessage message : messages)
					resultLogger.log(String.format("Owner: %s - ObjectId: %s - %s: %s", 
							message.getOwnerId(), message.getId(), message.getType(), message.getAction()));
				resultLogger.log("\n");
				
				return null;
			}
		};
		
		// delete log file
		Files.delete("logs/notification_server_light.log");
		resultLogger = ResultLogger.getLogger("notification_server_light.log");
		
		
		// start notification endpoint servers EPA and EPB
		resultLogger.log("\n- START NOTIFICATION ENDPOINTS");
		ServerHelper.startNotificationEndpointServer(LocalhostA);
		ShortcutsTyper.delayTime(5000);
		
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "notificationserver", "Excluded" })
	public void NotificationServerTestScenerio() {
		
		// subcribe profiles, devices to EPA
		// subcribe goals, sessions, sleeps to EPB
		// setup steps
	    // NOTICE: RUN THIS PART ONCE AND CONFIRM THE SUBSCRIPTIONS MANUALLY DUE TO
	    // SOME EXCEPTION WHEN CONFIRMING USING CODE
//	    resultLogger.log("\n\nPART 1\n-------------------------------------------------------");
//		resultLogger.log("\n- SUBCRIBE PROFILES, DEVICES TO EPA - " + EPA);
//		OpenAPI.subscribeNotification(ClientKey, ClientSecret, EPA, OpenAPI.NOTIFICATION_RESOURCE_PROFILE);
//		OpenAPI.subscribeNotification(ClientKey, ClientSecret, EPA, OpenAPI.NOTIFICATION_RESOURCE_DEVICE);
//		
//		OpenAPI.subscribeNotification(ClientKey, ClientSecret, EPA, OpenAPI.NOTIFICATION_RESOURCE_GOAL);
//		OpenAPI.subscribeNotification(ClientKey, ClientSecret, EPA, OpenAPI.NOTIFICATION_RESOURCE_SESSION);
//		OpenAPI.subscribeNotification(ClientKey, ClientSecret, EPA, OpenAPI.NOTIFICATION_RESOURCE_SLEEP);
//		ShortcutsTyper.delayTime(10000);
//		System.exit(1);
		
		// sign up 3 users: UA, UB, UC
		// authorize UA and UB to app
		resultLogger.log("\n- SIGN UP USERS A, B, C");
		String password = "qqqqqq";
		String emailA = MVPApi.generateUniqueEmail();
		String tokenA = MVPApi.signUp(emailA, password).token;
		String uidA = MVPApi.getUserId(tokenA);
		resultLogger.log("UserA: " + uidA);
		
		
		OpenAPI.getAccessToken(emailA, password, scope, ClientKey, returnUrl);
		
		
		// create profiles and pedometers for all users
		resultLogger.log("\n- CREATE PROFILES AND PEDOMETERS");
		resultLogger.log("Expect: 4 messages total from EPA for user A and B");
		ProfileData profileA = createRandomProfile(tokenA);		
		Pedometer pedometerA = createRandomPedometer(tokenA);
			
		ShortcutsTyper.delayTime(5000);
		
		
		// create goals, sessions and sleeps for all users
		// all timeline items in order: session, sleep, milestone, achievement, timezone, food
		resultLogger.log("\n- CREATE GOALS, TIMELINE ITEMS USING BOTH SINGLE AND BATCH INSERT");
		resultLogger.log("Expect: 6 messages total from EPB for user A and B");
		Goal goalA = createDefaultGoal(tokenA, 0);
		
		List<TimelineItem> itemsA = new ArrayList<TimelineItem>();
		
		itemsA.add(createSessionTimelineItem(tokenA));
		
		itemsA.addAll(createSleepMilestoneAchievementTimezoneFoodItems(tokenA));
		
		ShortcutsTyper.delayTime(5000);
		
		
		
		// update goals, profiles and pedometers for all users		
		resultLogger.log("\n- UPDATE GOALS, PROFILES AND PEDOMETERS");
		resultLogger.log("Expect: 6 messages total from EPA for user A and B");
		updateGoal(tokenA, goalA.getServerId());
		updateProfile(tokenA, profileA.getServerId());
		updatePedometer(tokenA, pedometerA.getServerId(), pedometerA.getSerialNumberString());
		
		
		// update all timeline items
		resultLogger.log("\n- UPDATE ALL TIMELINE ITEMS");
		resultLogger.log("Expect: 4 messages total from EPB for user A and B");
		for(int i = 0; i < itemsA.size(); i++) {
			updateTimelineItem(tokenA, itemsA.get(i).getItemType(), itemsA.get(i).getServerId());
		}
		
		ShortcutsTyper.delayTime(5000);
		
		// PART 4
		// update all timeline item state to 1 (deleted)
		resultLogger.log("\n\nPART 4\n-------------------------------------------------------");
		resultLogger.log("\n- UPDATE ALL TIMELINE ITEMS STATE OF USER A TO 1 (DELETED)");
		resultLogger.log("Expect: 2 delete messages from EPB for user A");
		for(int i = 0; i < itemsA.size(); i++) {
			itemsA.get(i).setState(1);
			MVPApi.updateTimelineItem(tokenA, itemsA.get(i));
		}
		
		ShortcutsTyper.delayTime(5000);
	}
	
	private ProfileData createRandomProfile(String token) {
		
		long timestamp = System.currentTimeMillis() / 1000;
		ProfileData profile = DataGenerator.generateRandomProfile(timestamp, null);
		ProfileResult result = MVPApi.createProfile(token, profile);
		profile.setServerId(result.profile.getServerId());
		
		return profile;
	}
	
	private void updateProfile(String token, String objectId) {
		
		ProfileData profile = DataGenerator.generateRandomProfile(System.currentTimeMillis() / 1000, null);
		profile.setServerId(objectId);
		MVPApi.updateProfile(token, profile);
	}

	private Pedometer createRandomPedometer(String token) {
		
		long timestamp = System.currentTimeMillis() / 1000;
		Pedometer pedometer = DataGenerator.generateRandomPedometer(timestamp, null);
		BaseResult result = MVPApi.createPedometer(token, pedometer);
		Pedometer resultPedometer = Pedometer.getPedometer(result.response);
		pedometer.setServerId(resultPedometer.getServerId());
		
		return pedometer;
	}

	private void updatePedometer(String token, String objectId, String serialNumber) {
		
		Pedometer pedo = DataGenerator.generateRandomPedometer(System.currentTimeMillis() / 1000, null);
		pedo.setServerId(objectId);
		pedo.setSerialNumberString(serialNumber);
		MVPApi.updatePedometer(token, pedo);
	}

	private Goal createDefaultGoal(String token, int diffFromToday) {
		
		Goal goal = Goal.getDefaultGoal(System.currentTimeMillis() / 1000 - 3600 * 24 * diffFromToday);
		GoalsResult result = MVPApi.createGoal(token, goal);
		goal.setServerId(result.goals[0].getServerId());
		
		return goal;
	}
	
	private void updateGoal(String token, String objectId) {
		
		Goal goal = DataGenerator.generateRandomGoal(System.currentTimeMillis() / 1000, null);
		goal.setServerId(objectId);
		MVPApi.updateGoal(token, goal);
	}
	
	private TimelineItem createSessionTimelineItem(String token) {
		
		long timestamp = System.currentTimeMillis() / 1000;
		TimelineItem item = DataGenerator.generateRandomActivitySessionTimelineItem(timestamp, null);
		BaseResult result = MVPApi.createTimelineItem(token, item);
		TimelineItem ritem = TimelineItem.getTimelineItem(result.response);
		
		item.setServerId(ritem.getServerId());
		
		return item;
	}
	
	private List<TimelineItem> createSleepMilestoneAchievementTimezoneFoodItems(String token) {
		
		long timestamp = System.currentTimeMillis() / 1000;
		List<TimelineItem> items = new ArrayList<TimelineItem>();
		items.add(DataGenerator.generateRandomSleepTimelineItem(timestamp, null));
		items.add(DataGenerator.generateRandomMilestoneItem(timestamp, TimelineItemDataBase.EVENT_TYPE_100_GOAL, null));
		items.add(DataGenerator.generateRandomLifetimeDistanceItem(timestamp, null));
		items.add(DataGenerator.generateRandomTimezoneTimelineItem(timestamp, null));
		items.add(DataGenerator.generateRandomFoodTimelineItem(timestamp, null));
		
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

	private void updateTimelineItem(String token, int itemType, String objectId) {
		
		long timestamp = System.currentTimeMillis() / 1000;
		TimelineItem item = null;
		
		switch(itemType) {
			
		case TimelineItemDataBase.TYPE_SESSION:
			item = DataGenerator.generateRandomActivitySessionTimelineItem(timestamp, null);
			break;
			
		case TimelineItemDataBase.TYPE_SLEEP:
			item = DataGenerator.generateRandomSleepTimelineItem(timestamp, null);
			break;
			
		case TimelineItemDataBase.TYPE_MILESTONE:
			item = DataGenerator.generateRandomMilestoneItem(timestamp, 
					TimelineItemDataBase.EVENT_TYPE_100_GOAL, null);
			break;
			
		case TimelineItemDataBase.TYPE_LIFETIME_DISTANCE:
			item = DataGenerator.generateRandomLifetimeDistanceItem(timestamp, null);
			break;
			
		case TimelineItemDataBase.TYPE_FOOD:
			item = DataGenerator.generateRandomFoodTimelineItem(timestamp, null);
			break;
			
		case TimelineItemDataBase.TYPE_TIMEZONE:
			item = DataGenerator.generateRandomTimezoneTimelineItem(timestamp, null);
			break;
		}
		
		item.setServerId(objectId);
		MVPApi.updateTimelineItem(token, item);
	}

}
