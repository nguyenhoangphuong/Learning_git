package com.misfit.ta.backend.aut.correctness.backendapi.onthefly;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.backend.aut.BackendAutomation;
import com.misfit.ta.backend.aut.DefaultValues;
import com.misfit.ta.backend.data.BaseParams;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.backend.data.pedometer.Pedometer;

public class BackendPedometerOTF extends BackendAutomation {

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "OTF", "pedometer" })
	public void CreatePedometerInsufficientParams() {
		
		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), "qqqqqq").token;
		BaseParams params = new BaseParams();
		params.addHeader("auth_token", token);
		
		BaseResult r = MVPApi.customRequest("pedometer", MVPApi.HTTP_POST, params);
		Assert.assertEquals(r.statusCode, 400, "Status code");
	}
		
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "OTF", "pedometer" })
	public void CreatePedometerWithNullSerialNumber() {
		
		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), "qqqqqq").token;
		Pedometer pedo = DefaultValues.RandomPedometer();
		pedo.setSerialNumberString(null);
		
		BaseParams params = new BaseParams();
		params.addHeader("auth_token", token);
		params.addParam("pedometer", pedo.toJsonIncludeNull().toString());
		
		BaseResult r = MVPApi.customRequest("pedometer", MVPApi.HTTP_POST, params);
		Assert.assertEquals(r.statusCode, 400, "Status code");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "OTF", "pedometer" })
	public void UpdatePedometerInsufficientParams() {
		
		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), "qqqqqq").token;
		MVPApi.createPedometer(token, DefaultValues.RandomPedometer());
		BaseParams params = new BaseParams();
		params.addHeader("auth_token", token);
		
		BaseResult r = MVPApi.customRequest("pedometer", MVPApi.HTTP_PUT, params);
		Assert.assertEquals(r.statusCode, 400, "Status code");

	}
	
}
