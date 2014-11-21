package com.misfit.ta.backend.aut.correctness.backendapi;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.misfit.ta.backend.api.metawatch.MVPApi;
import com.misfit.ta.backend.aut.BackendAutomation;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.backend.data.DataGenerator;
import com.misfit.ta.backend.data.sync.SyncLog;

public class BackendSyncLogTC extends BackendAutomation {

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "sync_log" })
	public void PushSyncLog() {
		
		String email = MVPApi.generateUniqueEmail();
		String token = MVPApi.signUp(email, "qwerty1").token;

		SyncLog log = DataGenerator.generateRandomSyncLog(System.currentTimeMillis() / 1000, 1, 60, null);
		BaseResult result = MVPApi.pushSyncLog(token, log);
		
		Assert.assertEquals(result.statusCode, 200, "Status code");
	}
}
