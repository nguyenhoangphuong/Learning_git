package com.misfit.ta.backend.aut.correctness.backendapi.onthefly;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.misfit.ta.backend.api.metawatch.MVPApi;
import com.misfit.ta.backend.aut.BackendAutomation;
import com.misfit.ta.backend.aut.DefaultValues;
import com.misfit.ta.backend.data.BaseParams;
import com.misfit.ta.backend.data.BaseResult;

public class BackendRegisterOTF extends BackendAutomation {

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "OTF", "signup" })
	public void RegisterInsufficientParams() {
		
		// no password param
		BaseParams params1 = new BaseParams();
		params1.addParam("email", MVPApi.generateUniqueEmail());
		
		BaseResult r1 = MVPApi.customRequest("signup", MVPApi.HTTP_POST, params1);
		Assert.assertEquals(r1.statusCode, 400, "Status code");
		Assert.assertEquals(r1.errorMessage, DefaultValues.InvalidPassword);
		
		r1 = MVPApi.customRequest("login", MVPApi.HTTP_POST, params1);
		Assert.assertEquals(r1.statusCode, 400, "Status code");
		Assert.assertEquals(r1.errorMessage, DefaultValues.InvalidPassword);
		
		// no email param
		BaseParams params2 = new BaseParams();
		params2.addParam("password", "qqqqqqq");
		
		BaseResult r2 = MVPApi.customRequest("signup", MVPApi.HTTP_POST, params2);
		Assert.assertEquals(r2.statusCode, 400, "Status code");
		Assert.assertEquals(r2.errorMessage, DefaultValues.InvalidEmail);
		
		r2 = MVPApi.customRequest("login", MVPApi.HTTP_POST, params2);
		Assert.assertEquals(r2.statusCode, 400, "Status code");
		Assert.assertEquals(r2.errorMessage, DefaultValues.InvalidEmail);
	}
	
}
