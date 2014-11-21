package com.misfit.ta.backend.aut.correctness.openapi.notificationserver;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.resting.json.JSONArray;
import com.google.resting.json.JSONException;
import com.misfit.ta.Settings;
import com.misfit.ta.backend.api.metawatch.MVPApi;
import com.misfit.ta.backend.api.openapi.OpenAPI;
import com.misfit.ta.backend.aut.BackendAutomation;
import com.misfit.ta.backend.aut.ResultLogger;
import com.misfit.ta.backend.aut.correctness.servercalculation.ServerCalculationTestHelpers;
import com.misfit.ta.backend.data.DataGenerator;
import com.misfit.ta.backend.data.goal.Goal;
import com.misfit.ta.backend.data.goal.GoalRawData;
import com.misfit.ta.backend.data.goal.GoalsResult;
import com.misfit.ta.backend.data.openapi.notification.NotificationMessage;
import com.misfit.ta.backend.data.statistics.Statistics;
import com.misfit.ta.backend.server.ServerHelper;
import com.misfit.ta.backend.server.notificationendpoint.NotificationEndpointServer;
import com.misfit.ta.base.BasicEvent;
import com.misfit.ta.utils.Files;
import com.misfit.ta.utils.ShortcutsTyper;

public class OpenAPINotificationServerAndroidScenerio extends BackendAutomation {

	private static String ClientKey = Settings.getParameter("MVPOpenAPIClientID");
	private static String ClientSecret = Settings.getParameter("MVPOpenAPIClientSecret");
	private static String EP = "http://jenkins.misfitwearables.com:8998/";
	private static String Localhost = "http://0.0.0.0:8998/";
	
