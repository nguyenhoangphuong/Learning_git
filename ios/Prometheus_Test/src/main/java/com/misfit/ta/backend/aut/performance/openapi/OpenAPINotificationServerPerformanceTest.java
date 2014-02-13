package com.misfit.ta.backend.aut.performance.openapi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.graphwalker.Util;

import com.google.resting.component.impl.ServiceResponse;
import com.google.resting.json.JSONArray;
import com.google.resting.json.JSONException;
import com.misfit.ta.backend.api.internalapi.MVPApi;
import com.misfit.ta.backend.api.openapi.OpenAPI;
import com.misfit.ta.backend.aut.ResultLogger;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.backend.data.DataGenerator;
import com.misfit.ta.backend.data.goal.Goal;
import com.misfit.ta.backend.data.goal.GoalsResult;
import com.misfit.ta.backend.data.openapi.OpenAPIThirdPartyApp;
import com.misfit.ta.backend.data.openapi.notification.NotificationMessage;
import com.misfit.ta.backend.data.pedometer.Pedometer;
import com.misfit.ta.backend.data.profile.ProfileData;
import com.misfit.ta.backend.data.timeline.TimelineItem;
import com.misfit.ta.backend.data.timeline.timelineitemdata.ActivitySessionItem;
import com.misfit.ta.backend.server.ServerHelper;
import com.misfit.ta.backend.server.notificationendpoint.NotificationEndpointServer;
import com.misfit.ta.base.BasicEvent;
import com.misfit.ta.utils.Files;
import com.misfit.ta.utils.ShortcutsTyper;
import com.misfit.ta.utils.TextTool;

public class OpenAPINotificationServerPerformanceTest {

	protected static Logger logger = Util.setupLogger(OpenAPINotificationServerPerformanceTest.class);
	private static String endpoint = "http://jenkins.misfitwearables.com:8998/";
	private static ResultLogger resultLogger;
	private static int numberOfNotifications = 0;
	private static int numberOfMessageRecords = 0;
	
	public static void main(String[] args) {
		
		if(args.length == 0)
			args = new String[] {"200", "1", "1"};

		int numberOfThread = Integer.valueOf(args[0]);
		int numberOfApp = Integer.valueOf(args[1]);
		int numberOfUser = Integer.valueOf(args[2]);
		
		// start endpoint
		setUpEndpoint();
		
		// register apps
		List<OpenAPIThirdPartyApp> apps = registerApps("nhhai16991@gmail.com", "qqqqqq", numberOfApp, numberOfThread);
		
		// subcribe apps to endpoint
		subcribeApps(apps, endpoint, numberOfThread);

		// sign up users
		List<String> emails = signUpUsers(numberOfUser, "qqqqqq", numberOfThread);

		// authorize users to apps
		authorizeUsersToApps(emails, "qqqqqq", apps, numberOfThread);

		// test notification: create, update, delete...
		testNotification(emails, "qqqqqq", numberOfThread);
		
		
		// summary
		ShortcutsTyper.delayTime(10000);
		resultLogger.log("Total notifications: " + numberOfNotifications);
		resultLogger.log("Total message records: " + numberOfMessageRecords);
		
		return;
	}
	
	
	// helpers
	protected static void setUpEndpoint() {
	
		NotificationEndpointServer.onNotificationReceived = new BasicEvent<Void>() {

			@Override
			public Void call(Object sender, Object arguments) {

				numberOfNotifications++;
				
				// get host and message string
				NotificationEndpointServer server = (NotificationEndpointServer)sender;
				String host = server.context.getRequest().getBaseUri().toString();
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
					
					numberOfMessageRecords += messages.size();
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
				for(NotificationMessage message : messages) {
					resultLogger.log(String.format("%s\t%s\t%s\t%s\t%s\t%s", host, 
							message.getOwnerId(), message.getId(),
							message.getType(), message.getAction(),
							message.getUpdatedAt().toString()));
				}
				
				return null;
			}
		};
		
		// delete log file
		Files.delete("logs/notification_perfomance_test.log");
		resultLogger = ResultLogger.getLogger("notification_perfomance_test");


		// start notification endpoint servers EPA and EPB
		ServerHelper.startNotificationEndpointServer("http://localhost:8999/");
	}
	
