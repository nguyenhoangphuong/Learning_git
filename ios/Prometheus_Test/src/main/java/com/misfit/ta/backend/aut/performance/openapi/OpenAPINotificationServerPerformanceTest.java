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

import com.google.resting.json.JSONArray;
import com.google.resting.json.JSONException;
import com.misfit.ta.backend.api.internalapi.MVPApi;
import com.misfit.ta.backend.api.openapi.OpenAPI;
import com.misfit.ta.backend.aut.ResultLogger;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.backend.data.DataGenerator;
import com.misfit.ta.backend.data.openapi.OpenAPIThirdPartyApp;
import com.misfit.ta.backend.data.openapi.notification.NotificationMessage;
import com.misfit.ta.backend.data.timeline.TimelineItem;
import com.misfit.ta.backend.server.ServerHelper;
import com.misfit.ta.backend.server.notificationendpoint.NotificationEndpointServer;
import com.misfit.ta.base.BasicEvent;
import com.misfit.ta.utils.Files;
import com.misfit.ta.utils.ShortcutsTyper;
import com.misfit.ta.utils.TextTool;

public class OpenAPINotificationServerPerformanceTest {

	protected static Logger logger = Util.setupLogger(OpenAPINotificationServerPerformanceTest.class);
	private static String localhost = "http://localhost:8998/";
	private static String endpoint = "http://jenkins.misfitwearables.com:8998/";
	private static ResultLogger resultLogger;
	
	private static int numberOfNotifications = 0;
	
	private static int numbeOfSuccessfulRegisterApp = 0;
	private static int numberOfSuccessfulSubscribeApp = 0;
	private static int numberOfSuccessfulAuthorization = 0;
	
	
	public static void main(String[] args) {
		
		if(args.length == 0)
			args = new String[] {"test", "200", "nhhai16991@gmail.com", "qqqqqq", "1"};

		String action = args[0];
		int numberOfThread = Integer.valueOf(args[1]);
		
		if(action.equals("help"))
			runHelp();
		if(action.equals("setup"))
			runSetup(args[2], args[3], Integer.valueOf(args[4]), numberOfThread);
		else if(action.equals("test"))
			runTest(args[2], args[3], Integer.valueOf(args[4]), numberOfThread);
		else if(action.equals("all"))
			runAll(Integer.valueOf(args[2]), Integer.valueOf(args[3]), Integer.valueOf(args[4]), numberOfThread);
		
		return;
	}
	
	// scenerios
	protected static void runHelp() {
		logger.info("===========================================================================================================");
		logger.info("OpenAPINotificationServerPerformanceTest setup numberOfThread email password numberOfApp");
		logger.info("OpenAPINotificationServerPerformanceTest test numberOfThread email password numberOfTimelineItem");
		logger.info("OpenAPINotificationServerPerformanceTest all numberOfApp numberOfUser numberOfTimelineItem numberOfThread");
		logger.info("===========================================================================================================");
	}
	
	protected static void runSetup(String username, String password, int numberOfApp, int numberOfThread) {
		
		// start endpoint
		setUpEndpoint();

		// register apps
		List<OpenAPIThirdPartyApp> apps = registerApps("nhhai16991@gmail.com", "qqqqqq", numberOfApp, numberOfThread);
	
		// subcribe apps to endpoint
		subcribeApps(apps, endpoint, numberOfThread);

		// authorize users to apps
		List<String> emails = new ArrayList<String>();
		emails.add(username);
		authorizeUsersToApps(emails, password, apps, numberOfThread);
		
		logger.info("Total app: " + numberOfApp);
		logger.info("Total app successfully created: " + numbeOfSuccessfulRegisterApp);
		logger.info("Total app successfully subscribed: " + numberOfSuccessfulSubscribeApp);
		logger.info("Total app user successfully authorized to: " + numberOfSuccessfulAuthorization);
	}
	
	protected static void runTest(String email, String password, int numberOfItems, int numberOfThread) {
		
		// start endpoint
		setUpEndpoint();
				
		// test notification: create, update, delete...
		runSimpleTestMultiThread(email, password, numberOfItems, numberOfThread);
		
		// summary
		ShortcutsTyper.delayTime(10000);
		logger.info("Total notifications: " + numberOfNotifications);
		resultLogger.log("Total notifications: " + numberOfNotifications);
	}
	
