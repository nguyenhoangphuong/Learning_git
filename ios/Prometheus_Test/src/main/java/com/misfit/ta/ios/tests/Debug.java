package com.misfit.ta.ios.tests;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;
import org.graphwalker.Util;

import com.misfit.ios.NuRemoteClient;
import com.misfit.ios.ViewUtils;
import com.misfit.ta.backend.BackendTestEnvironment;
import com.misfit.ta.backend.api.internalapi.MVPApi;
import com.misfit.ta.common.Verify;
import com.misfit.ta.gui.AppInstaller;
import com.misfit.ta.gui.DefaultStrings;
import com.misfit.ta.gui.Gui;
import com.misfit.ta.gui.HomeScreen;
import com.misfit.ta.gui.HomeSettings;
import com.misfit.ta.gui.PrometheusHelper;
import com.misfit.ta.gui.SignUp;
import com.misfit.ta.gui.SleepViews;
import com.misfit.ta.gui.Timeline;
import com.misfit.ta.gui.social.LeaderboardView;
import com.misfit.ta.utils.ShortcutsTyper;


public class Debug {
	
	protected static Logger logger = Util.setupLogger(Debug.class);
	
	// Shared queue for notifications from HTTP server
    static BlockingQueue<Map<String, String>> messageQueue = new LinkedBlockingQueue<Map<String, String>>();

	
	public static void main(String[] args) throws Exception {
		
//		BackendTestEnvironment.RequestLoggingEnable = true;
//		MVPApi.signIn("nhhai16991@gmail.com", "qqqqqq");
			
		Gui.init("192.168.1.144");
		MVPApi.getPedometer(MVPApi.signIn("aut_sociala@a.a", "qqqqqq").token);
	}
}