package com.misfit.ta.backend.aut.correctness;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.backend.aut.BackendAutomation;
import com.misfit.ta.backend.aut.DefaultValues;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.backend.data.statistics.Statistics;

public class BackendStatisticsTC extends BackendAutomation {

	private String password = "qwerty1";
	private Statistics defaultStatistics = DefaultValues.RandomStatistic();

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "profile" })
	public void CreateNewStatistics() {

		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), password).token;
		BaseResult r = MVPApi.createStatistics(token, defaultStatistics);
		Statistics stats = Statistics.fromResponse(r.response);

		Assert.assertTrue(r.isOK(), "Status code is OK");
		Assert.assertTrue(stats.getServerId() != null, "Server Id is not null");
	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "profile" })
	public void CreateDuplicatedStatistics() {

		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), password).token;
		BaseResult r1 = MVPApi.createStatistics(token, defaultStatistics);
		BaseResult r2 = MVPApi.createStatistics(token, defaultStatistics);
		Statistics stats1 = Statistics.fromResponse(r1.response);
		Statistics stats2 = Statistics.fromResponse(r2.response);

		Assert.assertEquals(r2.statusCode, 210, "Status code");
		Assert.assertEquals(stats1.getServerId(), stats2.getServerId(), "Server Id is the same");
		Assert.assertEquals(stats2.getPersonalRecords().getPersonalBestRecordsInPoint().getPoint(), 
				defaultStatistics.getPersonalRecords().getPersonalBestRecordsInPoint().getPoint(),
				"Personal best in point is the same");

	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "profile" })
	public void UpdateStatistics() {

		// create
		Statistics src = DefaultValues.RandomStatistic();
		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), password).token;
		MVPApi.createStatistics(token, src);

		// update
		src.getPersonalRecords().getPersonalBestRecordsInPoint().setPoint(5000d);
		src.getPersonalRecords().getPersonalBestRecordsInPoint().setTimestamp(System.currentTimeMillis() / 1000);
		BaseResult r = MVPApi.updateStatistics(token, src);
		Statistics stats = Statistics.fromResponse(r.response);

		Assert.assertEquals(r.statusCode, 210, "Status code");
		Assert.assertTrue(stats.getServerId() != null, "Server Id is not null");
		Assert.assertEquals(stats.getPersonalRecords().getPersonalBestRecordsInPoint().getPoint(), 5000d, "Personal best in points is updated");

	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "profile" })
	public void UpdateNonExistedStatistics() {

		// sign up but dont create statistics
		Statistics src = DefaultValues.RandomStatistic();
		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), password).token;

		// update
		src.getPersonalRecords().getPersonalBestRecordsInPoint().setPoint(5000d);
		src.getPersonalRecords().getPersonalBestRecordsInPoint().setTimestamp(System.currentTimeMillis() / 1000);
		BaseResult r = MVPApi.updateStatistics(token, src);
		Statistics stats = Statistics.fromResponse(r.response);

		Assert.assertEquals(r.statusCode, 400, "Status code");
		Assert.assertNull(stats, "Statistics is null");
	}

}
