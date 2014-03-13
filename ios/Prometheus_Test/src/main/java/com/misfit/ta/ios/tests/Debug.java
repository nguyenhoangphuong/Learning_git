package com.misfit.ta.ios.tests;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;
import org.graphwalker.Util;

import com.misfit.ta.gui.Gui;
import com.misfit.ta.gui.HomeScreen;
import com.misfit.ta.gui.social.LeaderboardView;


public class Debug {
	
	protected static Logger logger = Util.setupLogger(Debug.class);
	
	// Shared queue for notifications from HTTP server
    static BlockingQueue<Map<String, String>> messageQueue = new LinkedBlockingQueue<Map<String, String>>();

	
	public static void main(String[] args) throws Exception {

//		Files.delete("rawdata");
//		Files.getFile("rawdata");
//		ServerCalculationTestHelpers.runTest("rawdata/test1", "dcsc040@a.a", "qqqqqq");
		
		Gui.init("192.168.1.144");
		LeaderboardView.tapUserWithHandle("misterfit");
		LeaderboardView.tapToCloseCurrentUser();
		
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

	}
}