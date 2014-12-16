package com.misfit.ta.backend.aut.correctness.backendapi.onthefly;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.google.resting.json.JSONArray;
import com.google.resting.json.JSONException;
import com.google.resting.json.JSONObject;
import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.backend.aut.BackendAutomation;
import com.misfit.ta.backend.aut.DefaultValues;
import com.misfit.ta.backend.data.BaseParams;
import com.misfit.ta.backend.data.BaseResult;

public class BackendTimelineItemOTF extends BackendAutomation {

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "OTF", "timeline_items" })
	public void CreateTimelineItemsInsufficientParams() {
		
		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), "qqqqqq").token;
		
		BaseParams params = new BaseParams();
		params.addHeader("auth_token", token);
		
		// batch
		BaseResult r = MVPApi.customRequest("timeline_items/batch_insert", MVPApi.HTTP_POST, params);
		Assert.assertEquals(r.statusCode, 400, "Status code");
		
		// single
		r = MVPApi.customRequest("timeline_items", MVPApi.HTTP_POST, params);
		Assert.assertEquals(r.statusCode, 400, "Status code");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "OTF", "timeline_items" })
	public void CreateTimelineItemsInvalidParams() {
		
		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), "qqqqqq").token;
		
		// batch: don't user array object
		BaseParams params = new BaseParams();
		params.addHeader("auth_token", token);
		params.addParam("timeline_items", "This is not an array");
		
		BaseResult r = MVPApi.customRequest("timeline_items/batch_insert", MVPApi.HTTP_POST, params);
		Assert.assertEquals(r.statusCode, 400, "Status code");
		
		// single: don't use hash
		params = new BaseParams();
		params.addHeader("auth_token", token);
		params.addParam("timeline_item", "This is not an object");
		
		r = MVPApi.customRequest("graph_items", MVPApi.HTTP_POST, params);
		Assert.assertEquals(r.statusCode, 400, "Status code");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "OTF", "timeline_items" })
	public void CreateTimelineItemsInvalidData() throws JSONException {
		
		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), "qqqqqq").token;
		
		// use string for average value field
		JSONObject json = DefaultValues.RandomTimelineItem().toJson();
		json.put("itemType", "should be a number");
		
		JSONArray arr = new JSONArray();
		arr.put(json);
		
		// batch
		BaseParams params = new BaseParams();
		params.addHeader("auth_token", token);
		params.addParam("timeline_items", arr.toString());
		
		BaseResult r = MVPApi.customRequest("timeline_items/batch_insert", MVPApi.HTTP_POST, params);
		Assert.assertEquals(r.statusCode, 400, "Status code");
		
		// single
		params = new BaseParams();
		params.addHeader("auth_token", token);
		params.addParam("timeline_item", json.toString());
		
		r = MVPApi.customRequest("timeline_items", MVPApi.HTTP_POST, params);
		Assert.assertEquals(r.statusCode, 400, "Status code");
	}
	
}
