package com.misfit.ta.backend.aut.correctness.openapi.resourceapi;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.misfit.ta.Settings;
import com.misfit.ta.backend.api.internalapi.MVPApi;
import com.misfit.ta.backend.api.openapi.OpenAPI;
import com.misfit.ta.backend.aut.BackendAutomation;
import com.misfit.ta.backend.aut.DefaultValues;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.backend.data.DataGenerator;
import com.misfit.ta.backend.data.openapi.OpenAPIDevice;
import com.misfit.ta.backend.data.pedometer.Pedometer;
import com.misfit.ta.utils.TextTool;

public class OpenApiDeviceGetTC extends BackendAutomation {
	
	private String email = MVPApi.generateUniqueEmail();
	private Pedometer pedometer = DataGenerator.generateRandomPedometer(System.currentTimeMillis() / 1000, null);
	private String clientKey = Settings.getParameter("MVPOpenAPIClientID");
	
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		String token = MVPApi.signUp(email, "qqqqqq").token;
		MVPApi.createPedometer(token, pedometer);
	}
	

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "get_device" })
	public void GetDeviceUsingInvalidAccessToken() {
		
		// without access token
		BaseResult result = OpenAPI.getDevice(null, "me");
		Assert.assertEquals(result.statusCode, 401, "Status code");
		Assert.assertEquals(result.errorMessage, DefaultValues.InvalidAccessToken, "Error message");

		// invalid access token
		result = OpenAPI.getDevice(TextTool.getRandomString(10, 10), "me");
		Assert.assertEquals(result.statusCode, 401, "Status code");
		Assert.assertEquals(result.errorMessage, DefaultValues.InvalidAccessToken, "Error message");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "get_device" })
	public void GetDeviceWithoutPermission() {
		
		String accessToken = OpenAPI.getAccessToken(email, "qqqqqq", "public", OpenAPI.RESOURCE_PROFILE, clientKey);
		BaseResult result = OpenAPI.getDevice(accessToken, "me");
		Assert.assertEquals(result.statusCode, 403, "Status code");
		Assert.assertEquals(result.errorMessage, DefaultValues.ResourceForbidden, "Error message");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "get_device" })
	public void GetDeviceOfOtherUser() {
		
		// create new user
		String email2 = MVPApi.generateUniqueEmail();
		String token2 = MVPApi.signUp(email2, "qqqqqq").token;
		String userid = MVPApi.getUserId(token2);
		Pedometer pedometer2 = DataGenerator.generateRandomPedometer(System.currentTimeMillis() / 1000, null);
		MVPApi.createPedometer(token2, pedometer2);
		OpenAPI.getAccessToken(email2, "qqqqqq", "public", "profile", clientKey);
		
		// get that user's profile using access token from current user
		String accessToken = OpenAPI.getAccessToken(email, "qqqqqq", "public", OpenAPI.RESOURCE_DEVICES, clientKey);
		BaseResult result = OpenAPI.getDevice(accessToken, userid);
		Assert.assertEquals(result.statusCode, 403, "Status code");
		Assert.assertEquals(result.errorMessage, DefaultValues.ResourceForbidden, "Error message");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "get_device" })
	public void GetDeviceWithValidAccessToken() {
		
		String accessToken = OpenAPI.getAccessToken(email, "qqqqqq", "public", OpenAPI.RESOURCE_DEVICES, clientKey);
		BaseResult result = OpenAPI.getDevice(accessToken, "me");
		OpenAPIDevice rdevice = OpenAPIDevice.fromResponse(result.response);
		
		Assert.assertEquals(result.statusCode, 200, "Status code");
		Assert.assertEquals(rdevice.getDeviceType(), "Shine", "Device's type");
		Assert.assertEquals(rdevice.getSerialNumber(), pedometer.getSerialNumberString(), "Device's serial number");
		Assert.assertEquals(rdevice.getFirmwareVersion(), pedometer.getFirmwareRevisionString(), "Device's firmware version");
		Assert.assertEquals(rdevice.getBatteryLevel(), pedometer.getBatteryLevel(), "Device's battery level");
	}

}
