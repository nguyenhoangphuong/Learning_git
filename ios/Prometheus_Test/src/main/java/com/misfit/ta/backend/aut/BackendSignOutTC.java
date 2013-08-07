package com.misfit.ta.backend.aut;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.graphwalker.Util;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.backend.data.ProfileData;
import com.misfit.ta.backend.data.ProfileResult;

public class BackendSignOutTC extends BackendAutomation {
	String email;
	String password = "qwerty1";
	String udid;
	Logger logger = Util.setupLogger(BackendSignOutTC.class);

	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		udid = DefaultValues.UDID;
		email = MVPApi.generateUniqueEmail();
		String token = MVPApi.signUp(email, password, udid).token;
		MVPApi.createProfile(token, DefaultValues.DefaultProfile());
	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "signout" })
	public void SignOutValidToken() {
		// sign in then use the token to sign out

		String token = MVPApi.signIn(email, password, udid).token;
		BaseResult r = MVPApi.signOut(token);
		r.printKeyPairsValue();

		Assert.assertTrue("Status code is 200", r.isOK());
	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "signout" })
	public void SignOutExpiredToken() {
		// sign in then sign out
		// sign in again and use old token from 1st sign in to sign out
		// status code should be 200 since it is a sign out
		String token1 = MVPApi.signIn(email, password, udid).token;
		logger.info("Token 1: " + token1);
		MVPApi.signOut(token1);

		String token2 = MVPApi.signIn(email, password, udid).token;
		logger.info("Token 2: " + token2);

		BaseResult br = MVPApi.signOut(token1);
		br.printKeyPairsValue();

		Assert.assertTrue("Status code is 200 since this is sign out",
				br.statusCode == 200);
		Assert.assertEquals("Null error message since status code is 200",
				br.errorMessage, null);

		// then we try to update and the update should go successfully
		// there might be 210 code returned since we are not following
		// the right protocol. But it is something we can ignore
		ProfileData profile = MVPApi.getProfile(token2).profile;

		profile.updatedAt += 10;
		profile.name = "Dandelion" + System.nanoTime();
		profile.weight += 1;

		ProfileResult pr = MVPApi.updateProfile(token2, profile,
				profile.serverId);
		pr.printKeyPairsValue();

		Assert.assertTrue("Status code: 210 - Force client update (ignorable)",
				pr.statusCode == 210);
	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "signout" })
	public void SignOutInvalidToken() {
		// sign out with arbitrary with wrong format token

		MVPApi.signIn(email, password, udid);
		BaseResult r = MVPApi
				.signOut("12312578919836435123125-wrongformatorarbitrarytoken");
		r.printKeyPairsValue();

		Assert.assertTrue("Status code is 200", r.statusCode == 200);
		Assert.assertEquals("Null error message since status code is 200",
				r.errorMessage, null);
	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "signout" })
	public void SignOutOnTwoDevices() {
		// sign in on two devices (two udid) then sign out each devices
		// two sign out must be success

		String token1 = MVPApi.signIn(email, password, udid).token;
		String token2 = MVPApi.signIn(email, password, DefaultValues.UDID2).token;

		// sign out on 1st device
		BaseResult r1 = MVPApi.signOut(token1);
		r1.printKeyPairsValue();

		Assert.assertTrue("Status code is 200", r1.isOK());

		// sign out on 2nd device
		BaseResult r2 = MVPApi.signOut(token2);
		r2.printKeyPairsValue();

		Assert.assertTrue("Status code is 200", r2.isOK());
	}

}
