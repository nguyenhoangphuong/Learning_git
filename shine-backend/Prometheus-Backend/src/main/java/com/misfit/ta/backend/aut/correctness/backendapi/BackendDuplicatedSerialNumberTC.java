package com.misfit.ta.backend.aut.correctness.backendapi;

import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.misfit.ta.backend.api.metawatch.MVPApi;
import com.misfit.ta.backend.aut.BackendAutomation;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.common.MVPCommon;
import com.misfit.ta.utils.TextTool;

public class BackendDuplicatedSerialNumberTC extends BackendAutomation {

	private static String[] DSNList = new String[]  {"SH0AZ00Y9Q", "SH0AZ00XZH", "SH0HZ001K3", "SH0BZ000ZA"};
	private static String FactoryPrototypeSN = "9876543210";
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "ShineAPI_DSN" })
	public void BackendDSNGetLinkingStatus() {
		
		// normal sn
		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), "qqqqqq").token;
		String serialNumberString = TextTool.getRandomString(10, 10);
		BaseResult result = MVPApi.getDeviceLinkingStatusRaw(token, serialNumberString);

		Assert.assertEquals(result.statusCode, 200, "Status code");
		Assert.assertEquals(getDuplicatedStatus(result), 0, "Duplicated status");
		
		// duplicated sn
		serialNumberString = DSNList[MVPCommon.randInt(0, DSNList.length - 1)];
		result = MVPApi.getDeviceLinkingStatusRaw(token, serialNumberString);

		Assert.assertEquals(result.statusCode, 200, "Status code");
		Assert.assertNotEquals(getDuplicatedStatus(result), 0, "Duplicated status"); 
		
		// factory sn
		serialNumberString = FactoryPrototypeSN;
		result = MVPApi.getDeviceLinkingStatusRaw(token, serialNumberString);

		Assert.assertEquals(result.statusCode, 200, "Status code");
		Assert.assertTrue(getDuplicatedStatus(result) == 0 && getDeviceLinkingStatus(result) == 1, "Duplicated status");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "ShineAPI_DSN" })
	public void BackendDSNGenerateNewSerialNumberWithDefaultParams() {
		//device type is "shine" and serial number is 9876543210
		List<String> allSerialNumber = new ArrayList<String>();
		
		// get new sn
		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), "qqqqqq").token;
		BaseResult result = MVPApi.generateNewSerialNumber(token);
		Assert.assertEquals(result.statusCode, 200, "Status code");
		
		String serialNumber = getSerialNumber(result);
		allSerialNumber.add(serialNumber);
		
		// make sure the new sn is not in duplicated list
		result = MVPApi.getDeviceLinkingStatusRaw(token, serialNumber);
		Assert.assertEquals(getDuplicatedStatus(result), 0, "Duplicated status");
		
		//NOTE: we don't have the 5 serial number limit anymore, we can request as many new SN as we can

	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "ShineAPI_DSN" })
	public void BackendDSNGenerateNewSerialNumberForShine() {
		List<String> allSerialNumber = new ArrayList<String>();
		
		// get new sn
		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), "qqqqqq").token;
		BaseResult result = MVPApi.generateNewSerialNumber(token, "9876543210", "shine");
		String serialNumber = getSerialNumber(result);
		result = MVPApi.getDeviceLinkingStatusRaw(token, serialNumber);
		Assert.assertTrue(getDuplicatedStatus(result) == 0 && getDeviceLinkingStatus(result) == 1, "Duplicated status");
		allSerialNumber.add(serialNumber);
		// this one should get the default color code Z as black device
		Assert.assertEquals(serialNumber.substring(0, 3), "SH0", "New serial number is Shine number");
		Assert.assertEquals(serialNumber.charAt(3), 'Z', "Default color should be black");
		
		// make sure that the new sn is not duplicated each other
		for(int i = 0; i < DSNList.length; i++) {
			String originalSerialNumber = DSNList[i];
			result = MVPApi.generateNewSerialNumber(token, originalSerialNumber, "shine");
			serialNumber = getSerialNumber(result);
			if (allSerialNumber.size() > 1 && allSerialNumber.contains(serialNumber)) {
					Assert.fail("The new serial number is duplicated with old ones");
			}
			// make sure the new sn is not in duplicated list
			result = MVPApi.getDeviceLinkingStatusRaw(token, serialNumber);
			Assert.assertTrue(getDuplicatedStatus(result) == 0 && getDeviceLinkingStatus(result) == 1, "Duplicated status");
			allSerialNumber.add(serialNumber);
			
			//TODO: check if the new serial number keeps the color code
			Assert.assertEquals(serialNumber.substring(0, 3), "SH0", "New serial number is Shine number");
			Assert.assertEquals(serialNumber.charAt(3), originalSerialNumber.charAt(3), "Color code should be kept after we issue new serial number");
		}
		//NOTE: we don't have the 5 serial number limit anymore, we can request as many new SN as we can

	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "ShineAPI_DSN" })
	public void BackendDSNGenerateNewSerialNumberForFlash() {
		List<String> allSerialNumber = new ArrayList<String>();
		// Flash only has the DSN case with serial number 9876543210 for now.
		// get new sn
		String token = MVPApi.signUp(MVPApi.generateUniqueEmail(), "qqqqqq").token;
		
		// make sure that the new sn is not duplicated each other
		for(int i = 0; i < 6; i++) {
			BaseResult result = MVPApi.generateNewSerialNumber(token, "9876543210", "venus");
			String serialNumber = getSerialNumber(result);
			if (allSerialNumber.size() > 1 && allSerialNumber.contains(serialNumber)) {
					Assert.fail("The new serial number is duplicated with old ones");
			}
			// make sure the new sn is not in duplicated list
			result = MVPApi.getDeviceLinkingStatusRaw(token, serialNumber);
			Assert.assertTrue(getDuplicatedStatus(result) == 0 && getDeviceLinkingStatus(result) == 1, "Duplicated status");
			allSerialNumber.add(serialNumber);
			
			//TODO: check if the new serial number keeps the color code
			Assert.assertEquals(serialNumber.substring(0, 3), "FZ0", "New serial number is Flash number");
			Assert.assertEquals(serialNumber.charAt(3), 'Z', "Default color should be black");		}
		//NOTE: we don't have the 5 serial number limit anymore, we can request as many new SN as we can

	}
	
	
	private int getDuplicatedStatus(BaseResult result) {
		
		Integer status = (Integer)result.getJsonResponseValue("duplicated");
		return status;
	}

	private String getSerialNumber(BaseResult result) {
		return (String)result.getJsonResponseValue("serial_number");
	}
	
	private Integer getDeviceLinkingStatus(BaseResult result) {
		Integer status = (Integer)result.getJsonResponseValue("result");
		return status;
	}
	
}
