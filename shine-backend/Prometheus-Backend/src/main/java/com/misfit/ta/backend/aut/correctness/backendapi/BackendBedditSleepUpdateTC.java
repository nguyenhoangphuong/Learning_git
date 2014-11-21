package com.misfit.ta.backend.aut.correctness.backendapi;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.misfit.ta.backend.api.metawatch.MVPApi;
import com.misfit.ta.backend.aut.BackendAutomation;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.backend.data.DataGenerator;
import com.misfit.ta.backend.data.beddit.BedditSleepSession;

public class BackendBedditSleepUpdateTC extends BackendAutomation {

	protected String nonExistedServerId = "5354d777d5e2eaffcd002137";
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "BedditSleepUpdate" })
	public void UpdateNonExistedBedditSleep() {
		
		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), "qqqqqq").token;
		
		// without server id
		BedditSleepSession sleep = new BedditSleepSession();
		sleep.setServerId("");
		
		BaseResult r = MVPApi.updateBedditSleepSession(token, sleep);
		Assert.assertEquals(r.statusCode, 404, "Status code");
		
		// invalid server id
		sleep.setServerId("invalidServerid");

		r = MVPApi.updateBedditSleepSession(token, sleep);
		Assert.assertEquals(r.statusCode, 404, "Status code");

		// valid but non existed id
		sleep.setServerId(nonExistedServerId);

		r = MVPApi.updateBedditSleepSession(token, sleep);
		Assert.assertEquals(r.statusCode, 404, "Status code");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "BedditSleepUpdate" })
	public void UpdateBedditSleep() {
		
		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), "qqqqqq").token;
		BedditSleepSession sleep = DataGenerator.generateRandomBedditSleepSession(System.currentTimeMillis() / 1000, null);
		BaseResult r = MVPApi.createBedditSleepSession(token, sleep);
		sleep.setServerId(BedditSleepSession.getBedditSleepSessionFromResponse(r.response).getServerId());
		sleep.setUpdatedAt(BedditSleepSession.getBedditSleepSessionFromResponse(r.response).getUpdatedAt());

		// update and check caching
		int quality = 100;
		for(int i = 0; i < MVPApi.CACHE_TRY_TIME; i++) {
			
			// update
			quality++;
			sleep.getProperties().setNormalizedSleepQuality(quality);

			r = MVPApi.updateBedditSleepSession(token, sleep);
			sleep.setUpdatedAt(BedditSleepSession.getBedditSleepSessionFromResponse(r.response).getUpdatedAt());
			Assert.assertEquals(r.statusCode, 200, "Status code");
			
			// check caching
			r = MVPApi.searchBedditSleepSessions(token, null, null, null);
			BedditSleepSession updatedSleep = BedditSleepSession.getBedditSleepSessionsFromResponse(r.response).get(0);
			Assert.assertEquals((int)updatedSleep.getProperties().getNormalizedSleepQuality(), quality, "Sleep quality is updated");
		}
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "BedditSleepUpdate" })
	public void UpdateBedditSleepForceUpdate() {
		
		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), "qqqqqq").token;
		BedditSleepSession sleep = DataGenerator.generateRandomBedditSleepSession(System.currentTimeMillis() / 1000, null);
		BaseResult r = MVPApi.createBedditSleepSession(token, sleep);
		sleep.setServerId(BedditSleepSession.getBedditSleepSessionFromResponse(r.response).getServerId());
		sleep.setUpdatedAt(BedditSleepSession.getBedditSleepSessionFromResponse(r.response).getUpdatedAt());

		// update and check caching
		int quality = 100;
		for(int i = 0; i < MVPApi.CACHE_TRY_TIME; i++) {
			
			// update
			quality++;
			sleep.getProperties().setNormalizedSleepQuality(quality);
			sleep.setUpdatedAt(sleep.getUpdatedAt() + 1);

			r = MVPApi.updateBedditSleepSession(token, sleep);
			BedditSleepSession updatedSleep = BedditSleepSession.getBedditSleepSessionFromResponse(r.response);
			sleep.setUpdatedAt(updatedSleep.getUpdatedAt());
			
			Assert.assertEquals(r.statusCode, 210, "Status code");
			Assert.assertEquals((int)updatedSleep.getProperties().getNormalizedSleepQuality(), quality, "Sleep quality is updated");
			
			// check caching
			r = MVPApi.searchBedditSleepSessions(token, null, null, null);
			updatedSleep = BedditSleepSession.getBedditSleepSessionsFromResponse(r.response).get(0);			
			Assert.assertEquals((int)updatedSleep.getProperties().getNormalizedSleepQuality(), quality, "Sleep quality is updated");
		}
	}

}