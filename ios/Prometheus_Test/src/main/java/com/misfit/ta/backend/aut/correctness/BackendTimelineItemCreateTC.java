package com.misfit.ta.backend.aut.correctness;

import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.google.resting.component.impl.ServiceResponse;
import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.backend.aut.BackendAutomation;
import com.misfit.ta.backend.aut.DefaultValues;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.backend.data.DataGenerator;
import com.misfit.ta.backend.data.timeline.TimelineItem;
import com.misfit.ta.utils.TextTool;

public class BackendTimelineItemCreateTC  extends BackendAutomation {

	String password = "qwerty1";

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "timeline_item" })
	public void CreateNewTimelineItem() {
		
		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), password).token;
		BaseResult r = MVPApi.createTimelineItem(token, DefaultValues.RandomTimelineItem());
		TimelineItem item = TimelineItem.getTimelineItem(r.response);

		Assert.assertTrue(r.isOK(), "Status code is 200");
		Assert.assertTrue(item.getServerId() != null, "Server Id is not null");
	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "timeline_item" })
	public void CreateDuplicateTimelineItem() {
		
		// duplicate item by timestamp and type
		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), password).token;
		BaseResult r1 = MVPApi.createTimelineItem(token, DefaultValues.RandomTimelineItem(3600, 2));
		BaseResult r2 = MVPApi.createTimelineItem(token, DefaultValues.RandomTimelineItem(3600, 2));
		TimelineItem i1 = TimelineItem.getTimelineItem(r1.response);
		TimelineItem i2 = TimelineItem.getTimelineItem(r2.response);

		Assert.assertTrue(r2.isExisted(), "Status code is 210");
		Assert.assertEquals(i1.getServerId(), i2.getServerId(), "Server Id is the same");
		Assert.assertEquals(i1.getLocalId(), i2.getLocalId(), "Local id is the same");
		Assert.assertEquals(i1.getUpdatedAt(), i2.getUpdatedAt(), "Updated at is the same");
		
		// duplicate item by client id
		TimelineItem i3 = DefaultValues.RandomTimelineItem(3600);
		TimelineItem i4 = DefaultValues.RandomTimelineItem(3700);
		i4.setLocalId(i3.getLocalId());
		
		BaseResult r3 = MVPApi.createTimelineItem(token, i3);
		BaseResult r4 = MVPApi.createTimelineItem(token, i4);
		TimelineItem i5 = TimelineItem.getTimelineItem(r3.response);
		TimelineItem i6 = TimelineItem.getTimelineItem(r4.response);

		Assert.assertTrue(r4.isExisted(), "Status code is 210");
		Assert.assertEquals(i5.getServerId(), i6.getServerId(), "Server Id is the same");
		Assert.assertEquals(i5.getLocalId(), i6.getLocalId(), "Local id is the same");
		Assert.assertEquals(i5.getUpdatedAt(), i6.getUpdatedAt(), "Updated at is the same");

	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "timeline_item" })
	public void CreateTimelineItemsBatch() {
		
		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), password).token;
		List<TimelineItem> items = new ArrayList<TimelineItem>();
		for(int i = 0; i < 5; i++) {
			TimelineItem item = DefaultValues.RandomTimelineItem(1800 * i);
			items.add(item);
		}
		
		ServiceResponse r = MVPApi.createTimelineItems(token, items);
		List<TimelineItem> ritems = TimelineItem.getTimelineItems(r);
		
		Assert.assertEquals(r.getStatusCode(), 200, "Status code is 200");
		Assert.assertEquals(ritems.size(), items.size(), "Size of return items is the same");
		for(TimelineItem ritem : ritems) {
			Assert.assertNotNull(ritem.getServerId(), "ServerId is not null");
		}
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "timeline_item" })
	public void CreateTimelineItemsBatchDupplicated() {
		
		// create 5 graph items
		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), password).token;
		List<TimelineItem> items = new ArrayList<TimelineItem>();
		for(int i = 0; i < 5; i++) {
			TimelineItem item = DefaultValues.RandomTimelineItem(1800 * i, 2);
			items.add(item);
		}
		
		// create 3 graph items with 1 new and 2 dupplicated
		List<TimelineItem> dupItems = new ArrayList<TimelineItem>();
		dupItems.add(DefaultValues.RandomTimelineItem(1800 * 5, 2));
		dupItems.add(DefaultValues.RandomTimelineItem(1800 * 1, 2));
		dupItems.add(DefaultValues.RandomTimelineItem(1800 * 2, 2));
		
		// request
		MVPApi.createTimelineItems(token, items);
		ServiceResponse r = MVPApi.createTimelineItems(token, dupItems);
		List<TimelineItem> ritems = TimelineItem.getTimelineItems(r);
		
		Assert.assertEquals(r.getStatusCode(), 210, "Status code is 210");
		Assert.assertEquals(ritems.size(), dupItems.size(), "Size of return items is the same");
		for(TimelineItem ritem : ritems) {
			Assert.assertNotNull(ritem.getServerId(), "ServerId is not null");
		}
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "timeline_item" })
	public void CreateFoodItemWithValidImageData() {
		
		TimelineItem fooditem = DataGenerator.generateRandomFoodTimelineItem(System.currentTimeMillis() / 1000, null);
		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), password).token;
		BaseResult r = MVPApi.createTimelineItem(token, fooditem);
		TimelineItem item = TimelineItem.getTimelineItem(r.response);

		Assert.assertTrue(r.isOK(), "Status code is 200");
		Assert.assertTrue(item.getServerId() != null, "Server Id is not null");
		Assert.assertTrue(item.getAttachedImageUrl() != null, "Attached Image Url is not null");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "timeline_item" })
	public void CreateFoodItemWithInvalidImageData() {
		
		TimelineItem fooditem = DataGenerator.generateRandomFoodTimelineItem(System.currentTimeMillis() / 1000, null);
		fooditem.setAttachedImage(TextTool.getRandomString(500, 1000) + "   ?! SDA()_");
		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), password).token;
		BaseResult r = MVPApi.createTimelineItem(token, fooditem);

		Assert.assertNotEquals(r.statusCode, 200, "Status code");
	}
	
}
