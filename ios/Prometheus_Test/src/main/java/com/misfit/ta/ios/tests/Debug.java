package com.misfit.ta.ios.tests;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;
import org.graphwalker.Util;

import com.misfit.ios.NuRemoteClient;
import com.misfit.ios.ViewUtils;
import com.misfit.ta.gui.Gui;
import com.misfit.ta.gui.HomeScreen;
import com.misfit.ta.gui.HomeSettings;
import com.misfit.ta.gui.PrometheusHelper;
import com.misfit.ta.gui.social.SearchFriendView;


public class Debug {
	
	protected static Logger logger = Util.setupLogger(Debug.class);
	
	// Shared queue for notifications from HTTP server
    static BlockingQueue<Map<String, String>> messageQueue = new LinkedBlockingQueue<Map<String, String>>();

	
	public static void main(String[] args) throws Exception {

//		Files.delete("rawdata");
//		Files.getFile("rawdata");
//		ServerCalculationTestHelpers.runTest("rawdata/test1", "dcsc047@a.a", "qqqqqq");

		Gui.init("192.168.1.224");
		ViewUtils.isExistedView("UILabel", "goal weight: 140.0");
		ViewUtils.isExistedView("UILabel", "goal weight: _140.0_");
		
	}
}