	protected static void runAll(int numberOfApp, int numberOfUser, int numberOfItems, int numberOfThread) {
		
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
		testNotification(emails, "qqqqqq", numberOfThread, numberOfItems);
		
		
		// summary
		ShortcutsTyper.delayTime(10000);
		logger.info("Total app: " + numberOfApp);
		logger.info("Total app successfully created: " + numbeOfSuccessfulRegisterApp);
		logger.info("Total app successfully subscribed: " + numberOfSuccessfulSubscribeApp);
		logger.info("Total valid access token: " + numberOfSuccessfulAuthorization);
		logger.info("Total notifications: " + numberOfNotifications);
		
		resultLogger.log("Total app: " + numberOfApp);
		resultLogger.log("Total app successfully created: " + numbeOfSuccessfulRegisterApp);
		resultLogger.log("Total app successfully subscribed: " + numberOfSuccessfulSubscribeApp);
		resultLogger.log("Total valid access token: " + numberOfSuccessfulAuthorization);
		resultLogger.log("Total notifications: " + numberOfNotifications);
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
		ServerHelper.startNotificationEndpointServer(localhost);
		ShortcutsTyper.delayTime(5000);
	}
		
	protected static void runSimpleTestMultiThread(String email, String password, int numberOfItems, int numberOfThread) {

		// log in
		final long timestamp = System.currentTimeMillis() / 1000;
		final String token = MVPApi.signIn(email, password).token;

		// create list of items
    	List<TimelineItem> items = new ArrayList<TimelineItem>();
    	
    	for(int i = 0; i < numberOfItems; i++) {
			
    		items.add(DataGenerator.generateRandomActivitySessionTimelineItem(timestamp - i, null));
    	}

    	// run
    	Collection<Future<?>> futures = new LinkedList<Future<?>>();
    	ExecutorService executor = Executors.newFixedThreadPool(numberOfThread);
    	for(int i = 0; i < numberOfItems; i++) {
			
    		final TimelineItem item = items.get(i);
			Thread thread = new Thread() {

				public void run() {
					MVPApi.createTimelineItem(token, item);
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
	
	protected static void runSimpleTestThread(String email, String password, int numberOfItems) {

		// log in
		long timestamp = System.currentTimeMillis() / 1000;
		String token = MVPApi.signIn(email, password).token;


		// create things
		for(int i = 0; i < numberOfItems; i++)
			MVPApi.createTimelineItem(token, DataGenerator.generateRandomActivitySessionTimelineItem(timestamp - i, null));
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
					
					BaseResult r = OpenAPI.registerApp(app, cookie);
					if(!r.isOK())
						resultLogger.log("Register app fail: " + r.statusCode + " - " + r.rawData);
					else
						numbeOfSuccessfulRegisterApp++;
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
					
					BaseResult r = OpenAPI.subscribeNotification(clientKey, clientSecret, endpoint, OpenAPI.NOTIFICATION_RESOURCE_SESSION);
					if(!r.isOK())
						resultLogger.log("Subscribe app fail: " + r.statusCode + " - " + r.rawData);
					else
					{
						numberOfSuccessfulSubscribeApp++;
						ShortcutsTyper.delayTime(3000);
					}
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
        
        ShortcutsTyper.delayTime(Math.min(30000, 1000 * apps.size()));
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
						String accesstoken = OpenAPI.getAccessToken(email, password, scope, clientKey, returnUrl);
						if(accesstoken == null)
							resultLogger.log("Authorise user fail: " + email);
						else
							numberOfSuccessfulAuthorization++;
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

	protected static void testNotification(List<String> emails, final String password, int numberOfThread, final int numberOfItems) {
		
		Collection<Future<?>> futures = new LinkedList<Future<?>>();
    	ExecutorService executor = Executors.newFixedThreadPool(numberOfThread);
    	
    	for(int i = 0; i < emails.size(); i++) {
			
			final String email = emails.get(i);

			Thread thread = new Thread() {

				public void run() {
					runSimpleTestThread(email, password, numberOfItems);
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
