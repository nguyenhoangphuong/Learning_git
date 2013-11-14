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
import com.misfit.ta.backend.data.social.SocialUserBase;
import com.misfit.ta.backend.data.social.SocialUserFromSearchResult;
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
				
//		Gui.init("192.168.1.144");
//		String token = MVPApi.signIn("qa140@a.a", "qqqqqq").token;
//		BackendHelper.completeGoalInPast(token, 1);
//		BackendHelper.completeGoalInPast(token, 2);
//		BackendHelper.setPersonalBest(token, 500);
//		MVPApi.getTimelineItems(token, 0, Integer.MAX_VALUE, 0);
//		MVPApi.getGraphItems(token, 0, Integer.MAX_VALUE, 0);
//		MVPApi.userInfo(token);
		
		String thyToken = MVPApi.signIn("thy@misfitwearables.com", "test12").token;
		String thinhToken = MVPApi.signIn("thinh@misfitwearables.com", "misfit1").token;
		String haiToken = MVPApi.signIn("nhhai16991@gmail.com", "qqqqqq").token;
		
		String thy = "519facf09f12e57a7b0000d3";
		String thinh = "51b1d2c35138106d210000d8";
		String hai = "51cd11d95138105d0300066d";
		
		BaseResult result = null;
		
		// thy send requests to hai and thinh
		logger.info("SEND FRIEND REQUEST API ============");
		result = SocialAPI.sendFriendRequest(thyToken, thinh);
		result = SocialAPI.sendFriendRequest(thyToken, hai);

		
		// get apis
		logger.info("GET FRIENDS API ============");
		result = SocialAPI.getFriends(thyToken);
		printUsers(SocialUserBase.usersFromResponse(result.response));
		
		logger.info("GET FRIENS REQUESTS FROM ME API ============");
		result = SocialAPI.getFriendRequestsFromMe(thyToken);
		printUsers(SocialUserBase.usersFromResponse(result.response));
		
		logger.info("GET FRIEND REQUESTS TO ME API ============");
		result = SocialAPI.getFriendRequestsToMe(thyToken);
		printUsers(SocialUserBase.usersFromResponse(result.response));
		
		logger.info("GET FACEBOOK FRIENDS API ============");
		result = SocialAPI.getFacebookFriends(thyToken);
		printUsers(SocialUserFromSearchResult.usersFromResponse(result.response));
		
		logger.info("SEARCH SOCIAL USERS API ============");
		result = SocialAPI.searchSocialUsers(thyToken, "thinh");
		printUsers(SocialUserFromSearchResult.usersFromResponse(result.response));

				
		// thinh accepts then deletes
		logger.info("ACCEPT FRIEND REQUEST API ============");
		result = SocialAPI.acceptFriendRequest(thinhToken, thy);
		
		logger.info("DELETE FRIEND API ============");
		result = SocialAPI.deleteFriend(thinhToken, thy);
		
		// hai ignores
		logger.info("IGNORE FRIEND REQEST API ============");
		result = SocialAPI.ignoreFriendRequest(haiToken, thy);
			
	}
	
	public static void printUsers(SocialUserBase[] users) {
		
		logger.info("-----------------------------------------------------------------------");
		for(SocialUserBase user : users) {
			logger.info(user.toJson().toString());
		}
		logger.info("-----------------------------------------------------------------------\n\n");
	}
}
	