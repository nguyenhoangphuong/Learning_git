package com.misfit.ta.ios.tests;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;
import org.graphwalker.Util;

import com.misfit.ta.backend.api.openapi.OpenAPI;


public class Debug {
	
	protected static Logger logger = Util.setupLogger(Debug.class);
	
	// Shared queue for notifications from HTTP server
    static BlockingQueue<Map<String, String>> messageQueue = new LinkedBlockingQueue<Map<String, String>>();

	
	public static void main(String[] args) throws Exception {

//		Files.delete("rawdata");
//		Files.getFile("rawdata");
//		ServerCalculationTestHelpers.runTest("rawdata/test1", "dcsc043@a.a", "qqqqqq");
		
//		OpenAPI.getAccessToken("dup1@misfit.com", "misfit1", "public", "mWmljFBCUHQDmgke", "https://www.misfitwearables.com");
//		String token = MVPApi.signIn("dup1@misfit.com", "misfit1").token;
//		Goal goal = MVPApi.searchGoal(token, System.currentTimeMillis() / 1000 - 3600 * 24, null, 0l).goals[0];
//		goal.getProgressData().setPoints(500d);
//		MVPApi.updateGoal(token, goal);
		
//		Gui.init("192.168.1.144");
//		HomeSettings.getSpinnerGoal();
//		HomeSettings.setSpinnerGoal(600, 500);
		
//		Files.delete("keys");
//		Files.getFile("keys");
//		ServerHelper.startNotificationEndpointServer("https://0.0.0.0:8999/");
//		ShortcutsTyper.delayTime(5000);
//		
//		OpenAPI.subscribeNotification("mWmljFBCUHQDmgke", "VmlKdBdJ5B98d2PL0cI5kd6NCvRHK1Pi", 
//				"https://jenkins.misfitwearables.com", OpenAPI.NOTIFICATION_RESOURCE_DEVICE);
//		
//		while(true) {
//			ShortcutsTyper.delayOne();
//		}
		
		String ClientKey = "mWmljFBCUHQDmgke";
		String ClientSecret = "VmlKdBdJ5B98d2PL0cI5kd6NCvRHK1Pi";
		String EPA = "https://tester.int.misfitwearables.com/handle_post.php";
		String EPB = "https://tester.int.misfitwearables.com/handle_post.php";
		
//		OpenAPI.subscribeNotification(ClientKey, ClientSecret, EPA, OpenAPI.NOTIFICATION_RESOURCE_PROFILE);
		OpenAPI.subscribeNotification(ClientKey, ClientSecret, EPA, OpenAPI.NOTIFICATION_RESOURCE_DEVICE);
		OpenAPI.subscribeNotification(ClientKey, ClientSecret, EPB, OpenAPI.NOTIFICATION_RESOURCE_GOAL);
		OpenAPI.subscribeNotification(ClientKey, ClientSecret, EPB, OpenAPI.NOTIFICATION_RESOURCE_SESSION);
		OpenAPI.subscribeNotification(ClientKey, ClientSecret, EPB, OpenAPI.NOTIFICATION_RESOURCE_SLEEP);

	}
}