	private static String scope = OpenAPI.allScopesAsString();
	private static String returnUrl = "http://misfit.com/";
	
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
		resultLogger = ResultLogger.getLogger("notification_server_android_scenerios");
		
		
		// start notification endpoint servers EPA and EPB
		resultLogger.log("\n- START NOTIFICATION ENDPOINTS");
		ServerHelper.startNotificationEndpointServer(Localhost);
		ShortcutsTyper.delayTime(5000);
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "notificationserver", "Excluded" })
	public void NotificationServerTestScenerio() {
		
		// subcribe goals, sessions, sleeps to EP
		resultLogger.log("\n- SUBCRIBE GOALS, SESSIONS, SLEEPS TO EP - " + EP);
		OpenAPI.subscribeNotification(ClientKey, ClientSecret, EP, OpenAPI.NOTIFICATION_RESOURCE_GOAL);
		OpenAPI.subscribeNotification(ClientKey, ClientSecret, EP, OpenAPI.NOTIFICATION_RESOURCE_SESSION);
		OpenAPI.subscribeNotification(ClientKey, ClientSecret, EP, OpenAPI.NOTIFICATION_RESOURCE_SLEEP);
		
		ShortcutsTyper.delayTime(10000);
		
		
		// sign up 3 users: UA, UB, UC
		resultLogger.log("\n- SIGN UP USERS A, B");
		String password = "qqqqqq";
		String emailA = MVPApi.generateUniqueEmail();
		String emailB = MVPApi.generateUniqueEmail();
		String tokenA = MVPApi.signUp(emailA, password).token;
		String tokenB = MVPApi.signUp(emailB, password).token;
		String uidA = MVPApi.getUserId(tokenA);
		String uidB = MVPApi.getUserId(tokenB);
		resultLogger.log("UserA: " + uidA);
		resultLogger.log("UserB: " + uidB);
		
		
		// create profile / pedometer / statistics
		resultLogger.log("\n- CREATE PROFILE / PEDOMETER / STATISTICS");
		long timestamp = System.currentTimeMillis() / 1000;
		
		MVPApi.createProfile(tokenA, DataGenerator.generateRandomProfile(timestamp, null));
		MVPApi.createPedometer(tokenA, DataGenerator.generateRandomPedometer(timestamp, null));
		MVPApi.createStatistics(tokenA, Statistics.getDefaultStatistics());
		
		MVPApi.createProfile(tokenB, DataGenerator.generateRandomProfile(timestamp, null));
		MVPApi.createPedometer(tokenB, DataGenerator.generateRandomPedometer(timestamp, null));
		MVPApi.createStatistics(tokenB, Statistics.getDefaultStatistics());
		
		
		// authorize only UA
		resultLogger.log("\n- AUTHORIZE USERS");
		OpenAPI.getAccessToken(emailA, password, scope, ClientKey, returnUrl);
				
		
		// create goal for today
		resultLogger.log("\n- CREATE GOAL FOR TODAY");
		resultLogger.log("Expect: 1 message total from EP for user A");
		Goal goalA = Goal.getDefaultGoal();
		Goal goalB = Goal.getDefaultGoal();
		GoalsResult resultA = MVPApi.createGoal(tokenA, goalA);
		GoalsResult resultB = MVPApi.createGoal(tokenB, goalB);

		goalA.setServerId(resultA.goals[0].getServerId());
		goalA.setUpdatedAt(resultA.goals[0].getUpdatedAt());
		goalB.setServerId(resultB.goals[0].getServerId());
		goalB.setUpdatedAt(resultB.goals[0].getUpdatedAt());
		
		ShortcutsTyper.delayTime(5000);


		// story
		// - session: 60 minutes - 6000 steps - 600 points at 7:00
		// - inactive: 4 minutes - 400 steps - 40 points at 9:00
		// - inactive: 4 minutes - 400 steps - 40 points at 9:10
		// - session: 50 minutes - 5000 steps - 500 points at 10:00
		// - session: 30 minutes - 3000 steps - 300 points at 13:00
		// - gap: 2 active + 4 idle / 60 minutes - 2000 steps - 200 points from 15:00 to 16:00
		// - session: 40 minutes - 4000 steps - 400 points at 17:00
		// expect:
		// - 4 acitivity session tiles
		// - 100% tile
		// - 150% tile
		// - 200% tile
		// - progress is correct (points, steps, distance) 
		GoalRawData data = new GoalRawData();
		data.appendGoalRawData(ServerCalculationTestHelpers.generateEmptyRawData(0, 7 * 60));

		data.appendGoalRawData(ServerCalculationTestHelpers.generateSessionRawData(6000, 600, 60));
		data.appendGoalRawData(ServerCalculationTestHelpers.generateEmptyRawData(7 * 60 + 60, 9 * 60));

		data.appendGoalRawData(ServerCalculationTestHelpers.generateSessionRawData(400, 40, 4));
		data.appendGoalRawData(ServerCalculationTestHelpers.generateEmptyRawData(9 * 60 + 4, 9 * 60 + 10));
		data.appendGoalRawData(ServerCalculationTestHelpers.generateSessionRawData(400, 40, 4));
		data.appendGoalRawData(ServerCalculationTestHelpers.generateEmptyRawData(9 * 60 + 10 + 4, 10 * 60));

		data.appendGoalRawData(ServerCalculationTestHelpers.generateSessionRawData(5000, 500, 50));
		data.appendGoalRawData(ServerCalculationTestHelpers.generateEmptyRawData(10 * 60 + 50, 13 * 60));

		data.appendGoalRawData(ServerCalculationTestHelpers.generateSessionRawData(3000, 300, 30));
		data.appendGoalRawData(ServerCalculationTestHelpers.generateEmptyRawData(13 * 60 + 30, 15 * 60));

		data.appendGoalRawData(ServerCalculationTestHelpers.generateGapData(100, 10, 2, 4, 60));
		data.appendGoalRawData(ServerCalculationTestHelpers.generateEmptyRawData(15 * 60 + 60, 17 * 60));

		data.appendGoalRawData(ServerCalculationTestHelpers.generateSessionRawData(4000, 400, 40));
		data.appendGoalRawData(ServerCalculationTestHelpers.generateEmptyRawData(17 * 60 + 40, 24 * 60));

		
		// push to server
		resultLogger.log("\n- PUSH RAWDATA TO CALCULATION SERVER");
		resultLogger.log("Expect: 5 messages total (4 sessions created and 1 goal updated) from EP for user A");
		MVPApi.pushRawData(tokenA, goalA.getServerId(), data, 0);
		MVPApi.pushRawData(tokenB, goalB.getServerId(), data, 0);
		ShortcutsTyper.delayTime(10000);
		
		
		// set up: unsubscribe all
		OpenAPI.unsubscribeNotification(ClientKey, ClientSecret, OpenAPI.NOTIFICATION_RESOURCE_GOAL);
		OpenAPI.unsubscribeNotification(ClientKey, ClientSecret, OpenAPI.NOTIFICATION_RESOURCE_SESSION);
		OpenAPI.unsubscribeNotification(ClientKey, ClientSecret, OpenAPI.NOTIFICATION_RESOURCE_SLEEP);
	}
}
