package com.misfit.ta.backend.aut.correctness;

import org.testng.Assert;
import org.testng.annotations.*;

import com.misfit.ta.backend.data.profile.ProfileData;
import com.misfit.ta.backend.data.profile.ProfileResult;
import com.misfit.ta.backend.api.*;
import com.misfit.ta.backend.aut.BackendAutomation;
import com.misfit.ta.backend.aut.DefaultValues;

public class BackendProfileGetTC extends BackendAutomation {

	String email;
	String password = "qwerty1";
	String udid;
	ProfileData defaultProfile;

	@BeforeClass(alwaysRun = true)
	public void setUp() {

		// sign up and create profile
		email = MVPApi.generateUniqueEmail();
		defaultProfile = DefaultValues.DefaultProfile();
		String token = MVPApi.signUp(email, password, udid).token;
		MVPApi.createProfile(token, defaultProfile);
		udid = DefaultValues.UDID;

	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "profile" })
	public void GetProfileUseValidToken() {

		// sign in then use the token to get profile
		String token = MVPApi.signIn(email, password, udid).token;
		ProfileResult r = MVPApi.getProfile(token);
		r.printKeyPairsValue();

		Assert.assertTrue(r.isOK(), "Status code is 200");
		Assert.assertTrue(r.isEmptyErrMsg(), "Empty error message");
		Assert.assertEquals(r.profile.getDateOfBirth(), defaultProfile.getDateOfBirth(), "Profile birthday");
		Assert.assertEquals(r.profile.getWeight(), defaultProfile.getWeight(), "Profile weight");
	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "profile" })
	public void GetProfileUseExpiredToken() {

		// sign in then sign out then use the old token to get profile
		String token = MVPApi.signIn(email, password, udid).token;
		MVPApi.signOut(token);
		ProfileResult r = MVPApi.getProfile(token);
		r.printKeyPairsValue();

		Assert.assertTrue(r.isAuthInvalid(), "Status code is 401");
	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "profile" })
	public void GetProfileWithoutToken() {
		
		// sign in then use arbitrary token to get profile
		MVPApi.signIn(email, password, udid);
		ProfileResult r = MVPApi.getProfile(DefaultValues.ArbitraryToken);
		r.printKeyPairsValue();

		Assert.assertTrue(r.isAuthInvalid(), "Status code is 401");
	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "profile" })
	public void GetEmptyProfile() {
		
		// sign up then get profile without creating it
		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), password, udid).token;
		ProfileResult r = MVPApi.getProfile(token);
		r.printKeyPairsValue();

		Assert.assertTrue(r.isOK(), "Status code is 200");
		Assert.assertTrue(r.errorMessage == null || r.isEmptyErrMsg(), "Error message is empty");
		Assert.assertTrue(r.profile == null, "Profile is null");
	}

}
