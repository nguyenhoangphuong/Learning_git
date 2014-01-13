package com.misfit.ta.ios.tests;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.graphwalker.Util;

import com.misfit.ios.NuRemoteClient;
import com.misfit.ios.ViewUtils;
import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.backend.api.social.SocialAPI;
import com.misfit.ta.backend.aut.BackendHelper;
import com.misfit.ta.backend.data.DataGenerator;
import com.misfit.ta.backend.data.goal.Goal;
import com.misfit.ta.backend.data.pedometer.Pedometer;
import com.misfit.ta.backend.data.profile.ProfileData;
import com.misfit.ta.gui.Gui;
import com.misfit.ta.gui.PrometheusHelper;
import com.misfit.ta.gui.Sync;
import com.misfit.ta.gui.social.LeaderboardView;
import com.misfit.ta.gui.social.SearchFriendView;
import com.misfit.ta.gui.social.SocialProfileView;
import com.misfit.ta.utils.TextTool;

public class Debug {
	
	protected static Logger logger = Util.setupLogger(Debug.class);
	
	
	public static void main(String[] args) throws IOException {
		
//		String uid = MVPApi.getUserId(MVPApi.signIn("nhhai16991@gmail.com", "qqqqqq").token);
//		for(int i = 0; i < 3; i++) {
//			String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), "qqqqqq").token;
//			ProfileData profile = DataGenerator.generateRandomProfile(System.currentTimeMillis() / 1000, null);
//			profile.setHandle(TextTool.getRandomString(5, 10));
//			MVPApi.createProfile(token, profile);
//			SocialAPI.sendFriendRequest(token, uid);
//		}
		
		Gui.init("192.168.1.111");
		logger.info(SocialProfileView.getPrivacySwitchStatus());
	}
}
	