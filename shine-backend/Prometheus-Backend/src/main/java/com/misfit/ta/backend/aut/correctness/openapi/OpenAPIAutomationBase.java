package com.misfit.ta.backend.aut.correctness.openapi;


import org.testng.annotations.BeforeClass;

import com.misfit.ta.Settings;
import com.misfit.ta.backend.api.metawatch.MVPApi;
import com.misfit.ta.backend.aut.BackendAutomation;
import com.misfit.ta.backend.data.openapi.OpenAPIThirdPartyApp;

public class OpenAPIAutomationBase extends BackendAutomation {

	
	protected static String ClientKey = Settings.getParameter("MVPOpenAPIClientID");
	protected static String ClientSecret = Settings.getParameter("MVPOpenAPIClientSecret");
	protected static OpenAPIThirdPartyApp ClientApp;
	
	
	// each class has 2 user authorized to app: you and me, 
	// and a non-authorized stranger
	protected String myEmail;
	protected String yourEmail;
	protected String strangerEmail;
	
	protected String myToken;
	protected String yourToken;
	protected String strangerToken;
	
	protected String myUid;
	protected String yourUid;
	protected String strangerUid;
	
	
	// set up class
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		
		myEmail = MVPApi.generateUniqueEmail();
		yourEmail = MVPApi.generateUniqueEmail();
		strangerEmail = MVPApi.generateUniqueEmail();
		
		myToken = MVPApi.signUp(myEmail, "qqqqqq").token;
		yourToken = MVPApi.signUp(yourEmail, "qqqqqq").token;
		strangerToken = MVPApi.signUp(strangerEmail, "qqqqqq").token;
		
		myUid = MVPApi.getUserId(myToken);
		yourUid = MVPApi.getUserId(yourToken);
		strangerUid = MVPApi.getUserId(strangerToken);
		
		ClientApp = new OpenAPIThirdPartyApp();
		ClientApp.setClientKey(ClientKey);
		ClientApp.setClientSecret(ClientSecret);
	}
}
