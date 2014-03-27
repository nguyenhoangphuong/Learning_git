package com.misfit.ta.backend.aut.correctness.openapi.resourceapi;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.misfit.ta.backend.api.internalapi.MVPApi;
import com.misfit.ta.backend.api.openapi.OpenAPI;
import com.misfit.ta.backend.aut.DefaultValues;
import com.misfit.ta.backend.aut.correctness.openapi.OpenAPIAutomationBase;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.backend.data.DataGenerator;
import com.misfit.ta.backend.data.openapi.resourceapi.OpenAPIProfile;
import com.misfit.ta.backend.data.profile.ProfileData;
import com.misfit.ta.utils.TextTool;

public class OpenApiProfileGetTC extends OpenAPIAutomationBase {

	private String accessToken;
	private ProfileData profile = DataGenerator.generateRandomProfile(System.currentTimeMillis() / 1000, null);
	
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		
		super.beforeClass();
		
		MVPApi.createProfile(myToken, profile);
		MVPApi.createProfile(yourToken, profile);
		MVPApi.createProfile(strangerToken, profile);
		
		accessToken = OpenAPI.getAccessToken(myEmail, "qqqqqq", OpenAPI.RESOURCE_PROFILE, ClientKey, "https://www.google.com.vn/");
	}
	
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "get_profile" })
	public void GetProfileUsingInvalidAccessToken() {
		
		// empty access token
		String nullString = null;
		BaseResult result = OpenAPI.getProfile(nullString, "me");
		Assert.assertEquals(result.statusCode, 401, "Status code");
		Assert.assertEquals(result.code, 401, "OpenAPI code");
		Assert.assertEquals(result.message, DefaultValues.InvalidAccessToken, "Error message");

		// invalid access token
		result = OpenAPI.getProfile(TextTool.getRandomString(10, 10), "me");
		Assert.assertEquals(result.statusCode, 401, "Status code");
		Assert.assertEquals(result.code, 401, "OpenAPI code");
		Assert.assertEquals(result.message, DefaultValues.InvalidAccessToken, "Error message");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "get_profile" })
	public void GetProfileWithValidAccessToken() {
		
		// use "me" route
		BaseResult result = OpenAPI.getProfile(accessToken, "me");
		OpenAPIProfile data = OpenAPIProfile.fromResponse(result.response);
		
		Assert.assertEquals(result.statusCode, 200, "Status code");
		Assert.assertEquals(data.getBirthday(), getDateString(profile.getDateOfBirth()), "Profile birthday");
		Assert.assertEquals(data.getEmail(), myEmail, "Profile email");
		Assert.assertEquals(data.getGender(), profile.getGender().equals(0) ? "male" : "female", "Profile gender");
		Assert.assertEquals(data.getName(), profile.getName(), "Profile name");
		
		
		// use "myUid" route
		result = OpenAPI.getProfile(accessToken, myUid);
		data = OpenAPIProfile.fromResponse(result.response);
		
		Assert.assertEquals(result.statusCode, 200, "Status code");
		Assert.assertEquals(data.getBirthday(), getDateString(profile.getDateOfBirth()), "Profile birthday");
		Assert.assertEquals(data.getEmail(), myEmail, "Profile email");
		Assert.assertEquals(data.getGender(), profile.getGender().equals(0) ? "male" : "female", "Profile gender");
		Assert.assertEquals(data.getName(), profile.getName(), "Profile name");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "get_profile", "Excluded" })
	public void GetProfileWithoutPermission() {
		
		String invalidScopeAccessToken = OpenAPI.getAccessToken(myEmail, "qqqqqq", OpenAPI.RESOURCE_DEVICE, ClientKey, "https://www.google.com.vn/");
		BaseResult result = OpenAPI.getProfile(invalidScopeAccessToken, "me");
		
		Assert.assertEquals(result.statusCode, 403, "Status code");
		Assert.assertEquals(result.code, 403, "OpenAPI code");
		Assert.assertEquals(result.message, DefaultValues.ResourceForbidden, "Error message");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "get_profile" })
	public void GetProfileOfOtherUser() {
		
		// from other authorized user
		BaseResult result = OpenAPI.getProfile(accessToken, yourUid);
		Assert.assertEquals(result.statusCode, 403, "Status code");
		Assert.assertEquals(result.code, 403, "OpenAPI code");
		Assert.assertEquals(result.message, DefaultValues.ResourceForbidden, "Error message");

		// from unauthorized user
		result = OpenAPI.getProfile(accessToken, strangerUid);
		Assert.assertEquals(result.statusCode, 403, "Status code");
		Assert.assertEquals(result.code, 403, "OpenAPI code");
		Assert.assertEquals(result.message, DefaultValues.ResourceForbidden, "Error message");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "get_profile" })
	public void GetProfileUsingAppCredential() {
		
		// authorized user
		BaseResult result = OpenAPI.getProfile(ClientApp, myUid);
		OpenAPIProfile data = OpenAPIProfile.fromResponse(result.response);
		
		Assert.assertEquals(result.statusCode, 200, "Status code");
		Assert.assertEquals(data.getBirthday(), getDateString(profile.getDateOfBirth()), "Profile birthday");
		Assert.assertEquals(data.getEmail(), myEmail, "Profile email");
		Assert.assertEquals(data.getGender(), profile.getGender().equals(0) ? "male" : "female", "Profile gender");
		Assert.assertEquals(data.getName(), profile.getName(), "Profile name");
		
		
		// unauthorized user
		result = OpenAPI.getProfile(ClientApp, strangerUid);
		
		Assert.assertEquals(result.statusCode, 403, "Status code");
		Assert.assertEquals(result.code, 403, "OpenAPI code");
		Assert.assertEquals(result.message, DefaultValues.UnauthorizedAccess, "Error message");
	}
	
	/*
	 * TODO:
	 * - Get a resource using expired token
	 * - Get a resource with only selected fields
	 */

}