	protected static void runTestThread(String email, String password) {
	
		// total notification each user: 11 (5 create, 4 update and 2 delete)
		
		// log in
		long timestamp = System.currentTimeMillis() / 1000;
		String token = MVPApi.signIn(email, password).token;
		
		
		// create profile, pedometer, goal and timeline item
		ProfileData profile = DataGenerator.generateRandomProfile(timestamp, null);
		Pedometer pedo = DataGenerator.generateRandomPedometer(timestamp, null);
		Goal goal = DataGenerator.generateRandomGoal(timestamp, null);
		TimelineItem sleep = DataGenerator.generateRandomSleepTimelineItem(timestamp, null);
		TimelineItem session = DataGenerator.generateRandomActivitySessionTimelineItem(timestamp, null);
		
		List<TimelineItem> list = new ArrayList<TimelineItem>();
		list.add(sleep);
		
		MVPApi.createProfile(token, profile);
		MVPApi.createPedometer(token, pedo);
		GoalsResult gresult = MVPApi.createGoal(token, goal);
		BaseResult tresult1 = MVPApi.createTimelineItem(token, session);
		ServiceResponse tresponse2 = MVPApi.createTimelineItems(token, list);
		
		
		// set objectId
		goal.setServerId(gresult.goals[0].getServerId());
		session.setServerId(TimelineItem.getTimelineItem(tresult1.response).getServerId());
		sleep.setServerId(TimelineItem.getTimelineItems(tresponse2).get(0).getServerId());
		
		
		// update profile, pedometer, goal and timeline item
		profile.setName(profile.getName() + "_updated");
		pedo.setBatteryLevel(pedo.getBatteryLevel() + 1);
		goal.setValue(goal.getValue() + 100);
		
		ActivitySessionItem data = (ActivitySessionItem)session.getData();
		data.setPoint(data.getPoint() + 100);
		session.setData(data);
				
		MVPApi.updateProfile(token, profile);
		MVPApi.updatePedometer(token, pedo);
		MVPApi.updateGoal(token, goal);
		MVPApi.updateTimelineItem(token, session);
		
		
		// delete sleep, device
		session.setState(1);
		sleep.setState(1);
		MVPApi.updateTimelineItem(token, session);
		MVPApi.updateTimelineItem(token, sleep);
		MVPApi.unlinkDevice(token);
	}
	
	
	protected static List<OpenAPIThirdPartyApp> registerApps(String email, String password, int numberOfApp, int numberOfThread) {
		
		// log in first
		BaseResult result = OpenAPI.logIn(email, password);
		final String cookie = result.cookie;
		List<OpenAPIThirdPartyApp> apps = new ArrayList<OpenAPIThirdPartyApp>();
		
		// now reg app
		Collection<Future<?>> futures = new LinkedList<Future<?>>();
    	ExecutorService executor = Executors.newFixedThreadPool(numberOfThread);
    	
		for(int i = 0; i < numberOfApp; i++) {

			final OpenAPIThirdPartyApp app = new OpenAPIThirdPartyApp();
			app.setName("App_" + TextTool.getRandomString(10, 10) + System.nanoTime());
			apps.add(app);
			
			Thread thread = new Thread() {

				public void run() {
					
					OpenAPI.registerApp(app, cookie);
				}
			};

			futures.add(executor.submit(thread));
		}
		
		executor.shutdown();
        
        for (Future<?> future:futures) {
            try {
                future.get();
            } catch (Exception e) {
            }
        }
        
        // now get client id and client secret for apps
        List<OpenAPIThirdPartyApp> sapps = OpenAPI.getAllApps();
        List<OpenAPIThirdPartyApp> resultApps = new ArrayList<OpenAPIThirdPartyApp>();
        for(int i = 0; i < apps.size(); i++) {
        	
        	OpenAPIThirdPartyApp app = apps.get(i);
        	for(OpenAPIThirdPartyApp sapp : sapps) {
        		
        		if(sapp.getName().equals(app.getName())) {
        			resultApps.add(sapp);
        		}
        	}
        }
        
        return resultApps;
	}
	
