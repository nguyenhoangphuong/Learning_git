package com.misfit.ta.backend.aut;

import junit.framework.Assert;

import org.testng.annotations.*;

import com.misfit.ta.android.AutomationTest;
import com.misfit.ta.backend.data.*;
import com.misfit.ta.backend.api.*;

public class BackendProfileCreateTC extends AutomationTest
{

	String password = "qwerty1";
	String udid = DefaultValues.UDID;
	ProfileResult.ProfileData defaultProfile = DefaultValues.DefaultProfile();
	
	
	@Test(groups = { "ios", "Prometheus", "api", "profile" })
	public void CreateNewProfile()
	{
		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), password, udid).token;
		ProfileResult r = MVPApi.createProfile(token, defaultProfile);
		r.printKeyPairsValue();
		
		Assert.assertTrue("Status code is OK", r.statusCode == 200);
		Assert.assertTrue("Server Id is not null", r.profile.serverId != null);
		Assert.assertEquals("Local id is not null", r.profile.localId ,defaultProfile.localId);
		
		ProfileResult g = MVPApi.getProfile(token);
		Assert.assertEquals("Get name is the same", g.profile.name, defaultProfile.name);
		Assert.assertEquals("Get weight is the same", g.profile.weight, defaultProfile.weight);
		Assert.assertEquals("Get height is the same", g.profile.height, defaultProfile.height);
		Assert.assertEquals("Get birthday is the same", g.profile.dateOfBirth, defaultProfile.dateOfBirth);
	}

	@Test(groups = { "ios", "Prometheus", "api", "profile" })
	public void CreateDuplicateProfile()
	{
		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), password, udid).token;
		
		ProfileResult r1 = MVPApi.createProfile(token, defaultProfile);
		r1.printKeyPairsValue();
		ProfileResult r2 =  MVPApi.createProfile(token, defaultProfile);
		r2.printKeyPairsValue();
		
		Assert.assertTrue("Status code is 210", r2.statusCode == 210);
		Assert.assertEquals("Server Id is the same", r2.profile.serverId, r1.profile.serverId);
		Assert.assertEquals("Local id is the same", r2.profile.localId, r1.profile.localId);
		Assert.assertEquals("Updated at is the same", r2.profile.updatedAt, r1.profile.updatedAt);

	}
	
	@Test(groups = { "ios", "Prometheus", "api", "profile" })
	public void CreateProfileWithoutToken()
	{
		ProfileResult r = MVPApi.createProfile("", defaultProfile);
		r.printKeyPairsValue();
		
		Assert.assertTrue("Status code is 401", r.statusCode == 401);
		Assert.assertTrue("Profile is null", r.profile == null);
	}

}
