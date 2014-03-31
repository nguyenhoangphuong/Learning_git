package com.misfit.ta.ios.tests;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;
import org.graphwalker.Util;

import com.misfit.ta.backend.api.internalapi.MVPApi;
import com.misfit.ta.backend.aut.BackendHelper;
import com.misfit.ta.backend.aut.correctness.servercalculation.ServerCalculationTestHelpers;
import com.misfit.ta.backend.data.sync.sdk.SDKSyncLog;
import com.misfit.ta.utils.TextTool;


public class Debug {
	
	protected static Logger logger = Util.setupLogger(Debug.class);
	
	// Shared queue for notifications from HTTP server
    static BlockingQueue<Map<String, String>> messageQueue = new LinkedBlockingQueue<Map<String, String>>();

	
	public static void main(String[] args) throws Exception {

//		String token = BackendHelper.createAccount("ios127@a.a", "qqqqqq");
//		BackendHelper.createGoalsInThePast(token, 80, 7);
		BackendHelper.link("ios120@a.a", "qqqqqq", TextTool.getRandomString(10, 10));
		
//		Files.delete("rawdata");
//		Files.getFile("rawdata");
//		ServerCalculationTestHelpers.runTest("rawdata/test1", "dcsc047@a.a", "qqqqqq");

//		Gui.init("192.168.1.224");
		
	}
}