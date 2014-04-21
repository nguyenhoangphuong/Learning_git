package com.misfit.ta.backend.aut.correctness.backendapi;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.misfit.ta.backend.api.internalapi.MVPApi;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.backend.data.DataGenerator;
import com.misfit.ta.backend.data.timeline.TimelineItem;
import com.misfit.ta.backend.data.timeline.timelineitemdata.ActivitySessionItem;

public class BackendTimelineItemUpdateTC {

	protected String nonExistedServerId = "5354d777d5e2eaffcd002137";
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "TimelineItemUpdate" })
	public void UpdateNonExistedTimelineItem() {
		
		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), "qqqqqq").token;
		
		// without server id
		TimelineItem item = new TimelineItem();
		item.setServerId("");
		
		BaseResult r = MVPApi.updateTimelineItem(token, item);
		Assert.assertEquals(r.statusCode, 404, "Status code");
		
		// invalid server id
		item.setServerId("invalidServerid");

		r = MVPApi.updateTimelineItem(token, item);
		Assert.assertEquals(r.statusCode, 404, "Status code");

		// valid but non existed id
		item.setServerId(nonExistedServerId);

		r = MVPApi.updateTimelineItem(token, item);
		Assert.assertEquals(r.statusCode, 404, "Status code");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "TimelineItemUpdate" })
	public void UpdateTimelineItem() {
		
		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), "qqqqqq").token;
		TimelineItem item = DataGenerator.generateRandomActivitySessionTimelineItem(System.currentTimeMillis() / 1000, null);
		BaseResult r = MVPApi.createTimelineItem(token, item);
		item.setServerId(TimelineItem.getTimelineItem(r.response).getServerId());
		item.setUpdatedAt(TimelineItem.getTimelineItem(r.response).getUpdatedAt());

		// update and check caching
		int points = ((ActivitySessionItem)item.getData()).getPoint();
		for(int i = 0; i < MVPApi.CACHE_TRY_TIME; i++) {
			
			// update
			points += 100;
			((ActivitySessionItem)item.getData()).setPoint(points);

			r = MVPApi.updateTimelineItem(token, item);
			item.setUpdatedAt(TimelineItem.getTimelineItem(r.response).getUpdatedAt());
			Assert.assertEquals(r.statusCode, 200, "Status code");
			
			// check caching
			TimelineItem updatedItem = MVPApi.getTimelineItems(token, null, null, null).get(0);
			Assert.assertEquals((int)((ActivitySessionItem)updatedItem.getData()).getPoint(), points, "Timeline's points");
		}
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "TimelineItemUpdate" })
	public void UpdateBedditSleepForceUpdate() {
		
		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), "qqqqqq").token;
		TimelineItem item = DataGenerator.generateRandomActivitySessionTimelineItem(System.currentTimeMillis() / 1000, null);
		BaseResult r = MVPApi.createTimelineItem(token, item);
		item.setServerId(TimelineItem.getTimelineItem(r.response).getServerId());
		item.setUpdatedAt(TimelineItem.getTimelineItem(r.response).getUpdatedAt());

		// update and check caching
		int points = 100;
		for(int i = 0; i < MVPApi.CACHE_TRY_TIME; i++) {
			
			// update
			points += 100;
			((ActivitySessionItem)item.getData()).setPoint(points);
			item.setUpdatedAt(item.getUpdatedAt() + 1);

			r = MVPApi.updateTimelineItem(token, item);
			TimelineItem updatedItem = TimelineItem.getTimelineItem(r.response);
			item.setUpdatedAt(updatedItem.getUpdatedAt());
			
			Assert.assertEquals(r.statusCode, 210, "Status code");
			Assert.assertEquals((int)((ActivitySessionItem)updatedItem.getData()).getPoint(), points, "Timeline's points");
			
			// check caching
			updatedItem = MVPApi.getTimelineItems(token, null, null, null).get(0);			
			Assert.assertEquals((int)((ActivitySessionItem)updatedItem.getData()).getPoint(), points, "Timeline's points");
		}
	}

}
