package com.misfit.ta.backend.aut.correctness.openapi.authentication;


import org.testng.Assert;
import org.testng.annotations.Test;

import com.misfit.ta.Settings;
import com.misfit.ta.backend.api.openapi.OpenAPI;
import com.misfit.ta.backend.aut.BackendAutomation;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.common.Verify;
import com.misfit.ta.utils.TextTool;

public class OpenApiAuthorizeUsersTC extends BackendAutomation {
	
	protected static String clientKey = Settings.getParameter("MVPOpenAPIClientID");
	protected static String returnUrl = "https://www.google.com.vn/";
	

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "authorization" })
	public void AuthorizeUserBeforeLogIn() {
		
		BaseResult result = OpenAPI.authorizationDialog("token", clientKey, returnUrl, OpenAPI.RESOURCE_PROFILE, null, null);

		Assert.assertTrue(result.rawData.contains("<form action=\"/auth/users/session\" method=\"post\">"), "Contains log in form");
		Assert.assertTrue(result.rawData.contains("<a href=\"/auth/facebook\">Login with Facebook</a>"), "Contains facebook log in link");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "authorization" })
	public void AuthorizeUserAfterLoggedOut() {
			
		// log in then log out
		BaseResult result = OpenAPI.logIn("nhhai16991@gmail.com", "qqqqqq");
		String cookie = result.cookie;
		OpenAPI.logOut(cookie);
		
		
		// get authorize dialog
		result = OpenAPI.authorizationDialog(OpenAPI.RESPONSE_TYPE_TOKEN, clientKey, returnUrl, OpenAPI.RESOURCE_PROFILE, null, cookie);

		Assert.assertTrue(result.rawData.contains("<form action=\"/auth/users/session\" method=\"post\">"), "Contains log in form");
		Assert.assertTrue(result.rawData.contains("<a href=\"/auth/facebook\">Login with Facebook</a>"), "Contains facebook log in link");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "authorization" })
	public void AuthorizeUserMissingParameters() {
			
		// log in user first
		BaseResult result = OpenAPI.logIn("nhhai16991@gmail.com", "qqqqqq");
		String cookie = result.cookie;
		
		
		// without response type / client key / redirect uri / scopes
		BaseResult result1 = OpenAPI.authorizationDialog(null, clientKey, returnUrl, OpenAPI.RESOURCE_PROFILE, null, cookie);
		BaseResult result2 = OpenAPI.authorizationDialog(OpenAPI.RESPONSE_TYPE_TOKEN, null, returnUrl, OpenAPI.RESOURCE_PROFILE, null, cookie);
		BaseResult result3 = OpenAPI.authorizationDialog(OpenAPI.RESPONSE_TYPE_TOKEN, clientKey, null, OpenAPI.RESOURCE_PROFILE, null, cookie);
		BaseResult result4 = OpenAPI.authorizationDialog(OpenAPI.RESPONSE_TYPE_TOKEN, clientKey, returnUrl, null, null, cookie);
		
		boolean pass = true;
		pass &= Verify.verifyEquals(result1.statusCode, 200, "Status code (response type)");
		pass &= Verify.verifyEquals(result2.statusCode, 200, "Status code (client id)");
		pass &= Verify.verifyEquals(result3.statusCode, 200, "Status code (redirect uri)");
		pass &= Verify.verifyEquals(result4.statusCode, 200, "Status code (scopes)");
		
		pass &= Verify.verifyContainsNoCase(result1.rawData, "invalid response type", "Error message (response type)");
		pass &= Verify.verifyContainsNoCase(result2.rawData, "missing client_id parameter", "Error message (client id)");
		pass &= Verify.verifyContainsNoCase(result3.rawData, "Missing redirect_uri param", "Error message (redirect uri)");
		pass &= Verify.verifyContainsNoCase(result4.rawData, "Missing scope param", "Error message (scope)");
		
		Assert.assertTrue(pass, "All tests are passed");
	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "authorization" })
	public void AuthorizeUserInvalidParameters() {
			
		// log in user first
		BaseResult result = OpenAPI.logIn("nhhai16991@gmail.com", "qqqqqq");
		String cookie = result.cookie;
		
		
		// response type != code or token / random client key / random scopes / invalid url 
		// combination of valid scopes and invalid or empty scopes
		BaseResult result1 = OpenAPI.authorizationDialog("cotodeken", clientKey, returnUrl, OpenAPI.RESOURCE_PROFILE, null, cookie);
		BaseResult result2 = OpenAPI.authorizationDialog(OpenAPI.RESPONSE_TYPE_TOKEN, TextTool.getRandomString(10, 10), returnUrl, OpenAPI.RESOURCE_PROFILE, null, cookie);
		BaseResult result3 = OpenAPI.authorizationDialog(OpenAPI.RESPONSE_TYPE_TOKEN, clientKey, returnUrl, "superscopes", null, cookie);
		BaseResult result4 = OpenAPI.authorizationDialog(OpenAPI.RESPONSE_TYPE_TOKEN, clientKey, returnUrl, OpenAPI.RESOURCE_DEVICE + ",," + OpenAPI.RESOURCE_PROFILE, null, cookie);
		BaseResult result5 = OpenAPI.authorizationDialog(OpenAPI.RESPONSE_TYPE_TOKEN, clientKey, returnUrl, OpenAPI.RESOURCE_DEVICE + ",superscopes," + OpenAPI.RESOURCE_PROFILE, null, cookie);
		BaseResult result6 = OpenAPI.authorizationDialog(OpenAPI.RESPONSE_TYPE_TOKEN, clientKey, "/", OpenAPI.RESOURCE_DEVICE, null, cookie);
		
		boolean pass = true;
		pass &= Verify.verifyEquals(result1.statusCode, 200, "Status code (response type)");
		pass &= Verify.verifyEquals(result2.statusCode, 200, "Status code (client id)");
		pass &= Verify.verifyEquals(result3.statusCode, 200, "Status code (random scopes)");
		pass &= Verify.verifyEquals(result4.statusCode, 200, "Status code (scope combination: valid,,valid)");
		pass &= Verify.verifyEquals(result5.statusCode, 200, "Status code (scope combination: valid,invalid,valid)");
		pass &= Verify.verifyEquals(result6.statusCode, 200, "Status code (invalid redirect url)");
		
		pass &= Verify.verifyContainsNoCase(result1.rawData, "invalid response type", "Error message (response type)");
		pass &= Verify.verifyContainsNoCase(result2.rawData, "not authorized", "Error message (client id)");
		pass &= Verify.verifyContainsNoCase(result3.rawData, "invalid scope", "Error message (random scopes)");
		pass &= Verify.verifyContainsNoCase(result4.rawData, "invalid scope", "Error message (scope combination: valid,,valid)");
		pass &= Verify.verifyContainsNoCase(result5.rawData, "invalid scope", "Error message (scope combination: valid,invalid,valid)");
		pass &= Verify.verifyContainsNoCase(result6.rawData, "invalid url", "Error message (redirect url)");
		
		Assert.assertTrue(pass, "All tests are passed");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "authorization" })
	public void AuthorizeUserUsingToken() {
		
		String returnUrl = "http://www.misfitwearables.com/";
		
		// log in user
		BaseResult result = OpenAPI.logIn("nhhai16991@gmail.com", "qqqqqq");
		String cookie = result.cookie;
		
		// call authorize dialog and confirm
		BaseResult result2 = OpenAPI.authorizationDialog(OpenAPI.RESPONSE_TYPE_TOKEN, clientKey, 
				returnUrl, OpenAPI.RESOURCE_PROFILE, null, cookie);
		BaseResult result3 = OpenAPI.authorizationConfirm(OpenAPI.parseTransactionId(result2), cookie);
		
		String location = OpenAPI.parseReturnUrl(result3);
		String token = OpenAPI.parseAccessToken(result3);
		
		logger.info(result3.getHeaderValue("Location"));
		logger.info(location);
		logger.info(token);
		Assert.assertEquals(location, returnUrl, "Return url");
		Assert.assertNotNull(token, "Return token");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "authorization" })
	public void AuthorizeUserUsingCode() {
		
		String returnUrl = "http://www.misfitwearables.com/";
		
		// log in user
		BaseResult result = OpenAPI.logIn("nhhai16991@gmail.com", "qqqqqq");
		String cookie = result.cookie;
		
		// call authorize dialog and confirm
		BaseResult result2 = OpenAPI.authorizationDialog(OpenAPI.RESPONSE_TYPE_CODE, clientKey, 
				returnUrl, OpenAPI.RESOURCE_PROFILE, null, cookie);
		BaseResult result3 = OpenAPI.authorizationConfirm(OpenAPI.parseTransactionId(result2), cookie);
		
		String location = OpenAPI.parseReturnUrl(result3);
		String code = OpenAPI.parseCode(result3);
		
		logger.info(result3.getHeaderValue("Location"));
		logger.info(location);
		logger.info(code);
		Assert.assertEquals(location, returnUrl, "Return url");
		Assert.assertNotNull(code, "Return code");
	}
	
	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "authorization" })
	public void AuthorizeUserWihoutGivingPermissions() {
		
		String returnUrl = "http://www.misfitwearables.com/";
		
		// log in user
		BaseResult result = OpenAPI.logIn("nhhai16991@gmail.com", "qqqqqq");
		String cookie = result.cookie;
		
		// using code
		BaseResult result2 = OpenAPI.authorizationDialog(OpenAPI.RESPONSE_TYPE_CODE, clientKey, 
				returnUrl, OpenAPI.RESOURCE_PROFILE, null, cookie);
		BaseResult result3 = OpenAPI.authorizationDeny(OpenAPI.parseTransactionId(result2), cookie);
		
		String location = OpenAPI.parseReturnUrl(result3);
		String code = OpenAPI.parseCode(result3);
		
		logger.info(result3.getHeaderValue("Location"));
		logger.info(location);
		logger.info(code);
		Assert.assertEquals(location, returnUrl, "Return url");
		Assert.assertNull(code, "Return code");
		
		
		// using token
		result2 = OpenAPI.authorizationDialog(OpenAPI.RESPONSE_TYPE_CODE, clientKey, 
				returnUrl, OpenAPI.RESOURCE_PROFILE, null, cookie);
		result3 = OpenAPI.authorizationDeny(OpenAPI.parseTransactionId(result2), cookie);
		
		location = OpenAPI.parseReturnUrl(result3);
		code = OpenAPI.parseAccessToken(result3);
		
		logger.info(result3.getHeaderValue("Location"));
		logger.info(location);
		logger.info(code);
		Assert.assertEquals(location, returnUrl, "Return url");
		Assert.assertNull(code, "Return code");
	}
	
	/*
	 * TODO:
	 * - Call with same scope twice
	 * - Call with different scopes and check the content of return page
	 */
}
