package com.misfit.ta.backend.aut.correctness;

import org.apache.commons.lang.StringUtils;
import org.testng.annotations.Test;

import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.backend.aut.BackendAutomation;
import com.misfit.ta.backend.aut.DefaultValues;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.backend.data.account.AccountResult;
import com.misfit.ta.backend.data.pedometer.Pedometer;
import com.misfit.ta.utils.TextTool;

import org.testng.Assert;

public class BackendPedometerTC extends BackendAutomation {

	private String password = "test12";
	private String firmwareRevisionString = MVPApi.LATEST_FIRMWARE_VERSION_STRING;

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "pedometer" })
	public void LinkOneAccountToOneShine() {
		
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
	public void LinkOneAccountToTwoShines() {
		
		String email = MVPApi.generateUniqueEmail();
		String serialNumberString = TextTool.getRandomString(10);
		String serialNumberString2 = TextTool.getRandomString(10);
		
		// link new account to a new device
		String newAccToken = createNewAccount(email);
		Pedometer pedometer = createNewPedometer(newAccToken, serialNumberString);
		Assert.assertFalse(StringUtils.isEmpty(pedometer.getServerId()), "ServerId is not null");
		
		// check linking status
		String linkingStatus = MVPApi.getDeviceLinkingStatus(newAccToken, serialNumberString);
		Assert.assertTrue(DefaultValues.DeviceLinkedToYourAccount.equals(linkingStatus), "Failed to link account and device!");
		
		// link this account to another device
		Pedometer pedometer2 = createNewPedometer(newAccToken, serialNumberString2);
		Assert.assertEquals(pedometer2.getServerId(), pedometer.getServerId(), "Server forces client update with same serverId pedometer");
		
		// check linking status
		linkingStatus = MVPApi.getDeviceLinkingStatus(newAccToken, serialNumberString2);
		Assert.assertEquals(linkingStatus, DefaultValues.DeviceNotLinkToAnyAccount, "Device not link to any account");
	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "pedometer" })
	public void LinkToAlreadyLinkedShine() {
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
	public void LinkToShineWhichWasUnlinkedBefore() {
		
		String serialNumberString = TextTool.getRandomString(10);

		// link
		String email = MVPApi.generateUniqueEmail();
		String token = createNewAccount(email);
		createNewPedometer(token, serialNumberString);
		
		// unlink
		MVPApi.unlinkDevice(token);
		
		// link again
		createNewPedometer(token, serialNumberString);

		String linkingStatus = MVPApi.getDeviceLinkingStatus(token, serialNumberString);
		Assert.assertTrue(DefaultValues.DeviceLinkedToYourAccount.equals(linkingStatus), "Linking status");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "pedometer" })
	public void UnlinkNoneLinkedDevice() {
		
		String email = MVPApi.generateUniqueEmail();
		String serialNumberString = TextTool.getRandomString(10);
		
		// link new account to a new device
		String newAccToken = createNewAccount(email);
		String linkingStatus = MVPApi.unlinkDevice(newAccToken);
		Assert.assertEquals(linkingStatus, DefaultValues.DeviceNotLinkToThisAccount, "Status for unlink non-linked device");
		
		// check linking status
		linkingStatus = MVPApi.getDeviceLinkingStatus(newAccToken, serialNumberString);
		Assert.assertTrue(DefaultValues.DeviceNotLinkToAnyAccount.equals(linkingStatus), "Status message for linking status");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "pedometer" })
	public void UnlinkAlreadyLinkedDevice() {
		
		String email = MVPApi.generateUniqueEmail();
		String serialNumberString = TextTool.getRandomString(10);
		
		// link new account to a new device
		String newAccToken = createNewAccount(email);
		createNewPedometer(newAccToken, serialNumberString);
		
		// check linking status
		String linkingStatus = MVPApi.getDeviceLinkingStatus(newAccToken, serialNumberString);
		Assert.assertEquals(linkingStatus, DefaultValues.DeviceLinkedToYourAccount, "Linking status for successful link");
		
		// unlink device using this account
		MVPApi.unlinkDevice(newAccToken);
		linkingStatus = MVPApi.getDeviceLinkingStatus(newAccToken, serialNumberString);
		Assert.assertEquals(linkingStatus, DefaultValues.DeviceUsedToLinkToYourAccount, "Linking status using this account message");
		
		// unlink device using different account
		String token2 = createNewAccount(MVPApi.generateUniqueEmail());
		linkingStatus = MVPApi.unlinkDevice(token2);
		Assert.assertEquals(linkingStatus, DefaultValues.DeviceNotLinkToThisAccount, "Unlink using different account message");
		
		// get status
		linkingStatus = MVPApi.getDeviceLinkingStatus(token2, serialNumberString);
		Assert.assertEquals(linkingStatus, DefaultValues.DeviceUsedToLinkToOtherAccount, "Linking status using different account message");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "pedometer" })
	public void UpdatePedometer() {
		
		// sign up and create pedometer
		String email = MVPApi.generateUniqueEmail();
		String serialNumberString = TextTool.getRandomString(10);
		
		// link new account to a new device
		String token = createNewAccount(email);
		Pedometer pedo = createNewPedometer(token, serialNumberString);
		
		// update pedometer
		pedo.setBookmarkState(3);
		pedo.setClockState(3);
		
		Pedometer r = MVPApi.updatePedometer(token, pedo.toJson());
		
		Assert.assertEquals(3, (int)r.getClockState(), "Clock state is updated");
		Assert.assertEquals(3, (int)r.getBookmarkState(), "Bookmark state is updated");
		
		// test cache on server
		int count = 0;
		for (int i = 0; i < MVPApi.CACHE_TRY_TIME; i++) {

			Pedometer getpedo = MVPApi.getPedometer(token);

			if (getpedo == null) {
				count++;
				continue;
			}

			if (getpedo.getBookmarkState() != 3 ||
				getpedo.getClockState() != 3)
				count++;
		}

		Assert.assertEquals(count, 0, "Fail count");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "pedometer" })
	public void UpdatePedometerToUseOtherSerialNumber() {
		
		// sign up and create pedometer
		String email = MVPApi.generateUniqueEmail();
		String serialNumberString = TextTool.getRandomString(10);
		String serialNumberString2 = TextTool.getRandomString(10);
		
		// link new account to a new device
		String token = createNewAccount(email);
		Pedometer pedo = createNewPedometer(token, serialNumberString);
		
		// update pedometer
		pedo.setSerialNumberString(serialNumberString2);
		Pedometer r = MVPApi.updatePedometer(token, pedo.toJson());
		
		Assert.assertEquals(r.getSerialNumberString(), serialNumberString2, "Serial number");
		Assert.assertTrue(r == null || r.getSerialNumberString() == serialNumberString, "Cannot update serial number or force client update");
				
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "pedometer" })
	public void UpdateNonExistedPedometer() {
		
		// sign up and update pedometer without creating it
		String email = MVPApi.generateUniqueEmail();
		
		String token = createNewAccount(email);
		BaseResult r = MVPApi.updatePedometer(token, DefaultValues.RandomPedometer());
		Pedometer pedo = Pedometer.getPedometer(r.response);
		
		Assert.assertEquals(r.statusCode, 404, "Status code");
		Assert.assertEquals(pedo, null, "Pedometer from result");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "pedometer" })
	public void GetLatestFirmwareVersion() {
		
		String firmwareVersion = MVPApi.getLatestFirmwareVersionString();
		Assert.assertEquals(firmwareVersion, MVPApi.LATEST_FIRMWARE_VERSION_STRING, "Latest firmware version string");
	}
	
	// helpers
	private Pedometer createNewPedometer(String token, String serialNumberString) {
		long timestamp = System.currentTimeMillis();
		Pedometer pedometer = MVPApi.createPedometer(token, serialNumberString, firmwareRevisionString, timestamp, null, timestamp, TextTool.getRandomString(19, 20), null, timestamp);
		return pedometer;
	}

	private String createNewAccount(String email) {
		AccountResult acc = MVPApi.signUp(email, password);
		return acc.token;
	}

}
