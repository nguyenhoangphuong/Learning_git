package com.misfit.ta.backend.aut.correctness.backendapi;

import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.misfit.ta.backend.api.metawatch.MVPApi;
import com.misfit.ta.backend.aut.BackendAutomation;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.backend.data.DataGenerator;
import com.misfit.ta.backend.data.beddit.BedditSleepSession;

public class BackendBedditSleepCreateTC extends BackendAutomation {

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "BedditSleepCreate" })
	public void CreateNewBedditSleep() {
		
		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), "qqqqqq").token;
		BaseResult r = MVPApi.createBedditSleepSession(token, 
				DataGenerator.generateRandomBedditSleepSession(System.currentTimeMillis() / 1000, null));
		BedditSleepSession item = BedditSleepSession.getBedditSleepSessionFromResponse(r.response);

		Assert.assertTrue(r.isOK(), "Status code is 200");
		Assert.assertTrue(item.getServerId() != null, "Server Id is not null");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "BedditSleepCreate" })
	public void CreateDuplicateBedditSleep() {
		
		// duplicate item by timestamp
		long timestamp = System.currentTimeMillis() / 1000;
		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), "qqqqqq").token;
		BaseResult r1 = MVPApi.createBedditSleepSession(token, DataGenerator.generateRandomBedditSleepSession(timestamp, null));
		BaseResult r2 = MVPApi.createBedditSleepSession(token, DataGenerator.generateRandomBedditSleepSession(timestamp, null));
		BedditSleepSession i1 = BedditSleepSession.getBedditSleepSessionFromResponse(r1.response);
		BedditSleepSession i2 = BedditSleepSession.getBedditSleepSessionFromResponse(r2.response);

		Assert.assertTrue(r2.isExisted(), "Status code is 210");
		Assert.assertEquals(i1.getServerId(), i2.getServerId(), "Server Id is the same");
		Assert.assertEquals(i1.getLocalId(), i2.getLocalId(), "Local id is the same");
		Assert.assertEquals(i1.getUpdatedAt(), i2.getUpdatedAt(), "Updated at is the same");
		
		// duplicate item by client id
		BedditSleepSession i3 = DataGenerator.generateRandomBedditSleepSession(timestamp - 3600 * 24 * 2, null);
		BedditSleepSession i4 = DataGenerator.generateRandomBedditSleepSession(timestamp - 3600 * 24 * 1, null);
		i4.setLocalId(i3.getLocalId());
		
		BaseResult r3 = MVPApi.createBedditSleepSession(token, i3);
		BaseResult r4 = MVPApi.createBedditSleepSession(token, i4);
		BedditSleepSession i5 = BedditSleepSession.getBedditSleepSessionFromResponse(r3.response);
		BedditSleepSession i6 = BedditSleepSession.getBedditSleepSessionFromResponse(r4.response);

		Assert.assertTrue(r4.isExisted(), "Status code is 210");
		Assert.assertEquals(i5.getServerId(), i6.getServerId(), "Server Id is the same");
		Assert.assertEquals(i5.getLocalId(), i6.getLocalId(), "Local id is the same");
		Assert.assertEquals(i5.getUpdatedAt(), i6.getUpdatedAt(), "Updated at is the same");
	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "BedditSleepCreate" })
	public void CreateBedditSleepBatchInsert() {
		
		long timestamp = System.currentTimeMillis() / 1000;
		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), "qqqqqq").token;
		List<BedditSleepSession> items = new ArrayList<BedditSleepSession>();
		for(int i = 5; i > 0; i--) {
			BedditSleepSession item = DataGenerator.generateRandomBedditSleepSession(
					timestamp - 3600 * 24 * i, null);
			items.add(item);
		}
		
		BaseResult r = MVPApi.createBedditSleepSessions(token, items);
		List<BedditSleepSession> ritems = BedditSleepSession.getBedditSleepSessionsFromResponse(r.response);
		
		Assert.assertEquals(r.statusCode, 200, "Status code");
		Assert.assertEquals(ritems.size(), items.size(), "Size of return items is the same");
		for(BedditSleepSession ritem : ritems) {
			Assert.assertNotNull(ritem.getServerId(), "ServerId");
		}
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "BedditSleepCreate" })
	public void CreateBedditSleepBatchInsertDuplicated() {
		
		// create 5 beddit sleeps
		long timestamp = System.currentTimeMillis() / 1000;
		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), "qqqqqq").token;
		List<BedditSleepSession> items = new ArrayList<BedditSleepSession>();
		for(int i = 5; i > 0; i--) {
			BedditSleepSession item = DataGenerator.generateRandomBedditSleepSession(
					timestamp - 3600 * 24 * i, null);
			items.add(item);
		}
		
		// create 3 beddit sleeps, one duplicated by timestamp, one by localid
		List<BedditSleepSession> dupItems = new ArrayList<BedditSleepSession>();
		BedditSleepSession dupItemByLocalId = DataGenerator.generateRandomBedditSleepSession(timestamp - 6 * 3600 * 24, null);
		dupItemByLocalId.setLocalId(items.get(1).getLocalId());
		dupItems.add(DataGenerator.generateRandomBedditSleepSession(timestamp, null));
		dupItems.add(DataGenerator.generateRandomBedditSleepSession(timestamp - 3600 * 24 * 5, null));
		dupItems.add(dupItemByLocalId);
				
		// request
		BaseResult r = MVPApi.createBedditSleepSessions(token, items);
		List<BedditSleepSession> ritems = BedditSleepSession.getBedditSleepSessionsFromResponse(r.response);
		for(BedditSleepSession ritem : ritems) {
			for(BedditSleepSession item : items) {
				if(ritem.getLocalId().equals(item.getLocalId())) {
					item.setServerId(ritem.getServerId());
					item.setUpdatedAt(ritem.getUpdatedAt());
				}
			}
		}
		
		r = MVPApi.createBedditSleepSessions(token, dupItems);
		ritems = BedditSleepSession.getBedditSleepSessionsFromResponse(r.response);
		
		Assert.assertEquals(r.statusCode, 210, "Status code is 210");
		Assert.assertEquals(ritems.size(), dupItems.size(), "Size of return items is the same");
		Assert.assertNotNull(ritems.get(0).getServerId(), "ServerId of items[0]");
		
		Assert.assertEquals(ritems.get(1).getServerId(), items.get(0).getServerId(), "ServerId of items[1]");
		Assert.assertEquals(ritems.get(1).getLocalId(), items.get(0).getLocalId(), "LocalId of items[1]");
		Assert.assertEquals(ritems.get(1).getUpdatedAt(), items.get(0).getUpdatedAt(), "UpdatedAt of items[1]");
		Assert.assertEquals(ritems.get(1).getTimestamp(), items.get(0).getTimestamp(), "Timestamp of items[1]");
		
		Assert.assertEquals(ritems.get(2).getServerId(), items.get(1).getServerId(), "ServerId of items[2]");
		Assert.assertEquals(ritems.get(2).getLocalId(), items.get(1).getLocalId(), "LocalId of items[2]");
		Assert.assertEquals(ritems.get(2).getUpdatedAt(), items.get(1).getUpdatedAt(), "UpdatedAt of items[2]");
		Assert.assertEquals(ritems.get(2).getTimestamp(), items.get(1).getTimestamp(), "Timestamp of items[2]");
	}

}
