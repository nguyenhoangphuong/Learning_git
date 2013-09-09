package com.misfit.ta.backend.aut.onthefly;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.backend.aut.BackendAutomation;
import com.misfit.ta.backend.aut.DefaultValues;
import com.misfit.ta.backend.data.BaseParams;
import com.misfit.ta.backend.data.BaseResult;

public class BackendStatisticsOTF extends BackendAutomation {
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "OTF", "statistics" })
	public void CreateStatisticsInsufficientParams() {
		
		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), "qqqqqq").token;
		BaseParams params = new BaseParams();
		params.addHeader("auth_token", token);
		
		BaseResult r = MVPApi.customRequest("statistics", MVPApi.HTTP_POST, params);
		Assert.assertEquals(r.statusCode, 400, "Status code");

	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "OTF", "statistics" })
	public void UpdateStatisticsInsufficientParams() {
		
		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), "qqqqqq").token;
		MVPApi.createStatistics(token, DefaultValues.RandomStatistic());
		BaseParams params = new BaseParams();
		params.addHeader("auth_token", token);
		
		BaseResult r = MVPApi.customRequest("statistics", MVPApi.HTTP_PUT, params);
		Assert.assertEquals(r.statusCode, 400, "Status code");

	}

}
