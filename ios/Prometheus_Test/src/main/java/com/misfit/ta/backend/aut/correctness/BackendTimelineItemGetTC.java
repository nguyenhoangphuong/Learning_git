package com.misfit.ta.backend.aut.correctness;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.backend.aut.DefaultValues;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.backend.data.timeline.TimelineData;
import com.misfit.ta.backend.data.timeline.TimelineItem;

public class BackendTimelineItemGetTC {

	String email = MVPApi.generateUniqueEmail();
	String password = "qwerty1";
		
	@BeforeClass(alwaysRun = true)
	public void setUp() {
		
		// sign up and create timeline items
		String token = MVPApi.signUp(email, password).token;
		for(int i = 0; i < 5; i++)
			MVPApi.createTimelineItem(token, DefaultValues.RandomTimelineItem(2020 * i, 2));
	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "graph_item" })
	public void SearchTimelineItems() {
		String token = MVPApi.signIn(email, password).token;
		
		// search 1
		List<TimelineItem> items = MVPApi.getTimelineItems(token, 0, Integer.MAX_VALUE, 0);
		Assert.assertEquals(items.size(), 5, "Found 5 items");
		
		// search 2
		items = MVPApi.getTimelineItems(token, MVPApi.getDayStartEpoch(), MVPApi.getDayStartEpoch() + 2020 * 2, 0);
		Assert.assertEquals(items.size(), 3, "Found 3 items");
		
		// search 3
		items = MVPApi.getTimelineItems(token, MVPApi.getDayStartEpoch() + 2020 * 3 + 1, MVPApi.getDayStartEpoch() + 2020 * 4, 0);
		Assert.assertEquals(items.size(), 1, "Found 1 item");
		
		// search 4
		items = MVPApi.getTimelineItems(token, MVPApi.getDayStartEpoch() + 2020 * 5, Integer.MAX_VALUE, 0);
		Assert.assertEquals(items.size(), 0, "Found 0 items");
		
		// search 5
		items = MVPApi.getTimelineItems(token, MVPApi.getDayStartEpoch(), MVPApi.getDayStartEpoch() - 1000, 0);
		Assert.assertEquals(items.size(), 0, "Found 0 items");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "graph_item" })
	public void GetTimelineItem() {
		
		TimelineItem item = DefaultValues.RandomTimelineItem(3600);
		
		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), password).token;
		BaseResult r = MVPApi.createTimelineItem(token, item);
		TimelineItem ritem = TimelineItem.getTimelineItem(r.response);

		TimelineItem getitem = MVPApi.getTimelineItem(token, ritem.getServerId());
		Assert.assertNotNull(getitem, "Timeline item is not null");
		Assert.assertEquals(getitem.getItemType(), item.getItemType(), "ItemType is the same");
		
		TimelineData getdata = (TimelineData) getitem.getData();
		TimelineData postdata = (TimelineData) item.getData();
		Assert.assertEquals(postdata.getValue("Random"), getdata.getValue("Random"), "Random data is the same");
	}
	
}
