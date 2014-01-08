package com.misfit.ta.backend.aut.correctness;

import org.testng.Assert;
import org.testng.annotations.*;

import com.misfit.ta.backend.data.DataGenerator;
import com.misfit.ta.backend.data.profile.ProfileData;
import com.misfit.ta.backend.data.profile.ProfileResult;
import com.misfit.ta.backend.api.*;
import com.misfit.ta.backend.aut.BackendAutomation;
import com.misfit.ta.utils.TextTool;

public class BackendProfileCreateTC extends BackendAutomation {

	String password = "qwerty1";
	ProfileData defaultProfile = DataGenerator.generateRandomProfile(System.currentTimeMillis() / 1000, null);

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "profile" })
	public void CreateNewProfile() {
		
		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), password).token;
		ProfileResult r = MVPApi.createProfile(token, defaultProfile);
		r.printKeyPairsValue();

		Assert.assertTrue(r.isOK(), "Status code is OK");
		Assert.assertTrue(r.profile.getServerId() != null, "Server Id is not null");
		Assert.assertEquals(r.profile.getLocalId(), defaultProfile.getLocalId(), "Local id is not null");

		ProfileResult g = MVPApi.getProfile(token);
		Assert.assertEquals(g.profile.getWeight(), defaultProfile.getWeight(), "Get weight is the same");
		Assert.assertEquals(g.profile.getHeight(), defaultProfile.getHeight(), "Get height is the same");
		Assert.assertEquals(g.profile.getDateOfBirth(), defaultProfile.getDateOfBirth(), "Get birthday is the same");
	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "profile" })
	public void CreateDuplicateProfile() {
		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), password).token;

		ProfileResult r1 = MVPApi.createProfile(token, defaultProfile);
		r1.printKeyPairsValue();
		ProfileResult r2 = MVPApi.createProfile(token, defaultProfile);
		r2.printKeyPairsValue();

		Assert.assertTrue(r2.isExisted(), "Status code is 210");
		Assert.assertEquals(r2.profile.getServerId(), r1.profile.getServerId(), "Server Id is the same");
		Assert.assertEquals(r2.profile.getLocalId(), r1.profile.getLocalId(), "Local id is the same");
		Assert.assertEquals(r2.profile.getUpdatedAt(), r1.profile.getUpdatedAt(), "Updated at is the same");

	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "profile" })
	public void CreateProfileWithoutToken() {
		ProfileResult r = MVPApi.createProfile("", defaultProfile);
		r.printKeyPairsValue();
		Assert.assertTrue(r.isAuthInvalid(), "Status code is 401");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "profile" })
	public void CreateNewProfileWithInvalidHandle() {

		ProfileData profile = DataGenerator.generateRandomProfile(System.currentTimeMillis() / 1000, null);		
		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), password).token;
		String handle = TextTool.getRandomString(3, 5);
		
		// short handle
		profile = DataGenerator.generateRandomProfile(System.currentTimeMillis() / 1000, null);
		profile.setHandle(handle);
		
		token = MVPApi.signUp(MVPApi.generateUniqueEmail(), password).token;
		ProfileResult r = MVPApi.createProfile(token, profile);
		r.printKeyPairsValue();
		
		// update profile in case of server allows "short" (to run regression test)
		profile.setHandle(TextTool.getRandomString(7, 10));
		MVPApi.updateProfile(token, profile);

		Assert.assertEquals(r.statusCode, 400, "Status code");
	}
	
}
