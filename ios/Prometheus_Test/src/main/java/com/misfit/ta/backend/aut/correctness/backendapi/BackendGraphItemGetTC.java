package com.misfit.ta.backend.aut.correctness.backendapi;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.misfit.ta.backend.api.internalapi.MVPApi;
import com.misfit.ta.backend.aut.BackendAutomation;
import com.misfit.ta.backend.aut.DefaultValues;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.backend.data.graph.GraphItem;

public class BackendGraphItemGetTC extends BackendAutomation {

	String email = MVPApi.generateUniqueEmail();
	String password = "qwerty1";
		
	@BeforeClass(alwaysRun = true)
	public void setUp() {
		// sign up and create graph items
		String token = MVPApi.signUp(email, password).token;
		for(int i = 0; i < 5; i++)
			MVPApi.createGraphItem(token, DefaultValues.RandomGraphItem(2020 * i));
	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "graph_item" })
	public void SearchGraphItems() {
		String token = MVPApi.signIn(email, password).token;
		
		// search 1
		List<GraphItem> items = MVPApi.getGraphItems(token, 0l, (long)Integer.MAX_VALUE, 0l);
		Assert.assertEquals(items.size(), 5, "Found 5 items");
		
		// search 2
		items = MVPApi.getGraphItems(token, MVPApi.getDayStartEpoch(), MVPApi.getDayStartEpoch() + 2020 * 2, 0l);
		Assert.assertEquals(items.size(), 3, "Found 3 items");
		
		// search 3
		items = MVPApi.getGraphItems(token, MVPApi.getDayStartEpoch() + 2020 * 3 + 1, MVPApi.getDayStartEpoch() + 2020 * 4, 0l);
		Assert.assertEquals(items.size(), 1, "Found 1 item");
		
		// search 4
		items = MVPApi.getGraphItems(token, MVPApi.getDayStartEpoch() + 2020 * 5, (long)Integer.MAX_VALUE, 0l);
		Assert.assertEquals(items.size(), 0, "Found 0 items");
		
		// search 5
		items = MVPApi.getGraphItems(token, MVPApi.getDayStartEpoch(), MVPApi.getDayStartEpoch() - 1000, 0l);
		Assert.assertEquals(items.size(), 0, "Found 0 items");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "graph_item" })
	public void GetGraphItem() {
		
		GraphItem src = DefaultValues.RandomGraphItem(2020 * 6);
		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), password).token;
		BaseResult r = MVPApi.createGraphItem(token, src);
		GraphItem ritem = GraphItem.getGraphItem(r.response);
		
		GraphItem getitem = MVPApi.getGraphItem(token, ritem.getServerId());
		Assert.assertNotNull(getitem, "Graph item is not null");
		Assert.assertEquals(getitem.getAverageValue(), src.getAverageValue(), "Average value is the same");
	}
	
}
