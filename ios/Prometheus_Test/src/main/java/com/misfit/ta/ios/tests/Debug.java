package com.misfit.ta.ios.tests;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;
import org.graphwalker.Util;

import com.misfit.ta.backend.api.internalapi.MVPApi;
import com.misfit.ta.backend.data.profile.ProfileData;


public class Debug {
	
	protected static Logger logger = Util.setupLogger(Debug.class);
	
	// Shared queue for notifications from HTTP server
    static BlockingQueue<Map<String, String>> messageQueue = new LinkedBlockingQueue<Map<String, String>>();

	
	public static void main(String[] args) throws Exception {

//		String token = BackendHelper.createAccount("ios127@a.a", "qqqqqq");
//		BackendHelper.createGoalsInThePast(token, 80, 7);
		ProfileData profile = new ProfileData();
		profile.setWeight(-1d);
		String token = MVPApi.signIn("ios120@a.a", "qqqqqq").token;
		MVPApi.updateProfile(token, profile);
		
//		Files.delete("rawdata");
//		Files.getFile("rawdata");
//		ServerCalculationTestHelpers.runTest("rawdata/test1", "dcsc047@a.a", "qqqqqq");

//		Gui.init("192.168.1.224");
		
	}
}