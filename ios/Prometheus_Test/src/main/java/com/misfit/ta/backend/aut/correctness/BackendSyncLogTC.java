package com.misfit.ta.backend.aut.correctness;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.google.resting.component.impl.ServiceResponse;
import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.backend.aut.BackendAutomation;
import com.misfit.ta.utils.TextTool;

public class BackendSyncLogTC extends BackendAutomation {

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "sync_log" })
	public void PushSyncLog() {
		
		String email = MVPApi.generateUniqueEmail();
		String serialNumber = TextTool.getRandomString(10);
		Long timestamp = System.currentTimeMillis() / 1000;
		String token = MVPApi.signUp(email, "qwerty1").token;

		ServiceResponse r = MVPApi.syncLog(token, MVPApi.generateSyncLog(serialNumber, timestamp));
		
		Assert.assertEquals(r.getStatusCode(), 200, "Status code is 200");
	}
}
