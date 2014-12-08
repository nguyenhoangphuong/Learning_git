package com.misfit.ta.backend.aut.correctness.backendapi;

import java.util.Random;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.backend.aut.BackendAutomation;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.utils.TextTool;

public class BackendSerialNumberIssue extends BackendAutomation{
	String token;
	String password = "qwerty";
	private static Random rnd = new Random();

	public static String getRandomNumber(int digCount) {
	    StringBuilder sb = new StringBuilder(digCount);
	    for(int i=0; i < digCount; i++)
	        sb.append((char)('0' + rnd.nextInt(10)));
	    return sb.toString();
	}
	
	private String getSerialNumber(BaseResult result) {
		
		return (String)result.getJsonResponseValue("serial_number");
	}
	
	private int getDuplicatedStatus(BaseResult result) {
		
		Integer status = (Integer)result.getJsonResponseValue("duplicated");
		return status;
	}
	
	@BeforeClass(alwaysRun = true)
	public void setUp(){
		token = MVPApi.signUp(MVPApi.generateUniqueEmail(), password).token;
	}
	
	@Test(groups = { "Prometheus", "MVPBackend", "api", "SerialNumber" })
	public void GenerateNewSerialNumberSuccessFully(){
		String serial = getRandomNumber(10);
		BaseResult result = MVPApi.issueNewSerialNumber(token, serial, "venus");
		Assert.assertEquals(result.statusCode, 200, "Generate SN for flash successfully");
		String serialNumber = getSerialNumber(result);
		
		// make sure the new sn is not in duplicated list
		result = MVPApi.getDeviceLinkingStatusRaw(token, serialNumber);
		Assert.assertEquals(getDuplicatedStatus(result), 0, "Duplicated status");
		
		serial = getRandomNumber(10);
		result = MVPApi.issueNewSerialNumber(token, serial, "shine");
		Assert.assertEquals(result.statusCode, 200, "Generate SN for flash successfully");
		serialNumber = getSerialNumber(result);
		
		// make sure the new sn is not in duplicated list
		result = MVPApi.getDeviceLinkingStatusRaw(token, serialNumber);
		Assert.assertEquals(getDuplicatedStatus(result), 0, "Duplicated status");
	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "api", "SerialNumber" })
	public void GenerateNewSerialNumberWithInvalidData(){
		// Invalid device type
		String serial = getRandomNumber(10);
		BaseResult result = MVPApi.issueNewSerialNumber(token, serial, TextTool.getRandomString(6));
		Assert.assertEquals(result.statusCode, 400, "Invalid Device Type");
		
		// Device type is empty
		result = MVPApi.issueNewSerialNumber(token, serial, " ");
		Assert.assertEquals(result.statusCode, 400, "Device type is empty");
		
		// Device type is null
		result = MVPApi.issueNewSerialNumber(token, serial, "");
		Assert.assertEquals(result.statusCode, 400, "Device type is null");
		
		// SerialNumber is empty
		result = MVPApi.issueNewSerialNumber(token, " ", "shine");
		Assert.assertEquals(result.statusCode, 400, "SN is empty");
		
		// SerialNumber is null
		result = MVPApi.issueNewSerialNumber(token, "", "shine");
		Assert.assertEquals(result.statusCode, 400, "SN is null");
		
		// SerialNumber and device type are null
		result = MVPApi.issueNewSerialNumber(token, "", "");
		Assert.assertEquals(result.statusCode, 400, "SN and device type are null");
		
		// SerialNumber 
		result = MVPApi.issueNewSerialNumber(token, "@@@@@", "");
		Assert.assertEquals(result.statusCode, 400, "SN is @@@@@");
	}
	
	@Test(groups = {"Prometheus", "MVPBackend", "api", "SerialNumber" })
	public void ConfirmSNWithInvalidData(){
		// SN is wrong data
		BaseResult result = MVPApi.confirmSerialNumber(token, "@@@@@");
		Assert.assertEquals(result.statusCode, 400, "SN is wrong data");
		
		// SN is null
		result = MVPApi.confirmSerialNumber(token, "");
		Assert.assertEquals(result.statusCode, 400, "SN is null");
		
		// SN is empty
		result = MVPApi.confirmSerialNumber(token, " ");
		Assert.assertEquals(result.statusCode, 400, "SN is empty");
		
		// SN is wrong data
		result = MVPApi.confirmSerialNumber(token, "SH0AZZ0000001");
		Assert.assertEquals(result.statusCode, 400, "SN is wrong data");

		// 
		String serial = getRandomNumber(10);
		result = MVPApi.issueNewSerialNumber(token, serial, "venus");
		Assert.assertEquals(result.statusCode, 200, "Generate SN for flash successfully");
		String serialNumber = getSerialNumber(result);
		
		result = MVPApi.confirmSerialNumber(token, serialNumber);
		Assert.assertEquals(result.statusCode, 200, "SN is wrong data");
	}
	
	@Test(groups = {"Prometheus", "MVPBackend", "api", "SerialNumber" })	
	public void GenerateSNWithInvalidToken(){
		BaseResult result = MVPApi.issueNewSerialNumber(MVPApi.generateLocalId(), "9876543210", "shine");
		Assert.assertEquals(result.statusCode, 401, "Invalid Token");
	}
}
