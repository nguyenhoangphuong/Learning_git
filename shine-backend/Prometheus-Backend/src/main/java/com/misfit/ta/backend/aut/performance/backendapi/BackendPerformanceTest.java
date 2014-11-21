package com.misfit.ta.backend.aut.performance.backendapi;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.graphwalker.Util;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.resting.component.impl.ServiceResponse;
import com.google.resting.json.JSONArray;
import com.misfit.ta.Settings;
import com.misfit.ta.backend.api.metawatch.MVPApi;
import com.misfit.ta.backend.aut.BackendAutomation;
import com.misfit.ta.backend.aut.DefaultValues;
import com.misfit.ta.backend.aut.ResultLogger;
import com.misfit.ta.backend.data.DataGenerator;
import com.misfit.ta.backend.data.account.AccountResult;
import com.misfit.ta.backend.data.profile.ProfileData;
import com.misfit.ta.backend.data.profile.ProfileResult;

public class BackendPerformanceTest extends BackendAutomation {

	private String password = "misfit1";
	private int userCount = 0;

	Logger logger = Util.setupLogger(BackendPerformanceTest.class);

	@Override
	@BeforeMethod(alwaysRun = true)
	public void beforeMethod(Method method) {
		super.beforeMethod(method);
	}

	@Test(groups = { "performance" })
	public void testPerformanceInSequence() {
		ResultLogger rlog = ResultLogger.getLogger("result_testPerformanceInSequence");

		rlog.log("Number of try\t" + "signUpTime\t" + "signOutTime\t" + "signInTime\t" + "createProfileTime\t" + "getProfileTime\t" + "updateProfileTime\t" + "addTimelineItems\t" + "addGraphItems\t" + "email");

		JSONArray[] array = DataGenerator.generateTimelineItemsAndGraphItems();
		JSONArray timelineItems = array[0];
		JSONArray graphItems = array[1];

		ProfileData profile = DefaultValues.DefaultProfile();

		logger.info(" ===============  User " + userCount + " =================");
		String email = MVPApi.generateUniqueEmail();
		long temp = System.currentTimeMillis();
		// sign up first
		long s1 = System.currentTimeMillis();
		AccountResult r = MVPApi.signUp(email, password);
		long s2 = System.currentTimeMillis();
		String token = r.token;
		Assert.assertTrue(r.isOK(), "Status code is not 200: " + r.statusCode);

		// sign out then
		long s3 = System.currentTimeMillis();
		MVPApi.signOut(token);
		long s4 = System.currentTimeMillis();

		// sign in
		long s5 = System.currentTimeMillis();
		r = MVPApi.signIn(email, "misfit1");
		long s6 = System.currentTimeMillis();
		token = r.token;
		Assert.assertTrue(r.isOK(), "Status code is not 200: " + r.statusCode);

		// createProfile
		long s7 = System.currentTimeMillis();
		ProfileResult result = MVPApi.createProfile(token, profile);
		long s8 = System.currentTimeMillis();
		Assert.assertTrue(result.isOK(), "Status code is not 200: " + result.statusCode);

		// get Profile
		long s9 = System.currentTimeMillis();
		result = MVPApi.getProfile(token);
		long s10 = System.currentTimeMillis();

		// update profile
		ProfileData newProfile = result.profile;
		newProfile.setName(profile.getName() + "_new");
		newProfile.setUpdatedAt(newProfile.getUpdatedAt() + 100);
		long s11 = System.currentTimeMillis();
		result = MVPApi.updateProfile(token, newProfile);
		long s12 = System.currentTimeMillis();
		Assert.assertTrue(result.isExisted(), "Status code is not 210: " + result.statusCode);

		// create timeline items and graph items
		// generate timeline items
		long s13 = System.currentTimeMillis();
		ServiceResponse response = MVPApi.createTimelineItems(token, timelineItems);
		long s14 = System.currentTimeMillis();
		Assert.assertTrue(response.getStatusCode() <= 210, "Status code is > 210: " + response.getStatusCode());

		long s15 = System.currentTimeMillis();
		response = MVPApi.createGraphItems(token, graphItems);
		long s16 = System.currentTimeMillis();
		Assert.assertTrue(response.getStatusCode() <= 210, "Status code is > 210: " + response.getStatusCode());
		System.out.println("LOG [BackendStressTestThread.run]: ------------------------------------ DONE");

		rlog.log((userCount + 1) + "\t" + (s2 - s1) + "\t" + (s4 - s3) + "\t" + (s6 - s5) + "\t" + (s8 - s7) + "\t" + (s10 - s9) + "\t" + (s12 - s11) + "\t" + (s14 - s13) + "\t" + (s16 - s15) + "\t" + email);

	}

	@Test(groups = { "performance with different data size" })
	public void testPerformanceDifferentDataSize() {
		ResultLogger rlog = ResultLogger.getLogger("result_testPerformanceDifferentDataSize.log");

		rlog.log("Number of try\t" + "itemcount\t" + "signUpTime\t" + "addTimelineItems\t" + "addGraphItems\t" + "email");

		int count = 1;
		int step = 5;
		int numberOfUsers = Settings.getInt("NUMBER_OF_USERS");
		for (userCount = 0; userCount < numberOfUsers; userCount++) {
			count += step;
			JSONArray[] array = DataGenerator.generateTimelineItemsAndGraphItems(count, 1);
			JSONArray timelineItems = array[0];
			JSONArray graphItems = array[1];

			ProfileData profile = DefaultValues.DefaultProfile();

			logger.info(" ===============  User " + userCount + " =================");
			String email = MVPApi.generateUniqueEmail();
			long temp = System.currentTimeMillis();
			// sign up first
			long s1 = System.currentTimeMillis();
			AccountResult r = MVPApi.signUp(email, password);
			long s2 = System.currentTimeMillis();
			String token = r.token;
			Assert.assertTrue(r.isOK(), "Status code is not 200: " + r.statusCode);

			// create timeline items and graph items
			// generate timeline items
			long s13 = System.currentTimeMillis();
			ServiceResponse response = MVPApi.createTimelineItems(token, timelineItems);
			long s14 = System.currentTimeMillis();
			Assert.assertTrue(response.getStatusCode() == 200, "Status code is != 200: " + response.getStatusCode());

			long s15 = System.currentTimeMillis();
			response = MVPApi.createGraphItems(token, graphItems);
			long s16 = System.currentTimeMillis();
			Assert.assertTrue(response.getStatusCode() == 200, "Status code is != 200: " + response.getStatusCode());
			System.out.println("LOG [BackendStressTestThread.run]: ------------------------------------ DONE");

			rlog.log(userCount + "\t" + count + "\t" + (s2 - s1) + "\t" + (s14 - s13) + "\t" + (s16 - s15) + "\t" + email);

		}

	}

}