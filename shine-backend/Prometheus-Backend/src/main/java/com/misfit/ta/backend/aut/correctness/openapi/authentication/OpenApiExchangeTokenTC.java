	package com.misfit.ta.backend.aut.correctness.openapi.authentication;


import org.testng.Assert;
import org.testng.annotations.Test;

import com.misfit.ta.Settings;
import com.misfit.ta.backend.api.openapi.OpenAPI;
import com.misfit.ta.backend.aut.BackendAutomation;
import com.misfit.ta.backend.data.BaseResult;
import com.misfit.ta.common.Verify;
import com.misfit.ta.utils.TextTool;

public class OpenApiExchangeTokenTC extends BackendAutomation {
	
	protected static String clientKey = Settings.getParameter("MVPOpenAPIClientID");
	protected static String clientSecret = Settings.getParameter("MVPOpenAPIClientSecret");
	protected static String code = OpenAPI.getCode("nhhai16991@gmail.com", "qqqqqq", OpenAPI.RESOURCE_PROFILE,
			clientKey, "");
	

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "exchange_token" })
	public void ExchangeTokenMissingParameters() {
		
		String redirectUrl = "http://misfit.com/";
		String grantType = OpenAPI.GRANT_TYPE_AUTHORIZATION_CODE;
		
		// without grant_type / code / client_id / client_secret / redirect_uri
		BaseResult result1 = OpenAPI.exchangeCodeForToken(null, code, clientKey, clientSecret, redirectUrl, null);
		BaseResult result2 = OpenAPI.exchangeCodeForToken(grantType, null, clientKey, clientSecret, redirectUrl, null);
		BaseResult result3 = OpenAPI.exchangeCodeForToken(grantType, code, null, clientSecret, redirectUrl, null);
		BaseResult result4 = OpenAPI.exchangeCodeForToken(grantType, code, clientKey, null, redirectUrl, null);
		BaseResult result5 = OpenAPI.exchangeCodeForToken(grantType, code, clientKey, clientSecret, null, null);

		boolean pass = true;
		pass &= Verify.verifyEquals(result1.statusCode, 200, "Status code (grant type)") == null;
		pass &= Verify.verifyEquals(result2.statusCode, 200, "Status code (code)") == null;
		pass &= Verify.verifyEquals(result3.statusCode, 401, "Status code (client id)") == null;
		pass &= Verify.verifyEquals(result4.statusCode, 401, "Status code (client secret)") == null;
		pass &= Verify.verifyEquals(result5.statusCode, 200, "Status code (redirect uri)") == null;
		
		pass &= Verify.verifyContainsNoCase(result1.rawData, "invalid grant type", "Error message (grant type)") == null;
		pass &= Verify.verifyContainsNoCase(result2.rawData, "missing code parameter", "Error message (code)") == null;
		pass &= Verify.verifyContainsNoCase(result3.rawData, "Unauthorized", "Error message (client id)") == null;
		pass &= Verify.verifyContainsNoCase(result4.rawData, "Unauthorized", "Error message (client secret)") == null;
		pass &= Verify.verifyContainsNoCase(result5.rawData, "Missing redirectUri", "Error message (redirect uri)") == null;
		
		Assert.assertTrue(pass, "All tests are passed");
	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "exchange_token" })
	public void ExchangeTokenInvalidParameters() {
			
		String redirectUrl = "https://www.misfitwearables.com/";
		String grantType = OpenAPI.GRANT_TYPE_AUTHORIZATION_CODE;
		
		// without grant_type / code / client_id / client_secret / redirect_url != redirect_url in dialog/authorize
		BaseResult result1 = OpenAPI.exchangeCodeForToken("hacker_code", code, clientKey, clientSecret, redirectUrl, null);
		BaseResult result2 = OpenAPI.exchangeCodeForToken(grantType, TextTool.getRandomString(10, 10), clientKey, clientSecret, redirectUrl, null);
		BaseResult result3 = OpenAPI.exchangeCodeForToken(grantType, code, TextTool.getRandomString(10, 10), TextTool.getRandomString(10, 10), redirectUrl, null);
		BaseResult result4 = OpenAPI.exchangeCodeForToken(grantType, code, clientKey, TextTool.getRandomString(10, 10), redirectUrl, null);
		BaseResult result5 = OpenAPI.exchangeCodeForToken(grantType, code, TextTool.getRandomString(10, 10), clientSecret, redirectUrl, null);
		BaseResult result6 = OpenAPI.exchangeCodeForToken(grantType, code, clientKey, clientSecret, redirectUrl, null);

		boolean pass = true;
		pass &= Verify.verifyEquals(result1.statusCode, 200, "Status code (random grant type)") == null;
		pass &= Verify.verifyEquals(result2.statusCode, 200, "Status code (random code)") == null;
		pass &= Verify.verifyEquals(result3.statusCode, 401, "Status code (random client id & client secret)") == null;
		pass &= Verify.verifyEquals(result4.statusCode, 401, "Status code (random client secret)") == null;
		pass &= Verify.verifyEquals(result5.statusCode, 401, "Status code (random client id)") == null;
		pass &= Verify.verifyEquals(result6.statusCode, 200, "Status code (redirect url doesn't match)") == null;
		
		pass &= Verify.verifyContainsNoCase(result1.rawData, "invalid grant type", "Status code (random grant type)") == null;
		pass &= Verify.verifyContainsNoCase(result2.rawData, "Invalid parameter", "Status code (random code)") == null;
		pass &= Verify.verifyContainsNoCase(result3.rawData, "Unauthorized", "Status code (random client id & client secret)") == null;
		pass &= Verify.verifyContainsNoCase(result4.rawData, "Unauthorized", "Status code (random client secret)") == null;
		pass &= Verify.verifyContainsNoCase(result5.rawData, "Unauthorized", "Status code (random client id)") == null;
		pass &= Verify.verifyContainsNoCase(result6.rawData, "Invalid parameter", "Status code (redirect url doesn't match)") == null;
		
		Assert.assertTrue(pass, "All tests are passed");
	}

	@Test(groups = { "ios", "Prometheus", "MVPBackend", "openapi", "exchange_token" })
	public void ExchangeTokenUsingValidCode() {
		
		String redirectUrl = "http://misfit.com/";
	
		// get code using dialog/authorize and then exchange for token
		BaseResult result = OpenAPI.exchangeCodeForToken(OpenAPI.GRANT_TYPE_AUTHORIZATION_CODE, code, 
				clientKey, clientSecret, redirectUrl, null);
		
		String exchangedToken = (String)result.getJsonResponseValue("access_token");
		String tokenType = (String)result.getJsonResponseValue("token_type");
		
		Assert.assertNotNull(exchangedToken, "Exchanged token");
		Assert.assertEquals(tokenType, "bearer", "Exchanged token type");
	}
	
}
