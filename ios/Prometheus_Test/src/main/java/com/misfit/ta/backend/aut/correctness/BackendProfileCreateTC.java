package com.misfit.ta.backend.aut.correctness;

import org.testng.Assert;
import org.testng.annotations.*;

import com.misfit.ta.backend.data.profile.ProfileData;
import com.misfit.ta.backend.data.profile.ProfileResult;
import com.misfit.ta.backend.api.*;
import com.misfit.ta.backend.aut.BackendAutomation;
import com.misfit.ta.backend.aut.DefaultValues;

public class BackendProfileCreateTC extends BackendAutomation {

	String password = "qwerty1";
	ProfileData defaultProfile = DefaultValues.DefaultProfile();

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

}
