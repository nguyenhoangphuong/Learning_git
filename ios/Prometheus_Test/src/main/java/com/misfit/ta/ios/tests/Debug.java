package com.misfit.ta.ios.tests;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import org.apache.log4j.Logger;
import org.graphwalker.Util;

import com.google.resting.json.JSONException;
import com.misfit.ta.backend.api.internalapi.MVPApi;
import com.misfit.ta.backend.aut.BackendHelper;
import com.misfit.ta.backend.aut.correctness.servercalculation.ServerCalculationTestHelpers;
import com.misfit.ta.backend.data.DataGenerator;
import com.misfit.ta.gui.AppInstaller;
import com.misfit.ta.gui.Gui;
import com.misfit.ta.gui.HomeScreen;
import com.misfit.ta.gui.social.LeaderboardView;
import com.misfit.ta.gui.social.SocialProfileView;
import com.misfit.ta.utils.Files;


public class Debug {
	
	protected static Logger logger = Util.setupLogger(Debug.class);
	
	public static void main(String[] args) throws JSONException, IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {

//		Files.delete("rawdata");
//		Files.getFile("rawdata");
//		ServerCalculationTestHelpers.runTest("rawdata/test1", "dcsc037@a.a", "qqqqqq");
		
//		Gui.init("192.168.1.250");
//		HomeScreen.tapSleepTimeline();
//		HomeScreen.tapWeightTimeline();
//		HomeScreen.tapActivityTimeline();
//		HomeScreen.tapMenuSocial();
//		LeaderboardView.tapYesterday();
//		LeaderboardView.tapToday();
//		HomeScreen.tapWordView();
//		HomeScreen.tapLeaderboard();
//		HomeScreen.tapSocialProfile();
		
		MVPApi.unlinkDevice(MVPApi.signIn("ios_automation_search_friend@misfitqa.com", "qwerty1").token);
	}

}