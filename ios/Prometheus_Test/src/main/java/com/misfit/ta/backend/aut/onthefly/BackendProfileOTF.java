package com.misfit.ta.backend.aut.onthefly;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.backend.aut.BackendAutomation;
import com.misfit.ta.backend.aut.DefaultValues;
import com.misfit.ta.backend.data.BaseParams;
import com.misfit.ta.backend.data.BaseResult;

public class BackendProfileOTF extends BackendAutomation {

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "OTF", "profile" })
	public void CreateProfileInsufficientParams() {
		
		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), "qqqqqq").token;
		BaseParams params = new BaseParams();
		params.addHeader("auth_token", token);
		
		BaseResult r = MVPApi.customRequest("profile", MVPApi.HTTP_POST, params);
		Assert.assertEquals(r.statusCode, 400, "Status code");

	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "OTF", "profile" })
	public void CreateProfileNullParams() {
		
		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), "qqqqqq").token;
		BaseParams params = new BaseParams();
		params.addHeader("auth_token", token);
		params.addParam("profile", null);
		
		BaseResult r = MVPApi.customRequest("profile", MVPApi.HTTP_POST, params);
		Assert.assertEquals(r.statusCode, 400, "Status code");

	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "OTF", "profile" })
	public void UpdateProfileInsufficientParams() {
		
		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), "qqqqqq").token;
		MVPApi.createProfile(token, DefaultValues.DefaultProfile());
		BaseParams params = new BaseParams();
		params.addHeader("auth_token", token);
		
		BaseResult r = MVPApi.customRequest("profile", MVPApi.HTTP_PUT, params);
		Assert.assertEquals(r.statusCode, 400, "Status code");

	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "OTF", "profile" })
	public void UpdateProfileNullParams() {
		
		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), "qqqqqq").token;
		MVPApi.createProfile(token, DefaultValues.DefaultProfile());
		BaseParams params = new BaseParams();
		params.addHeader("auth_token", token);
		params.addParam("profile", null);
		
		BaseResult r = MVPApi.customRequest("profile", MVPApi.HTTP_PUT, params);
		Assert.assertEquals(r.statusCode, 400, "Status code");
	}

}
