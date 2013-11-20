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
import com.misfit.ta.backend.api.social.SocialAPI;
import com.misfit.ta.backend.aut.BackendHelper;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.backend.data.DataGenerator;
import com.misfit.ta.backend.data.profile.ProfileData;
import com.misfit.ta.backend.data.social.Leaderboard;
import com.misfit.ta.backend.data.social.SocialUserBase;
import com.misfit.ta.backend.data.social.SocialUserLeaderBoardEvent;
import com.misfit.ta.backend.data.social.SocialUserWithStatus;
import com.misfit.ta.backend.data.social.SocialUserWorldEvent;
import com.misfit.ta.backend.data.sync.SyncFileData;
import com.misfit.ta.backend.data.sync.SyncLog;
import com.misfit.ta.backend.seed.GenerateUserSeed;
import com.misfit.ta.base.ParallelThreadExecutor;
import com.misfit.ta.gui.DefaultStrings;
import com.misfit.ta.gui.Gui;
import com.misfit.ta.gui.HomeScreen;
import com.misfit.ta.gui.PrometheusHelper;
import com.misfit.ta.gui.Timeline;
import com.sun.jna.platform.unix.X11.XClientMessageEvent.Data;

public class Debug {
	
	protected static Logger logger = Util.setupLogger(Debug.class);
	
	public static void main(String[] args) {
				
		Gui.init("192.168.1.115");
		while(true) {
			Gui.getProperty("PTAECASyncAnimationView", 0, "alpha");
		}
		
//		String token = MVPApi.signIn("mfwcqa.social@gmail.com", "qqqqqq").token;
//		BackendHelper.completeGoalInPast(token, 1);
//		BackendHelper.completeGoalInPast(token, 2);
//		BackendHelper.setPersonalBest(token, 500);
//		MVPApi.getTimelineItems(token, 0, Integer.MAX_VALUE, 0);
//		MVPApi.getGraphItems(token, 0, Integer.MAX_VALUE, 0);
//		MVPApi.userInfo(token);
		
		
	}
	
	public static void printUsers(SocialUserBase[] users) {
		
		logger.info("-----------------------------------------------------------------------");
		for(SocialUserBase user : users) {
			logger.info(user.toJson().toString());
		}
		logger.info("-----------------------------------------------------------------------\n\n");
	}
}
	