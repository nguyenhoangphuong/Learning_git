package com.misfit.ta.backend.aut.correctness.openapi.notificationserver;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.resting.component.impl.ServiceResponse;
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
import com.misfit.ta.utils.ShortcutsTyper;

public class OpenAPINotificationServerScenerio extends BackendAutomation {

	private static String ClientKey = Settings.getParameter("MVPOpenAPIClientID");
	private static String ClientSecret = Settings.getParameter("MVPOpenAPIClientSecret");
	private static String EPA = "http://jenkins.misfitwearables.com:8998/";
	private static String EPB = "http://jenkins.misfitwearables.com:8999/";
	
	private static String scope = OpenAPI.allScopesAsString();
	private static String returnUrl = "https://www.google.com.vn";
	
	private static ResultLogger resultLogger = ResultLogger.getLogger("notification_server_scenerios");
	
	
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		
		// set up interface to call back when notification is received
		NotificationEndpointServer.onNotificationReceived = new BasicEvent<Void>() {

			@SuppressWarnings("unchecked")
			@Override
			public Void call(Object sender, Object arguments) {

				NotificationEndpointServer server = (NotificationEndpointServer)sender;
				List<NotificationMessage> messages = (List<NotificationMessage>) arguments;

				resultLogger.log("-------------------- Notification received --------------------");
				resultLogger.log("Endpoint: " + server.context.getRequest().getBaseUri());
				resultLogger.log("Messages: " + messages.size());
				for(NotificationMessage message : messages)
					resultLogger.log(message.toJson().toString());
				resultLogger.log("\n\n");

				return null;
			}
		};
		
