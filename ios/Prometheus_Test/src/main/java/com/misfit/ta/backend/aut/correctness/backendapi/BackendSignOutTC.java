package com.misfit.ta.backend.aut.correctness.backendapi;

import org.apache.log4j.Logger;
import org.graphwalker.Util;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.misfit.ta.backend.api.internalapi.MVPApi;
import com.misfit.ta.backend.aut.BackendAutomation;
import com.misfit.ta.backend.aut.DefaultValues;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.backend.data.profile.ProfileData;
import com.misfit.ta.backend.data.profile.ProfileResult;

public class BackendSignOutTC extends BackendAutomation {
	
	String email;
	String password = "qwerty1";
	Logger logger = Util.setupLogger(BackendSignOutTC.class);

	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		email = MVPApi.generateUniqueEmail();
		String token = MVPApi.signUp(email, password).token;
		MVPApi.createProfile(token, DefaultValues.DefaultProfile());
	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "signout" })
	public void SignOutValidToken() {
		// sign in then use the token to sign out

		String token = MVPApi.signIn(email, password).token;
		BaseResult r = MVPApi.signOut(token);
		r.printKeyPairsValue();

		Assert.assertTrue(r.isOK(), "Status code is 200");
	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "signout" })
	public void SignOutExpiredToken() {
		
		// sign in then sign out
		// sign in again and use old token from 1st sign in to sign out
		// status code should be 200 since it is a sign out
		String token1 = MVPApi.signIn(email, password).token;
		logger.info("Token 1: " + token1);
		MVPApi.signOut(token1);

		String token2 = MVPApi.signIn(email, password).token;
		logger.info("Token 2: " + token2);

		BaseResult br = MVPApi.signOut(token1);
		br.printKeyPairsValue();

		Assert.assertTrue(br.statusCode == 200, "Status code is 200 since this is sign out");
		Assert.assertEquals(br.errorMessage, null, "Null error message since status code is 200");

		// then we try to update and the update should go successfully
		// there might be 210 code returned since we are not following
		// the right protocol. But it is something we can ignore
		ProfileData profile = MVPApi.getProfile(token2).profile;

		profile.setUpdatedAt(profile.getUpdatedAt() + 10);
		profile.setName("Dandelion" + System.nanoTime());
		profile.setWeight(profile.getWeight() + 1);

		ProfileResult pr = MVPApi.updateProfile(token2, profile);
		pr.printKeyPairsValue();

		Assert.assertTrue(pr.statusCode == 210, "Status code: 210 - Force client update (ignorable)");
	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "signout" })
	public void SignOutInvalidToken() {
		
		// sign out with arbitrary with wrong format token
		MVPApi.signIn(email, password);
		BaseResult r = MVPApi.signOut(DefaultValues.ArbitraryToken);
		r.printKeyPairsValue();

		Assert.assertTrue(r.statusCode == 200, "Status code is 200");
		Assert.assertEquals(r.errorMessage, null, "Null error message since status code is 200");
	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "signout" })
	public void SignOutOnTwoDevices() {
		// sign in on two devices (two udid) then sign out each devices
		// two sign out must be success

		String token1 = MVPApi.signIn(email, password).token;
		String token2 = MVPApi.signIn(email, password).token;

		// sign out on 1st device
		BaseResult r1 = MVPApi.signOut(token1);
		r1.printKeyPairsValue();

		Assert.assertTrue(r1.isOK(), "Status code is 200");

		// sign out on 2nd device
		BaseResult r2 = MVPApi.signOut(token2);
		r2.printKeyPairsValue();

		Assert.assertTrue(r2.isOK(), "Status code is 200");
	}

}
