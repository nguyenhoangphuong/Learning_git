package com.misfit.ta.backend.aut.performance;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.graphwalker.Util;
import org.testng.Assert;

import com.google.resting.component.impl.ServiceResponse;
import com.google.resting.json.JSONArray;
import com.misfit.ta.Settings;
import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.backend.aut.DefaultValues;
import com.misfit.ta.backend.aut.ResultLogger;
import com.misfit.ta.backend.data.BaseParams;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.backend.data.account.AccountResult;
import com.misfit.ta.backend.data.goal.GoalsResult;
import com.misfit.ta.backend.data.goal.ProgressData;
import com.misfit.ta.backend.data.goal.TripleTapData;
import com.misfit.ta.backend.data.pedometer.Pedometer;
import com.misfit.ta.backend.data.profile.ProfileData;
import com.misfit.ta.backend.data.profile.ProfileResult;
import com.misfit.ta.base.Clock;
import com.misfit.ta.utils.TextTool;

public class BackendDatabaseSeedingThread implements Runnable {

	private String password = "misfit1";
	private String udid;

	private int userCount = 0;
	private JSONArray timelineItems;
	private JSONArray graphItems;
	private ResultLogger rlog;

	private String token;


	private boolean randomized = false;
	private long userRequestTime;
	private long countRequest;
	private String mySerial = TextTool.getRandomString(5, 6) + System.currentTimeMillis();

	private Clock clock;

	Logger logger = Util.setupLogger(BackendDatabaseSeedingThread.class);

	public BackendDatabaseSeedingThread(int userCount, JSONArray timelineItems, JSONArray graphItems, ResultLogger rlog, boolean randomized) {
		this.userCount = userCount;
		this.timelineItems = timelineItems;
		this.graphItems = graphItems;
		this.rlog = rlog;
		this.randomized = randomized;
	}

	public void run() {

		clock = new Clock();

		logger.info(" ===============  User " + userCount + " =================");
		String email = MVPApi.generateUniqueEmail();

		// sign up first
		clock.tick("sign_up");
		AccountResult r = MVPApi.signUp(email, password);
		clock.tock();
		
		token = r.token;


		int operation = Settings.getInt("OPERATION");
		if (randomized) {
			Random rand = new Random(System.currentTimeMillis());
			operation = (randomized) ? rand.nextInt(6) : -1;
			System.out.println("LOG [BackendDatabaseSeedingThread.run]: random:  " + operation);
		}

		System.out.println("LOG [BackendDatabaseSeedingThread.run]: operation: " + operation + " : " + (operation == 0 || operation <= -1));

		countRequest = 1;
		if (operation == 0 || operation <= -1) {
			doAccountOperation(email);
		}
		
		if (operation == 1 || operation <= -1) {
			doProfileOperation();
		}

		if (operation == 2 || operation <= -1) {
			doPedometerOperations();
		}

		if (operation == 3 || operation <= -1) {
			doLinkinOperation();
		}

		if (operation == 4 || operation <= -1) {
			doGoalOperation();
		}

		if (operation == 5 || operation <= -1) {
			doTimelineOperation();
		}


		System.out.println("LOG [BackendStressTestThread.run]: ------------------------------------ DONE");

		userRequestTime = clock.getSumIntervals();

		rlog.log((userCount + 1) + clock.getTimeInteval()
                + email + "\t" 
                + userRequestTime + "\t"
                + countRequest + "\t"
                + (String.valueOf(userRequestTime/countRequest))
				);
	}

	public void doAccountOperation(String email) {
		// // sign out then
		clock.tick("signout");
		BaseResult br = MVPApi.signOut(token);
		clock.tock();
		//        Assert.assertTrue(br.isOK(), "Status code is not 200: " + br.statusCode);

		// // sign in
		clock.tick("signin");
		AccountResult r = MVPApi.signIn(email, "misfit1");
		clock.tock();
		token = r.token;
		countRequest += 2;
		//       Assert.assertTrue(r.isOK(), "Status code is not 200: " + r.statusCode);
	}
	public void doProfileOperation() {
		ProfileData profile = DefaultValues.DefaultProfile();
		// createProfile
		clock.tick("createProfile");
		ProfileResult result = MVPApi.createProfile(token, profile);
		clock.tock();
		//        Assert.assertTrue(result.isOK(), "Status code is not 200: " + result.statusCode);


		// get Profile
		clock.tick("getProfile");
		result = MVPApi.getProfile(token);
		clock.tock();
		//        Assert.assertTrue(result.isOK(), "Status code is not 200: " + result.statusCode);

		// update profile
		ProfileData newProfile = result.profile;
		newProfile.setWeight(profile.getWeight() + 1);
		//       newProfile.updatedAt += 100;
		clock.tick("update profile");
		result = MVPApi.updateProfile(token, newProfile, profile.getServerId());
		clock.tock();
		countRequest += 3;
		//       Assert.assertTrue(result.isExisted(), "Status code is not 210: " + result.statusCode);
	}

