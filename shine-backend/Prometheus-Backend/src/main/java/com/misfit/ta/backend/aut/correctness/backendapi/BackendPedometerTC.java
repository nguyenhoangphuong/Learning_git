package com.misfit.ta.backend.aut.correctness.backendapi;

import org.apache.commons.lang.StringUtils;
import org.testng.annotations.Test;

import com.misfit.ta.android.DeviceManager;
import com.misfit.ta.backend.api.internalapi.MVPApi;
import com.misfit.ta.backend.aut.BackendAutomation;
import com.misfit.ta.backend.aut.DefaultValues;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.backend.data.DataGenerator;
import com.misfit.ta.backend.data.account.AccountResult;
import com.misfit.ta.backend.data.pedometer.Pedometer;
import com.misfit.ta.utils.TextTool;
import com.sun.jna.platform.unix.X11.XClientMessageEvent.Data;

import org.testng.Assert;

public class BackendPedometerTC extends BackendAutomation {

	private String password = "test12";

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "pedometer" })
	public void CreateDuplicatePedometer() {
		
		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), "qqqqqq").token;
		Pedometer pedo = DataGenerator.generateRandomPedometer(System.currentTimeMillis() / 1000, null);
		
		BaseResult result = MVPApi.createPedometer(token, pedo);
		Assert.assertEquals(result.statusCode, 200, "Status code");
		
		pedo.setLastSuccessfulSyncedTime(System.currentTimeMillis() / 1000 + 1);
		pedo.setUnlinkedTime(System.currentTimeMillis() / 1000 + 2);
		result = MVPApi.createPedometer(token, pedo);
		Pedometer spedo = Pedometer.getPedometer(result.response);
		
		Assert.assertTrue(result.statusCode != 210, "Status code");
		Assert.assertEquals(pedo.getLocalId(), spedo.getLocalId(), "Local id");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "pedometer" })
	public void UpdatePedometerFullParam() {
		
		// create pedometer
		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), "qqqqqq").token;
		Pedometer pedo = DataGenerator.generateRandomPedometer(System.currentTimeMillis() / 1000, null);
		
		BaseResult result = MVPApi.createPedometer(token, pedo);
		Assert.assertEquals(result.statusCode, 200, "Status code");
		
		Pedometer spedo = Pedometer.getPedometer(result.response);
		pedo.setServerId(spedo.getServerId());
		pedo.setUpdatedAt(spedo.getUpdatedAt());
		pedo.setLocalId(spedo.getLocalId());
		
		
		// update normal flow and test cache
		long currentTimestamp = System.currentTimeMillis() / 1000;
		for(int i = 0; i < MVPApi.CACHE_TRY_TIME; i++) {
			
			pedo.setLastSuccessfulSyncedTime(currentTimestamp++);
			pedo.setUnlinkedTime(currentTimestamp++);
			result = MVPApi.updatePedometer(token, pedo);
			Assert.assertEquals(result.statusCode, 200, "Status code");
			pedo.setUpdatedAt(Pedometer.getPedometer(result.response).getUpdatedAt());
			
			spedo = MVPApi.getPedometer(token);
			Assert.assertEquals(spedo.getLocalId(), pedo.getLocalId(), "Local id");
			Assert.assertEquals(spedo.getLastSuccessfulSyncedTime(), pedo.getLastSuccessfulSyncedTime(), "Last successful time");
			Assert.assertEquals(spedo.getUnlinkedTime(), pedo.getUnlinkedTime(), "Unlinked time");
			
			spedo = Pedometer.getPedometer(MVPApi.userInfo(token).response);
			Assert.assertEquals(spedo.getLocalId(), pedo.getLocalId(), "Local id");
			Assert.assertEquals(spedo.getLastSuccessfulSyncedTime(), pedo.getLastSuccessfulSyncedTime(), "Last successful time");
			Assert.assertEquals(spedo.getUnlinkedTime(), pedo.getUnlinkedTime(), "Unlinked time");
		}
		
		// update force update
		for(int i = 0; i < MVPApi.CACHE_TRY_TIME; i++) {
			
			pedo.setLastSuccessfulSyncedTime(currentTimestamp++);
			pedo.setUnlinkedTime(currentTimestamp++);
			pedo.setUpdatedAt(0);
			result = MVPApi.updatePedometer(token, pedo);
			Assert.assertEquals(result.statusCode, 210, "Status code");
			
			spedo = Pedometer.getPedometer(result.response);
			Assert.assertEquals(spedo.getLocalId(), pedo.getLocalId(), "Local id");
			Assert.assertEquals(spedo.getLastSuccessfulSyncedTime(), pedo.getLastSuccessfulSyncedTime(), "Last successful time");
			Assert.assertEquals(spedo.getUnlinkedTime(), pedo.getUnlinkedTime(), "Unlinked time");
			
			spedo = MVPApi.getPedometer(token);
			Assert.assertEquals(spedo.getLocalId(), pedo.getLocalId(), "Local id");
			Assert.assertEquals(spedo.getLastSuccessfulSyncedTime(), pedo.getLastSuccessfulSyncedTime(), "Last successful time");
			Assert.assertEquals(spedo.getUnlinkedTime(), pedo.getUnlinkedTime(), "Unlinked time");
			
			spedo = Pedometer.getPedometer(MVPApi.userInfo(token).response);
			Assert.assertEquals(spedo.getLocalId(), pedo.getLocalId(), "Local id");
			Assert.assertEquals(spedo.getLastSuccessfulSyncedTime(), pedo.getLastSuccessfulSyncedTime(), "Last successful time");
			Assert.assertEquals(spedo.getUnlinkedTime(), pedo.getUnlinkedTime(), "Unlinked time");
		}
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "pedometer" })
	public void LinkOneAccountToOneShine() {
		
		String email = MVPApi.generateUniqueEmail();
		String serialNumberString = TextTool.getRandomString(10, 10);
		
		// link new account to a new device
		String newAccToken = createNewAccount(email);
		Pedometer pedometer = createNewPedometer(newAccToken, serialNumberString);
		Assert.assertFalse(StringUtils.isEmpty(pedometer.getServerId()), "ServerId is not empty");
		
		// check linking status
		String linkingStatus = MVPApi.getDeviceLinkingStatus(newAccToken, serialNumberString);
		Assert.assertEquals(linkingStatus, DefaultValues.DeviceLinkedToYourAccount, "Linking status");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "pedometer" })
	public void LinkOneAccountToTwoShines() {
		
		String email = MVPApi.generateUniqueEmail();
		String serialNumberString = TextTool.getRandomString(10, 10);
		String serialNumberString2 = TextTool.getRandomString(10, 10);
		
		// link new account to a new device
		String newAccToken = createNewAccount(email);
		Pedometer pedometer = createNewPedometer(newAccToken, serialNumberString);
		Assert.assertFalse(StringUtils.isEmpty(pedometer.getServerId()), "ServerId is not empty");
		
		// check linking status
		String linkingStatus = MVPApi.getDeviceLinkingStatus(newAccToken, serialNumberString);
		Assert.assertEquals(linkingStatus, DefaultValues.DeviceLinkedToYourAccount, "Linking status");
		
		// link this account to another device
		Pedometer pedometer2 = createNewPedometer(newAccToken, serialNumberString2);
		Assert.assertTrue(pedometer2.getServerId() != pedometer.getServerId(), "ServerId is the same");
		
		// check linking status
		linkingStatus = MVPApi.getDeviceLinkingStatus(newAccToken, serialNumberString2);
		Assert.assertEquals(linkingStatus, DefaultValues.DeviceLinkedToYourAccount, "Wrong message!");
	}

