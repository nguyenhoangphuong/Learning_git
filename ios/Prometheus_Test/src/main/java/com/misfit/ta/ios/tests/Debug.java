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
import com.misfit.ta.utils.TextTool;
import com.sun.jna.platform.unix.X11.XClientMessageEvent.Data;

public class Debug {
	
	protected static Logger logger = Util.setupLogger(Debug.class);
	
	public static void main(String[] args) {
			
		for(int i = 0; i < 10; i++) {
			
			String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), "qqqqqq").token;
			String handle = TextTool.getRandomString(4, 8) + System.nanoTime();
			
			ProfileData profile = DataGenerator.generateRandomProfile(System.currentTimeMillis() / 1000, null);
			profile.setPrivacy(1);
			profile.setName(TextTool.getRandomString(6, 12));
			profile.setHandle(handle);
			
			MVPApi.createProfile(token, profile);
			
			
			SocialAPI.sendFriendRequest(token, "51cd11d95138105d0300066d");
//			SocialAPI.sendFriendRequest(token, "519facf09f12e57a7b0000d3");
//			SocialAPI.sendFriendRequest(token, "51a41ac89f12e53f79000001");
		}
	}
	
	public static void printUsers(SocialUserBase[] users) {
		
		logger.info("-----------------------------------------------------------------------");
		for(SocialUserBase user : users) {
			logger.info(user.toJson().toString());
		}
		logger.info("-----------------------------------------------------------------------\n\n");
	}
}
	