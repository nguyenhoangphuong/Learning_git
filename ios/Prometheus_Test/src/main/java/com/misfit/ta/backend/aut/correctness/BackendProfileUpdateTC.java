package com.misfit.ta.backend.aut.correctness;

import org.apache.log4j.Logger;
import org.graphwalker.Util;
import org.testng.Assert;
import org.testng.annotations.*;

import com.misfit.ta.backend.data.profile.DisplayUnit;
import com.misfit.ta.backend.data.profile.ProfileData;
import com.misfit.ta.backend.data.profile.ProfileResult;
import com.misfit.ta.backend.api.*;
import com.misfit.ta.backend.aut.BackendAutomation;
import com.misfit.ta.backend.aut.DefaultValues;

public class BackendProfileUpdateTC extends BackendAutomation {

	String email;
	String password = "qwerty1";
	ProfileData defaultProfile;
	Logger logger = Util.setupLogger(BackendProfileUpdateTC.class);

	@BeforeClass(alwaysRun = true)
	public void setUp() {
		
		// sign up and create profile
		email = MVPApi.generateUniqueEmail();
		String token = MVPApi.signUp(email, password).token;
		defaultProfile = DefaultValues.DefaultProfile();
		MVPApi.createProfile(token, defaultProfile);
	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "profile" })
	public void UpdateProfileUsingValidToken() {
		
		// sign in and use valid token to update profile
		String token = MVPApi.signIn(email, password).token;
		defaultProfile = MVPApi.getProfile(token).profile;
		ProfileData data = new ProfileData();

		String name = "Dandelion" + System.nanoTime(); 
		data.setUpdatedAt(defaultProfile.getUpdatedAt() + 10);
		data.setName(name);
		data.setWeight(defaultProfile.getWeight() + 1);
		data.setDateOfBirth(defaultProfile.getDateOfBirth() + 10000);
		data.setGoalLevel(2);
		data.setDisplayedUnits(new DisplayUnit(1, 1, 1));

		ProfileResult r = MVPApi.updateProfile(token, data, defaultProfile.getServerId());
		r.printKeyPairsValue();

		Assert.assertTrue(r.isExisted(), "Status code is OK: 210");
		Assert.assertNotEquals(defaultProfile.getUpdatedAt(), r.profile.getUpdatedAt(), "UpdatedAt has changed");
		Assert.assertEquals(name, r.profile.getName(), "Name has changed");
		Assert.assertEquals(defaultProfile.getWeight() + 1, r.profile.getWeight(), "Weight has changed");
		Assert.assertEquals((Long)(defaultProfile.getDateOfBirth() + 10000), r.profile.getDateOfBirth(), "Birthday has changed");
		Assert.assertEquals((Integer)2, r.profile.getGoalLevel(), "GoalLevel has changed");
		Assert.assertEquals(r.profile.getDisplayedUnits().getHeightUnit(), 1, "HeightUnit has changed");
		Assert.assertEquals(r.profile.getDisplayedUnits().getWeightUnit(), 1, "WeightUnit has changed");
		Assert.assertEquals(r.profile.getDisplayedUnits().getTemperatureScale(), 1, "TemperatureScale has changed");
	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "profile" })
	public void UpdateProfileUsingExpiredToken() {
		
		// sign in and sign out then and use old token to update profile
		String token = MVPApi.signIn(email, password).token;
		defaultProfile = MVPApi.getProfile(token).profile;
		MVPApi.signOut(token);

		ProfileData data = new ProfileData();

		data.setUpdatedAt(defaultProfile.getUpdatedAt() + 10);
		data.setName("Dandelion" + System.nanoTime());
		data.setWeight(defaultProfile.getWeight() + 1);

		ProfileResult r = MVPApi.updateProfile(token, data, defaultProfile.getServerId());
		r.printKeyPairsValue();

		Assert.assertTrue(r.isAuthInvalid(), "Status code is 401");
	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "profile" })
	public void UpdateProfileWithoutToken() {
		// sign in and use arbitrary token to update profile

		String token = MVPApi.signIn(email, password).token;
		defaultProfile = MVPApi.getProfile(token).profile;
		MVPApi.signOut(token);

		ProfileData data = new ProfileData();

		data.setUpdatedAt(defaultProfile.getUpdatedAt() + 10);
		data.setName("Dandelion" + System.nanoTime());
		data.setWeight(defaultProfile.getWeight() + 1);

		ProfileResult r = MVPApi.updateProfile(DefaultValues.ArbitraryToken, data, defaultProfile.getServerId());
		r.printKeyPairsValue();

		Assert.assertTrue(r.isAuthInvalid(), "Status code is 401");
	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "profile" })
	public void UpdateProfileHadBeenUpdated() {
		
		// sign in and decrease updatedAt of profile then use that to update profile
		String token = MVPApi.signIn(email, password).token;
		defaultProfile = MVPApi.getProfile(token).profile;
		ProfileData data = new ProfileData();

		String name = "Dandelion" + System.nanoTime();
		data.setUpdatedAt(defaultProfile.getUpdatedAt() - 10);
		data.setName(name);
		data.setWeight(defaultProfile.getWeight() + 1);

		ProfileResult r = MVPApi.updateProfile(token, data, defaultProfile.getServerId());
		r.printKeyPairsValue();

		Assert.assertTrue(r.isExisted(), "Status code is OK: 210");
		Assert.assertNotEquals(defaultProfile.getUpdatedAt(), r.profile.getUpdatedAt(), "UpdatedAt has changed");
		Assert.assertEquals(name, r.profile.getName(), "Name has changed");
		Assert.assertEquals(defaultProfile.getWeight() + 1, r.profile.getWeight(), "Weight has changed");
		Assert.assertEquals(defaultProfile.getDateOfBirth(), r.profile.getDateOfBirth(), "Birthday has not changed");
	}
}