		// start notification endpoint servers EPA and EPB
		resultLogger.log("- START NOTIFICATION ENDPOINTS");
		ServerHelper.startNotificationEndpointServer("http://localhost:8998/");
		ServerHelper.startNotificationEndpointServer("http://localhost:8999/");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "notificationserver", "Excluded" })
	public void NotificationServerTestScenerio() {
		
		// PART 1
		// subcribe profiles, devices to EPA
		// subcribe goals, sessions, sleeps to EPB
		resultLogger.log("- SUBCRIBE PROFILES, DEVICES TO EPA - " + EPA);
		OpenAPI.subscribeNotification(ClientKey, ClientSecret, EPA, OpenAPI.NOTIFICATION_RESOURCE_PROFILE);
		OpenAPI.subscribeNotification(ClientKey, ClientSecret, EPA, OpenAPI.NOTIFICATION_RESOURCE_DEVICE);
		
		resultLogger.log("- SUBCRIBE GOALS, SESSIONS, SLEEPS TO EPB - " + EPB);
		OpenAPI.subscribeNotification(ClientKey, ClientSecret, EPB, OpenAPI.NOTIFICATION_RESOURCE_GOAL);
		OpenAPI.subscribeNotification(ClientKey, ClientSecret, EPB, OpenAPI.NOTIFICATION_RESOURCE_SESSION);
		OpenAPI.subscribeNotification(ClientKey, ClientSecret, EPB, OpenAPI.NOTIFICATION_RESOURCE_SLEEP);
		
		ShortcutsTyper.delayTime(10000);
		
		
		// sign up 3 users: UA, UB, UC
		// authorize UA and UB to app
		resultLogger.log("- SIGN UP USERS A, B, C");
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
		resultLogger.log("- CREATE PROFILES AND PEDOMETERS");
		resultLogger.log("Expect: 4 messages total from EPA for user A and B");
		ProfileData profileA = createRandomProfile(tokenA);
		ProfileData profileB = createRandomProfile(tokenB);
		ProfileData profileC = createRandomProfile(tokenC);
		
		Pedometer pedometerA = createRandomPedometer(tokenA);
		Pedometer pedometerB = createRandomPedometer(tokenB);
		Pedometer pedometerC = createRandomPedometer(tokenC);
			
		ShortcutsTyper.delayTime(3000);
		
		
		// create goals, sessions and sleeps for all users
		// all timeline items in order: session, sleep, milestone, achievement, timezone, food
		resultLogger.log("- CREATE GOALS, TIMELINE ITEMS USING BOTH SINGLE AND BATCH INSERT");
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
		
		ShortcutsTyper.delayTime(3000);
		
		
		// PART 2
		// subcribe goals from EPB to EPA
		resultLogger.log("- SUBCRIBE GOALS FROM EPB TO EPA");
		OpenAPI.subscribeNotification(ClientKey, ClientSecret, EPA, OpenAPI.NOTIFICATION_RESOURCE_GOAL);
		ShortcutsTyper.delayTime(3000);
		
		
		// update goals, profiles and pedometers for all users
		resultLogger.log("- UPDATE GOALS, PROFILES AND PEDOMETERS");
		resultLogger.log("Expect: 6 messages total from EPA for user A and B");
		MVPApi.updateGoal(tokenA, goalA);
		MVPApi.updateGoal(tokenB, goalB);
		MVPApi.updateGoal(tokenC, goalC);
		
		MVPApi.updateProfile(tokenA, profileA);
		MVPApi.updateProfile(tokenB, profileB);
		MVPApi.updateProfile(tokenC, profileC);
		
		MVPApi.updatePedometer(tokenA, pedometerA);
		MVPApi.updatePedometer(tokenB, pedometerB);
		MVPApi.updatePedometer(tokenC, pedometerC);
		
		
		// update all timeline items
		resultLogger.log("- UPDATE ALL TIMELINE ITEMS");
		resultLogger.log("Expect: 4 messages total from EPB for user A and B");
		for(int i = 0; i < itemsA.size(); i++) {
			
			MVPApi.updateTimelineItem(tokenA, itemsA.get(i));
			MVPApi.updateTimelineItem(tokenB, itemsB.get(i));
			MVPApi.updateTimelineItem(tokenC, itemsC.get(i));
		}
		
		ShortcutsTyper.delayTime(3000);
		
		
		// PART 3
		// unsubcribe goals from EPA
		resultLogger.log("- UNSUBCRIBE GOALS FROM EPA");
		OpenAPI.unsubscribeNotification(ClientKey, ClientSecret, OpenAPI.NOTIFICATION_RESOURCE_GOAL);
		
		ShortcutsTyper.delayTime(3000);
		
		
		// update all goals
		resultLogger.log("- UPDATE GOALS");
		resultLogger.log("Expect: no notifications");
		MVPApi.updateGoal(tokenA, goalA);
		MVPApi.updateGoal(tokenB, goalB);
		MVPApi.updateGoal(tokenC, goalC);
		
		ShortcutsTyper.delayTime(3000);
		
		
		// subcribe goals to EPB again
		resultLogger.log("- SUBCRIBE GOALS TO EPB AGAIN");
		OpenAPI.subscribeNotification(ClientKey, ClientSecret, EPB, OpenAPI.NOTIFICATION_RESOURCE_GOAL);
		
		ShortcutsTyper.delayTime(3000);


		// update all goals
		resultLogger.log("- UPDATE GOALS");
		resultLogger.log("Expect: 2 messages total from EPB for user A and B");
		MVPApi.updateGoal(tokenA, goalA);
		MVPApi.updateGoal(tokenB, goalB);
		MVPApi.updateGoal(tokenC, goalC);

		ShortcutsTyper.delayTime(3000);
		
		
		// PART 4
		// update all timeline item state to 1 (deleted)
		resultLogger.log("- UPDATE ALL TIMELINE ITEMS STATE OF USER A TO 1 (DELETED)");
		resultLogger.log("Expect: 2 delete messages from EPB for user A");
		for(int i = 0; i < itemsA.size(); i++) {

			itemsA.get(i).setState(1);
			MVPApi.updateTimelineItem(tokenA, itemsA.get(i));
		}
		
		ShortcutsTyper.delayTime(3000);
		
		
		// unlink pedometer of user B
		resultLogger.log("- UNLINK PEDOMETER OF USER B");
		resultLogger.log("Expect: 1 delete message from EPA to user B");
		MVPApi.unlinkDevice(tokenB);
		
		ShortcutsTyper.delayTime(3000);
		
		
		// create pedometer for user B again
		resultLogger.log("- CREATE PEDOMETER FOR USER B AGAIN");
		resultLogger.log("Expect: 1 create message from EPA to user B");
		pedometerB = createRandomPedometer(tokenB);

		ShortcutsTyper.delayTime(3000);
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

}
