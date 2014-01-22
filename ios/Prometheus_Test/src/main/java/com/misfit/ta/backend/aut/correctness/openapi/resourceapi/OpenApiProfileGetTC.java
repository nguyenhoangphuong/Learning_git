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
import com.misfit.ta.backend.data.openapi.OpenAPIProfile;
import com.misfit.ta.backend.data.profile.ProfileData;
import com.misfit.ta.utils.TextTool;

public class OpenApiProfileGetTC extends BackendAutomation {

	private String email = MVPApi.generateUniqueEmail();
	private ProfileData profile = DataGenerator.generateRandomProfile(System.currentTimeMillis() / 1000, null);
	private String clientKey = Settings.getParameter("MVPOpenAPIClientID");
	private String userid;
	
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		String token = MVPApi.signUp(email, "qqqqqq").token;
		MVPApi.createProfile(token, profile);
		userid = MVPApi.getUserId(token);
	}
	
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "get_profile" })
	public void GetProfileUsingInvalidAccessToken() {
		
		// empty access token
		BaseResult result = OpenAPI.getProfile(null, "me");
		Assert.assertEquals(result.statusCode, 401, "Status code");
		Assert.assertEquals(result.errorMessage, DefaultValues.InvalidAccessToken, "Error message");

		// invalid access token
		result = OpenAPI.getProfile(TextTool.getRandomString(10, 10), "me");
		Assert.assertEquals(result.statusCode, 401, "Status code");
		Assert.assertEquals(result.errorMessage, DefaultValues.InvalidAccessToken, "Error message");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "get_profile" })
	public void GetProfileWithoutPermission() {
		
		String accessToken = OpenAPI.getAccessToken(email, "qqqqqq", "public", OpenAPI.RESOURCE_DEVICES, clientKey);
		BaseResult result = OpenAPI.getProfile(accessToken, "me");
		
		Assert.assertEquals(result.statusCode, 403, "Status code");
		Assert.assertEquals(result.errorMessage, DefaultValues.ResourceForbidden, "Error message");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "get_profile" })
	public void GetProfileOfOtherUser() {
		
		// create new user
		String email2 = MVPApi.generateUniqueEmail();
		String token2 = MVPApi.signUp(email2, "qqqqqq").token;
		String userid2 = MVPApi.getUserId(token2);
		ProfileData profile2 = DataGenerator.generateRandomProfile(System.currentTimeMillis() / 1000, null);
		MVPApi.createProfile(token2, profile2);
		OpenAPI.getAccessToken(email2, "qqqqqq", "public", "profile", clientKey);

		// get that user's profile using access token from current user
		String accessToken = OpenAPI.getAccessToken(email, "qqqqqq", "public", OpenAPI.RESOURCE_DEVICES, clientKey);
		BaseResult result = OpenAPI.getProfile(accessToken, userid2);
		Assert.assertEquals(result.statusCode, 403, "Status code");
		Assert.assertEquals(result.errorMessage, DefaultValues.ResourceForbidden, "Error message");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "get_profile" })
	public void GetProfileWithValidAccessToken() {
		
		String accessToken = OpenAPI.getAccessToken(email, "qqqqqq", "public", OpenAPI.RESOURCE_DEVICES, clientKey);
		BaseResult result = OpenAPI.getProfile(accessToken, "me");
		OpenAPIProfile data = OpenAPIProfile.fromResponse(result.response);
		
		Assert.assertEquals(result.statusCode, 200, "Status code");
		Assert.assertEquals(data.getBirthday(), profile, "");
		Assert.assertEquals(data.getEmail(), profile.getEmail(), "Profile email");
		Assert.assertEquals(data.getGender(), profile.getGender().equals(0) ? "male" : "female", "Profile gender");
		Assert.assertEquals(data.getName(), profile.getName(), "Profile name");
		Assert.assertEquals(data.getId(), userid, "Profile user id");
	}

}
