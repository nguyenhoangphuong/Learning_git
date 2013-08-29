package com.misfit.ta.backend.aut.correctness;

import java.util.List;
import java.util.ArrayList;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.google.resting.component.impl.ServiceResponse;
import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.backend.aut.BackendAutomation;
import com.misfit.ta.backend.aut.DefaultValues;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.backend.data.graph.GraphItem;

public class BackendGraphItemCreateTC extends BackendAutomation {
	
	String password = "qwerty1";

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "graph_item" })
	public void CreateNewGraphItem() {
		
		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), password).token;
		BaseResult r = MVPApi.createGraphItem(token, DefaultValues.RandomGraphItem());
		GraphItem item = GraphItem.getGraphItem(r.response);

		Assert.assertTrue(r.isOK(), "Status code is 200");
		Assert.assertTrue(item.getServerId() != null, "Server Id is not null");
	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "graph_item" })
	public void CreateDuplicateGraphItem() {
		
		// duplicate item by timestamp
		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), password).token;
		BaseResult r1 = MVPApi.createGraphItem(token, DefaultValues.RandomGraphItem(0));
		BaseResult r2 = MVPApi.createGraphItem(token, DefaultValues.RandomGraphItem(0));
		GraphItem g1 = GraphItem.getGraphItem(r1.response);
		GraphItem g2 = GraphItem.getGraphItem(r2.response);

		Assert.assertTrue(r2.isExisted(), "Status code is 210");
		Assert.assertEquals(g1.getServerId(), g2.getServerId(), "Server Id is the same");
		Assert.assertEquals(g1.getLocalId(), g2.getLocalId(), "Local id is the same");
		Assert.assertEquals(g1.getUpdatedAt(), g2.getUpdatedAt(), "Updated at is the same");
		
		// duplicate item by client id
		GraphItem g3 = DefaultValues.RandomGraphItem(2020 * 1);
		GraphItem g4 = DefaultValues.RandomGraphItem(2020 * 2);
		g4.setLocalId(g3.getLocalId());
		
		BaseResult r3 = MVPApi.createGraphItem(token, g3);
		BaseResult r4 = MVPApi.createGraphItem(token, g4);
		GraphItem g5 = GraphItem.getGraphItem(r3.response);
		GraphItem g6 = GraphItem.getGraphItem(r4.response);

		Assert.assertTrue(r4.isExisted(), "Status code is 210");
		Assert.assertEquals(g5.getServerId(), g6.getServerId(), "Server Id is the same");
		Assert.assertEquals(g5.getLocalId(), g6.getLocalId(), "Local id is the same");
		Assert.assertEquals(g5.getUpdatedAt(), g6.getUpdatedAt(), "Updated at is the same");

	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "graph_item" })
	public void CreateGraphItemsBatch() {
		
		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), password).token;
		List<GraphItem> items = new ArrayList<GraphItem>();
		for(int i = 0; i < 5; i++) {
			GraphItem item = DefaultValues.RandomGraphItem(2020 * i);
			items.add(item);
		}
		
		ServiceResponse r = MVPApi.createGraphItems(token, items);
		List<GraphItem> ritems = GraphItem.getGraphItems(r);
		
		Assert.assertEquals(r.getStatusCode(), 200, "Status code is 200");
		Assert.assertEquals(ritems.size(), items.size(), "Size of return items is the same");
		for(GraphItem ritem : ritems) {
			Assert.assertNotNull(ritem.getServerId(), "ServerId is not null");
		}
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "graph_item" })
	public void CreateGraphItemsBatchDupplicated() {
		
		// create 5 graph items
		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), password).token;
		List<GraphItem> items = new ArrayList<GraphItem>();
		for(int i = 0; i < 5; i++) {
			GraphItem item = DefaultValues.RandomGraphItem(2020 * i);
			items.add(item);
		}
		
		// create 3 graph items with 1 new and 2 dupplicated
		List<GraphItem> dupItems = new ArrayList<GraphItem>();
		dupItems.add(DefaultValues.RandomGraphItem(2020 * 5));
		dupItems.add(DefaultValues.RandomGraphItem(2020 * 1));
		dupItems.add(DefaultValues.RandomGraphItem(2020 * 2));
		
		// request
		MVPApi.createGraphItems(token, items);
		ServiceResponse r = MVPApi.createGraphItems(token, dupItems);
		List<GraphItem> ritems = GraphItem.getGraphItems(r);
		
		Assert.assertEquals(r.getStatusCode(), 210, "Status code is 210");
		Assert.assertEquals(ritems.size(), dupItems.size(), "Size of return items is the same");
		for(GraphItem ritem : ritems) {
			Assert.assertNotNull(ritem.getServerId(), "ServerId is not null");
		}
	}

}