//	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "demo" })
//	public void demoLinkingAccountToShine(){
//		String email = MVPApi.generateUniqueEmail();
//		String serialNumberString = TextTool.getRandomString(10, 10);
//		System.out.println("Serial Number 1 : " + serialNumberString);
//		String serialNumberString2 = TextTool.getRandomString(10, 10);
//		System.out.println("Serial Number 2 : " + serialNumberString2);
//		
//		//Link account to the first shine
//		String token = MVPApi.signUp(email, "qwerty").token;
//		Pedometer pedometer = DataGenerator.generateRandomPedometer(System.currentTimeMillis(), null);
//		pedometer.setSerialNumberString(serialNumberString);
//		BaseResult res1 = MVPApi.createPedometer(token, pedometer);
//		
//		//Check linking status
//		String status = MVPApi.getDeviceLinkingStatus(token, serialNumberString);
//		System.out.println("Linking status of the first shine : " + status);
//		
//		//Link that account to the second shine
//		//Can't not update serial number
////		pedometer.setSerialNumberString(serialNumberString2);
////		BaseResult result = MVPApi.updatePedometer(token, pedometer);
////		System.out.println("Result : " + result.statusCode);
//		Pedometer pedometer2 = createNewPedometer(token, serialNumberString2);
//		System.out.println("ServerID of pedometer1 : " + Pedometer.getPedometer(res1.response).getServerId());
//		System.out.println("ServerID of pedometer2 : " + pedometer2.getServerId());
//		status = MVPApi.getDeviceLinkingStatus(token, serialNumberString2);
//		System.out.println("Linking status of the second shine : " + status);
//	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "pedometer" })
	public void LinkToAlreadyLinkedShine() {
		String serialNumberString = TextTool.getRandomString(10, 10);

		// link device to account 1
		String email1 = MVPApi.generateUniqueEmail();
		String newAccToken1 = createNewAccount(email1);
		createNewPedometer(newAccToken1, serialNumberString);

		String email2 = MVPApi.generateUniqueEmail();
		String newAccToken2 = createNewAccount(email2);
		
		// account 2 steals device
		Pedometer pedo = DataGenerator.generateRandomPedometer(System.currentTimeMillis() / 1000, null);
		pedo.setSerialNumberString(serialNumberString);
		MVPApi.createPedometer(newAccToken2, pedo);
		
		// check linking status
		String linkingStatus = MVPApi.getDeviceLinkingStatus(newAccToken1, serialNumberString);
		Assert.assertTrue(DefaultValues.DeviceLinkedToYourAccount.equals(linkingStatus), "Failed to unlink account and device!");

		linkingStatus = MVPApi.getDeviceLinkingStatus(newAccToken2, serialNumberString);
		Assert.assertTrue(DefaultValues.DeviceLinkedToAnotherAccount.equals(linkingStatus), "Failed to steal device!");
	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "pedometer", "KnownIssue" })
	public void LinkToShineWhichWasUnlinkedBefore() {
		
		String serialNumberString = TextTool.getRandomString(10, 10);

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
		String serialNumberString = TextTool.getRandomString(10, 10);
		
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
		String serialNumberString = TextTool.getRandomString(10, 10);
		
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
		
		// create pedometer
		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), "qqqqqq").token;
		Pedometer cpedo = DataGenerator.generateRandomPedometer(System.currentTimeMillis() / 1000, null);

		BaseResult result = MVPApi.createPedometer(token, cpedo);
		Assert.assertEquals(result.statusCode, 200, "Status code");

		Pedometer spedo = Pedometer.getPedometer(result.response);
		cpedo.setServerId(spedo.getServerId());
		cpedo.setUpdatedAt(spedo.getUpdatedAt());
		cpedo.setLocalId(spedo.getLocalId());
		
		
		// update normal flow and test cache
		int clockState = 0;
		int bookmarkState = 0;
		for(int i = 0; i < MVPApi.CACHE_TRY_TIME; i++) {
			
			Pedometer pedo = new Pedometer();
			pedo.setClockState(++clockState % 4);
			pedo.setBookmarkState(++bookmarkState % 4);
			pedo.setUpdatedAt(cpedo.getUpdatedAt());
			result = MVPApi.updatePedometer(token, pedo);
			Assert.assertEquals(result.statusCode, 200, "Status code");
			cpedo.setUpdatedAt(Pedometer.getPedometer(result.response).getUpdatedAt());
			
			spedo = MVPApi.getPedometer(token);
			Assert.assertEquals(spedo.getLocalId(), cpedo.getLocalId(), "Local id");
			Assert.assertEquals(spedo.getClockState(), pedo.getClockState(), "Clock state");
			Assert.assertEquals(spedo.getBookmarkState(), pedo.getBookmarkState(), "Bookmark state");
			
			spedo = Pedometer.getPedometer(MVPApi.userInfo(token).response);
			Assert.assertEquals(spedo.getLocalId(), cpedo.getLocalId(), "Local id");
			Assert.assertEquals(spedo.getClockState(), pedo.getClockState(), "Clock state");
			Assert.assertEquals(spedo.getBookmarkState(), pedo.getBookmarkState(), "Bookmark state");
		}
		
		// update force update
		for(int i = 0; i < MVPApi.CACHE_TRY_TIME; i++) {
			
			Pedometer pedo = new Pedometer();
			pedo.setClockState(++clockState % 4);
			pedo.setBookmarkState(++bookmarkState % 4);
			result = MVPApi.updatePedometer(token, pedo);
			
			spedo = Pedometer.getPedometer(result.response);
			Assert.assertEquals(spedo.getLocalId(), cpedo.getLocalId(), "Local id");
			Assert.assertEquals(spedo.getClockState(), pedo.getClockState(), "Clock state");
			Assert.assertEquals(spedo.getBookmarkState(), pedo.getBookmarkState(), "Bookmark state");
			
			spedo = MVPApi.getPedometer(token);
			Assert.assertEquals(spedo.getLocalId(), cpedo.getLocalId(), "Local id");
			Assert.assertEquals(spedo.getClockState(), pedo.getClockState(), "Clock state");
			Assert.assertEquals(spedo.getBookmarkState(), pedo.getBookmarkState(), "Bookmark state");
			
			spedo = Pedometer.getPedometer(MVPApi.userInfo(token).response);
			Assert.assertEquals(spedo.getLocalId(), cpedo.getLocalId(), "Local id");
			Assert.assertEquals(spedo.getClockState(), pedo.getClockState(), "Clock state");
			Assert.assertEquals(spedo.getBookmarkState(), pedo.getBookmarkState(), "Bookmark state");
		}
		
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "pedometer", "KnownIssue" })
	public void UpdatePedometerToUseOtherSerialNumber() {
		
		// sign up and create pedometer
		String email = MVPApi.generateUniqueEmail();
		String serialNumberString = TextTool.getRandomString(10, 10);
		String serialNumberString2 = TextTool.getRandomString(10, 10);
		
		// link new account to a new device
		String token = createNewAccount(email);
		Pedometer pedo = createNewPedometer(token, serialNumberString);
		
		// update pedometer
		pedo.setSerialNumberString(serialNumberString2);
		Pedometer r = MVPApi.updatePedometer(token, pedo.toJson());
		
		Assert.assertEquals(r.getSerialNumberString(), serialNumberString2, "Serial number");
		Assert.assertTrue(r != null, "Cannot update serial number or force client update");
		Assert.assertTrue(r.getSerialNumberString() != serialNumberString, "Cannot update serial number or force client update");
				
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "pedometer" })
	public void UpdateNonExistedPedometer() {
		
		// sign up and update pedometer without creating it
		String email = MVPApi.generateUniqueEmail();
		
		String token = createNewAccount(email);
		BaseResult r = MVPApi.updatePedometer(token, DefaultValues.RandomPedometer());
		Pedometer pedo = Pedometer.getPedometer(r.response);
		
		Assert.assertEquals(r.statusCode, 400, "Status code");
		Assert.assertEquals(pedo, null, "Pedometer from result");
	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "multipledevices" })
	public void LinkMultipleDevices(){
		String email = MVPApi.generateUniqueEmail();
		String pass = "qwerty";
		//SignUp account
		String token = MVPApi.signUp(email, pass).token;
		//Create the first pedometer with first shine
		Pedometer pedometerItem = DataGenerator.generateRandomPedometer(System.currentTimeMillis(), null);
		pedometerItem.setDeviceType(DataGenerator.SHINE_FLASH);
		BaseResult baseResult = MVPApi.createPedometer(token, pedometerItem);
		Assert.assertTrue(baseResult.isOK(), "Link shine failed!");
		
		String status = MVPApi.getDeviceLinkingStatus(token, pedometerItem.getSerialNumberString());
		Assert.assertEquals(status, DefaultValues.DeviceLinkedToYourAccount, "Link failed with the first shine!");
		
		//Create the second pedometer with beddit
		pedometerItem = DataGenerator.generateRandomPedometer(System.currentTimeMillis(), null);
		pedometerItem.setDeviceType(DataGenerator.PEBBLE);
		pedometerItem.setIsCurrent(true);
		baseResult = MVPApi.createPedometer(token, pedometerItem);
		Assert.assertTrue(baseResult.isOK(), "Link pebble failed!");

		//Create the third pedometer with shine
		pedometerItem = DataGenerator.generateRandomPedometer(System.currentTimeMillis(), null);
		pedometerItem.setDeviceType(DataGenerator.SHINE_FLASH);
		baseResult = MVPApi.createPedometer(token, pedometerItem);
		Assert.assertTrue(baseResult.isOK(), "Link shine failed!");
		
		//Create the fourth pedometer with shine
		pedometerItem = DataGenerator.generateRandomPedometer(System.currentTimeMillis(), null);
		pedometerItem.setDeviceType(DataGenerator.SHINE_FLASH);
		baseResult = MVPApi.createPedometer(token, pedometerItem);
		Assert.assertTrue(baseResult.isOK(), "Link shine failed!");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "pedometer" })
	public void GetLatestFirmwareVersion() {
		
		String firmwareVersion = MVPApi.getLatestFirmwareVersionString();
		Assert.assertEquals(firmwareVersion, MVPApi.LATEST_FIRMWARE_VERSION_STRING, "Latest firmware version string");
	}
	
	// helpers
	private Pedometer createNewPedometer(String token, String serialNumberString) {
		
		long timestamp = System.currentTimeMillis();
		Pedometer pedometer = DataGenerator.generateRandomPedometer(timestamp, null);
		pedometer.setSerialNumberString(serialNumberString);
		
		BaseResult result = MVPApi.createPedometer(token, pedometer);
		return Pedometer.getPedometer(result.response);
	}

	private String createNewAccount(String email) {
		AccountResult acc = MVPApi.signUp(email, password);
		return acc.token;
	}

}
