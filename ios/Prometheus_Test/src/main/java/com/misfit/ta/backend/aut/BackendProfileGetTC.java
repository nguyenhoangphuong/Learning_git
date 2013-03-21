package com.misfit.ta.backend.aut;

import junit.framework.Assert;

import org.testng.annotations.*;

import com.misfit.ta.backend.data.*;
import com.misfit.ta.backend.api.*;
import com.misfit.ta.ios.AutomationTest;

public class BackendProfileGetTC extends AutomationTest
{

	String email;
	String password = "qwerty1";
	String udid;
	ProfileData defaultProfile;
	
	@BeforeClass
	public void setUp()
	{
	    email = MVPApi.generateUniqueEmail();
	    defaultProfile = DefaultValues.DefaultProfile();
		String token = MVPApi.signUp(email, password, udid).token;
		MVPApi.createProfile(token, defaultProfile);
		udid = DefaultValues.UDID;
		
	}
	
	@Test(groups = { "ios", "ABC", "api", "profile" })
	public void GetProfileUseValidToken()
	{
		// sign in then use the token to get profile
		
		String token = MVPApi.signIn(email, password, udid).token;
		ProfileResult r = MVPApi.getProfile(token);
		r.printKeyPairsValue();
		
		Assert.assertTrue("Status code is 200", r.isOK());
		Assert.assertTrue("Empty error message", r.isEmptyErrMsg());
		Assert.assertEquals("Profile name", r.profile.name, defaultProfile.name);
		Assert.assertEquals("Profile birthday", r.profile.dateOfBirth, defaultProfile.dateOfBirth);
		Assert.assertEquals("Profile weight", r.profile.weight, defaultProfile.weight);
	}

	@Test(groups = { "ios", "ABC", "api", "profile" })
	public void GetProfileUseExpiredToken()
	{
		// sign in then sign out then use the old token to get profile
		
		String token = MVPApi.signIn(email, password, udid).token;
		MVPApi.signOut(token);
		
		ProfileResult r = MVPApi.getProfile(token);
		r.printKeyPairsValue();
		
		Assert.assertTrue("Status code is not 200", !r.isOK());
		Assert.assertTrue("Status code is 401", r.statusCode == 401);
		Assert.assertTrue("Profile is null", r.profile == null);
	}
	
	@Test(groups = { "ios", "ABC", "api", "profile" })
	public void GetProfileWithoutToken()
	{
		// sign in then use arbitrary token to get profile
		
		MVPApi.signIn(email, password, udid);		
		ProfileResult r = MVPApi.getProfile(DefaultValues.ArbitraryToken);
		r.printKeyPairsValue();
		
		Assert.assertTrue("Status code is not 200", !r.isOK());
		Assert.assertTrue("Status code is 401", r.statusCode == 401);
		Assert.assertTrue("Profile is null", r.profile == null);
	}
	
	@Test(groups = { "ios", "ABC", "api", "profile" })
	public void GetEmptyProfile()
	{
		// sign up then get profile without creating it
		
		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), password, udid).token;		
		ProfileResult r = MVPApi.getProfile(token);
		r.printKeyPairsValue();
		
		Assert.assertTrue("Status code is 200", r.isOK());
		Assert.assertTrue("Error message is empty", r.isEmptyErrMsg());
		Assert.assertTrue("Profile is null", r.profile == null);
	}
	
}
