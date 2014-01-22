package com.misfit.ta.ios.tests;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.graphwalker.Util;

import com.misfit.ta.backend.api.RequestHelper;
import com.misfit.ta.backend.api.internalapi.MVPApi;
import com.misfit.ta.backend.api.openapi.OpenAPI;
import com.misfit.ta.backend.aut.BackendHelper;
import com.misfit.ta.backend.data.BaseParams;
import com.misfit.ta.backend.data.timeline.TimelineItem;
import com.misfit.ta.backend.server.ServerHelper;
import com.misfit.ta.common.MVPCalculator;
import com.misfit.ta.gui.AppInstaller;
import com.misfit.ta.gui.Gui;
import com.misfit.ta.utils.ShortcutsTyper;
import com.misfit.ta.utils.TextTool;
import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;

public class Debug {
	
	protected static Logger logger = Util.setupLogger(Debug.class);
	
	static String accessToken = "O4qbZUPYNdIguU04kerCTBKmjSeFJnIWxWnJBuZ8fsDdS2HlGxH3byVDPQkqMoEFVbmWkhxvYB9YcPHGdCgsVIEPHTGM8pYX07Fc3B8mYbVw1d6iHZXWGBnJgQdJcKpKyda8ZqgBkbJpq2Vy3w7mndWGsug6rF0Adc1AT9rKqoP2TsmdskGpnQ68OGnO9eW6GYJSoyLqnipJ1nKeoqE8VcKbgdQ5aKYvURdNt0EIcMtYjYjRfGWQTRPxp8lkuVRd";
	static String invalidAccessToken = TextTool.getRandomString(10, 10);
	static String myUid = "51cd11d95138105d0300066d";
	static String youUid = "51b1d2c35138106d210000d8";
	static String strangerUid = "519e9a079f12e50709000218";
	static String fromDate = "2014-01-01";
	static String toDate = "2014-21-01";
	
	public static void main(String[] args) throws IOException {

//		OpenAPI.getAccessToken("thinh@misfitwearables.com", "test12", "public", "mWmljFBCUHQDmgke", "/");
//		OpenAPI.getAccessToken("nhhai16991@gmail.com", "qqqqqq", "public", "mWmljFBCUHQDmgke", "/");
		int test = Integer.parseInt(args[0]);
		switch(test) {
		case 1:
			getDevice();
			break;
		case 2:
			getProfile();
			break;
		case 3:
			getGoals();
			break;
		case 4:
			getSummary();
			break;
		case 5:
			getSessions();
			break;
		case 6:
			getSleeps();
			break;
		default:
			logger.info("Hello world!");
		}
	}
	
	public static void getDevice() {
		
		logger.info("Invalid and empty access token");
		logger.info("==========================================================================");
		OpenAPI.getDevice(null, "me");
		OpenAPI.getDevice(invalidAccessToken, "me");
		
		logger.info("\n\nGet resource route /me and /myuid");
		logger.info("==========================================================================");
		OpenAPI.getDevice(accessToken, myUid);
		OpenAPI.getDevice(accessToken, "me");
		
		logger.info("\n\nGet resource of other authorized and non-authorized users");
		logger.info("==========================================================================");
		OpenAPI.getDevice(accessToken, youUid);
		OpenAPI.getDevice(accessToken, strangerUid);
		
	}
	
	public static void getProfile() {

		logger.info("Invalid and empty access token");
		logger.info("==========================================================================");
		OpenAPI.getProfile(null, "me");
		OpenAPI.getProfile(invalidAccessToken, "me");
		
		logger.info("\n\nGet resource route /me and /myuid");
		logger.info("==========================================================================");
		OpenAPI.getProfile(accessToken, myUid);
		OpenAPI.getProfile(accessToken, "me");
		
		logger.info("\n\nGet resource of other authorized and non-authorized users");
		logger.info("==========================================================================");
		OpenAPI.getProfile(accessToken, youUid);
		OpenAPI.getProfile(accessToken, strangerUid);
	}

