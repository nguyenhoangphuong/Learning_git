package com.misfit.ta.backend.aut.performance.openapi;

import java.util.ArrayList;
import java.util.Arrays;
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
import com.misfit.ta.backend.api.metawatch.MVPApi;
import com.misfit.ta.backend.api.openapi.OpenAPI;
import com.misfit.ta.backend.aut.ResultLogger;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.backend.data.DataGenerator;
import com.misfit.ta.backend.data.account.AccountResult;
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
	private static String localhost = "http://0.0.0.0:8998/";
	private static String endpoint = "http://jenkins.misfitwearables.com:8998/";
	private static ResultLogger resultLogger;
	private static List<String> errorMessages = new ArrayList<String>();
		
	private static int numbeOfSuccessfulRegisterApp = 0;
	private static int numberOfSuccessfulSubscribeApp = 0;
	private static int numberOfSuccessfulUnsubscribeApp = 0;
	private static int numberOfSuccessfulAuthorization = 0;
	
	private static int numberOfNotifications = 0;
	private static int numberOfUserSuccessfullySignIn = 0;
	private static int numberOfUserSuccessfullySignUp = 0;
	private static int numberOfTimelineItemSuccessfullyCreated = 0;
	
	
	public static void main(String[] args) {
		
		if(args.length == 0)
			args = new String[] {"test", "200", "nhhai16991@gmail.com", "qqqqqq", "1"};

		String action = args[0];
		int numberOfThread = Integer.valueOf(args[1]);
		
		if(action.equals("help"))
			runHelp();
		if(action.equals("signup"))
			runSignUp(args[2], args[3], numberOfThread);
		if(action.equals("setup"))
			runSetUp(args[2], args[3], Integer.valueOf(args[4]), args[5], numberOfThread);
		else if(action.equals("test"))
			runTest(args[2], args[3], Integer.valueOf(args[4]), numberOfThread);
		else if(action.equals("cleanup"))
			runCleanUp(args[2], numberOfThread);

		return;
	}
	
	// scenerios
	protected static void runHelp() {
		logger.info("=====================================================================================================================");
		logger.info("HOW TO USE:");
		logger.info("Sign up: create some users");
		logger.info("OpenAPINotificationServerPerformanceTest signup {numberOfThread} {emails} {password}");
		logger.info("");
		logger.info("Set up: create X apps, subscribe those apps to sessions resource and authorize users Y to those apps");
		logger.info("OpenAPINotificationServerPerformanceTest setup {numberOfThread} {emails} {password} {numberOfApp} {appPrefix}");
		logger.info("");
		logger.info("Test: create X activity session timeline items of user Y in multiple threads");
		logger.info("OpenAPINotificationServerPerformanceTest test {numberOfThread} {emails} {password} {numberOfTimelineItem}");
		logger.info("");
		logger.info("Clean up: unsubscribe app to remove its record from AWS SNS");
		logger.info("OpenAPINotificationServerPerformanceTest cleanup {numberOfThread} {appPrefix}");
		logger.info("");
		logger.info("EXAMPLE:");
		logger.info("OpenAPINotificationServerPerformanceTest signup 200 ns_user1@qa.com,ns_user2@qa.com qqqqqq");
		logger.info("OpenAPINotificationServerPerformanceTest setup 200 ns_user1@qa.com,ns_user2@qa.com qqqqqq 100 TestNotificationApp");
		logger.info("OpenAPINotificationServerPerformanceTest test 200 ns_user1@qa.com,ns_user2@qa.com qqqqqq 1");
		logger.info("OpenAPINotificationServerPerformanceTest cleanup 200 TestNotificationApp");
		logger.info("=====================================================================================================================");
	}
	
	protected static void runSignUp(String usersParam, final String password, int numberOfThread) {
		
		// log in to get token
    	Collection<Future<?>> futures = new LinkedList<Future<?>>();
    	ExecutorService executor = Executors.newFixedThreadPool(numberOfThread);
    	
    	List<String> emails = Arrays.asList(usersParam.split(","));
    	final List<String> emailsSuccessful = new ArrayList<String>();
    	
    	for(int i = 0; i < emails.size(); i++) {
			
    		final String email = emails.get(i);
			Thread thread = new Thread() {

				public void run() {
					AccountResult r = MVPApi.signUp(email, password);
					if(r.isOK() && r.token != null) {
						emailsSuccessful.add(email);
						numberOfUserSuccessfullySignUp++;
					}
					else
						errorMessages.add("Sign up error: " + r.statusCode + " - " + r.rawData);
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
    	
    	
    	// summary
    	String users = "";
    	for(String email : emailsSuccessful) {
    		users += ("," + email);
    	}
    	
    	resultLogger.log("Total users successfully signed up: " + numberOfUserSuccessfullySignUp);
    	resultLogger.log("Emails: " + users.substring(1));
    	logger.info("Total users successfully signed up: " + numberOfUserSuccessfullySignUp);
    	logger.info("Emails: " + users.substring(1));
    	
    	logError();
	}
	
	protected static void runSetUp(String usersParam, String password, int numberOfApp, String appPrefix, int numberOfThread) {
		
		// start endpoint
		setUpEndpoint();

		// register apps
		List<OpenAPIThirdPartyApp> apps = registerApps("nhhai16991@gmail.com", "qqqqqq", numberOfApp, appPrefix, numberOfThread);
	
		// subcribe apps to endpoint
		subcribeApps(apps, endpoint, numberOfThread);

		// authorize users to apps
		List<String> emails = Arrays.asList(usersParam.split(","));
		authorizeUsersToApps(emails, password, apps, numberOfThread);
		
		resultLogger.log("Total app successfully created: " + numbeOfSuccessfulRegisterApp);
		resultLogger.log("Total app successfully subscribed: " + numberOfSuccessfulSubscribeApp);
		resultLogger.log("Total successful authorization: " + numberOfSuccessfulAuthorization);
		logger.info("Total app successfully created: " + numbeOfSuccessfulRegisterApp);
		logger.info("Total app successfully subscribed: " + numberOfSuccessfulSubscribeApp);
		logger.info("Total successful authorization: " + numberOfSuccessfulAuthorization);
		
		logError();
	}
	
	protected static void runTest(String usersParam, String password, int numberOfItems, int numberOfThread) {
		
		// start endpoint
		setUpEndpoint();
				
		// test notification: create, update, delete...
		List<String> emails = Arrays.asList(usersParam.split(","));
		runSimpleTestMultiThread(emails, password, numberOfItems, numberOfThread);
		
		// summary
		ShortcutsTyper.delayTime(10000);
		logger.info("Total of users succesfully sign in: " + numberOfUserSuccessfullySignIn);
		logger.info("Total of timeline items successfully created: " + numberOfTimelineItemSuccessfullyCreated);
		logger.info("Total notifications: " + numberOfNotifications);
		resultLogger.log("Total of users succesfully sign in: " + numberOfUserSuccessfullySignIn);
		resultLogger.log("Total of timeline items successfully created: " + numberOfTimelineItemSuccessfullyCreated);
		resultLogger.log("Total notifications: " + numberOfNotifications);
		
		logError();
	}
	
	protected static void runCleanUp(String appPrefix, int numberOfThread) {
		
		
		// get all apps
		BaseResult result = OpenAPI.logIn("nhhai16991@gmail.com", "qqqqqq");
		String cookie = result.cookie;
		
		List<OpenAPIThirdPartyApp> allApps = OpenAPI.getAllApps(cookie);
		List<OpenAPIThirdPartyApp> apps = new ArrayList<OpenAPIThirdPartyApp>();
		
		for(OpenAPIThirdPartyApp app : allApps) {
			if(app.getName().startsWith(appPrefix))
				apps.add(app);
		}
		
		
		// unsubscribe
		unsubscribeApps(apps, numberOfThread);
		
		resultLogger.log("Total number of successful unsubscribe app: " + numberOfSuccessfulUnsubscribeApp);
		logger.info("Total number of successful unsubscribe app: " + numberOfSuccessfulUnsubscribeApp);
		
		logError();
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
		
	protected static void logError() {
		
		if(errorMessages.isEmpty())
			return;
		
		resultLogger.log("\n\nERROR MESSAGES: ");
		logger.info("ERROR MESSAGES: ");
		for(String error : errorMessages) {
			resultLogger.log(error);
			logger.info(error);
		}
	}


	
	protected static List<OpenAPIThirdPartyApp> registerApps(String devEmail, String devPassword, int numberOfApp, String appPrefix, int numberOfThread) {
		
		// log in first
		BaseResult result = OpenAPI.logIn(devEmail, devPassword);
		final String cookie = result.cookie;
		List<OpenAPIThirdPartyApp> apps = new ArrayList<OpenAPIThirdPartyApp>();
		
		// now reg app
		Collection<Future<?>> futures = new LinkedList<Future<?>>();
    	ExecutorService executor = Executors.newFixedThreadPool(numberOfThread);
    	
		for(int i = 0; i < numberOfApp; i++) {

			final OpenAPIThirdPartyApp app = new OpenAPIThirdPartyApp();
			app.setName(appPrefix + TextTool.getRandomString(10, 10) + System.nanoTime());
			apps.add(app);
			
			Thread thread = new Thread() {

				public void run() {
					
					BaseResult r = OpenAPI.registerApp(app, cookie);
					if(!r.isOK())
						errorMessages.add("Register app fail: " + r.statusCode + " - " + r.rawData);
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
        List<OpenAPIThirdPartyApp> sapps = OpenAPI.getAllApps(cookie);
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
						errorMessages.add("Subscribe app fail: " + r.statusCode + " - " + r.rawData);
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
 	
	protected static void authorizeUsersToApps(List<String> emails, final String password, List<OpenAPIThirdPartyApp> apps, int numberOfThread) {
		
    	final String scope = OpenAPI.allScopesAsString();
    	final String returnUrl = "http://misfit.com/";
    	
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
							errorMessages.add("Authorise user fail: " + email);
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

	protected static void runSimpleTestMultiThread(List<String> emails, final String password, int numberOfItems, int numberOfThread) {
		
		// create list of items
		long timestamp = System.currentTimeMillis() / 1000;
    	List<TimelineItem> items = new ArrayList<TimelineItem>();
    	
    	for(int i = 0; i < numberOfItems; i++) {
			
    		items.add(DataGenerator.generateRandomActivitySessionTimelineItem(timestamp - i, null));
    	}
    	
    	
    	// log in to get token
    	Collection<Future<?>> futures = new LinkedList<Future<?>>();
    	ExecutorService executor = Executors.newFixedThreadPool(numberOfThread);
    	
    	final List<String> tokens = new ArrayList<String>();
    	for(int i = 0; i < emails.size(); i++) {
			
    		final String email = emails.get(i);
			Thread thread = new Thread() {

				public void run() {
					AccountResult r = MVPApi.signIn(email, password);
					if(r.isOK() && r.token != null) {
						tokens.add(r.token);
						numberOfUserSuccessfullySignIn++;
					}
					else
						errorMessages.add("Sign in error: " + r.statusCode + " - " + r.rawData);
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
    	
    			
    	// create timeline items
    	futures = new LinkedList<Future<?>>();
    	executor = Executors.newFixedThreadPool(numberOfThread);
    	
    	for(int i = 0; i < numberOfItems; i++) {
		
    		for(int j = 0; j < tokens.size(); j++) {
    			
    			final TimelineItem item = items.get(i);
    			final String token = tokens.get(j);
    			
    			Thread thread = new Thread() {

    				public void run() {
    					BaseResult r = MVPApi.createTimelineItem(token, item);
    					if(r.isOK())
    						numberOfTimelineItemSuccessfullyCreated++;
    					else
    						errorMessages.add("Session creates fail: " + r.statusCode + " - " + r.rawData);
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

	protected static void unsubscribeApps(List<OpenAPIThirdPartyApp> apps, int numberOfThread) {
		
		Collection<Future<?>> futures = new LinkedList<Future<?>>();
    	ExecutorService executor = Executors.newFixedThreadPool(numberOfThread);
    	
		for(int i = 0; i < apps.size(); i++) {

			OpenAPIThirdPartyApp app = apps.get(i);
			final String clientKey = app.getClientKey();
			final String clientSecret = app.getClientSecret();
			
			Thread thread = new Thread() {

				public void run() {
					
					BaseResult r = OpenAPI.unsubscribeNotification(clientKey, clientSecret, OpenAPI.NOTIFICATION_RESOURCE_SESSION);
					if(!r.isOK())
						errorMessages.add("Unsubscribe app fail: " + r.statusCode + " - " + r.rawData);
					else
					{
						numberOfSuccessfulUnsubscribeApp++;
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
	}
	
}
