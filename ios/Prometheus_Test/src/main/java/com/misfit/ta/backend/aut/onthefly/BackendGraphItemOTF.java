package com.misfit.ta.backend.aut.onthefly;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.backend.aut.BackendAutomation;
import com.misfit.ta.backend.data.BaseParams;
import com.misfit.ta.backend.data.BaseResult;

public class BackendGraphItemOTF extends BackendAutomation {

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "OTF", "graph_items" })
	public void CreateGraphItemsInsufficientParams() {
		
		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), "qqqqqq").token;
		
		BaseParams params = new BaseParams();
		params.addHeader("auth_token", token);
		
		// batch
		BaseResult r = MVPApi.customRequest("graph_items/batch_insert", MVPApi.HTTP_POST, params);
		Assert.assertEquals(r.statusCode, 400, "Status code");
		
		// single
		r = MVPApi.customRequest("graph_items", MVPApi.HTTP_POST, params);
		Assert.assertEquals(r.statusCode, 400, "Status code");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "OTF", "graph_items" })
	public void CreateGraphItemsNullParams() {
		
		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), "qqqqqq").token;
		
		// batch 
		BaseParams params = new BaseParams();
		params.addHeader("auth_token", token);
		params.addParam("graph_items", null);
		
		BaseResult r = MVPApi.customRequest("graph_items", MVPApi.HTTP_POST, params);
		Assert.assertEquals(r.statusCode, 400, "Status code");
		
		r = MVPApi.customRequest("graph_items", MVPApi.HTTP_POST, params);
		Assert.assertEquals(r.statusCode, 400, "Status code");

		// single
		params = new BaseParams();
		params.addHeader("auth_token", token);
		params.addParam("goal", null);
		
		r = MVPApi.customRequest("graph_item", MVPApi.HTTP_POST, params);
		Assert.assertEquals(r.statusCode, 400, "Status code");

	}
}
