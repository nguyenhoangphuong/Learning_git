package com.misfit.ta.ios.tests;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.graphwalker.Util;
import org.testng.Assert;

import com.misfit.ios.NuRemoteClient;
import com.misfit.ios.ViewUtils;
import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.backend.aut.BackendHelper;
import com.misfit.ta.backend.data.DataGenerator;
import com.misfit.ta.backend.data.sync.SyncFileData;
import com.misfit.ta.backend.data.sync.SyncLog;
import com.misfit.ta.gui.DefaultStrings;
import com.misfit.ta.gui.Gui;
import com.misfit.ta.gui.HomeScreen;
import com.misfit.ta.gui.PrometheusHelper;
import com.misfit.ta.gui.Timeline;
import com.sun.jna.platform.unix.X11.XClientMessageEvent.Data;

public class Debug {
	
	protected static Logger logger = Util.setupLogger(Debug.class);
	
	protected static int randInt(int includeFrom, int includeTo) {
		
		Random r = new Random();
		return r.nextInt(includeTo - includeFrom + 1) + includeFrom;
	}

	public static void main(String[] args) {
				
//		Gui.init("192.168.1.144");
		String token = MVPApi.signIn("qa140@a.a", "qqqqqq").token;
//		BackendHelper.completeGoalInPast(token, 1);
//		BackendHelper.completeGoalInPast(token, 2);
//		BackendHelper.setPersonalBest(token, 500);
//		MVPApi.getTimelineItems(token, 0, Integer.MAX_VALUE, 0);
//		MVPApi.getGraphItems(token, 0, Integer.MAX_VALUE, 0);
//		MVPApi.userInfo(token);
		
		long start = System.currentTimeMillis() / 1000;
		DataGenerator.createUserWithRandomData(MVPApi.generateUniqueEmail(), "qwerty1", 30, 5, 10, 6);
		long end = System.currentTimeMillis() / 1000;
		
		logger.info("Running time: " + (end - start));
	}
}
