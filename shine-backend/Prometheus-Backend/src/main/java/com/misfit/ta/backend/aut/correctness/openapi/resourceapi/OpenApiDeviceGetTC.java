package com.misfit.ta.backend.aut.correctness.openapi.resourceapi;


import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.misfit.ta.backend.api.MVPApi;
import com.misfit.ta.backend.api.openapi.OpenAPI;
import com.misfit.ta.backend.aut.DefaultValues;
import com.misfit.ta.backend.aut.correctness.openapi.OpenAPIAutomationBase;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.backend.data.DataGenerator;
import com.misfit.ta.backend.data.openapi.resourceapi.OpenAPIDevice;
import com.misfit.ta.backend.data.pedometer.Pedometer;
import com.misfit.ta.utils.TextTool;

public class OpenApiDeviceGetTC extends OpenAPIAutomationBase {
	
	private String accessToken;
	private Pedometer pedometer = DataGenerator.generateRandomPedometer(System.currentTimeMillis() / 1000, null);
	
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		
		super.beforeClass();
		
		MVPApi.createPedometer(myToken, pedometer);
		MVPApi.createPedometer(yourToken, DataGenerator.generateRandomPedometer(System.currentTimeMillis() / 1000, null));
		MVPApi.createPedometer(strangerToken, DataGenerator.generateRandomPedometer(System.currentTimeMillis() / 1000, null));
		
		accessToken = OpenAPI.getAccessToken(myEmail, "qqqqqq", OpenAPI.RESOURCE_DEVICE, ClientKey, "http://misfit.com/");
	}
	

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "get_device" })
	public void GetDeviceUsingInvalidAccessToken() {
		
		// without access token
		String nullString = null;
		BaseResult result = OpenAPI.getDevice(nullString, "me");
		Assert.assertEquals(result.statusCode, 401, "Status code");
		Assert.assertEquals(result.code, 401, "OpenAPI code");
		Assert.assertEquals(result.message, DefaultValues.InvalidAccessToken, "Error message");

		// invalid access token
		result = OpenAPI.getDevice(TextTool.getRandomString(10, 10), "me");
		Assert.assertEquals(result.statusCode, 401, "Status code");
		Assert.assertEquals(result.code, 401, "OpenAPI code");
		Assert.assertEquals(result.message, DefaultValues.InvalidAccessToken, "Error message");
	}
	
	
	
		
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "get_device" })
	public void GetDeviceWithValidAccessToken() {
		
		// use "me" route
		BaseResult result = OpenAPI.getDevice(accessToken, "me");
		OpenAPIDevice rdevice = OpenAPIDevice.fromResponse(result.response);
		
		Assert.assertEquals(result.statusCode, 200, "Status code");
		Assert.assertEquals(rdevice.getDeviceType(), "shine", "Device's type");
		Assert.assertEquals(rdevice.getSerialNumber(), pedometer.getSerialNumberString(), "Device's serial number");
		Assert.assertEquals(rdevice.getFirmwareVersion(), pedometer.getFirmwareRevisionString(), "Device's firmware version");
		Assert.assertEquals(rdevice.getBatteryLevel(), pedometer.getBatteryLevel(), "Device's battery level");
		
		
		// use "myUid" route
		result = OpenAPI.getDevice(accessToken, myUid);
		rdevice = OpenAPIDevice.fromResponse(result.response);
		
		Assert.assertEquals(result.statusCode, 200, "Status code");
		Assert.assertEquals(rdevice.getDeviceType(), "shine", "Device's type");
		Assert.assertEquals(rdevice.getSerialNumber(), pedometer.getSerialNumberString(), "Device's serial number");
		Assert.assertEquals(rdevice.getFirmwareVersion(), pedometer.getFirmwareRevisionString(), "Device's firmware version");
		Assert.assertEquals(rdevice.getBatteryLevel(), pedometer.getBatteryLevel(), "Device's battery level");
	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "get_device", "Excluded" })
	public void GetDeviceWithoutPermission() {
		
		String invalidScopeAccessToken = OpenAPI.getAccessToken(myEmail, "qqqqqq", OpenAPI.RESOURCE_PROFILE, ClientKey, "http://misfit.com/");
		BaseResult result = OpenAPI.getDevice(invalidScopeAccessToken, "me");
		Assert.assertEquals(result.statusCode, 403, "Status code");
		Assert.assertEquals(result.code, 403, "OpenAPI code");
		Assert.assertEquals(result.message, DefaultValues.ResourceForbidden, "Error message");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "get_device" })
	public void GetDeviceOfOtherUser() {
				
		// from other authorized user
		BaseResult result = OpenAPI.getDevice(accessToken, yourUid);
		Assert.assertEquals(result.statusCode, 403, "Status code");
		Assert.assertEquals(result.code, 403, "OpenAPI code");
		Assert.assertEquals(result.message, DefaultValues.ResourceForbidden, "Error message");
		
		// from unauthorized user
		result = OpenAPI.getDevice(accessToken, strangerUid);
		Assert.assertEquals(result.statusCode, 403, "Status code");
		Assert.assertEquals(result.code, 403, "OpenAPI code");
		Assert.assertEquals(result.message, DefaultValues.ResourceForbidden, "Error message");
	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "get_device" })
	public void GetDeviceUsingAppCredential() {
		
		// get from authorized user
		BaseResult result = OpenAPI.getDevice(ClientApp, myUid);
		OpenAPIDevice rdevice = OpenAPIDevice.fromResponse(result.response);
		
		Assert.assertEquals(result.statusCode, 200, "Status code");
		Assert.assertEquals(rdevice.getDeviceType(), "shine", "Device's type");
		Assert.assertEquals(rdevice.getSerialNumber(), pedometer.getSerialNumberString(), "Device's serial number");
		Assert.assertEquals(rdevice.getFirmwareVersion(), pedometer.getFirmwareRevisionString(), "Device's firmware version");
		Assert.assertEquals(rdevice.getBatteryLevel(), pedometer.getBatteryLevel(), "Device's battery level");
		
		
		// get from unauthorized user
		result = OpenAPI.getDevice(ClientApp, strangerUid);
		
		Assert.assertEquals(result.statusCode, 403, "Status code");
		Assert.assertEquals(result.code, 403, "OpenAPI code");
		Assert.assertEquals(result.message, DefaultValues.UnauthorizedAccess, "Error message");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "get_device" })
	public void GetDeviceWhichWasUnlinked() {
		
		// unlink
		MVPApi.unlinkDevice(myToken);
		
		// get device
		BaseResult result = OpenAPI.getDevice(accessToken, "me");
		OpenAPIDevice rdevice = OpenAPIDevice.fromResponse(result.response);
		
		// link again so other tests can run
		MVPApi.updatePedometer(myToken, pedometer);

		Assert.assertEquals(result.statusCode, 200, "Status code");
		Assert.assertNull(rdevice.getDeviceType(), "Device's type");
		Assert.assertNull(rdevice.getSerialNumber(), "Device's serial number");
		Assert.assertNull(rdevice.getFirmwareVersion(), "Device's firmware version");
		Assert.assertNull(rdevice.getBatteryLevel(), "Device's battery level");
	}
	
	/*
	 * TODO:
	 * - Get an unlinked shine (serial number == UNLINKED)
	 * - Get a resource using expired token
	 * - Get a resource with only selected fields
	 */
}
