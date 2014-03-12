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

public class OpenAPINotificationServerScenerio extends BackendAutomation {

	private static String ClientKey = Settings.getParameter("MVPOpenAPIClientID");
	private static String ClientSecret = Settings.getParameter("MVPOpenAPIClientSecret");
	private static String EPA = "https://jenkins.misfitwearables.com:8998/";
	private static String EPB = "https://jenkins.misfitwearables.com:8999/";
	private static String LocalhostA = "https://0.0.0.0:8998/";
	private static String LocalhostB = "https://0.0.0.0:8999/";
	
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
		Files.delete("logs/notification_server_scenerios.log");
		resultLogger = ResultLogger.getLogger("notification_server_scenerios");
		
		
		// start notification endpoint servers EPA and EPB
		resultLogger.log("\n- START NOTIFICATION ENDPOINTS");
		ServerHelper.startNotificationEndpointServer(LocalhostA);
		ServerHelper.startNotificationEndpointServer(LocalhostB);
		ShortcutsTyper.delayTime(5000);
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "notificationserver", "Excluded" })
	public void NotificationServerTestScenerio() {
		
		// PART 1
		// subcribe profiles, devices to EPA
		// subcribe goals, sessions, sleeps to EPB
		resultLogger.log("\n\nPART 1\n-------------------------------------------------------");
		resultLogger.log("\n- SUBCRIBE PROFILES, DEVICES TO EPA - " + EPA);
		OpenAPI.subscribeNotification(ClientKey, ClientSecret, EPA, OpenAPI.NOTIFICATION_RESOURCE_PROFILE);
		OpenAPI.subscribeNotification(ClientKey, ClientSecret, EPA, OpenAPI.NOTIFICATION_RESOURCE_DEVICE);
		
		resultLogger.log("\n- SUBCRIBE GOALS, SESSIONS, SLEEPS TO EPB - " + EPB);
		OpenAPI.subscribeNotification(ClientKey, ClientSecret, EPB, OpenAPI.NOTIFICATION_RESOURCE_GOAL);
		OpenAPI.subscribeNotification(ClientKey, ClientSecret, EPB, OpenAPI.NOTIFICATION_RESOURCE_SESSION);
		OpenAPI.subscribeNotification(ClientKey, ClientSecret, EPB, OpenAPI.NOTIFICATION_RESOURCE_SLEEP);
		
		ShortcutsTyper.delayTime(10000);
		
		
		// sign up 3 users: UA, UB, UC
		// authorize UA and UB to app
		resultLogger.log("\n- SIGN UP USERS A, B, C");
		String password = "qqqqqq";
		String emailA = MVPApi.generateUniqueEmail();
		String emailB = MVPApi.generateUniqueEmail();
		String emailC = MVPApi.generateUniqueEmail();
		String tokenA = MVPApi.signUp(emailA, password).token;
		String tokenB = MVPApi.signUp(emailB, password).token;
		String tokenC = MVPApi.signUp(emailC, password).token;
		String uidA = MVPApi.getUserId(tokenA);
		String uidB = MVPApi.getUserId(tokenB);
		resultLogger.log("UserA: " + uidA);
		resultLogger.log("UserB: " + uidB);
		
		
		OpenAPI.getAccessToken(emailA, password, scope, ClientKey, returnUrl);
		OpenAPI.getAccessToken(emailB, password, scope, ClientKey, returnUrl);
		
		
		// create profiles and pedometers for all users
		resultLogger.log("\n- CREATE PROFILES AND PEDOMETERS");
		resultLogger.log("Expect: 4 messages total from EPA for user A and B");
		ProfileData profileA = createRandomProfile(tokenA);
		ProfileData profileB = createRandomProfile(tokenB);
		ProfileData profileC = createRandomProfile(tokenC);
		
		Pedometer pedometerA = createRandomPedometer(tokenA);
		Pedometer pedometerB = createRandomPedometer(tokenB);
		Pedometer pedometerC = createRandomPedometer(tokenC);
			
		ShortcutsTyper.delayTime(5000);
		
		
		// create goals, sessions and sleeps for all users
		// all timeline items in order: session, sleep, milestone, achievement, timezone, food
		resultLogger.log("\n- CREATE GOALS, TIMELINE ITEMS USING BOTH SINGLE AND BATCH INSERT");
		resultLogger.log("Expect: 6 messages total from EPB for user A and B");
		Goal goalA = createDefaultGoal(tokenA, 0);
		Goal goalB = createDefaultGoal(tokenB, 0);
		Goal goalC = createDefaultGoal(tokenC, 0);
		
		List<TimelineItem> itemsA = new ArrayList<TimelineItem>();
		List<TimelineItem> itemsB = new ArrayList<TimelineItem>();
		List<TimelineItem> itemsC = new ArrayList<TimelineItem>();
		
		itemsA.add(createSessionTimelineItem(tokenA));
		itemsB.add(createSessionTimelineItem(tokenB));
		itemsC.add(createSessionTimelineItem(tokenC));
		
		itemsA.addAll(createSleepMilestoneAchievementTimezoneFoodItems(tokenA));
		itemsB.addAll(createSleepMilestoneAchievementTimezoneFoodItems(tokenB));
		itemsC.addAll(createSleepMilestoneAchievementTimezoneFoodItems(tokenC));
		
		ShortcutsTyper.delayTime(5000);
		
		
		// PART 2
		// subcribe goals from EPB to EPA
		resultLogger.log("\n\nPART 2\n-------------------------------------------------------");
		resultLogger.log("\n- SUBCRIBE GOALS FROM EPB TO EPA");
		OpenAPI.subscribeNotification(ClientKey, ClientSecret, EPA, OpenAPI.NOTIFICATION_RESOURCE_GOAL);
		ShortcutsTyper.delayTime(10000);
		
		
		// update goals, profiles and pedometers for all users		
		resultLogger.log("\n- UPDATE GOALS, PROFILES AND PEDOMETERS");
		resultLogger.log("Expect: 6 messages total from EPA for user A and B");
		updateGoal(tokenA, goalA.getServerId());
		updateGoal(tokenB, goalB.getServerId());
		updateGoal(tokenC, goalC.getServerId());
		
		updateProfile(tokenA, profileA.getServerId());
		updateProfile(tokenB, profileB.getServerId());
		updateProfile(tokenC, profileC.getServerId());
		
		updatePedometer(tokenA, pedometerA.getServerId(), pedometerA.getSerialNumberString());
		updatePedometer(tokenB, pedometerB.getServerId(), pedometerB.getSerialNumberString());
		updatePedometer(tokenC, pedometerC.getServerId(), pedometerC.getSerialNumberString());
		
		
		// update all timeline items
		resultLogger.log("\n- UPDATE ALL TIMELINE ITEMS");
		resultLogger.log("Expect: 4 messages total from EPB for user A and B");
		for(int i = 0; i < itemsA.size(); i++) {
			
			updateTimelineItem(tokenA, itemsA.get(i).getItemType(), itemsA.get(i).getServerId());
			updateTimelineItem(tokenB, itemsB.get(i).getItemType(), itemsB.get(i).getServerId());
			updateTimelineItem(tokenC, itemsC.get(i).getItemType(), itemsC.get(i).getServerId());
		}
		
		ShortcutsTyper.delayTime(5000);
		
		
		// PART 3
		// unsubcribe goals from EPA
		resultLogger.log("\n\nPART 3\n-------------------------------------------------------");
		resultLogger.log("\n- UNSUBCRIBE GOALS FROM EPA");
		OpenAPI.unsubscribeNotification(ClientKey, ClientSecret, OpenAPI.NOTIFICATION_RESOURCE_GOAL);
		
		ShortcutsTyper.delayTime(5000);
		
		
		// update all goals
		resultLogger.log("\n- UPDATE GOALS");
		resultLogger.log("Expect: no notifications");
		MVPApi.updateGoal(tokenA, goalA);
		MVPApi.updateGoal(tokenB, goalB);
		MVPApi.updateGoal(tokenC, goalC);
		
		ShortcutsTyper.delayTime(5000);
		
		
		// subcribe goals to EPB again
		resultLogger.log("\n- SUBCRIBE GOALS TO EPB AGAIN");
		OpenAPI.subscribeNotification(ClientKey, ClientSecret, EPB, OpenAPI.NOTIFICATION_RESOURCE_GOAL);
		
		ShortcutsTyper.delayTime(10000);


		// update all goals
		resultLogger.log("\n- UPDATE GOALS");
		resultLogger.log("Expect: 2 messages total from EPB for user A and B");
		updateGoal(tokenA, goalA.getServerId());
		updateGoal(tokenB, goalB.getServerId());
		updateGoal(tokenC, goalC.getServerId());

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
		
		
		// unlink pedometer of user B
		resultLogger.log("\n- UNLINK PEDOMETER OF USER B");
		resultLogger.log("Expect: 1 delete message from EPA to user B");
		MVPApi.unlinkDevice(tokenB);
		
		ShortcutsTyper.delayTime(5000);
		
		
		// relink pedometer for user B again
		resultLogger.log("\n- RELINK PEDOMETER FOR USER B AGAIN");
		resultLogger.log("Expect: 1 create message from EPA to user B");
		updatePedometer(tokenB, "", TextTool.getRandomString(10, 10));

		ShortcutsTyper.delayTime(5000);
		
		
		
		// CLEAN UP TEST, UNSUBSCRIBE ALL RESOURCES
		resultLogger.log("\n\nCLEAN UP\n-------------------------------------------------------");
		resultLogger.log("\n- UNSUBSCRIBE ALL ENDPOINTS");
		OpenAPI.unsubscribeNotification(ClientKey, ClientSecret, OpenAPI.NOTIFICATION_RESOURCE_PROFILE);
		OpenAPI.unsubscribeNotification(ClientKey, ClientSecret, OpenAPI.NOTIFICATION_RESOURCE_DEVICE);

		OpenAPI.unsubscribeNotification(ClientKey, ClientSecret, OpenAPI.NOTIFICATION_RESOURCE_GOAL);
		OpenAPI.unsubscribeNotification(ClientKey, ClientSecret, OpenAPI.NOTIFICATION_RESOURCE_SESSION);
		OpenAPI.unsubscribeNotification(ClientKey, ClientSecret, OpenAPI.NOTIFICATION_RESOURCE_SLEEP);
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