	protected static void subcribeApps(List<OpenAPIThirdPartyApp> apps, final String endpoint, int numberOfThread) {
		
		Collection<Future<?>> futures = new LinkedList<Future<?>>();
    	ExecutorService executor = Executors.newFixedThreadPool(numberOfThread);
    	
		for(int i = 0; i < apps.size(); i++) {

			OpenAPIThirdPartyApp app = apps.get(i);
			final String clientKey = app.getClientKey();
			final String clientSecret = app.getClientSecret();
			
			Thread thread = new Thread() {

				public void run() {
					
					OpenAPI.subscribeNotification(clientKey, clientSecret, endpoint, OpenAPI.NOTIFICATION_RESOURCE_PROFILE);
					OpenAPI.subscribeNotification(clientKey, clientSecret, endpoint, OpenAPI.NOTIFICATION_RESOURCE_DEVICE);
					OpenAPI.subscribeNotification(clientKey, clientSecret, endpoint, OpenAPI.NOTIFICATION_RESOURCE_GOAL);
					OpenAPI.subscribeNotification(clientKey, clientSecret, endpoint, OpenAPI.NOTIFICATION_RESOURCE_SESSION);
					OpenAPI.subscribeNotification(clientKey, clientSecret, endpoint, OpenAPI.NOTIFICATION_RESOURCE_SLEEP);
					ShortcutsTyper.delayTime(10000);
				}
			};

			futures.add(executor.submit(thread));
		}
		
		executor.shutdown();
        
        for (Future<?> future:futures) {
            try {
                future.get();
            } catch (Exception e) {
            }
        }
        
        ShortcutsTyper.delayTime(30000);
	}
	
 	protected static List<String> signUpUsers(int numberOfUsers, final String password, int numberOfThread) {
		
		final List<String> emails = new ArrayList<String>();		
		Collection<Future<?>> futures = new LinkedList<Future<?>>();
    	ExecutorService executor = Executors.newFixedThreadPool(numberOfThread);
    	
    	for(int i = 0; i < numberOfUsers; i++) {
    		
			final String email = MVPApi.generateUniqueEmail();
			
			Thread thread = new Thread() {

				public void run() {
					
					BaseResult result = MVPApi.signUp(email, password);
					if(result.isOK())
						emails.add(email);
				}
			};

			futures.add(executor.submit(thread));
		}
		
		executor.shutdown();
        
        for (Future<?> future:futures) {
            try {
                future.get();
            } catch (Exception e) {
            }
        }
		
		return emails;
	}
 	
	protected static void authorizeUsersToApps(List<String> emails, final String password, List<OpenAPIThirdPartyApp> apps, int numberOfThread) {
		
    	final String scope = OpenAPI.allScopesAsString();
    	final String returnUrl = "https://www.google.com.vn/";
    	
    	Collection<Future<?>> futures = new LinkedList<Future<?>>();
    	ExecutorService executor = Executors.newFixedThreadPool(numberOfThread);
    	
		for(int i = 0; i < emails.size(); i++) {
			for(OpenAPIThirdPartyApp app : apps) {

				final String email = emails.get(i);
				final String clientKey = app.getClientKey();
				
				Thread thread = new Thread() {
					
					public void run() {
						OpenAPI.getAccessToken(email, password, scope, clientKey, returnUrl);
					}
				};
				
				futures.add(executor.submit(thread));
			}
		}
		
		executor.shutdown();
        
        for (Future<?> future:futures) {
            try {
                future.get();
            } catch (Exception e) {
            }
        }
	}

	protected static void testNotification(List<String> emails, final String password, int numberOfThread) {
		
		Collection<Future<?>> futures = new LinkedList<Future<?>>();
    	ExecutorService executor = Executors.newFixedThreadPool(numberOfThread);
    	
    	for(int i = 0; i < emails.size(); i++) {
			
			final String email = emails.get(i);

			Thread thread = new Thread() {

				public void run() {
					
					runTestThread(email, password);
				}
			};

			futures.add(executor.submit(thread));
		}
		
		executor.shutdown();
        
        for (Future<?> future:futures) {
            try {
                future.get();
            } catch (Exception e) {
            }
        }
	}

}