	public void doPedometerOperations() {
		long now = System.currentTimeMillis()/1000;
		clock.tick("createPedo");
		Pedometer pedo = MVPApi.createPedometer(token, mySerial, "hw1234", now, now, now, "localId", null, now);
		clock.tock();
		//        Assert.assertTrue(pedo != null, "Pedometer can not be created");


		clock.tick("getPedo");
		pedo = MVPApi.showPedometer(token);
		clock.tock();
		//         Assert.assertTrue(pedo != null, "Can not get pedometer");


		pedo.setUpdatedAt(System.currentTimeMillis()/1000);
		clock.tick("updatePedo");
		pedo = MVPApi.updatePedometer(token, mySerial, "hw1234", now, now, now, "localId", null, now);
		clock.tock();
		//         Assert.assertTrue(pedo != null, "Pedometer can not be updated");
		countRequest += 3;
	}

	public void doLinkinOperation() {
	    clock.tick("getLinkingDevice");
		String status = MVPApi.getDeviceLinkingStatus(token,mySerial);
		clock.tock();
		//         Assert.assertTrue(status != null, "Can not get linking status");

		clock.tick("unlink");
		status = MVPApi.unlinkDevice(token);
		clock.tock();
		//         Assert.assertTrue(status != null, "Can not unlink device");
		countRequest += 2;
	}

	public void doGoalOperation() {
		long now = System.currentTimeMillis();
		ProgressData progressData = new ProgressData(300, 5000, 1200, 500);
		clock.tick("createGoal");
		GoalsResult goalResult = MVPApi.createGoal(token, 2500, now, now + 8400, 
				0, progressData, new ArrayList<TripleTapData>(), "mylocalid", now);
		clock.tock();
		//        Assert.assertTrue(goalResult.isOK(), "Status code is not 200: " + goalResult.statusCode);

		clock.tick("getGoal");
		goalResult = MVPApi.getGoal(token, goalResult.goals[0].getServerId());
		clock.tock();
		//        Assert.assertTrue(goalResult.isOK(), "Status code is not 200: " + goalResult.statusCode);

		clock.tick("searchGoal");
		goalResult = MVPApi.searchGoal(token, now, now + 8400, now);
		clock.tock();
		//        Assert.assertTrue(goalResult.isOK(), "Status code is not 200: " + goalResult.statusCode);

		clock.tick("updateGoal");
		goalResult = MVPApi.updateGoal(token, now + 234, goalResult.goals[0].getServerId(), 
				2500, now, now + 8400, 0, progressData, new ArrayList<TripleTapData>(), "mylocalid");
		clock.tock();
		//        Assert.assertTrue(goalResult.isExisted(), "Status code is not 210: " + goalResult.statusCode);


		countRequest += 4;
	}

	public void doTimelineOperation() {

		// create timeline items and graph items
		// generate timeline items
	    clock.tick("createTimeline");
		ServiceResponse response = MVPApi.createTimelineItems(token, timelineItems);
		clock.tock();
		//        Assert.assertTrue(response.getStatusCode() <= 210, "Status code is > 210: " + response.getStatusCode());

		clock.tick("createGraphItem");
		response = MVPApi.createGraphItems(token, graphItems);
		countRequest += 2;
		//        Assert.assertTrue(response.getStatusCode() <= 210, "Status code is > 210: " + response.getStatusCode());

	}

	public void doSyncOperation() {
	    clock.tick("sync");
		MVPApi.syncLog(token, MVPApi.generateSyncLog());
		clock.tock();
	}
}
