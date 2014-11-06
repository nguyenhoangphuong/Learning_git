package com.misfit.ta.backend.aut.correctness.backendapi;

import java.util.List;
import java.util.ArrayList;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.google.resting.component.impl.ServiceResponse;
import com.misfit.ta.backend.api.internalapi.MVPApi;
import com.misfit.ta.backend.aut.BackendAutomation;
import com.misfit.ta.backend.aut.DefaultValues;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.backend.data.goal.Goal;
import com.misfit.ta.backend.data.goal.GoalsResult;
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
	public void CreateGraphItemsWithGoal(){
		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), password).token;
		Goal defaultGoal = Goal.getDefaultGoal();
		GoalsResult goalResult = MVPApi.createGoal(token, defaultGoal);
		
		goalResult.printKeyPairsValue();

		Assert.assertTrue(goalResult.isOK(), "Status code is OK");
		Assert.assertTrue(goalResult.goals[0].getServerId() != null, "Server Id is not null");
		Assert.assertEquals(goalResult.goals[0].getLocalId(), defaultGoal.getLocalId(), "Local id is not the same");
		
		List<String> listValue = new ArrayList<String>();
		List<GraphItem> listGraphItem = new ArrayList<GraphItem>();
		for(int i = 0; i < 40; i++){
			GraphItem graphItem = DefaultValues.RandomGraphItem(2020 * i);
			graphItem.setAverageValue(i * 1.0);
			listGraphItem.add(graphItem);
			listValue.add(String.valueOf(i*1.0));
		}
		ServiceResponse serviceResponse = MVPApi.createGraphItems(token, listGraphItem);
		Assert.assertEquals(serviceResponse.getStatusCode(), 200, "Can't not create multiple graph items for user");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "graph_item" })
	public void CreateDuplicateGraphItem() {
		
		// duplicate item by timestamp
		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), password).token;
		BaseResult r1 = MVPApi.createGraphItem(token, DefaultValues.RandomGraphItem(0));
		BaseResult r2 = MVPApi.createGraphItem(token, DefaultValues.RandomGraphItem(0));
		GraphItem g1 = GraphItem.getGraphItem(r1.response);
		GraphItem g2 = GraphItem.getGraphItem(r2.response);

		Assert.assertTrue(r2.isExisted(), "Status code isn't 210 - it means duplicated content error");
		Assert.assertEquals(g1.getServerId(), g2.getServerId(), "Server Id isn't the same");
		Assert.assertEquals(g1.getLocalId(), g2.getLocalId(), "Local id isn't the same");
		Assert.assertEquals(g1.getUpdatedAt(), g2.getUpdatedAt(), "Updated at isn't the same");
		
		// duplicate item by client id
		GraphItem g3 = DefaultValues.RandomGraphItem(2020 * 1);
		GraphItem g4 = DefaultValues.RandomGraphItem(2020 * 2);
		g4.setLocalId(g3.getLocalId());
		
		BaseResult r3 = MVPApi.createGraphItem(token, g3);
		BaseResult r4 = MVPApi.createGraphItem(token, g4);
		GraphItem g5 = GraphItem.getGraphItem(r3.response);
		GraphItem g6 = GraphItem.getGraphItem(r4.response);

		Assert.assertTrue(r4.isExisted(), "Status code isn't 210 - it means duplicated content error");
		Assert.assertEquals(g5.getServerId(), g6.getServerId(), "Server Id isn't the same");
		Assert.assertEquals(g5.getLocalId(), g6.getLocalId(), "Local id isn't the same");
		Assert.assertEquals(g5.getUpdatedAt(), g6.getUpdatedAt(), "Updated at isn't the same");

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
		ServiceResponse r1 = MVPApi.createGraphItems(token, items);
		System.out.println("r1 : " + GraphItem.getGraphItems(r1).size());
		System.out.println("r1 status : " + r1.getStatusCode());
		ServiceResponse r = MVPApi.createGraphItems(token, dupItems);
		List<GraphItem> ritems = GraphItem.getGraphItems(r);
		
		System.out.println("R status : " + r.getStatusCode());
		System.out.println("ritems.size() : " + ritems.size());
		System.out.println("dupItems.size() : " + dupItems.size());
		Assert.assertEquals(r.getStatusCode(), 210, "Status code isn't 210 - it means duplicated error");
		for(GraphItem ritem : ritems) {
			Assert.assertNotNull(ritem.getServerId(), "ServerId is not null");
		}
	}
	
	
	//API for create new type account (test_get_old = 2) => create graph_item(actual is graph activity day) => get data
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "graph_item" })
	public void createGraphActivityDay(){
		//Create new type account
		String email = MVPApi.generateUniqueEmail();
		String token = MVPApi.signUp(email, password, true).token;
		//Create graph activity day
		List<GraphItem> listGraphItem = new ArrayList<GraphItem>();
		for(int i = 0; i < 40; i++){
			GraphItem graphItem = DefaultValues.RandomGraphItem(2020 * i);
			graphItem.setAverageValue(i * 1.0);
			listGraphItem.add(graphItem);
		}
		ServiceResponse serviceResponse = MVPApi.createGraphItems(token, listGraphItem);
		Assert.assertEquals(serviceResponse.getStatusCode(), 200, "Can't not create graph activity day for new type user");
		
		//Get data from the new schema DB
		ServiceResponse responseResult = MVPApi.getGraphActivityDay(token);
		List<GraphItem> listResult = GraphItem.getListGraphItem(responseResult);
		Assert.assertEquals(listResult.size(), listGraphItem.size(), "Not the same graph_item in graph_activity_day"); 
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "graph_item" })
	public void createGraphItemWithTimestampNull(){
		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), password).token;
		GraphItem graphItem = DefaultValues.RandomGraphItem();
		graphItem.setTimestamp(null);
		BaseResult r = MVPApi.createGraphItem(token, graphItem);
//		GraphItem item = GraphItem.getGraphItem(r.response);

//		Assert.assertTrue(r.isOK(), "Status code is 200");
//		Assert.assertTrue(item.getServerId() != null, "Server Id is not null");

	}
}
