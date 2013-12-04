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
import com.misfit.ta.backend.data.goal.Goal;
import com.misfit.ta.backend.data.profile.ProfileData;
import com.misfit.ta.backend.data.social.Leaderboard;
import com.misfit.ta.backend.data.social.SocialUserBase;
import com.misfit.ta.backend.data.social.SocialUserLeaderBoardEvent;
import com.misfit.ta.backend.data.social.SocialUserWithStatus;
import com.misfit.ta.backend.data.social.SocialUserWorldEvent;
import com.misfit.ta.backend.data.sync.SyncFileData;
import com.misfit.ta.backend.data.sync.SyncLog;
import com.misfit.ta.backend.data.timeline.TimelineItem;
import com.misfit.ta.backend.data.timeline.timelineitemdata.TimelineItemDataBase;
import com.misfit.ta.backend.seed.GenerateUserSeed;
import com.misfit.ta.base.ParallelThreadExecutor;
import com.misfit.ta.common.MVPCommon;
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
		
		int numberOfGoals = 17;
		
		String[] emails = new String[] 
				{
					"thinh@misfitwearables.com",
				};
		
		String[] passwords = new String[]
				{
					"misfit1",
				};
		
		
		for(int i = 0; i < emails.length; i++) {
			
			long startTime = MVPApi.getDayStartEpoch(System.currentTimeMillis() / 1000 - numberOfGoals * 3600 * 24);
			String token = MVPApi.signIn(emails[i], passwords[i]).token;
			
			Goal[] goals = MVPApi.searchGoal(token, startTime, Integer.MAX_VALUE, 0).goals;
			
			logger.info("GOAL OF USER: " + emails[i]);
			logger.info("====================================================================================");
			for(Goal goal : goals)
				logger.info("Points: " + goal.getProgressData().getPoints());
			logger.info("====================================================================================\n\n");
		}
		
	}
}
	