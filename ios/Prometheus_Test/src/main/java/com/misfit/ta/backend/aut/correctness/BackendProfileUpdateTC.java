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
		data.setGoalLevel(2);
		data.setDisplayedUnits(new DisplayUnit(1, 1, 1));

		ProfileResult r = MVPApi.updateProfile(token, data);
		r.printKeyPairsValue();

		Assert.assertTrue(r.isExisted(), "Status code is OK: 210");
		Assert.assertEquals(name, r.profile.getName(), "Name has changed");
		Assert.assertEquals(defaultProfile.getWeight() + 1, r.profile.getWeight(), "Weight has changed");
		Assert.assertEquals((Integer)2, r.profile.getGoalLevel(), "GoalLevel has changed");
		Assert.assertEquals(r.profile.getDisplayedUnits().getHeightUnit(), 1, "HeightUnit has changed");
		Assert.assertEquals(r.profile.getDisplayedUnits().getWeightUnit(), 1, "WeightUnit has changed");
		Assert.assertEquals(r.profile.getDisplayedUnits().getTemperatureScale(), 1, "TemperatureScale has changed");
		
		// test cache by getting server profile again and see if server return updated version
		int count = 0;
		for(int i = 0; i < MVPApi.CACHE_TRY_TIME; i++) {
			
			ProfileResult r2 = MVPApi.getProfile(token);
			ProfileData p = r2.profile;
			r2.printKeyPairsValue();
			
			if(p == null) {
				count++;
				continue;
			}
			
			if(!p.getWeight().equals(data.getWeight()))
				count++;
		}
		
		Assert.assertEquals(count, 0, "Fail count");
	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "profile" })
	public void UpdateNonExistedProfile() {
		
		// sign up and update profile without creating it
		ProfileData data = DefaultValues.DefaultProfile();
		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), password).token;
		ProfileResult r = MVPApi.updateProfile(token, data);
		
		Assert.assertEquals(r.statusCode, 400, "Status code");
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

		ProfileResult r = MVPApi.updateProfile(token, data);
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

		ProfileResult r = MVPApi.updateProfile(DefaultValues.ArbitraryToken, data);
		r.printKeyPairsValue();

		Assert.assertTrue(r.isAuthInvalid(), "Status code is 401");
	}

}
