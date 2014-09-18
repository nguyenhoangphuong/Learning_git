package com.misfit.ta.backend.aut.correctness.backendapi;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.resting.component.impl.ServiceResponse;
import com.misfit.ta.backend.api.internalapi.MVPApi;
import com.misfit.ta.backend.aut.BackendAutomation;
import com.misfit.ta.backend.aut.DefaultValues;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.backend.data.graph.GraphItem;
import com.misfit.ta.common.MVPCommon;

public class BackendGraphItemGetTC extends BackendAutomation {

	String defaultEmail = MVPApi.generateUniqueEmail();
	String defaultPassword = "qwerty1";
	
	@BeforeClass(alwaysRun = true)
	public void setUp() {
		// sign up and create graph items
		String token = MVPApi.signUp(defaultEmail, defaultPassword).token;
		for(int i = 0; i < 10; i++)
			MVPApi.createGraphItem(token, DefaultValues.RandomGraphItem(2020 * i));
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "testmanual" })
	public void getGraphItemAfterMigration(){
		ServiceResponse serviceResponseResult = MVPApi.getMultipleGraphItems("");
		List<GraphItem> listGraphItemResult = GraphItem.getListGraphItem(serviceResponseResult);
		List<String> listValueResult = new ArrayList<String>();

		for(int i = 0; i < listGraphItemResult.size(); i++){
			listValueResult.add(String.valueOf(listGraphItemResult.get(i).getAverageValue()));
		}
		
		final String md5HexResult = DigestUtils.md5Hex(listValueResult.toString());
		System.out.println("md5HexResult : " + md5HexResult);
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "graph_item" })
	public void createGraphItemsBeforeMigration(){
		String email = MVPApi.generateUniqueEmail();
		String password = "qwerty1";
		String token = MVPApi.signUp(email, password).token;
		
		List<GraphItem> listGraphItem = new ArrayList<GraphItem>();
		List<String> listValue = new ArrayList<String>();
		for(int i = 0; i < 40; i++){
			GraphItem graphItem = DefaultValues.RandomGraphItem(2020 * i);
			graphItem.setAverageValue(i * 1.0);
			listGraphItem.add(graphItem);
			listValue.add(String.valueOf(i*1.0));
		}
		
		ServiceResponse serviceResponse = MVPApi.createGraphItems(token, listGraphItem);
		Assert.assertEquals(serviceResponse.getStatusCode(), 200, "Can't not create multiple graph items for user");
		final String md5HexBeforeMigration = DigestUtils.md5Hex(listValue.toString());
		System.out.println("md5HexBeforeMigration : " + md5HexBeforeMigration);
	}

	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "graph_item" })
	public void SearchGraphItems() {
		String token = MVPApi.signIn(defaultEmail, defaultPassword).token;
		
		// search 1
		List<GraphItem> items = MVPApi.getGraphItems(token, 0l, (long)Integer.MAX_VALUE, 0l);
		Assert.assertEquals(items.size(), 5, "Found 5 items");
		
		// search 2
		items = MVPApi.getGraphItems(token, MVPCommon.getDayStartEpoch(), MVPCommon.getDayStartEpoch() + 2020 * 2, 0l);
		Assert.assertEquals(items.size(), 3, "Found 3 items");
		
		// search 3
		items = MVPApi.getGraphItems(token, MVPCommon.getDayStartEpoch() + 2020 * 3 + 1, MVPCommon.getDayStartEpoch() + 2020 * 4, 0l);
		Assert.assertEquals(items.size(), 1, "Found 1 item");
		
		// search 4
		items = MVPApi.getGraphItems(token, MVPCommon.getDayStartEpoch() + 2020 * 5, (long)Integer.MAX_VALUE, 0l);
		Assert.assertEquals(items.size(), 0, "Found 0 items");
		
		// search 5
		items = MVPApi.getGraphItems(token, MVPCommon.getDayStartEpoch(), MVPCommon.getDayStartEpoch() - 1000, 0l);
		Assert.assertEquals(items.size(), 0, "Found 0 items");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "graph_item" })
	public void GetGraphItem() {
		
		GraphItem src = DefaultValues.RandomGraphItem(2020 * 6);
		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), defaultPassword).token;
		BaseResult r = MVPApi.createGraphItem(token, src);
		GraphItem ritem = GraphItem.getGraphItem(r.response);
		
		GraphItem getitem = MVPApi.getGraphItem(token, ritem.getServerId());
		Assert.assertNotNull(getitem, "Graph item is not null");
		Assert.assertEquals(getitem.getAverageValue(), src.getAverageValue(), "Average value is the same");
	}
	
}
