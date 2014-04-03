package com.misfit.ta.backend.aut.correctness.backendapi;

import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.misfit.ta.backend.api.internalapi.MVPApi;
import com.misfit.ta.backend.aut.BackendAutomation;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.common.MVPCommon;
import com.misfit.ta.utils.TextTool;

public class BackendDuplicatedSerialNumberTC extends BackendAutomation {

	private static String[] DSNList = new String[]  {"SH0AZ00Y9Q", "SH0AZ00XZH", "SH0HZ001K3", "SH0AZ01U7F"};
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
		Assert.assertNotEquals(getDuplicatedStatus(result), 0, "Duplicated status");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "ShineAPI_DSN" })
	public void BackendDSNGenerateNewSerialNumber() {
		
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
		
		// make sure that the new sn is not duplicated each other
		for(int i = 0; i < 4; i++) {
			result = MVPApi.generateNewSerialNumber(token);
			serialNumber = getSerialNumber(result);
			for(String sn : allSerialNumber) {
				if(sn.equals(serialNumber))
					Assert.fail("The new serial number is duplicated with old ones");
			}
		
			result = MVPApi.getDeviceLinkingStatusRaw(token, serialNumber);
			Assert.assertEquals(getDuplicatedStatus(result), 0, "Duplicated status");
			allSerialNumber.add(serialNumber);
		}
		
		// make sure that a user can issue maximum 5 new sn
		result = MVPApi.generateNewSerialNumber(token);
		Assert.assertEquals(result.statusCode, 400, "Status code");
		Assert.assertEquals(result.errorCode, 100, "Error code");
	}
	
	private int getDuplicatedStatus(BaseResult result) {
		
		Integer status = (Integer)result.getJsonResponseValue("duplicated");
		return status;
	}

	private String getSerialNumber(BaseResult result) {
		
		return (String)result.getJsonResponseValue("serial_number");
	}
	
}
