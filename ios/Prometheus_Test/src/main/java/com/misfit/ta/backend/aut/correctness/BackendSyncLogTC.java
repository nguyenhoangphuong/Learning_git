package com.misfit.ta.backend.aut.correctness;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.google.resting.component.impl.ServiceResponse;
import com.misfit.ta.backend.api.MVPApi;

public class BackendSyncLogTC {

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "sync_log" })
	public void PushSyncLog() {
		
		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), "qwerty1").token;
		ServiceResponse r = MVPApi.syncLog(token, MVPApi.generateSyncLog());

		Assert.assertEquals(r.getStatusCode(), 200, "Status code is 200");
	}
}
