package com.misfit.ta.backend.aut.correctness;

import org.apache.commons.lang.StringUtils;
import org.testng.annotations.Test;

import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.backend.aut.BackendAutomation;
import com.misfit.ta.backend.aut.DefaultValues;
import com.misfit.ta.backend.data.account.AccountResult;
import com.misfit.ta.backend.data.pedometer.Pedometer;
import com.misfit.ta.utils.TextTool;
import org.testng.Assert;

public class BackendPedometerTC extends BackendAutomation {

	private String password = "test12";
	private String firmwareRevisionString = "0.0.27r";

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "pedometer" })
	public void linkDevice() {
		String email = MVPApi.generateUniqueEmail();
		String serialNumberString = TextTool.getRandomString(10);
		
		// link new account to a new device
		String newAccToken = createNewAccount(email);
		Pedometer pedometer = createNewPedometer(newAccToken, serialNumberString);
		Assert.assertFalse(StringUtils.isEmpty(pedometer.getServerId()), "Failed to create new pedometer!");
		
		// check linking status
		String linkingStatus = MVPApi.getDeviceLinkingStatus(newAccToken, serialNumberString);
		Assert.assertTrue(DefaultValues.DeviceLinkedToYourAccount.equals(linkingStatus), "Failed to link account and device!");
	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "pedometer" })
	public void stealDevice() {
		String serialNumberString = TextTool.getRandomString(10);

		// link device to account 1
		String email1 = MVPApi.generateUniqueEmail();
		String newAccToken1 = createNewAccount(email1);
		createNewPedometer(newAccToken1, serialNumberString);

		String email2 = MVPApi.generateUniqueEmail();
		String newAccToken2 = createNewAccount(email2);
		
		// account 2 steals device
		createNewPedometer(newAccToken2, serialNumberString);
		
		// check linking status
		String linkingStatus = MVPApi.getDeviceLinkingStatus(newAccToken1, serialNumberString);
		Assert.assertTrue(DefaultValues.DeviceLinkedToYourAccount.equals(linkingStatus), "Failed to unlink account and device!");

		linkingStatus = MVPApi.getDeviceLinkingStatus(newAccToken2, serialNumberString);
		Assert.assertTrue(DefaultValues.DeviceLinkedToAnotherAccount.equals(linkingStatus), "Failed to steal device!");
	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "pedometer" })
	public void unlinkDevice() {
		
		String email = MVPApi.generateUniqueEmail();
		String serialNumberString = TextTool.getRandomString(10);
		
		// link new account to a new device
		String newAccToken = createNewAccount(email);
		createNewPedometer(newAccToken, serialNumberString);
		
		// check linking status
		String linkingStatus = MVPApi.getDeviceLinkingStatus(newAccToken, serialNumberString);
		Assert.assertTrue(DefaultValues.DeviceLinkedToYourAccount.equals(linkingStatus), "Failed to link account and device!");
		
		// unlink device
		MVPApi.unlinkDevice(newAccToken, serialNumberString);
		linkingStatus = MVPApi.getDeviceLinkingStatus(newAccToken, serialNumberString);
		Assert.assertTrue(DefaultValues.DeviceUsedToLinkToYourAccount.equals(linkingStatus), "Failed to unlink account and device!");
	}

	private Pedometer createNewPedometer(String token, String serialNumberString) {
		long timestamp = System.currentTimeMillis();
		Pedometer pedometer = MVPApi.createPedometer(token, serialNumberString, firmwareRevisionString, timestamp, null, timestamp, TextTool.getRandomString(19, 20), null, timestamp);
		System.out.println(pedometer.toJson().toString());
		return pedometer;
	}

	private String createNewAccount(String email) {
		AccountResult acc = MVPApi.signUp(email, password);
		return acc.token;
	}

}
