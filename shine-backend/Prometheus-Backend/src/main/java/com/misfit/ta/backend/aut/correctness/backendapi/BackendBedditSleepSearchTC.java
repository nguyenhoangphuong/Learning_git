package com.misfit.ta.backend.aut.correctness.backendapi;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.misfit.ta.backend.api.internalapi.MVPApi;
import com.misfit.ta.backend.aut.BackendAutomation;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.backend.data.DataGenerator;
import com.misfit.ta.backend.data.beddit.BedditSleepSession;
import com.misfit.ta.utils.ShortcutsTyper;

public class BackendBedditSleepSearchTC extends BackendAutomation {

	protected String token = "";
	protected long timestamp = System.currentTimeMillis() / 1000;
	
	@BeforeClass(alwaysRun = true)
	public void setUp() {
		
		// sign up and create items
		token = MVPApi.signUp(MVPApi.generateUniqueEmail(), "qqqqqq").token;
		for(int i = 5; i >= 0; i--)
			MVPApi.createBedditSleepSession(token, DataGenerator.generateRandomBedditSleepSession(timestamp - 3600 * 24 * i, null));
	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "BedditSleepGet" })
	public void BedditSleepSearch_WithTimeRange() {
		
		// everything
		BaseResult result = MVPApi.searchBedditSleepSessions(token, null, null, null);
		List<BedditSleepSession> sleeps = BedditSleepSession.getBedditSleepSessionsFromResponse(result.response);
		
		Assert.assertEquals(sleeps.size(), 6, "Number of sleeps");
		
		
		// with only start time
		result = MVPApi.searchBedditSleepSessions(token, timestamp - 3600 * 24 * 3, null, null);
		sleeps = BedditSleepSession.getBedditSleepSessionsFromResponse(result.response);

		Assert.assertEquals(sleeps.size(), 4, "Number of sleeps");
		
		
		// in a speific range
		result = MVPApi.searchBedditSleepSessions(token, timestamp - 3600 * 24 * 4, timestamp - 3600 * 24 * 2, null);
		sleeps = BedditSleepSession.getBedditSleepSessionsFromResponse(result.response);

		Assert.assertEquals(sleeps.size(), 3, "Number of sleeps");
				
		
		// start time == end time
		result = MVPApi.searchBedditSleepSessions(token, timestamp, timestamp, null);
		sleeps = BedditSleepSession.getBedditSleepSessionsFromResponse(result.response);

		Assert.assertEquals(sleeps.size(), 1, "Number of sleeps");
		
		
		// no goal in that range
		result = MVPApi.searchBedditSleepSessions(token, timestamp - 3600 * 24 * 14, timestamp - 3600 * 24 * 12, null);
		sleeps = BedditSleepSession.getBedditSleepSessionsFromResponse(result.response);

		Assert.assertEquals(sleeps.size(), 0, "Number of sleeps");
		

		// with end time only
		result = MVPApi.searchBedditSleepSessions(token, null, timestamp - 3600 * 24 * 2, null);
		sleeps = BedditSleepSession.getBedditSleepSessionsFromResponse(result.response);

		Assert.assertEquals(sleeps.size(), 4, "Number of sleeps");
	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "BedditSleepGet" })
	public void BedditSleepSearch_WithModifiedSince() {
		
		// wait 5 seconds
		ShortcutsTyper.delayTime(5000);

		
		// search all goals which were modified since 3 seconds ago => no result
		BaseResult result = MVPApi.searchBedditSleepSessions(token, null, null, System.currentTimeMillis() / 1000 - 3);
		List<BedditSleepSession> sleeps = BedditSleepSession.getBedditSleepSessionsFromResponse(result.response);
		
		Assert.assertEquals(sleeps.size(), 0, "Number of sleeps");
		
		
		// update a progress
		result = MVPApi.searchBedditSleepSessions(token, null, null, null);
		sleeps = BedditSleepSession.getBedditSleepSessionsFromResponse(result.response);
		sleeps.get(0).getProperties().setNormalizedSleepQuality(101);
		
		MVPApi.updateBedditSleepSession(token, sleeps.get(0));
		
		
		// search all goals which were modified since 3 seconds ago => 1 result
		result = MVPApi.searchBedditSleepSessions(token, null, null, System.currentTimeMillis() / 1000 - 3);
		sleeps = BedditSleepSession.getBedditSleepSessionsFromResponse(result.response);
		
		Assert.assertEquals(sleeps.size(), 1, "Number of sleeps");
		Assert.assertEquals((int)sleeps.get(0).getProperties().getNormalizedSleepQuality(), 101, "Sleep quality");
	}

}
