package com.misfit.ta.ios.tests;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;
import org.graphwalker.Util;

import com.misfit.ios.NuRemoteClient;
import com.misfit.ios.ViewUtils;
import com.misfit.ta.backend.BackendTestEnvironment;
import com.misfit.ta.backend.api.internalapi.MVPApi;
import com.misfit.ta.backend.api.openapi.OpenAPI;
import com.misfit.ta.backend.aut.BackendHelper;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.backend.data.DataGenerator;
import com.misfit.ta.backend.data.goal.Goal;
import com.misfit.ta.backend.data.pedometer.Pedometer;
import com.misfit.ta.backend.data.profile.ProfileData;
import com.misfit.ta.backend.data.profile.ProfileResult;
import com.misfit.ta.backend.data.timeline.TimelineItem;
import com.misfit.ta.backend.data.timeline.timelineitemdata.TimelineItemDataBase;
import com.misfit.ta.common.MVPCommon;
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

		String token = MVPApi.signUp("bd00@a.a", "qqqqqq").token;
		for (int i = 0; i < 30; i++) {
			long timestamp = System.currentTimeMillis() / 1000 - 3600 * 24 * i;
			
			MVPApi.createGoal(token, Goal.getDefaultGoal(timestamp));
			MVPApi.createBedditSleepSession(token, DataGenerator.generateRandomBedditSleepSession(timestamp, null));
		}
		
		

//		BackendHelper.link(MVPApi.signIn("hainguyen@misfitwearables.com", "qqqqqq").token, "HaiHongHao");
		
		
//		Gui.init("192.168.1.144");
//		logger.info(SleepViews.isTonightUtilitiesView());
		
	}
}