	public static void getGoals() {

		logger.info("Invalid and empty access token");
		logger.info("==========================================================================");
		OpenAPI.getGoals(null, "me", fromDate, toDate);
		OpenAPI.getGoals(invalidAccessToken, "me", fromDate, toDate);
		
		logger.info("\n\nGet resource route /me and /myuid");
		logger.info("==========================================================================");
		OpenAPI.getGoals(accessToken, myUid, fromDate, toDate);
		OpenAPI.getGoals(accessToken, "me", fromDate, toDate);
		
		logger.info("\n\nGet resource of other authorized and non-authorized users");
		logger.info("==========================================================================");
		OpenAPI.getGoals(accessToken, youUid, fromDate, toDate);
		OpenAPI.getGoals(accessToken, strangerUid, fromDate, toDate);
		
		logger.info("\n\nInvalid params");
		logger.info("==========================================================================");
		OpenAPI.getGoals(accessToken, "me", null, toDate);
		OpenAPI.getGoals(accessToken, "me", fromDate, null);
		OpenAPI.getGoals(accessToken, "me", "abc", toDate);
		OpenAPI.getGoals(accessToken, "me", fromDate, "def");
	}
	
	public static void getSummary() {

		logger.info("Invalid and empty access token");
		logger.info("==========================================================================");
		OpenAPI.getSummary(null, "me", fromDate, toDate);
		OpenAPI.getSummary(invalidAccessToken, "me", fromDate, toDate);
		
		logger.info("\n\nGet resource route /me and /myuid");
		logger.info("==========================================================================");
		OpenAPI.getSummary(accessToken, myUid, fromDate, toDate);
		OpenAPI.getSummary(accessToken, "me", fromDate, toDate);
		
		logger.info("\n\nGet resource of other authorized and non-authorized users");
		logger.info("==========================================================================");
		OpenAPI.getSummary(accessToken, youUid, fromDate, toDate);
		OpenAPI.getSummary(accessToken, strangerUid, fromDate, toDate);
		
		logger.info("\n\nInvalid params");
		logger.info("==========================================================================");
		OpenAPI.getSummary(accessToken, "me", null, toDate);
		OpenAPI.getSummary(accessToken, "me", fromDate, null);
		OpenAPI.getSummary(accessToken, "me", "abc", toDate);
		OpenAPI.getSummary(accessToken, "me", fromDate, "def");
	}

	public static void getSessions() {

		logger.info("Invalid and empty access token");
		logger.info("==========================================================================");
		OpenAPI.getSessions(null, "me", fromDate, toDate);
		OpenAPI.getSessions(invalidAccessToken, "me", fromDate, toDate);
		
		logger.info("\n\nGet resource route /me and /myuid");
		logger.info("==========================================================================");
		OpenAPI.getSessions(accessToken, myUid, fromDate, toDate);
		OpenAPI.getSessions(accessToken, "me", fromDate, toDate);
		
		logger.info("\n\nGet resource of other authorized and non-authorized users");
		logger.info("==========================================================================");
		OpenAPI.getSessions(accessToken, youUid, fromDate, toDate);
		OpenAPI.getSessions(accessToken, strangerUid, fromDate, toDate);
		
		logger.info("\n\nInvalid params");
		logger.info("==========================================================================");
		OpenAPI.getSessions(accessToken, "me", null, toDate);
		OpenAPI.getSessions(accessToken, "me", fromDate, null);
		OpenAPI.getSessions(accessToken, "me", "abc", toDate);
		OpenAPI.getSessions(accessToken, "me", fromDate, "def");
	}
	
	public static void getSleeps() {

		logger.info("Invalid and empty access token");
		logger.info("==========================================================================");
		OpenAPI.getSleeps(null, "me", fromDate, toDate);
		OpenAPI.getSleeps(invalidAccessToken, "me", fromDate, toDate);
		
		logger.info("\n\nGet resource route /me and /myuid");
		logger.info("==========================================================================");
		OpenAPI.getSleeps(accessToken, myUid, fromDate, toDate);
		OpenAPI.getSleeps(accessToken, "me", fromDate, toDate);
		
		logger.info("\n\nGet resource of other authorized and non-authorized users");
		logger.info("==========================================================================");
		OpenAPI.getSleeps(accessToken, youUid, fromDate, toDate);
		OpenAPI.getSleeps(accessToken, strangerUid, fromDate, toDate);
		
		logger.info("\n\nInvalid params");
		logger.info("==========================================================================");
		OpenAPI.getSleeps(accessToken, "me", null, toDate);
		OpenAPI.getSleeps(accessToken, "me", fromDate, null);
		OpenAPI.getSleeps(accessToken, "me", "abc", toDate);
		OpenAPI.getSleeps(accessToken, "me", fromDate, "def");
	}

}